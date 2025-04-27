package com.TestBoot.boot_001.utils;

import com.TestBoot.boot_001.pojos.Car;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Slf4j
@NoArgsConstructor
public class Generators {

    private  VinFileHandler vinFileHandler;

    public Generators(VinFileHandler vinFileHandler) {
        this.vinFileHandler = vinFileHandler;
    }
    public void selectRandomColor(WebDriver driver) {
        WebElement colorSelectElement = driver.findElement(By.id("MainContent_ddlColor1"));
        Select colorSelect = new Select(colorSelectElement);

        List<WebElement> opciones = colorSelect.getOptions().subList(1, colorSelect.getOptions().size());

        Random random = new Random();
        WebElement opcionAleatoria = opciones.get(random.nextInt(opciones.size()));

        colorSelect.selectByValue(opcionAleatoria.getAttribute("value"));

        log.info("Color seleccionado aleatoriamente: {}", opcionAleatoria.getText());
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

    public String selectRandomSaleDate(WebDriver driver) {

        LocalDate actualDate = LocalDate.now();
        int beforeYear = actualDate.getYear() - 1;
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

        return String.format("%02d/%02d/%d", dia, mes, beforeYear);

    }

    public String selectRandomReimbursementDate(WebDriver driver) {


        LocalDate actualDate = LocalDate.now();
        int añoAnterior = actualDate.getYear() - 1;
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

        return String.format("%02d/%02d/%d", dia, mes, añoAnterior);


    }

    public String insertRandomPlate(WebDriver driver, WebDriverWait wait) {

        String letters = generateLetters(3);

        String numbers = String.format("%03d", new Random().nextInt(1000));

        return letters + numbers;

    }

    private String generateLetters(int cantidad) {
        Random random = new Random();
        StringBuilder letters = new StringBuilder();

        for (int i = 0; i < cantidad; i++) {
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

        return "Marca no encontrada.";
    }

    public void generateCar(Car car){
        String year = generateYear();
        String make = generateMake();
        String model = generateModel(make);

        car.setYear(year);
        car.setMake(make);
        car.setModel(model);
    }

}
