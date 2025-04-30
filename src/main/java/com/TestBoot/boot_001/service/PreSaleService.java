package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.pojos.Car;
import com.TestBoot.boot_001.utils.Generators;
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
import java.util.UUID;
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
//        options.addArguments("--headless=new");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-data-dir=/tmp/chrome-profile-" + UUID.randomUUID());


        return new ChromeDriver(options);
    }
    Car car = new Car();
    public PreSaleService(Env env, Generators generators) {
        this.env = env;
        this.generators = generators;
    }

    public void login(String username, String password, WebDriver driver, WebDriverWait wait) {

        try {
            driver.get(env.getBaseUrl() + env.getLoginUrl());

            waitAndType(wait, By.id("LoginUser_UserName"), username);

            waitAndType(wait, By.id("LoginUser_Password"), password);

            waitAndClick(driver, wait, By.className("iniciar-sesin-1"));

            wait.until(ExpectedConditions.urlContains(env.getHomeUrl()));

            List<WebElement> bottons = driver.findElements(By.id("ucTermsConditions_btnSubmit"));
            List<WebElement> check1 = driver.findElements(By.id("ucTermsConditions_gvTermConditionsBullets_chkChecked_0"));

            if (!bottons.isEmpty() && bottons.getFirst().isDisplayed()) {
                log.info("---------------boton de terminos encontradp: " + bottons.getFirst());
                log.info("--------------------check encontrado: " + check1.getFirst());
                waitAndClick(driver, wait, By.id("ucTermsConditions_gvTermConditionsBullets_chkChecked_0"));
                waitAndClick(driver, wait, By.id("ucTermsConditions_gvTermConditionsBullets_chkChecked_1"));
                waitAndClick(driver, wait, By.id("ucTermsConditions_gvTermConditionsBullets_chkChecked_2"));
                waitAndClick(driver, wait, By.id("ucTermsConditions_gvTermConditionsBullets_chkChecked_3"));
                waitAndClick(driver, wait, By.id("ucTermsConditions_btnSubmit"));
            }

            waitAndClick(driver, wait, By.id("MainContent_navMenu_btnCarPartialRegistration"));

        } catch (TimeoutException | ElementNotFoundException | SeleniumTimeoutException | FormException |
                 LoginException | PdfUploadException |SignatureException e) {
            throw new LoginException("Error: " + e, e);
        } catch (Exception e) {
            throw new LoginException("Falla durante proceso de de login: " + e, e);
        } finally {
            MDC.clear();
        }
    }

    public void fillFormAndContinue(WebDriver driverParam, WebDriverWait wait) {
        try {

            generators.generateCar(car);

            selectDropdownByValue(wait, "MainContent_ddlMySubType", "16");

            car.setVin(generators.generateVin());

            waitAndType(wait, By.id("MainContent_txtVIN"), car.getVin());

            waitAndClick(driverParam, wait, By.id("MainContent_BtnSearchVIN"));

            WebElement yearInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("MainContent_txtYear")));


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


            waitAndType( wait, By.id("MainContent_txtLicense"), env.getConcesionario());
            waitAndClick(driverParam, wait, By.id("MainContent_BtnSearchLicense"));

            WebElement dealerNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("MainContent_txtConcesionarioName")));
            WebElement licenseDateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("MainContent_txtLicenseDate")));

            String dealerName = dealerNameInput.getAttribute("value");
            String licenseDate = licenseDateInput.getAttribute("value");

            if ((dealerName == null || dealerName.trim().isEmpty()) ||
                    (licenseDate == null || licenseDate.trim().isEmpty())) {

                throw new FormException("valores de licencia de dealer null: ");
            }


            waitAndClick(driverParam, wait, By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));

            waitAndClick(driverParam, wait, By.id("MainContent_btnConfirmationOwner"));

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            throw new FormException("Error: " + e.getMessage());
        } catch (Exception e) {
            throw new FormException("falla al momento de autorrellenar formulario pre-venta: " + e.getMessage() );
        }
    }

    public void performSignatureStep1(WebDriver driver, WebDriverWait wait) {
        try {
            waitAndClick(driver, wait, By.id("MainContent_chkConfirmation"));

            waitAndClick(driver, wait, By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            throw new SignatureException("Error: " + e.getMessage());
        } catch (Exception e) {
            throw new SignatureException("Error durante la primera firma digital: " + e.getMessage());
        }
    }

    public void performSignatureStep2(WebDriver driver, WebDriverWait wait) {
        try {
            waitAndClick(driver, wait, By.id("MainContent_CheckBox1"));

            waitAndClick(driver, wait, By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            throw new SignatureException("Error: " + e.getMessage());
        } catch (Exception e) {
            throw new SignatureException("Error durante la segunda firma digital: " + e.getMessage());
        }
    }

    public void uploadPdfAndFinish(WebDriver driver, WebDriverWait wait) {
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

            waitAndClick(driver, wait, By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));

            waitAndClick(driver, wait, By.id("MainContent_btnYes"));

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            throw new PdfUploadException("Error inesperado al cargar pdf: " + e.getMessage());
        } catch (Exception e) {
            throw new PdfUploadException("Error : " +e.getMessage());
        }
    }
    private void waitAndType(WebDriverWait wait, By locator, String value) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(value);
        } catch (Exception e) {
            throw new ElementNotFoundException("Elemento no disponible para escribir: " + locator, e);
        }
    }

    private void waitAndClick(WebDriver driver, WebDriverWait wait, By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            try{
                wait.until(ExpectedConditions.elementToBeClickable(locator)).click();

            }catch (Exception e){
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            }

        } catch (Exception e) {
            throw new ElementNotFoundException("Elemento no disponible para clicar: " + locator, e);
        }
    }

    private void selectDropdownByValue(WebDriverWait wait, String selectId, String value) {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(selectId)));
            new Select(dropdown).selectByValue(value);
        } catch (Exception e) {
            throw new ElementNotFoundException("No se pudo seleccionar el valor en el desplegable" + value, e);
        }
    }

    private String getAttributeValue(WebDriver driver, By locator) {
        try {
            return Objects.requireNonNull(driver.findElement(locator).getAttribute("value"));
        } catch (Exception e) {
            throw new ElementNotFoundException("No se pudo obtener el valor del atributo para el elemento: " + locator, e);
        }
    }

    @Async("taskExecutor")
    public void runFullPreSaleProcess(String username, String password, CountDownLatch latch, WebDriver driver, WebDriverWait wait) {
        int createdCases = 0;



        int zero = 1;
        int condition = env.getCiclo();


        while (zero <= condition) {
            try{

            login(username, password, driver, wait);

            boolean success = true;

            while (success) {
                try {

                    fillFormAndContinue(driver, wait);

                    performSignatureStep1(driver, wait);

                    performSignatureStep2(driver, wait);

                    uploadPdfAndFinish(driver, wait);

                    zero += 1;
                    createdCases += 1;

                    log.info("[COMPLETED CASE NO. {}]  Proceso de preventa completado exitosamente", createdCases);

                } catch (Exception e) {
                    generators.reInsertVin(car.getVin());
                    log.error("{}\n", e.getMessage());
                  success = false;


                }
            }

            }  catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                       PdfUploadException | SignatureException e) {
                generators.reInsertVin(car.getVin());
                log.error("[ERROR]  occurred: {}\n", e.getClass().getSimpleName());

            }catch (Exception e){
                generators.reInsertVin(car.getVin());
                log.error("" + e);
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
            WebDriver driver4 = null;

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

            if(env.getUsersActives() == 4){
                driver1 = preSaleService.initDriver();
                driver2 = preSaleService.initDriver();
                driver3 = preSaleService.initDriver();
                driver4 = preSaleService.initDriver();
            }

            WebDriverWait wait1 = new WebDriverWait(driver1, Duration.ofSeconds(env.getWaitTimeOut()));
            WebDriverWait wait2 = new WebDriverWait(driver2, Duration.ofSeconds(env.getWaitTimeOut()));
            WebDriverWait wait3 = new WebDriverWait(driver3, Duration.ofSeconds(env.getWaitTimeOut()));
            WebDriverWait wait4 = new WebDriverWait(driver4, Duration.ofSeconds(env.getWaitTimeOut()));


                try {
                    CountDownLatch latch = new CountDownLatch(env.getUsersActives());

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
                    if(env.getUsersActives() == 4){
                        preSaleService.runFullPreSaleProcess(env.getUser1(),
                                env.getPassword1(), latch, driver1, wait1 );

                        preSaleService.runFullPreSaleProcess(env.getUser2(),
                                env.getPassword2(), latch, driver2, wait2 );

                        preSaleService.runFullPreSaleProcess(env.getUser3(),
                                env.getPassword3(), latch, driver3, wait3 );

                        preSaleService.runFullPreSaleProcess(env.getUser4(),
                                env.getPassword4(), latch, driver4, wait4 );
                    }
                    latch.await();

                }  catch (Exception e) {
                    log.error("ERROR IN FULL PROCESS: {}", e.getClass().getSimpleName());
                }
            System.exit(1);
        }
    }
}