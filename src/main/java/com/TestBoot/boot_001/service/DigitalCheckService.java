package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.utils.DomElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.TestBoot.boot_001.interactions.DomInteractions.waitAndClick;

@Service
@Slf4j
public class DigitalCheckService {

    private final DomElement element;

    @Autowired
    public DigitalCheckService(DomElement element) {
        this.element = element;
    }

    public void performSignatureStep(WebDriverWait wait) {
        try {

            waitAndClick(wait, By.id(element.getDigitalCheckButton1()));

            waitAndClick(wait, By.id(element.getContinueButton()));

            waitAndClick(wait, By.id(element.getDigitalCheckButton2()));

            waitAndClick(wait, By.id(element.getContinueButton()));

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            throw new SignatureException(e.getMessage());
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new PreSaleException(e.getMessage());
        }
    }
}
