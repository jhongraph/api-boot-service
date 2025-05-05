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

    private final VinFileHandler vinFileHandler;
    private final DomElement element;


    public Generators(VinFileHandler vinFileHandler, DomElement element) {
        this.vinFileHandler = vinFileHandler;
        this.element = element;
    }

    public void selectRandomColor(WebDriver driver) {
        Select colorDropdown = new Select(driver.findElement(By.id(element.getColorSelect())));
        List<WebElement> options = colorDropdown.getOptions();
        int randomIndex = new Random().nextInt(options.size() - 1) + 1;
        colorDropdown.selectByIndex(randomIndex);

    }

    public String generateVin() {
        return  vinFileHandler.obtenerYConsumirVin();
    }

    public String generateContractNumber() {
        String prefix = "102";
        String year = "002";
        String randomPart = String.format("%04d", new Random().nextInt(10000));

        return prefix + year + randomPart;
    }

    public void selectRandomSaleDate(WebDriver driver) {

        WebElement dateInput = driver.findElement(By.id(element.getSaleDateInput()));

        LocalDate dateNow = LocalDate.now();
        int beforeYear = dateNow.getYear() - 1;
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

        String generatedDate = String.format("%02d/%02d/%d", dia, mes, beforeYear);

        dateInput.clear();
        dateInput.sendKeys(generatedDate);
    }

    public void selectRandomReimbursementDate(WebDriver driver) {

        WebElement dateInput = driver.findElement(By.id(element.getReimbursementDateInput()));

        LocalDate dateNow = LocalDate.now();
        int beforeYear = dateNow.getYear() - 1;
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

        String generatedDate = String.format("%02d/%02d/%d", dia, mes, beforeYear);

        dateInput.clear();
        dateInput.sendKeys(generatedDate);
    }

    public void insertRandomPlate(WebDriverWait wait) {
        WebElement plateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id(element.getPlateInput())));

        String letters = generateLetters();

        String numbers = String.format("%03d", new Random().nextInt(1000));

        String plate = letters + numbers;

        plateInput.clear();
        plateInput.sendKeys(plate);
    }

    private String generateLetters() {
        Random random = new Random();
        StringBuilder letters = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            char letra = (char) ('A' + random.nextInt(26));
            letters.append(letra);
        }

        return letters.toString();
    }

    public String generateYear(){
        int currentYear = LocalDate.now().getYear();
        int minYear = currentYear - 2;

        Random random = new Random();
        int randomYear = random.nextInt(currentYear - minYear + 1) + minYear;
        return String.valueOf(randomYear);
    }

    public String generateMake() {
        String[] brands = {"TOYOTA", "LEXUS", "KIA", "HYUNDAI"};

        Random random = new Random();

        int randomIndex = random.nextInt(brands.length);

        return brands[randomIndex];
    }
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

        return "MARCA NO ENCONTRADA.";
    }

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
