package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.utils.Generators;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@Getter
@Slf4j
public class ExecuteFullTask {

    @Value("${chrome.driver.path}")
    private String path;

    private final LoginService loginService;
    private final AutoFillService autoFillService;
    private final DigitalCheckService digitalCheckService;
    private final UploadFileService uploadFileService;
    private final Generators generators;
    private final Env env;

    public ExecuteFullTask(LoginService loginService, AutoFillService autoFillService, DigitalCheckService digitalCheckService, UploadFileService uploadFileService, Generators generators, Env env) {
        this.loginService = loginService;
        this.autoFillService = autoFillService;
        this.digitalCheckService = digitalCheckService;
        this.uploadFileService = uploadFileService;
        this.generators = generators;
        this.env = env;
    }

    public WebDriver initDriver() {

        System.setProperty("webdriver.chrome.driver", path);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--headless=new");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-data-dir=/tmp/chrome-profile-" + UUID.randomUUID());

        return new ChromeDriver(options);
    }

    private final AtomicInteger totalCreatedCases = new AtomicInteger(0);

    @Async("Executor")
    public void executeTask(String username, String password, CountDownLatch latch) {

        WebDriver driver = initDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(env.getWaitTimeOut()));

        try {

            int createdCasesByUser = 0;
            int condition = env.getCiclo();
            boolean success = true;

            while (createdCasesByUser < condition ) {
                try {
                    if (success) {
                        loginService.login(username, password, driver, wait);
                        success = false;
                    }
                    autoFillService.fillFormAndContinue(driver, wait);

                    digitalCheckService.performSignatureStep(wait);

                    uploadFileService.uploadPdfAndFinish(wait);

                    createdCasesByUser++;
                    int global = totalCreatedCases.incrementAndGet();
                    log.info("""
                            
                            ====================================================
                              [USUARIO: {} | CASO #: {} | TOTAL GLOBAL: {}]
                            ====================================================
                            """, username, createdCasesByUser, global);

                } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                         PdfUploadException | SignatureException e) {
                    log.error("ERROR: {}", e.getMessage());
                    success = true;
                }
            }

        } catch (Exception e) {
            driver.quit();
            throw new PreSaleException(e.getMessage());
        } finally {
            latch.countDown();
            {

            }
        }
    }
}
