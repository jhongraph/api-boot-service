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

            log.info("==============================================");
            log.info("[STEP 1] STARTING LOGIN PROCESS FOR USER: {}", username);
            log.info("[PASO 1] INICIANDO PROCESO DE INICIO DE SESION");
            log.info("==============================================\n");

            log.info("[OK] Navigated to login page: {}", env.getBaseUrl() + env.getLoginUrl());
            log.info("[OK] Navegado a la página de inicio de sesion\n");

            waitAndType(wait, By.id("LoginUser_UserName"), username);
            log.info("[OK] Entered username: {}", username);
            log.info("[OK] Nombre de usuario ingresado\n");

            waitAndType(wait, By.id("LoginUser_Password"), password);
            log.info("[OK] Entered password");
            log.info("[OK] Contraseña ingresada\n");

            waitAndClick(wait, By.className("iniciar-sesin-1"));
            log.info("[OK] Submitted login form");
            log.info("[OK] Formulario de inicio enviado\n");

            wait.until(ExpectedConditions.urlContains(env.getHomeUrl()));
            log.info("[SUCCESS] Login successful");
            log.info("[SUCCESS] Inicio de sesion exitoso\n");

            waitAndClick(wait, By.id("MainContent_navMenu_btnCarPartialRegistration"));
            log.info("[OK] Navigated to car registration section");
            log.info("[OK] Seccion de registro de vehiculos abierta");

            log.info("==============================================\n");
        } catch (TimeoutException | ElementNotFoundException e) {
            log.error("[ERROR] Failed during login process\n");
            throw new SeleniumTimeoutException("Failed during login process");
        } catch (Exception e) {
            log.error("[ERROR] Login failed\n");
            throw new LoginException("Failed during login process");
        }
    }

    public void fillFormAndContinue(WebDriver driverParam, WebDriverWait wait) {
        try {

            Car car = new Car();
            log.info("==============================================");
            log.info("[STEP 2] FILLING OUT PRE-SALE FORM");
            log.info("[PASO 2] LLENANDO FORMULARIO DE PREVENTA");
            log.info("==============================================\n");

            generators.generateCar(car);

            selectDropdownByValue(wait, "MainContent_ddlMySubType", "16");
            log.info("[OK] Selected 'SubType' dropdown option");
            log.info("[OK] Opcion de 'SubTipo' seleccionada\n");

            String vin = car.getVin();

            waitAndType(wait, By.id("MainContent_txtVIN"), vin);
            log.info("[OK] Entered VIN number");
            log.info("[OK] Numero VIN ingresado\n");

            waitAndClick(wait, By.id("MainContent_BtnSearchVIN"));
            log.info("[OK] Submitted VIN search");
            log.info("[OK] Busqueda de VIN enviada\n");

            WebElement yearInput = driverParam.findElement(By.id("MainContent_txtYear"));
            yearInput.clear();
            yearInput.sendKeys(String.valueOf(car.getYear()));
            log.info("[OK] Entered year");
            log.info("[OK] Año ingresado\n");

            waitAndType(wait, By.id("MainContent_txtMake"), "");
            waitAndType(wait, By.id("MainContent_txtMake"), car.getMake());
            log.info("[OK] Entered vehicle make");
            log.info("[OK] Marca del vehiculo ingresada\n");

            waitAndType(wait, By.id("MainContent_txtModel"), "");
            waitAndType(wait, By.id("MainContent_txtModel"), car.getModel());
            log.info("[OK] Entered vehicle model");
            log.info("[OK] Modelo del vehiculo ingresado\n");

            wait.until(driver -> !getAttributeValue(driverParam, By.id("MainContent_txtYear")).isEmpty());
            log.info("[OK] VIN search results loaded");
            log.info("[OK] Resultados de busqueda del VIN cargados\n");

            generators.insertRandomPlate(driverParam, wait);
            log.info("[OK] Random plate inserted");
            log.info("[OK] Tablilla aleatoria insertada\n");

            Select colorDropdown = new Select(driverParam.findElement(By.id("MainContent_ddlColor1")));
            List<WebElement> options = colorDropdown.getOptions();
            int randomIndex = new Random().nextInt(options.size() - 1) + 1;
            colorDropdown.selectByIndex(randomIndex);
            log.info("[OK] Selected random color");
            log.info("[OK] Color aleatorio seleccionado\n");

            String contract = generators.generateContractNumber();

            waitAndType(wait, By.id("MainContent_contracNumber"), contract);
            log.info("[OK] Entered contract number");
            log.info("[OK] Numero de contrato ingresado\n");

            waitAndType(wait, By.id("MainContent_txtLicenseNumber"), env.getLicence());
            log.info("[OK] Entered license number");
            log.info("[OK] Numero de licencia ingresado\n");

            generators.selectRandomSaleDate(driverParam);
            log.info("[OK] Selected random sale date");
            log.info("[OK] Fecha de venta aleatoria seleccionada\n");

            generators.selectRandomReimbursementDate(driverParam);
            log.info("[OK] Selected random reimbursement date");
            log.info("[OK] Fecha de desembolso aleatoria seleccionada\n");

            waitAndClick(wait, By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));
            log.info("[OK] Confirmed pre-sale data");
            log.info("[OK] Datos de preventa confirmados\n");

            waitAndClick(wait, By.id("MainContent_btnConfirmationOwner"));
            log.info("[OK] Confirmed ownership");
            log.info("[OK] Propiedad confirmada");

            log.info("------------contract: {},--vin: {}", contract, vin);

            log.info("==============================================\n");
        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            log.error("[ERROR] Form fill failed\n");
            throw new SeleniumTimeoutException("Timeout during operation:");
        } catch (Exception e) {
            log.error("[ERROR] Form fill failed\n");
            throw new FormException("Failed while filling out the form");
        }
    }

    public void performSignatureStep1(WebDriverWait wait) {
        try {
            log.info("==============================================");
            log.info("[STEP 3] PERFORMING SIGNATURE STEP 1");
            log.info("[PASO 3] REALIZANDO FIRMA 1");
            log.info("==============================================\n");

            waitAndClick(wait, By.id("MainContent_chkConfirmation"));
            log.info("[OK] Selected first confirmation checkbox");
            log.info("[OK] Primer checkbox de confirmacion seleccionado\n");

            waitAndClick(wait, By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));
            log.info("[OK] Proceeded to next step");
            log.info("[OK] Avanzado al siguiente paso");

            log.info("==============================================\n");
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
            log.info("==============================================");
            log.info("[STEP 4] PERFORMING SIGNATURE STEP 2");
            log.info("[PASO 4] REALIZANDO FIRMA 2");
            log.info("==============================================\n");

            waitAndClick(wait, By.id("MainContent_CheckBox1"));
            log.info("[OK] Selected second confirmation checkbox");
            log.info("[OK] Segundo checkbox de confirmacion seleccionado\n");

            waitAndClick(wait, By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));
            log.info("[OK] Proceeded to next step");
            log.info("[OK] Avanzado al siguiente paso");

            log.info("==============================================\n");
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
            log.info("==============================================");
            log.info("[STEP 5] UPLOADING PDF AND FINALIZING PRE-SALE");
            log.info("[PASO 5] SUBIENDO PDF Y FINALIZANDO PREVENTA");
            log.info("==============================================\n");

            log.info("[INFO] Starting PDF upload...");
            log.info("[INFO] Iniciando carga de PDF...");

            WebElement uploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input.ruButton.ruBrowse")));
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("input[type='file']")));

            String filePath = env.getPdfRoute();
            fileInput.sendKeys(filePath);

            WebElement fileNameCell = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//td[normalize-space(text()) != '']")));
            log.info("[OK] PDF uploaded: {}", fileNameCell.getText());
            log.info("[OK] PDF subido: {}\n", fileNameCell.getText());

            Thread.sleep(2000);

            waitAndClick(wait, By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));
            log.info("[OK] Proceeded to final confirmation");
            log.info("[OK] Avanzado a la confirmacion final\n");

            waitAndClick(wait, By.id("MainContent_btnYes"));
            log.info("[OK] Pre-sale finalized");
            log.info("[OK] Preventa finalizada");

            log.info("==============================================\n");
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
        int maxAttempts = 3;
        int attempt = 0;
        boolean success = false;

        while (attempt < maxAttempts && !success) {
            try {

                login(username, password, driver, wait);
                log.info("[OK] Login successful");
                log.info("[OK] Inicio de sesion exitoso\n");

                int zero = 1;
                int condition = env.getCiclo();

                while (zero <= condition) {
                    fillFormAndContinue(driver, wait);
                    log.info("[OK] Pre-sale form filled out");
                    log.info("[OK] Formulario de preventa completado\n");

                    performSignatureStep1(wait);
                    log.info("[OK] First signature completed");
                    log.info("[OK] Primera firma completada\n");

                    performSignatureStep2(wait);
                    log.info("[OK] Second signature completed");
                    log.info("[OK] Segunda firma completada\n");

                    uploadPdfAndFinish(wait);
                    log.info("[SUCCESS] PDF uploaded and pre-sale finalized");
                    log.info("[SUCCESS] PDF subido y preventa finalizada");

                    log.info("==============================================\n");
                    zero += 1;
                    attempt = 0;
                    success = true;
                }



            } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                     PdfUploadException | SignatureException e) {
                log.error("[ERROR]  occurred: {}\n", e.getClass().getSimpleName());
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

        if (success) {
            log.info("[COMPLETED] Full pre-sale process completed | Proceso de preventa completado exitosamente");
            log.info("==============================================\n");
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

                    log.info("=========================================");
                    log.info("       Starting users works");
                    log.info("=========================================");

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

                    log.info("=========================================");
                    log.info("         All process finished ");
                    log.info("=========================================");

                }  catch (Exception e) {
                    log.error("ERROR IN FULL PROCESS: {}", e.getClass().getSimpleName());
                    throw new PreSaleException("error: ", e);

                }
            System.exit(1);
        }

    }
}