package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.pojos.Car;
import com.TestBoot.boot_001.utils.DomElement;
import com.TestBoot.boot_001.utils.VinFileHandler;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.TestBoot.boot_001.interactions.DomInteractions.waitAndClick;

@Service
@Slf4j
public class UploadFileService {

    private final Env env;
    private final DomElement element;
    private final Car car;
    private final VinFileHandler fileHandler;

    @Autowired
    public UploadFileService(Env env, DomElement element, Car car, VinFileHandler fileHandler) {
        this.env = env;
        this.element = element;
        this.car = car;
        this.fileHandler = fileHandler;
    }


    public void uploadPdfAndFinish(WebDriverWait wait) {
        try {

            String filePath = env.getPdfRoute();

            List<WebElement> fileInputsQuantity = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.cssSelector(element.getFileInputCssSelector())));

            int quantity = fileInputsQuantity.size();


            if (quantity > 7) quantity = 5;


            for (int i = 0; i < quantity; i++) {
                List<WebElement> fileInputs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector(element.getFileInputCssSelector())));

                if (fileInputs.isEmpty()) break;

                fileInputs.getFirst().sendKeys(filePath);
                Thread.sleep(2000);
            }

            waitAndClick(wait, By.id(element.getContinueButton()));

            waitAndClick(wait, By.id(element.getTryAgainButton()));

        } catch (Exception e) {
            fileHandler.reInsertVin(car.getVin());
            throw new PdfUploadException(e.getMessage(), e);
        }
    }
}
