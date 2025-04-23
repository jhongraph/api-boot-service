package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.pojos.Car;
import com.TestBoot.boot_001.utils.Generators;
import com.TestBoot.boot_001.utils.VinFileHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.slf4j.MDC;


import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
@Getter
public class PreSaleService {
    @Value("${chrome.driver.path}")
    private String path;

    @Autowired
    private final Env env;
    private final Generators generators;

    public WebDriver initDriver(){

        System.setProperty("webdriver.chrome.driver", path);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        // options.addArguments("--headless=new"); // Opcional para modo sin interfaz

        return new ChromeDriver(options);
    }


    public PreSaleService(Env env, Generators generators) {
        this.env = env;
        this.generators = generators;
    }





    public void login(String username, String password, WebDriver driver, WebDriverWait wait) {

        try {
            driver.get(env.getBaseUrl() + env.getLoginUrl());

            waitAndType(wait, By.id("LoginUser_UserName"), username);

            waitAndType(wait, By.id("LoginUser_Password"), password);

            waitAndClick(wait, By.className("iniciar-sesin-1"));

            wait.until(ExpectedConditions.urlContains(env.getHomeUrl()));

            waitAndClick(wait, By.id("MainContent_navMenu_btnCarPartialRegistration"));

        } catch (TimeoutException | ElementNotFoundException e) {
            log.error("[ERROR] Failed during login process\n");
            throw new SeleniumTimeoutException("Failed during login process");
        } catch (Exception e) {
            log.error("[ERROR] Login failed\n");
            throw new LoginException("Failed during login process");
        } finally {
            MDC.clear();
        }
    }

    public void fillFormAndContinue(WebDriver driverParam, WebDriverWait wait) {
        try {
            Car car = new Car();

            generators.generateCar(car);

            selectDropdownByValue(wait, "MainContent_ddlMySubType", "16");

            waitAndType(wait, By.id("MainContent_txtVIN"), car.getVin());

            waitAndClick(wait, By.id("MainContent_BtnSearchVIN"));

            WebElement yearInput = driverParam.findElement(By.id("MainContent_txtYear"));

            if (yearInput.getAttribute("value").isEmpty()) {
                yearInput.sendKeys(String.valueOf(car.getYear()));
            }


            WebElement makeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("MainContent_txtMake")));

            if (makeInput.getAttribute("value").isEmpty()) {
                makeInput.sendKeys(car.getMake());
            }

