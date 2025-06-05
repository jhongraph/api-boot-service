package com.TestBoot.boot_001.service.preSale;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.pojos.PreSaleRequest;
import com.TestBoot.boot_001.service.BaseExecut;
import com.TestBoot.boot_001.service.DigitalCheckService;
import com.TestBoot.boot_001.service.UploadFileService;
import com.TestBoot.boot_001.utils.DomElement;
import com.TestBoot.boot_001.utils.WebSocketSender;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class PreSaleService extends BaseExecut {
    private final Env env;
    private final AutoFillService autoFill;
    private final UploadFileService uploadFiles;
    private final DigitalCheckService digitalCheck;
    private final DomElement element;
    private final WebSocketSender sender;

    public PreSaleService(Env env, DomElement element, WebSocketSender sender, AutoFillService autoFill, DigitalCheckService digitalCheck, UploadFileService uploadFiles) {
        super(env, element);
        this.env = env;
        this.autoFill = autoFill;
        this.uploadFiles = uploadFiles;
        this.digitalCheck = digitalCheck;
        this.element = element;
        this.sender = sender;

    }

    @Override
    @Async("concurrencyExecutor")
    public void executeTask(PreSaleRequest request, CountDownLatch latch) {

        String message;
        String service = "pre-sale";
        WebDriver driver = initDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(env.getWaitTimeOut()));
        final AtomicInteger totalPreSaleCreatedCases = new AtomicInteger(0);

        try {

            int createdCasesByUser = 0;

            boolean success = true;

            while (createdCasesByUser < request.getProcessQuantity()) {
                try {
                    if (success) {
                        login(request.getUsername(), request.getPassword(), request.getUrlBase(), element.getPreSaleButton(), driver, wait);
                        success = false;
                    }

                    autoFill.fillFormAndContinue(driver, wait);

                    digitalCheck.performSignatureStep(wait, element.getDigitalCheckButton1(), element.getDigitalCheckButton2(), 2);

                    uploadFiles.uploadPdfAndFinish(wait);

                    createdCasesByUser++;
                    int global = totalPreSaleCreatedCases.incrementAndGet();

                    message = String.format("""
                            
                            ====================================================
                              [USUARIO: %s | CASO #: %d | TOTAL GLOBAL: %d]
                            ====================================================
                            """, request.getUsername(), createdCasesByUser, global);

                    sender.sendLog("info", service, message);


                } catch (LoginException | SessionNotCreatedException | TimeoutException | ElementNotFoundException |
                         FormException | SeleniumTimeoutException |
                         PdfUploadException | SignatureException e) {
                    Throwable realCause = e.getCause();
                    if (realCause instanceof LoginException ||
                            realCause instanceof TimeoutException ||
                            realCause instanceof ElementNotFoundException ||
                            realCause instanceof FormException ||
                            realCause instanceof SeleniumTimeoutException ||
                            realCause instanceof PdfUploadException ||
                            realCause instanceof SessionNotCreatedException ||
                            realCause instanceof SignatureException) {

                        message = String.format("ERROR: [%S]: %S", e.getClass().getSimpleName(), e.getMessage());

                        sender.sendLog("error", service, message);
                    } else {
                        message = "ERROR: " + e.getMessage();
                        sender.sendLog("error", service, message);
                        throw new PreSaleException("ERROR: ", e);
                    }
                    success = true;
                }
            }

        } finally {
            driver.quit();
            latch.countDown();
            {

            }
        }
    }

}
