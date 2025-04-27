package com.TestBoot.boot_001.Tasks;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.interactions.ClickOn;
import com.TestBoot.boot_001.interactions.ElementVisibility;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.SendKeys;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.springframework.stereotype.Component;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

@Component
@Slf4j
public class UploadPdf implements Task {
    private final Env env;

    public UploadPdf(Env env) {
        this.env = env;
    }

    public static UploadPdf withRandomData(Env env) {
        return Tasks.instrumented(UploadPdf.class, env);
    }


    @Override
    public <T extends Actor> void performAs(T actor) {
        String filePath = env.getPdfRoute();

        try {
            // Esperar que el botón y el input de archivo sean visibles
            actor.attemptsTo(
                    ElementVisibility.forElement(By.cssSelector("input.ruButton.ruBrowse")),
                    ElementVisibility.forElement(By.id("MainContent_ucAttachment770_gvwDocuments_lblFileType_1")),
                    SendKeys.of(filePath).into(By.id("MainContent_ucAttachment770_gvwDocuments_rauFileUpload_1file0")),
                    ElementVisibility.forElement(By.xpath("//td[normalize-space(text()) != '']"))
            );
            Thread.sleep(2000);
            // Esperar que el siguiente botón sea visible y luego hacer clic
            actor.attemptsTo(
                    WaitUntil.the(By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0"), isVisible())
                            .forNoMoreThan(10).seconds(),
                    ClickOn.element(By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0")),

                    WaitUntil.the(By.id("MainContent_btnYes"), isVisible())
                            .forNoMoreThan(10).seconds(),
                    ClickOn.element(By.id("MainContent_btnYes"))

            );

        } catch (TimeoutException | ElementNotFoundException | FormException | SeleniumTimeoutException |
                 PdfUploadException | SignatureException e) {
            throw new PdfUploadException("Error inesperado al cargar pdf: " + e.getMessage());
        } catch (Exception e) {
            throw new PdfUploadException("Error inesperado: " + e.getMessage());
        }
    }

}
