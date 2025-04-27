package com.TestBoot.boot_001.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class EnterTextIfEmpty implements Task {

    private final By locator;
    private final String textToEnter;

    public EnterTextIfEmpty(By locator, String textToEnter) {
        this.locator = locator;
        this.textToEnter = textToEnter;
    }

    public static EnterTextIfEmpty into(By locator, String textToEnter) {
        return instrumented(EnterTextIfEmpty.class, locator, textToEnter);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebElementFacade element = BrowseTheWeb.as(actor).find(locator);
        element.waitUntilVisible();  // espera que sea visible

        if (element.getValue() == null || element.getValue().isEmpty()) {
            element.type(textToEnter);
        }
    }
}
