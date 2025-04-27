package com.TestBoot.boot_001.interactions;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EnterText implements Task {

    private final String text;
    private final By locator;

    public EnterText(String text, By locator) {
        this.text = text;
        this.locator = locator;
    }

    public static EnterText into(By locator, String text) {
        return Tasks.instrumented(EnterText.class, text, locator);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));


        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        WebElementFacade element = BrowseTheWeb.as(actor).find(locator);
        element.waitUntilVisible().clear();
        element.sendKeys(text);
    }
}
