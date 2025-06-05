package com.TestBoot.boot_001.service.LoteRegister;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.FormException;
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

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static com.TestBoot.boot_001.interactions.DomInteractions.*;

@Service
@Slf4j
public class AutoFillRegisterService {

    private final Env env;
    private final DomElement element;
    private final Generators generators;
    private final Car car;
    private final VinFileHandler fileHandler;

    @Autowired
    public AutoFillRegisterService(Env env, DomElement element, Generators generators, Car car, VinFileHandler fileHandler) {
        this.env = env;
        this.element = element;
        this.generators = generators;
        this.car = car;
        this.fileHandler = fileHandler;
    }


    public void fillFormAndContinue(WebDriver driverParam, WebDriverWait wait) {
        try {

            generators.generateCar(car);

            waitAndType(wait, By.id(element.getRegistrationVinImput()), car.getVin());


            waitAndType(wait, By.id(element.getRegistrationLicenceInput()), env.getLicence());

            selectDropdownByValue(wait, element.getRegistrationBuyType(), element.getRegistrationBuyTypeValue());

            int price = ThreadLocalRandom.current().nextInt(21000, 150001);

            waitAndType(wait, By.id(element.getRegistrationSalePrice()), String.valueOf(price));

            waitAndClick(wait, By.id(element.getContinueButton()));
            waitAndClick(wait, By.id(element.getContinueButton()));


            waitAndClick(wait, By.id(element.getRegistrationDataUserConfirmButton()));

            List<WebElement> buttons = driverParam.findElements(By.id(element.getRegistrationYes()));
            if (!buttons.isEmpty() && buttons.getFirst().isDisplayed()) {

                waitAndClick(wait, By.id(element.getRegistrationYes()));
            }


            waitAndType(wait, By.id(element.getRegistrationDealerLicenceInput()), env.getConcesionario());

            waitAndClick(wait, By.id(element.getRegistrationSerchLicenceButton()));

            Thread.sleep(2000);


            WebElement dealerNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getRegistrationConcessionaryName())));
            WebElement licenseDateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getRegistrationLicenceDateInput())));

            String dealerName = dealerNameInput.getAttribute("value");
            String licenseDate = licenseDateInput.getAttribute("value");

            if ((dealerName == null || dealerName.trim().isEmpty()) ||
                    (licenseDate == null || licenseDate.trim().isEmpty())) {

                throw new FormException("VALORES DE LICENCIA VACIOS");
            }


            driverParam.findElement(By.tagName("body")).click();
            waitAndClick(wait, By.id(element.getContinueButton()));
            driverParam.findElement(By.tagName("body")).click();


            driverParam.findElement(By.tagName("body")).click();
            generators.selectRandomColor(driverParam, element.getRegistrationColorSelect());
            driverParam.findElement(By.tagName("body")).click();

            WebElement yearInput = driverParam.findElement(By.id(element.getRegistrationYearInput()));

            if (Objects.requireNonNull(yearInput.getAttribute("value")).isEmpty()) {
                driverParam.findElement(By.tagName("body")).click();
                yearInput.sendKeys(String.valueOf(car.getYear()));
                driverParam.findElement(By.tagName("body")).click();
            }

            WebElement makeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getRegistrationMakeInput())));

            if (Objects.requireNonNull(makeInput.getAttribute("value")).isEmpty()) {
                driverParam.findElement(By.tagName("body")).click();
                makeInput.sendKeys(car.getMake());
                driverParam.findElement(By.tagName("body")).click();
            }

            WebElement modelInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getRegistrationModelInput())));

            if (Objects.requireNonNull(modelInput.getAttribute("value")).isEmpty()) {
                driverParam.findElement(By.tagName("body")).click();
                modelInput.sendKeys(car.getModel());
                driverParam.findElement(By.tagName("body")).click();
            }

            WebElement doorInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getRegistrationDoorInput())));
            if (Objects.equals(doorInput.getAttribute("value"), "0")) {
                driverParam.findElement(By.tagName("body")).click();
                doorInput.sendKeys("4");
                driverParam.findElement(By.tagName("body")).click();
            }

            WebElement cilinInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getRegistrationCilinInput())));
            if (Objects.equals(cilinInput.getAttribute("value"), "0")) {
                driverParam.findElement(By.tagName("body")).click();
                cilinInput.sendKeys("4");
                driverParam.findElement(By.tagName("body")).click();
            }

            WebElement powerInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getRegistrationCilinInput())));
            if (Objects.equals(powerInput.getAttribute("value"), "0")) {
                driverParam.findElement(By.tagName("body")).click();
                powerInput.sendKeys("169");
                driverParam.findElement(By.tagName("body")).click();
            }

            WebElement odometer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getRegistrationOdometer())));
            if (Objects.requireNonNull(odometer.getAttribute("value")).isEmpty()) {
                driverParam.findElement(By.tagName("body")).click();
                odometer.sendKeys(String.valueOf(ThreadLocalRandom.current().nextInt(2, 11)));
                driverParam.findElement(By.tagName("body")).click();
            }

            driverParam.findElement(By.tagName("body")).click();
            selectDropdownByValue(wait, element.getRegistrationOrigenType(), element.getRegistrationOrigenTittleValue());
            driverParam.findElement(By.tagName("body")).click();


            driverParam.findElement(By.tagName("body")).click();
            generators.registrationRandomDate(driverParam, element.getRegistrationOrigenSaleDateSelect());
            driverParam.findElement(By.tagName("body")).click();

            Thread.sleep(500);


            driverParam.findElement(By.tagName("body")).click();
            selectDropdownByValue(wait, element.getRegistrationCarTypeSelect(), element.getRegistrationCarUseTypeValue());
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            selectDropdownByValue(wait, element.getRegistrationCarUseTypeSelect(), element.getRegistrationCarUseTypeValue());
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            selectDropdownByValue(wait, element.getRegistrationCarPropurtionTypeSelect(), element.getRegistrationCarPropurtionTypeValue());
            driverParam.findElement(By.tagName("body")).click();


            WebElement plateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getRegistrationPlateInput())));
            if (Objects.requireNonNull(plateInput.getAttribute("value")).isEmpty()) {
                driverParam.findElement(By.tagName("body")).click();
                Thread.sleep(500);
                generators.insertRandomPlate(wait, element.getRegistrationPlateInput());
                driverParam.findElement(By.tagName("body")).click();
            }


            WebElement tagInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element.getRegistrationTagInput())));
            if (Objects.requireNonNull(tagInput.getAttribute("value")).isEmpty()) {
                driverParam.findElement(By.tagName("body")).click();
                Thread.sleep(500);
                waitAndType(wait, By.id(element.getRegistrationTagInput()), generators.generateTag());
                driverParam.findElement(By.tagName("body")).click();
            }


            driverParam.findElement(By.tagName("body")).click();
            Thread.sleep(500);
            generators.registrationRandomDate(driverParam, element.getRegistrationTagDateSelect());
            driverParam.findElement(By.tagName("body")).click();

            int contribution = price / 2 + 9000;

            driverParam.findElement(By.tagName("body")).click();
            waitAndType(wait, By.id(element.getRegistrationContributoryPriceInput()), String.valueOf(contribution));
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            waitAndType(wait, By.id(element.getRegistrationArbitragePriceInput()), String.valueOf(contribution / 2 + 2000));
            driverParam.findElement(By.tagName("body")).click();


            driverParam.findElement(By.tagName("body")).click();
            generators.registrationRandomDate(driverParam, element.getRegistrationPayDateSelect());
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            generators.registrationRandomDate(driverParam, element.getRegistrationSelleDateSelect());
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            waitAndType(wait, By.id(element.getRegistrationContractInput()), generators.generateContractNumber());
            driverParam.findElement(By.tagName("body")).click();

            driverParam.findElement(By.tagName("body")).click();
            waitAndClick(wait, By.id(element.getContinueButton()));
            driverParam.findElement(By.tagName("body")).click();


        } catch (Exception e) {
            fileHandler.reInsertVin(car.getVin());
            throw new FormException(e.getMessage(), e);
        }
    }
}
