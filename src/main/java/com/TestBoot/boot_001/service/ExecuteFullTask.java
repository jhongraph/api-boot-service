package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.pojos.User;
import com.TestBoot.boot_001.utils.GenerateUser;
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
    private final GenerateUser generateUser;
    private final Env env;

    public ExecuteFullTask(LoginService loginService, AutoFillService autoFillService, DigitalCheckService digitalCheckService, UploadFileService uploadFileService, Generators generators, GenerateUser generateUser, Env env) {
        this.loginService = loginService;
        this.autoFillService = autoFillService;
        this.digitalCheckService = digitalCheckService;
        this.uploadFileService = uploadFileService;
        this.generators = generators;
        this.generateUser = generateUser;
        this.env = env;
    }

    public WebDriver initDriver(){

        System.setProperty("webdriver.chrome.driver", path);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
//         options.addArguments("--headless=new");

        return new ChromeDriver(options);
    }

    @Async("Executor")
    public void ejecutarTarea() {

        WebDriver driver = initDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(env.getWaitTimeOut()));
        User user = generateUser.gettingUser();

        try {

        int createdCases = 0;
            int zero = 1;
            int condition = env.getCiclo();
        boolean success = true;

        while (zero < condition) {
            try {
                if (success) {
                    try {
                    loginService.login(user.getUsername(), user.getPassword(), driver, wait);
                    success = false;
                    }catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                            PdfUploadException | SignatureException e) {
                        throw new PreSaleException("ERROR: ", e);
                    }
                }
                    autoFillService.fillFormAndContinue(driver, wait);

                    digitalCheckService.performSignatureStep(wait);

                    uploadFileService.uploadPdfAndFinish(wait);

                    zero += 1;
                    createdCases += 1;
                    log.info("[CASO COMPLETADO NO. {}]", createdCases);
            } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                     PdfUploadException | SignatureException e) {
                log.error("ERROR: {}", e.getMessage());
                success = true;
            }
        }

    } catch (Exception e){
     log.error(e.getMessage());

        }
    }
}
