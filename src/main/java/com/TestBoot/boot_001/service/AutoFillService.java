package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.pojos.Car;
import com.TestBoot.boot_001.utils.DomElement;
import com.TestBoot.boot_001.utils.Generators;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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

    @Autowired
    public AutoFillService(Env env, DomElement element, Generators generators) {
        this.env = env;
        this.element = element;
        this.generators = generators;
    }



    public void fillFormAndContinue(WebDriver driverParam, WebDriverWait wait) {
        try {
            Car car = new Car();

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

            generators.insertRandomPlate(wait);

            generators.selectRandomColor(driverParam);

            String contract = generators.generateContractNumber();

            waitAndType(wait, By.id(element.getContractNumberInput()), contract);

            waitAndType(wait, By.id(element.getLicenceInput()), env.getLicence());

            generators.selectRandomSaleDate(driverParam);

            generators.selectRandomReimbursementDate(driverParam);

            waitAndType(wait, By.id(element.getDealerLicenceInput()), env.getConcesionario());
            waitAndClick(wait, By.id(element.getDealerLicenseSerchButton()));

            WebElement dealerNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getConcessionaryName())));
            WebElement licenseDateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getDealerLicenseDate())));

            String dealerName = dealerNameInput.getAttribute("value");
            String licenseDate = licenseDateInput.getAttribute("value");

            if ((dealerName == null || dealerName.trim().isEmpty()) ||
                    (licenseDate == null || licenseDate.trim().isEmpty())) {

                throw new FormException("VALORES DE LICENCIA VACIOS");
            }

            waitAndClick(wait, By.id(element.getContinueButton()));

            waitAndClick(wait, By.id(element.getDateUserConfirmation()));

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            throw new FormException(e.getMessage());
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new PreSaleException(e.getMessage());
        }
    }
}
