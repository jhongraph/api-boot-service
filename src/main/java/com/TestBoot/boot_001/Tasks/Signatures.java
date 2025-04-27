package com.TestBoot.boot_001.Tasks;

import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.interactions.ClickOn;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

@Slf4j
public class Signatures implements Task {


    public static Signatures withRandomData() {
        return Tasks.instrumented(Signatures.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        try {

            actor.attemptsTo(
                   ClickOn.element(By.id("MainContent_chkConfirmation")),
                   ClickOn.element(By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0")),
                   ClickOn.element(By.id("MainContent_CheckBox1")),
                   ClickOn.element(By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"))
            );

            log.debug("------------ firmas finalizadas");

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            throw new SignatureException("Error: " + e.getMessage());
        } catch (Exception e) {
            throw new SignatureException("Error durante la primera firma digital: " + e.getMessage());
        }
    }
}
