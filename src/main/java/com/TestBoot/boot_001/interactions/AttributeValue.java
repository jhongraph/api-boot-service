package com.TestBoot.boot_001.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AttributeValue implements Question<String> {

    private final By locator;
    private final String attribute;

    public AttributeValue(By locator, String attribute) {
        this.locator = locator;
        this.attribute = attribute;
    }

    public static AttributeValue of(By locator, String attribute) {
        return new AttributeValue(locator, attribute);
    }

    @Override
    public String answeredBy(Actor actor) {
        // Obtener el WebElementFacade
        WebElementFacade element = BrowseTheWeb.as(actor).find(locator);

        // Esperar hasta que el elemento sea visible
        element.waitUntilVisible();

        // Usar getWrappedElement() para acceder al WebElement y obtener el atributo
        WebElement webElement = element.getWrappedElement();

        // Retornar el atributo directamente con WebElement
        return webElement.getAttribute(attribute);
    }
}
