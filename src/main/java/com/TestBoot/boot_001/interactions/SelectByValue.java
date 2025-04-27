package com.TestBoot.boot_001.interactions;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.*;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class SelectByValue implements Task {

    private final By locator;
    private final String value;

    public SelectByValue(By locator, String value) {
        this.locator = locator;
        this.value = value;
    }

    public static SelectByValue from(By locator, String value) {
        return Tasks.instrumented(SelectByValue.class, locator, value);
    }


    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        WebElementFacade dropdown = BrowseTheWeb.as(actor).find(locator);
        new Select(dropdown).selectByValue(value);
    }
}

