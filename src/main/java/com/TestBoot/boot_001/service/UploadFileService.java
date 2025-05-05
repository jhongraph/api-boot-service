package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.utils.DomElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.TestBoot.boot_001.interactions.DomInteractions.waitAndClick;

@Service
@Slf4j
public class UploadFileService {

    private final Env env;
    private final DomElement element;

    @Autowired
    public UploadFileService(Env env, DomElement element) {
        this.env = env;
        this.element = element;
    }



    public void uploadPdfAndFinish(WebDriverWait wait) {
        try {

            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(element.getFileInputCssSelector())));

            String filePath = env.getPdfRoute();
            fileInput.sendKeys(filePath);


            Thread.sleep(2000);

            waitAndClick(wait, By.id(element.getContinueButton()));

            waitAndClick(wait, By.id(element.getTryAgainButton()));

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            throw new PdfUploadException(e.getMessage());
        } catch (Exception e) {
            log.error("ERROR: {}",e.getMessage());
            throw new PreSaleException(e.getMessage());
        }
    }
}