            WebElement modelInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("MainContent_txtModel")));

            if (modelInput.getAttribute("value").isEmpty()) {
                modelInput.sendKeys(car.getModel());
            }

            wait.until(driver -> !getAttributeValue(driverParam, By.id("MainContent_txtYear")).isEmpty());

            generators.insertRandomPlate(driverParam, wait);

            Select colorDropdown = new Select(driverParam.findElement(By.id("MainContent_ddlColor1")));
            List<WebElement> options = colorDropdown.getOptions();
            int randomIndex = new Random().nextInt(options.size() - 1) + 1;
            colorDropdown.selectByIndex(randomIndex);

            String contract = generators.generateContractNumber();

            waitAndType(wait, By.id("MainContent_contracNumber"), contract);

            waitAndType(wait, By.id("MainContent_txtLicenseNumber"), env.getLicence());

            generators.selectRandomSaleDate(driverParam);

            generators.selectRandomReimbursementDate(driverParam);

            waitAndType(wait, By.id("MainContent_txtLicense"), env.getConcesionario());
            waitAndClick(wait, By.id("MainContent_BtnSearchLicense"));

            WebElement dealerNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("MainContent_txtConcesionarioName")));
            WebElement licenseDateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("MainContent_txtLicenseDate")));

            String dealerName = dealerNameInput.getAttribute("value");
            String licenseDate = licenseDateInput.getAttribute("value");

            if ((dealerName == null || dealerName.trim().isEmpty()) ||
                    (licenseDate == null || licenseDate.trim().isEmpty())) {

                throw new FormException("valores de licencia de dealer null ");
            }

            waitAndClick(wait, By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));

            waitAndClick(wait, By.id("MainContent_btnConfirmationOwner"));

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            log.error("[ERROR] Form fill failed\n");
            throw new FormException("Error durante llenado de formulario: " + e.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("[ERROR] Form fill failed\n{}", e.getMessage());
            throw new FormException("Failed while filling out the form" );
        }
    }

    public void performSignatureStep1(WebDriverWait wait) {
        try {

            waitAndClick(wait, By.id("MainContent_chkConfirmation"));

            waitAndClick(wait, By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            log.error("[ERROR] Timeout occurred\n");
            throw new SignatureException("Timeout during operation: ");
        } catch (Exception e) {
            log.error("[ERROR] Signature step 1 failed\n");
            throw new SignatureException("Error during first signature confirmation");
        }
    }

    public void performSignatureStep2(WebDriverWait wait) {
        try {
            waitAndClick(wait, By.id("MainContent_CheckBox1"));

            waitAndClick(wait, By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            log.error("[ERROR] Timeout occurred\n");
            throw new SignatureException("Error during second signature confirmation");
        } catch (Exception e) {
            log.error("[ERROR] Signature step 2 failed\n");
            throw new SignatureException("Error during second signature confirmation");
        }
    }

    public void uploadPdfAndFinish(WebDriverWait wait) {
        try {

            WebElement uploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input.ruButton.ruBrowse")));
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("input[type='file']")));

            String filePath = env.getPdfRoute();
            fileInput.sendKeys(filePath);

            WebElement fileNameCell = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//td[normalize-space(text()) != '']")));

            Thread.sleep(2000);

            waitAndClick(wait, By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));

            waitAndClick(wait, By.id("MainContent_btnYes"));

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            log.error("[ERROR] Timeout occurred: {}\n", e.getClass().getSimpleName());
            throw new PdfUploadException("error about upload pdf");
        } catch (Exception e) {
            log.error("[ERROR] Unexpected error uploading PDF\n");
            throw new PdfUploadException("Unexpected error during PDF upload");
        }
    }

    private void waitAndType(WebDriverWait wait, By locator, String value) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(value);
        } catch (Exception e) {
            throw new ElementNotFoundException("Unable to type into element: " + locator, e);
        }
    }

    private void waitAndClick(WebDriverWait wait, By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            throw new ElementNotFoundException("Unable to click element: " + locator, e);
        }
    }

    private void selectDropdownByValue(WebDriverWait wait, String selectId, String value) {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(selectId)));
            new Select(dropdown).selectByValue(value);
        } catch (Exception e) {
            throw new ElementNotFoundException("Unable to select dropdown value: " + value, e);
        }
    }

    private String getAttributeValue(WebDriver driver, By locator) {
        try {
            return Objects.requireNonNull(driver.findElement(locator).getAttribute("value"));
        } catch (Exception e) {
            throw new ElementNotFoundException("Unable to get attribute value for element: " + locator, e);
        }
    }


    @Async("taskExecutor")
    public void runFullPreSaleProcess(String username, String password, CountDownLatch latch, WebDriver driver, WebDriverWait wait) {
        int createdCases = 0;
        int maxAttempts = 3;
        int attempt = 0;
        boolean success = false;

        while (attempt < maxAttempts && !success) {
            try {
                login(username, password, driver, wait);

                int zero = 1;
                int condition = env.getCiclo();

                while (zero <= condition) {
                    fillFormAndContinue(driver, wait);

                    performSignatureStep1(wait);

                    performSignatureStep2(wait);

                    uploadPdfAndFinish(wait);

                    zero += 1;
                    attempt = 0;
                    success = true;
                        createdCases += 1;
                        log.info("[COMPLETED CASE NO. {}] Full pre-sale process completed | Proceso de preventa completado exitosamente", createdCases);
                }
            } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                     PdfUploadException | SignatureException e) {
                log.error("[ERROR]  occurred: {}\n", e.getMessage());
                attempt += 1;
                log.error("[ERROR] Attempt {} failed | Intento {} fallido\n", attempt, attempt);

                if (attempt < maxAttempts) {
                    log.info("[RETRY] Retrying... | Reintentando...\n");
                } else {
                    log.error("[FAIL] Max retries reached | Se agotaron los reintentos. Abortando...\n");
                }
            } catch (Exception e) {
                log.error("[CRITICAL] CRITICAL ERROR - FORCING SHUTDOWN | ERROR CRITICO - FORZANDO CIERRE /ERROR: {}\n", e.getMessage());
                System.exit(1);
            }

        }



    }

    @Component
    @Slf4j
    public static class AppRunner implements CommandLineRunner {

        @Autowired
        private  Env env;
        @Autowired
        private PreSaleService preSaleService;

        @Override
        public void run(String... args) {



            WebDriver driver1 = null;
            WebDriver driver2 = null;
            WebDriver driver3 = null;

            if(env.getUsersActives() == 1){
                driver1 = preSaleService.initDriver();
            }

            if(env.getUsersActives() == 2){
                driver1 = preSaleService.initDriver();
                driver2 = preSaleService.initDriver();
            }

            if(env.getUsersActives() == 3){
                driver1 = preSaleService.initDriver();
                driver2 = preSaleService.initDriver();
                driver3 = preSaleService.initDriver();
            }

            WebDriverWait wait1 = new WebDriverWait(driver1, Duration.ofSeconds(env.getWaitTimeOut()));
            WebDriverWait wait2 = new WebDriverWait(driver2, Duration.ofSeconds(env.getWaitTimeOut()));
            WebDriverWait wait3 = new WebDriverWait(driver3, Duration.ofSeconds(env.getWaitTimeOut()));

                try {
                    CountDownLatch latch = new CountDownLatch(2);

                    if(env.getUsersActives() == 1){
                        preSaleService.runFullPreSaleProcess(env.getUser1(),
                                env.getPassword1(), latch, driver1, wait1 );
                    }
                    if(env.getUsersActives() == 2){
                        preSaleService.runFullPreSaleProcess(env.getUser1(),
                                env.getPassword1(), latch, driver1, wait1 );

                        preSaleService.runFullPreSaleProcess(env.getUser2(),
                                env.getPassword2(), latch, driver2, wait2 );
                    }
                    if(env.getUsersActives() == 3){
                        preSaleService.runFullPreSaleProcess(env.getUser1(),
                                env.getPassword1(), latch, driver1, wait1 );

                        preSaleService.runFullPreSaleProcess(env.getUser2(),
                                env.getPassword2(), latch, driver2, wait2 );

                        preSaleService.runFullPreSaleProcess(env.getUser3(),
                                env.getPassword3(), latch, driver3, wait3 );
                    }
                    latch.await();

                }  catch (Exception e) {
                    log.error("ERROR IN FULL PROCESS: {}", e.getClass().getSimpleName());
                    throw new PreSaleException("error: ", e);

                }
            System.exit(1);
        }
    }
}