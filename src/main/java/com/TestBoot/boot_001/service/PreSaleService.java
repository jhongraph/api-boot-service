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
    private  WebDriverWait wait;
    private final Env env;
    private final Generators generators;

    @Value("${Chrome_driver.path}")
    private String path;

    public PreSaleService(WebDriver driver, Env env, Generators generators) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(env.getWait_time_out()));
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
            log.info("══════════════════════════════════════════════");
            log.info("🛠️ [STEP 1] STARTING LOGIN PROCESS");
            log.info("🛠️ [PASO 1] INICIANDO PROCESO DE INICIO DE SESIÓN");
            log.info("══════════════════════════════════════════════");

            System.setProperty("webdriver.chrome.driver", path);
            driver.get(env.getBase_url() + env.getLogin_url());
            log.info("✔️ Navigated to login page: {}", env.getBase_url() + env.getLogin_url());
            log.info("✔️ Navegado a la página de inicio de sesión\n");

            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("LoginUser_UserNam")));

            if (usernameField == null) throw new LoginException("Error: login not precent in determinate time");

            log.info("-------------{}", usernameField);

            waitAndType(By.id("LoginUser_UserName"), env.getUser());
            log.info("✔️ Entered username");
            log.info("✔️ Nombre de usuario ingresado\n");

            waitAndType(By.id("LoginUser_Password"), env.getPassword());
            log.info("✔️ Entered password");
            log.info("✔️ Contraseña ingresada\n");

            waitAndClick(By.className("iniciar-sesin-1"));
            log.info("✔️ Submitted login form");
            log.info("✔️ Formulario de inicio enviado\n");

            wait.until(ExpectedConditions.urlContains(env.getHome_url()));
            log.info("✅ Login successful");
            log.info("✅ Inicio de sesión exitoso\n");

            waitAndClick(By.id("MainContent_navMenu_btnCarPartialRegistration"));
            log.info("✔️ Navigated to car registration section");
            log.info("✔️ Sección de registro de vehículos abierta\n");

            log.info("══════════════════════════════════════════════");
        }  catch (TimeoutException | ElementNotFoundException e) {
        log.error("Failed during login process");
        driver.quit();
        throw new SeleniumTimeoutException ("Failed during login process");
    }catch (Exception e) {
            log.error("Login failed");
            throw new LoginException("Failed during login process");
        }
    }

    public void fillFormAndContinue() {
        try {
            Car car = new Car();
            log.info("══════════════════════════════════════════════");
            log.info("📄 [STEP 2] FILLING OUT PRE-SALE FORM");
            log.info("📄 [PASO 2] LLENANDO FORMULARIO DE PREVENTA");
            log.info("══════════════════════════════════════════════");

            generators.generateCar(car);

            WebElement dropdown = driver.findElement(By.id("MainContent_ddlMySubType"));
            selectDropdownByValue("MainContent_ddlMySubType", "16");

            log.info("✔️ Selected 'SubType' dropdown option");
            log.info("✔️ Opción de 'SubTipo' seleccionada\n");

            waitAndType(By.id("MainContent_txtVIN"), generators.generateVin());
            log.info("✔️ Entered VIN number");
            log.info("✔️ Número VIN ingresado\n");

            waitAndClick(By.id("MainContent_BtnSearchVIN"));
            log.info("✔️ Submitted VIN search");
            log.info("✔️ Búsqueda de VIN enviada\n");

            WebElement yearInput = driver.findElement(By.id("MainContent_txtYear"));
            yearInput.clear();
            yearInput.sendKeys(String.valueOf(car.getYear()));
            log.info("✔️ Entered year");
            log.info("✔️ Año ingresado\n");

            waitAndType(By.id("MainContent_txtMake"), "");
            waitAndType(By.id("MainContent_txtMake"), car.getMake());
            log.info("✔️ Entered vehicle make");
            log.info("✔️ Marca del vehículo ingresada\n");

            waitAndType(By.id("MainContent_txtModel"), "");
            waitAndType(By.id("MainContent_txtModel"), car.getModel());
            log.info("✔️ Entered vehicle model");
            log.info("✔️ Modelo del vehículo ingresado\n");

            wait.until(driver -> !getAttributeValue(By.id("MainContent_txtYear")).isEmpty());
            log.info("✔️ VIN search results loaded");
            log.info("✔️ Resultados de búsqueda del VIN cargados\n");

            generators.insertRandomPlate(driver, wait);
            log.info("✔️ Random plate inserted");
            log.info("✔️ Tablilla aleatoria insertada\n");

            Select colorDropdown = new Select(driver.findElement(By.id("MainContent_ddlColor1")));
            List<WebElement> options = colorDropdown.getOptions();
            int randomIndex = new Random().nextInt(options.size() - 1) + 1;
            colorDropdown.selectByIndex(randomIndex);
            log.info("✔️ Selected random color");
            log.info("✔️ Color aleatorio seleccionado\n");

            waitAndType(By.id("MainContent_contracNumber"), generators.generateContractNumber());
            log.info("✔️ Entered contract number");
            log.info("✔️ Número de contrato ingresado\n");

            waitAndType(By.id("MainContent_txtLicenseNumber"), env.getLicence());
            log.info("✔️ Entered license number");
            log.info("✔️ Número de licencia ingresado\n");

            generators.selectRandomSaleDate(driver);
            log.info("✔️ Selected random sale date");
            log.info("✔️ Fecha de venta aleatoria seleccionada\n");

            generators.selectRandomReimbursementDate(driver);
            log.info("✔️ Selected random reimbursement date");
            log.info("✔️ Fecha de desembolso aleatoria seleccionada\n");

            waitAndClick(By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));
            log.info("✔️ Confirmed pre-sale data");
            log.info("✔️ Datos de preventa confirmados\n");

            waitAndClick(By.id("MainContent_btnConfirmationOwner"));
            log.info("✔️ Confirmed ownership");
            log.info("✔️ Propiedad confirmada\n");

            log.info("══════════════════════════════════════════════");
        }catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException | PdfUploadException | SignatureException e) {
            log.error("Form fill failed");
            driver.quit(); // Cierra el driver actual;
            throw new SeleniumTimeoutException ("Timeout during operation: ");
        } catch (Exception e) {
            log.error("Form fill failed");
            throw new FormException("Failed while filling out the form");
        }
    }

    public void performSignatureStep1() {
        try {
            log.info("══════════════════════════════════════════════");
            log.info("✍️ [STEP 3] PERFORMING SIGNATURE STEP 1");
            log.info("✍️ [PASO 3] REALIZANDO FIRMA 1");
            log.info("══════════════════════════════════════════════");

            waitAndClick(By.id("MainContent_chkConfirmation"));
            log.info("✔️ Selected first confirmation checkbox");
            log.info("✔️ Primer checkbox de confirmación seleccionado\n");

            waitAndClick(By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));
            log.info("✔️ Proceeded to next step");
            log.info("✔️ Avanzado al siguiente paso\n");

            log.info("══════════════════════════════════════════════");
        }catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException | PdfUploadException | SignatureException e) {
        log.error("Timeout occurred");
        driver.quit(); // Cierra el driver actual;
        throw new SignatureException ("Timeout during operation: ");
    }  catch (Exception e) {
            log.error("Signature step 1 failed");
            throw new SignatureException("Error during first signature confirmation");
        }
    }

    public void performSignatureStep2() {
        try {
            log.info("══════════════════════════════════════════════");
            log.info("✍️ [STEP 4] PERFORMING SIGNATURE STEP 2");
            log.info("✍️ [PASO 4] REALIZANDO FIRMA 2");
            log.info("══════════════════════════════════════════════");

            waitAndClick(By.id("MainContent_CheckBox1"));
            log.info("✔️ Selected second confirmation checkbox");
            log.info("✔️ Segundo checkbox de confirmación seleccionado\n");

            waitAndClick(By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));
            log.info("✔️ Proceeded to next step");
            log.info("✔️ Avanzado al siguiente paso\n");

            log.info("══════════════════════════════════════════════");
        }catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException | PdfUploadException | SignatureException e) {
            log.error("Timeout occurred");
            driver.quit(); // Cierra el driver actual;
            throw new SignatureException ("Error during second signature confirmation");
        }  catch (Exception e) {
            log.error("Signature step 2 failed");
            throw new SignatureException("Error during second signature confirmation");
        }
    }

    public void uploadPdfAndFinish() {
        try {
            log.info("══════════════════════════════════════════════");
            log.info("📄 [STEP 5] UPLOADING PDF AND FINALIZING PRE-SALE");
            log.info("📄 [PASO 5] SUBIENDO PDF Y FINALIZANDO PREVENTA");
            log.info("══════════════════════════════════════════════");

            log.info("Starting PDF upload...");
            log.info("Iniciando carga de PDF...\n");

            WebElement uploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input.ruButton.ruBrowse")));
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("input[type='file']")));

            String filePath = env.getPdf_route();
            fileInput.sendKeys(filePath);

            WebElement fileNameCell = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//td[normalize-space(text()) != '']")));
            log.info("✔️ PDF uploaded: {}", fileNameCell.getText());
            log.info("✔️ PDF subido: {} \n", fileNameCell.getText());

            Thread.sleep(2000);

            waitAndClick(By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"));
            log.info("✔️ Proceeded to final confirmation");
            log.info("✔️ Avanzado a la confirmación final\n");

            waitAndClick(By.id("MainContent_btnYes"));
            log.info("✔️ Pre-sale finalized");
            log.info("✔️ Preventa finalizada\n");

            log.info("══════════════════════════════════════════════");
        }catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException | PdfUploadException | SignatureException e) {
            log.error("Timeout occurred: {}", e.getMessage());
            driver.quit(); // Cierra el driver actual;
            throw new PdfUploadException ("error aboud upload pdf");
        } catch (Exception e) {
            log.error("Unexpected error uploading PDF\n");
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
            log.info("✔️ Login successful");
            log.info("✔️ Inicio de sesión exitoso\n");

            int zero = 1;
            int condition = env.getCiclo();

            while (zero <= condition) {


                fillFormAndContinue();
                log.info("✔️ Pre-sale form filled out");
                log.info("✔️ Formulario de preventa completado\n");

                performSignatureStep1();
                log.info("✔️ First signature completed");
                log.info("✔️ Primera firma completada\n");

                performSignatureStep2();
                log.info("✔️ Second signature completed");
                log.info("✔️ Segunda firma completada\n");

                uploadPdfAndFinish();
                log.info("✅ PDF uploaded and pre-sale finalized");
                log.info("️✅ PDF subido y preventa finalizada\n");

                log.info("══════════════════════════════════════════════");
                zero += 1;
            }

        }catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException | PdfUploadException | SignatureException e) {
            log.error("Timeout occurred");
            driver.quit(); // Cierra el driver actual;
            throw new SeleniumTimeoutException ("Timeout during operation");
        }  catch (Exception e) {
            log.error("Unexpected error in full process\n");
            throw new PreSaleException("Unexpected error during full pre-sale process");
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
                    preSaleService.runFullPreSaleProcess();
                    success = true;
                } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException | PdfUploadException | SignatureException e) {
                    attempt += 1;
                    log.error("❌ Attempt {} failed | Intento {} fallido", attempt, attempt);

                    if (attempt < maxAttempts) {
                        log.info("🔁 Retrying... | Reintentando...\n");
                        resetDriver();
                    } else {
                        log.error("⛔ Max retries reached | Se agotaron los reintentos. Abortando...\n");

                    }

                } catch (Exception e) {
                    log.error("💥 CRITICAL ERROR - FORCING SHUTDOWN | ERROR CRÍTICO - FORZANDO CIERRE", e);
                }
            }

            if (success) {
                log.info("🏁 Full pre-sale process completed | Proceso de preventa completado exitosamente\n");
                log.info("══════════════════════════════════════════════\n");
            }
            preSaleService.getDriver().quit();
            System.exit(1);
        }


        private void resetDriver() {
            if (preSaleService.getDriver() != null) {
                preSaleService.getDriver().quit();
            }
            preSaleService.initDriver();
        }
    }
}