package com.TestBoot.boot_001.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.WebDriver;

public class WaitUntilUrlContains implements Task {

    private final String expectedUrl;

    public WaitUntilUrlContains(String expectedUrl) {
        this.expectedUrl = expectedUrl;
    }

    public static WaitUntilUrlContains urlContains(String expectedUrl) {
        return Tasks.instrumented(WaitUntilUrlContains.class, expectedUrl);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        while (!driver.getCurrentUrl().contains(expectedUrl)) {
            try {
                Thread.sleep(500); // Espera 0.5 segundos antes de volver a verificar
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
