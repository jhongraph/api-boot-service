package com.TestBoot.boot_001.interactions;


import net.serenitybdd.screenplay.*;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

public class IsElementPresent implements Question<Boolean> {

    private final By locator;

    public IsElementPresent(By locator) {
        this.locator = locator;
    }

    public static IsElementPresent at(By locator) {
        return new IsElementPresent(locator);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            return BrowseTheWeb.as(actor).getDriver().findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
