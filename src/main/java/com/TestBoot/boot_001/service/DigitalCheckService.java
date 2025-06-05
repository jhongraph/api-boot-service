package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.pojos.Car;
import com.TestBoot.boot_001.utils.DomElement;
import com.TestBoot.boot_001.utils.VinFileHandler;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

import static com.TestBoot.boot_001.interactions.DomInteractions.waitAndClick;

@Service
@Slf4j
public class DigitalCheckService {

    private final DomElement element;
    private final Car car;
    private final VinFileHandler fileHandler;

    @Autowired
    public DigitalCheckService(DomElement element, Car car, VinFileHandler fileHandler) {

        this.element = element;
        this.car = car;
        this.fileHandler = fileHandler;
    }

    public void performSignatureStep(WebDriverWait wait, String input1, String input2, @Nullable Integer quantityCheck) {
        try {

            waitAndClick(wait, By.id(input1));

            waitAndClick(wait, By.id(element.getContinueButton()));

            if (quantityCheck != null && quantityCheck == 2) {

                waitAndClick(wait, By.id(input2));

                waitAndClick(wait, By.id(element.getContinueButton()));
            }


        } catch (Exception e) {
            fileHandler.reInsertVin(car.getVin());
            throw new SignatureException(e.getMessage(), e);
        }
    }
}
