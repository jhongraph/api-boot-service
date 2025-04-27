package com.TestBoot.boot_001.interactions;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.*;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;

public class WaitUntilAttributeNotEmpty implements Task {

    private final By locator;
    private final String attribute;

    public WaitUntilAttributeNotEmpty(By locator, String attribute) {
        this.locator = locator;
        this.attribute = attribute;
    }

    public static WaitUntilAttributeNotEmpty forAttribute(By locator, String attribute) {
        return Tasks.instrumented(WaitUntilAttributeNotEmpty.class, locator, attribute);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebElementFacade element = BrowseTheWeb.as(actor).find(locator);
        element.waitUntilVisible(); // Asegura que el elemento esté visible primero

        int retries = 0;
        String value = "";
        while (retries < 10) {
            value = element.getAttribute(attribute);
            if (value != null && !value.trim().isEmpty()) {
                break;
            }
            try {
                Thread.sleep(500); // Espera medio segundo antes de reintentar
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupción mientras se esperaba el atributo: " + attribute, e);
            }
            retries++;
        }

        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("El atributo '" + attribute + "' está vacío o no se encontró en: " + locator);
        }
    }
}
