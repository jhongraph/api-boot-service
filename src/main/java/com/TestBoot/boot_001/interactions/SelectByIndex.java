package com.TestBoot.boot_001.interactions;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.*;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class SelectByIndex implements Task {

    private final By locator;
    private final int index;

    public SelectByIndex(By locator, int index) {
        this.locator = locator;
        this.index = index;
    }

    public static SelectByIndex from(By locator, int index) {
        return Tasks.instrumented(SelectByIndex.class, locator, index);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebElementFacade dropdown = BrowseTheWeb.as(actor).find(locator);
        new Select(dropdown).selectByIndex(index);
    }
}
