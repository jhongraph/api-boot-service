package com.TestBoot.boot_001.utils;

import com.TestBoot.boot_001.pojos.Car;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Slf4j
public class Generators {

    // Selects a random color from a dropdown list.
    // Selecciona un color aleatorio de una lista desplegable.
    public void selectRandomColor(WebDriver driver) {
        WebElement colorSelectElement = driver.findElement(By.id("MainContent_ddlColor1"));
        Select colorSelect = new Select(colorSelectElement);

        List<WebElement> opciones = colorSelect.getOptions().subList(1, colorSelect.getOptions().size());

        Random random = new Random();
        WebElement opcionAleatoria = opciones.get(random.nextInt(opciones.size()));

        colorSelect.selectByValue(opcionAleatoria.getAttribute("value"));

        log.info("Color seleccionado aleatoriamente: {}", opcionAleatoria.getText());
    }

    // Generates a random 17-character VIN (Vehicle Identification Number).
    // Genera un VIN (Número de Identificación de Vehículo) aleatorio de 17 caracteres.
    public String generateVin() {
        StringBuilder vin = new StringBuilder();
        String VIN_CHARS = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789";
        Random random = new Random();

        for (int i = 0; i < 17; i++) {
            vin.append(VIN_CHARS.charAt(random.nextInt(VIN_CHARS.length())));
        }

        return vin.toString();
    }

    // Generates a random contract number based on a specific pattern.
    // Genera un número de contrato aleatorio basado en un patrón específico.
    public String generateContractNumber() {
        String prefix = "102";
        String year = "002";
        String randomPart = String.format("%04d", new Random().nextInt(10000));

        return prefix + year + randomPart;
    }

    // Selects a random sale date from the previous year and inputs it.
    // Selecciona una fecha de venta aleatoria del año anterior e ingresarla.
    public void selectRandomSaleDate(WebDriver driver) {

        WebElement fechaInput = driver.findElement(By.id("MainContent_SaleDate"));

        LocalDate fechaActual = LocalDate.now();
        int añoAnterior = fechaActual.getYear() - 1;
        Random random = new Random();

        int mes = random.nextInt(12) + 1;

        int dia;
        if (mes == 2) {
            dia = random.nextInt(28) + 1;
        } else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
            // Meses con 30 días
            dia = random.nextInt(30) + 1;
        } else {
            dia = random.nextInt(31) + 1;
        }

        String fechaGenerada = String.format("%02d/%02d/%d", dia, mes, añoAnterior);

        fechaInput.clear();
        fechaInput.sendKeys(fechaGenerada);
    }

    // Selects a random reimbursement date from the previous year and inputs it.
    // Selecciona una fecha de reembolso aleatoria del año anterior e ingresarla.
    public void selectRandomReimbursementDate(WebDriver driver) {

        WebElement fechaInput = driver.findElement(By.name("ctl00$MainContent$DisbursementDate"));

        LocalDate fechaActual = LocalDate.now();
        int añoAnterior = fechaActual.getYear() - 1;
        Random random = new Random();

        int mes = random.nextInt(12) + 1;

        int dia;
        if (mes == 2) {
            dia = random.nextInt(28) + 1;
        } else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
            dia = random.nextInt(30) + 1;
        } else {
            dia = random.nextInt(31) + 1;
        }

        String fechaGenerada = String.format("%02d/%02d/%d", dia, mes, añoAnterior);

        fechaInput.clear();
        fechaInput.sendKeys(fechaGenerada);
    }

    // Inserts a random plate number into the respective input field.
    // Inserta un número de placa aleatorio en el campo de entrada respectivo.
    public void insertRandomPlate(WebDriver driver, WebDriverWait wait) {
        WebElement placaInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("MainContent_txtPlateNumber")));

        String letras = generateLetters(3);

        String numeros = String.format("%03d", new Random().nextInt(1000));

        String placa = letras + numeros;

        placaInput.clear();
        placaInput.sendKeys(placa);
    }

    // Generates a string of random letters of a specified length.
    // Genera una cadena de letras aleatorias de una longitud específica.
    private String generateLetters(int cantidad) {
        Random random = new Random();
        StringBuilder letras = new StringBuilder();

        for (int i = 0; i < cantidad; i++) {
            char letra = (char) ('A' + random.nextInt(26));
            letras.append(letra);
        }

        return letras.toString();
    }

    // Generates a random year within a two-year range from the current year.
    // Genera un año aleatorio dentro de un rango de dos años desde el año actual.
    public String generateYear(){
        int currentYear = LocalDate.now().getYear();
        int minYear = currentYear - 2;

        Random random = new Random();
        int randomYear = random.nextInt(currentYear - minYear + 1) + minYear;
        log.info(String.valueOf(randomYear));

        return String.valueOf(randomYear);
    }

    // Generates a random make (brand) for a car.
    // Genera una marca (make) aleatoria para un coche.
    public String generateMake() {
        String[] brands = {"TOYOTA", "LEXUS", "KIA", "HYUNDAI"};

        Random random = new Random();

        int randomIndex = random.nextInt(brands.length);

        return brands[randomIndex];
    }

    // Generates a random model based on the car make.
    // Genera un modelo aleatorio basado en la marca del coche.
    public String generateModel(String make) {
        Random random = new Random();

        String[] toyotaModels = {"Corolla", "Camry", "Hilux", "Land Cruiser"};
        String[] lexusModels = {"RX", "NX", "ES", "LC"};
        String[] kiaModels = {"Seltos", "Sportage", "Carnival", "Stinger"};
        String[] hyundaiModels = {"Tucson", "Elantra", "Sonata", "Santa Fe"};

        make = make.toUpperCase();

        if (make.equals("TOYOTA")) return toyotaModels[random.nextInt(toyotaModels.length)];
        if (make.equals("LEXUS")) return  lexusModels[random.nextInt(lexusModels.length)];
        if (make.equals("KIA")) return  kiaModels[random.nextInt(kiaModels.length)];
        if (make.equals("HYUNDAI")) return  hyundaiModels[random.nextInt(hyundaiModels.length)];

        return "Marca no encontrada.";
    }

    // Generates a car object with random details.
    // Genera un objeto Car con detalles aleatorios.
    public void generateCar(Car car){
        String vin  = generateVin();
        String year = generateYear();
        String make = generateMake();
        String model = generateModel(make);

        car.setVin(vin);
        car.setYear(year);
        car.setMake(make);
        car.setModel(model);
    }

}
