package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.pojos.Car;
import com.TestBoot.boot_001.utils.Generators;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
public class PreSaleService {

    @Getter
    private WebDriver driver;
    private WebDriverWait wait;
    private final Env env;
    private final Generators generators;

    @Value("${Chrome_driver.path}")
    private String path;

    public PreSaleService(Env env, Generators generators) {
        this.env = env;
        this.generators = generators;
    }

    public void initDriver() {
        System.setProperty("webdriver.chrome.driver", path);
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(env.getWait_time_out()));
    }

    public void login() {
        try {
            log.info("==============================================");
            log.info("[STEP 1] STARTING LOGIN PROCESS");
            log.info("[PASO 1] INICIANDO PROCESO DE INICIO DE SESION");
            log.info("==============================================");

            System.setProperty("webdriver.chrome.driver", path);
            driver.get(env.getBase_url() + env.getLogin_url());
            log.info("[OK] Navigated to login page: {}", env.getBase_url() + env.getLogin_url());
            log.info("[OK] Navegado a la página de inicio de sesion");

            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("LoginUser_UserName")));

            if (usernameField == null) throw new LoginException("Error: login not present in determinate time");

            log.debug("Field found: {}", usernameField);

            waitAndType(By.id("LoginUser_UserName"), env.getUser());
            log.info("[OK] Entered username");
            log.info("[OK] Nombre de usuario ingresado");

            waitAndType(By.id("LoginUser_Password"), env.getPassword());
            log.info("[OK] Entered password");
            log.info("[OK] Contraseña ingresada");

            waitAndClick(By.className("iniciar-sesin-1"));
            log.info("[OK] Submitted login form");
            log.info("[OK] Formulario de inicio enviado");

            wait.until(ExpectedConditions.urlContains(env.getHome_url()));
            log.info("[SUCCESS] Login successful");
            log.info("[SUCCESS] Inicio de sesion exitoso");

            waitAndClick(By.id("MainContent_navMenu_btnCarPartialRegistration"));
            log.info("[OK] Navigated to car registration section");
            log.info("[OK] Seccion de registro de vehiculos abierta");

            log.info("==============================================");
        } catch (TimeoutException | ElementNotFoundException e) {
            log.error("[ERROR] Failed during login process");
            if (driver != null) {
                driver.quit();
            }
            throw new SeleniumTimeoutException("Failed during login process");
        } catch (Exception e) {
            log.error("[ERROR] Login failed");
            throw new LoginException("Failed during login process");
        }
    }

    public void fillFormAndContinue() {
        try {
            Car car = new Car();
            log.info("==============================================");
            log.info("[STEP 2] FILLING OUT PRE-SALE FORM");
            log.info("[PASO 2] LLENANDO FORMULARIO DE PREVENTA");
            log.info("==============================================");

            generators.generateCar(car);

            selectDropdownByValue("MainContent_ddlMySubType", "16");
            log.info("[OK] Selected 'SubType' dropdown option");
            log.info("[OK] Opcion de 'SubTipo' seleccionada");

            waitAndType(By.id("MainContent_txtVIN"), generators.generateVin());
            log.info("[OK] Entered VIN number");
            log.info("[OK] Numero VIN ingresado");

            waitAndClick(By.id("MainContent_BtnSearchVIN"));
            log.info("[OK] Submitted VIN search");
            log.info("[OK] Busqueda de VIN enviada");

            WebElement yearInput = driver.findElement(By.id("MainContent_txtYear"));
            yearInput.clear();
            yearInput.sendKeys(String.valueOf(car.getYear()));
            log.info("[OK] Entered year");
            log.info("[OK] Año ingresado");

            waitAndType(By.id("MainContent_txtMake"), "");
            waitAndType(By.id("MainContent_txtMake"), car.getMake());
            log.info("[OK] Entered vehicle make");
            log.info("[OK] Marca del vehiculo ingresada");

            waitAndType(By.id("MainContent_txtModel"), "");
            waitAndType(By.id("MainContent_txtModel"), car.getModel());
            log.info("[OK] Entered vehicle model");
            log.info("[OK] Modelo del vehiculo ingresado");

            wait.until(driver -> !getAttributeValue(By.id("MainContent_txtYear")).isEmpty());
            log.info("[OK] VIN search results loaded");
            log.info("[OK] Resultados de busqueda del VIN cargados");

            generators.insertRandomPlate(driver, wait);
            log.info("[OK] Random plate inserted");
            log.info("[OK] Tablilla aleatoria insertada");

            Select colorDropdown = new Select(driver.findElement(By.id("MainContent_ddlColor1")));
            List<WebElement> options = colorDropdown.getOptions();
            int randomIndex = new Random().nextInt(options.size() - 1) + 1;
            colorDropdown.selectByIndex(randomIndex);
            log.info("[OK] Selected random color");
            log.info("[OK] Color aleatorio seleccionado");

            waitAndType(By.id("MainContent_contracNumber"), generators.generateContractNumber());
            log.info("[OK] Entered contract number");
            log.info("[OK] Numero de contrato ingresado");

            waitAndType(By.id("MainContent_txtLicenseNumber"), env.getLicence());
            log.info("[OK] Entered license number");
            log.info("[OK] Numero de licencia ingresado");

            generators.selectRandomSaleDate(driver);
            log.info("[OK] Selected random sale date");
            log.info("[OK] Fecha de venta aleatoria seleccionada");

            generators.selectRandomReimbursementDate(driver);
            log.info("[OK] Selected random reimbursement date");
            log.info("[OK] Fecha de desembolso aleatoria seleccionada");

            waitAndClick(By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));
            log.info("[OK] Confirmed pre-sale data");
            log.info("[OK] Datos de preventa confirmados");

            waitAndClick(By.id("MainContent_btnConfirmationOwner"));
            log.info("[OK] Confirmed ownership");
            log.info("[OK] Propiedad confirmada");

            log.info("==============================================");
        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException | PdfUploadException | SignatureException e) {
            log.error("[ERROR] Form fill failed");
            if (driver != null) {
                driver.quit();
            }
            throw new SeleniumTimeoutException("Timeout during operation: ");
        } catch (Exception e) {
            log.error("[ERROR] Form fill failed");
            throw new FormException("Failed while filling out the form");
        }
    }

    public void performSignatureStep1() {
        try {
            log.info("==============================================");
            log.info("[STEP 3] PERFORMING SIGNATURE STEP 1");
            log.info("[PASO 3] REALIZANDO FIRMA 1");
            log.info("==============================================");

            waitAndClick(By.id("MainContent_chkConfirmation"));
            log.info("[OK] Selected first confirmation checkbox");
            log.info("[OK] Primer checkbox de confirmacion seleccionado");

            waitAndClick(By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));
            log.info("[OK] Proceeded to next step");
            log.info("[OK] Avanzado al siguiente paso");

            log.info("==============================================");
        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException | PdfUploadException | SignatureException e) {
            log.error("[ERROR] Timeout occurred");
            if (driver != null) {
                driver.quit();
            }
            throw new SignatureException("Timeout during operation: ");
        } catch (Exception e) {
            log.error("[ERROR] Signature step 1 failed");
            throw new SignatureException("Error during first signature confirmation");
        }
    }

    public void performSignatureStep2() {
        try {
            log.info("==============================================");
            log.info("[STEP 4] PERFORMING SIGNATURE STEP 2");
            log.info("[PASO 4] REALIZANDO FIRMA 2");
            log.info("==============================================");

            waitAndClick(By.id("MainContent_CheckBox1"));
            log.info("[OK] Selected second confirmation checkbox");
            log.info("[OK] Segundo checkbox de confirmacion seleccionado");

            waitAndClick(By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));
            log.info("[OK] Proceeded to next step");
            log.info("[OK] Avanzado al siguiente paso");

            log.info("==============================================");
        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException | PdfUploadException | SignatureException e) {
            log.error("[ERROR] Timeout occurred");
            if (driver != null) {
                driver.quit();
            }
            throw new SignatureException("Error during second signature confirmation");
        } catch (Exception e) {
            log.error("[ERROR] Signature step 2 failed");
            throw new SignatureException("Error during second signature confirmation");
        }
    }

    public void uploadPdfAndFinish() {
        try {
            log.info("==============================================");
            log.info("[STEP 5] UPLOADING PDF AND FINALIZING PRE-SALE");
            log.info("[PASO 5] SUBIENDO PDF Y FINALIZANDO PREVENTA");
            log.info("==============================================");

            log.info("[INFO] Starting PDF upload...");
            log.info("[INFO] Iniciando carga de PDF...");

            WebElement uploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input.ruButton.ruBrowse")));
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("input[type='file']")));

            String filePath = env.getPdf_route();
            fileInput.sendKeys(filePath);

            WebElement fileNameCell = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//td[normalize-space(text()) != '']")));
            log.info("[OK] PDF uploaded: {}", fileNameCell.getText());
            log.info("[OK] PDF subido: {}", fileNameCell.getText());

            Thread.sleep(2000);

            waitAndClick(By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));
            log.info("[OK] Proceeded to final confirmation");
            log.info("[OK] Avanzado a la confirmacion final");

            waitAndClick(By.id("MainContent_btnYes"));
            log.info("[OK] Pre-sale finalized");
            log.info("[OK] Preventa finalizada");

            log.info("==============================================");
        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException | PdfUploadException | SignatureException e) {
            log.error("[ERROR] Timeout occurred: {}", e.getMessage());
            if (driver != null) {
                driver.quit();
            }
            throw new PdfUploadException("error about upload pdf");
        } catch (Exception e) {
            log.error("[ERROR] Unexpected error uploading PDF");
            throw new PdfUploadException("Unexpected error during PDF upload");
        }
    }

    private void waitAndType(By locator, String value) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(value);
        } catch (Exception e) {
            throw new ElementNotFoundException("Unable to type into element: " + locator, e);
        }
    }

    private void waitAndClick(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            throw new ElementNotFoundException("Unable to click element: " + locator, e);
        }
    }

    private void selectDropdownByValue(String selectId, String value) {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(selectId)));
            new Select(dropdown).selectByValue(value);
        } catch (Exception e) {
            throw new ElementNotFoundException("Unable to select dropdown value: " + value, e);
        }
    }

    private String getAttributeValue(By locator) {
        try {
            return Objects.requireNonNull(driver.findElement(locator).getAttribute("value"));
        } catch (Exception e) {
            throw new ElementNotFoundException("Unable to get attribute value for element: " + locator, e);
        }
    }

    public void runFullPreSaleProcess() {
        try {
            login();
            log.info("[OK] Login successful");
            log.info("[OK] Inicio de sesion exitoso");

            int zero = 1;
            int condition = env.getCiclo();

            while (zero <= condition) {
                fillFormAndContinue();
                log.info("[OK] Pre-sale form filled out");
                log.info("[OK] Formulario de preventa completado");

                performSignatureStep1();
                log.info("[OK] First signature completed");
                log.info("[OK] Primera firma completada");

                performSignatureStep2();
                log.info("[OK] Second signature completed");
                log.info("[OK] Segunda firma completada");

                uploadPdfAndFinish();
                log.info("[SUCCESS] PDF uploaded and pre-sale finalized");
                log.info("[SUCCESS] PDF subido y preventa finalizada");

                log.info("==============================================");
                zero += 1;
            }

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException | PdfUploadException | SignatureException e) {
            log.error("[ERROR]  occurred: {}", e.getClass().getSimpleName());
            driver.quit();
            throw new SeleniumTimeoutException("error diring init app", e);

        } catch (Exception e) {
            driver.quit();
            log.error("[ERROR] Unexpected error in full process: {}", e.getClass().getSimpleName());
            throw new PreSaleException("Unexpected error during full pre-sale process", e);
        }
    }

    @Component
    @Slf4j
    public static class AppRunner implements CommandLineRunner {

        @Autowired
        private PreSaleService preSaleService;

        @Autowired
        private ApplicationContext context;

        @Override
        public void run(String... args) {
            int maxAttempts = 3;
            int attempt = 0;
            boolean success = false;

            while (attempt < maxAttempts && !success) {
                try {
                    preSaleService.initDriver();
                    preSaleService.runFullPreSaleProcess();
                    success = true;
                } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException | PdfUploadException | SignatureException e) {
                    attempt += 1;
                    log.error("[ERROR] Attempt {} failed | Intento {} fallido", attempt, attempt);

                    if (attempt < maxAttempts) {
                        log.info("[RETRY] Retrying... | Reintentando...");
                        preSaleService.getDriver().quit();
                    } else {
                        log.error("[FAIL] Max retries reached | Se agotaron los reintentos. Abortando...");
                    }

                } catch (Exception e) {
                    log.error("[CRITICAL] CRITICAL ERROR - FORCING SHUTDOWN | ERROR CRITICO - FORZANDO CIERRE", e);
                }
            }

            if (success) {
                log.info("[COMPLETED] Full pre-sale process completed | Proceso de preventa completado exitosamente");
                log.info("==============================================");
            }
            preSaleService.getDriver().quit();

            System.exit(1);
        }

    }
}