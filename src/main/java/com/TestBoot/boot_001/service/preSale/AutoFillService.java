package com.TestBoot.boot_001.service.preSale;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.pojos.Car;
import com.TestBoot.boot_001.utils.DomElement;
import com.TestBoot.boot_001.utils.Generators;
import com.TestBoot.boot_001.utils.VinFileHandler;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.TestBoot.boot_001.interactions.DomInteractions.*;
import static com.TestBoot.boot_001.interactions.DomInteractions.getAttributeValue;
import static com.TestBoot.boot_001.interactions.DomInteractions.waitAndClick;
import static com.TestBoot.boot_001.interactions.DomInteractions.waitAndType;

@Service
@Slf4j
public class AutoFillService {

    private final Env env;
    private final DomElement element;
    private final Generators generators;
    private final Car car;
    private final VinFileHandler fileHandler;

    @Autowired
    public AutoFillService(Env env, DomElement element, Generators generators, Car car, VinFileHandler fileHandler) {
        this.env = env;
        this.element = element;
        this.generators = generators;
        this.car = car;
        this.fileHandler = fileHandler;
    }


    public void fillFormAndContinue(WebDriver driverParam, WebDriverWait wait) {
        try {

            generators.generateCar(car);

            selectDropdownByValue(wait, element.getSubTypeSelect(), element.getSubTypeRegisterValue());

            waitAndType(wait, By.id(element.getVinInput()), car.getVin());

            waitAndClick(wait, By.id(element.getVinSerchButton()));

            WebElement yearInput = driverParam.findElement(By.id(element.getYearInput()));

            if (Objects.requireNonNull(yearInput.getAttribute("value")).isEmpty()) {
                yearInput.sendKeys(String.valueOf(car.getYear()));
            }

            WebElement makeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getMakeInput())));

            if (Objects.requireNonNull(makeInput.getAttribute("value")).isEmpty()) {
                makeInput.sendKeys(car.getMake());
            }

            WebElement modelInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getModelInput())));

            if (Objects.requireNonNull(modelInput.getAttribute("value")).isEmpty()) {
                modelInput.sendKeys(car.getModel());
            }

            wait.until(driver -> !getAttributeValue(driverParam, By.id(element.getYearInput())).isEmpty());

            driverParam.findElement(By.tagName("body")).click();
            generators.insertRandomPlate(wait, element.getPlateInput());
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            generators.selectRandomColor(driverParam, element.getColorSelect());
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            String contract = generators.generateContractNumber();
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            waitAndType(wait, By.id(element.getContractNumberInput()), contract);
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            waitAndType(wait, By.id(element.getLicenceInput()), env.getLicence());
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            generators.preSaleRandomDate(driverParam, element.getSaleDateInput());
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            generators.preSaleRandomDate(driverParam, element.getReimbursementDateInput());
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            waitAndType(wait, By.id(element.getDealerLicenceInput()), env.getConcesionario());
            driverParam.findElement(By.tagName("body")).click();
            waitAndClick(wait, By.id(element.getDealerLicenseSerchButton()));
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            WebElement dealerNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getConcessionaryName())));
            driverParam.findElement(By.tagName("body")).click();
            WebElement licenseDateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getDealerLicenseDate())));
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            String dealerName = dealerNameInput.getAttribute("value");
            driverParam.findElement(By.tagName("body")).click();
            String licenseDate = licenseDateInput.getAttribute("value");
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            if ((dealerName == null || dealerName.trim().isEmpty()) ||
                    (licenseDate == null || licenseDate.trim().isEmpty())) {
                driverParam.findElement(By.tagName("body")).click();

                throw new FormException("VALORES DE LICENCIA VACIOS");
            }

            driverParam.findElement(By.tagName("body")).click();
            waitAndClick(wait, By.id(element.getContinueButton()));
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            waitAndClick(wait, By.id(element.getDateUserConfirmation()));
            driverParam.findElement(By.tagName("body")).click();

        } catch (Exception e) {
            fileHandler.reInsertVin(car.getVin());
            throw new FormException(e.getMessage(), e);
        }
    }
}
