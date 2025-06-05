package com.TestBoot.boot_001.interactions;

import com.TestBoot.boot_001.exception.ElementNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Objects;

public class DomInteractions {

    public static void waitAndType(WebDriverWait wait, By locator, String value) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(value);
        } catch (Exception e) {
            throw new ElementNotFoundException(e.getClass().getSimpleName() + ", " + "ELEMENTO NO DISPONIBLE PARA ESCRIBIR: " + locator, e);
        }
    }

    public static void waitAndClick(WebDriverWait wait, By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            throw new ElementNotFoundException(e.getClass().getSimpleName() + ", " + "ELEMENTO NO DISPONIBLE PARA CLICAR: " + locator, e);
        }
    }

    public static void selectDropdownByValue(WebDriverWait wait, String selectId, String value) {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(selectId)));
            new Select(dropdown).selectByValue(value);
        } catch (Exception e) {
            throw new ElementNotFoundException(e.getClass().getSimpleName() + ", " + "ELEMENTO NO ENCONTRADO: " + value, e);
        }
    }

    public static String getAttributeValue(WebDriver driver, By locator) {
        try {
            return Objects.requireNonNull(driver.findElement(locator).getAttribute("value"));
        } catch (Exception e) {
            throw new ElementNotFoundException(e.getClass().getSimpleName() + ", " + "NO SE PUDO OBTENER EL VALOR: " + locator, e);
        }
    }
}
