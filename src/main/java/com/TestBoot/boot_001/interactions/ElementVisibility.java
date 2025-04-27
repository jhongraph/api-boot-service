package com.TestBoot.boot_001.interactions;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;

public class ElementVisibility implements Task {

    private final By locator;

    public ElementVisibility(By locator) {
        this.locator = locator;
    }

    public static ElementVisibility forElement(By locator) {
        return new ElementVisibility(locator);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        // Obtener el WebElementFacade usando el actor
        WebElementFacade element = BrowseTheWeb.as(actor).find(locator);

        // Espera hasta que el elemento sea visible
        element.waitUntilVisible();
    }
}
