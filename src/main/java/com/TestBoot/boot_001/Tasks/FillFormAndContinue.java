package com.TestBoot.boot_001.Tasks;

import com.TestBoot.boot_001.exception.FormException;
import com.TestBoot.boot_001.interactions.*;
import com.TestBoot.boot_001.pojos.Car;
import com.TestBoot.boot_001.utils.Generators;
import com.TestBoot.boot_001.config.Env;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;


import java.time.Duration;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Component
public class FillFormAndContinue implements Task {

    private final Env env;
    private final  Generators generators;

    public FillFormAndContinue(Env env, Generators generators) {
        this.env = env;
        this.generators = generators;
    }

    public static FillFormAndContinue withRandomData(Env env, Generators generators) {
        return Tasks.instrumented(FillFormAndContinue.class, env, generators );
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        log.debug("---------------------------- iniciando fill");
        log.debug("---------------------------- "+env.getMode());
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            Car car = new Car();
            generators.generateCar(car);

            actor.attemptsTo(
                    SelectByValue.from(By.id("MainContent_ddlMySubType"), "16")
            );

            log.info("--------------- registro selecionado");
            log.debug("¿Generators es null? {}", generators.generateVin() );


            car.setVin(generators.generateVin());
            log.debug("---------" + car.getVin());


            log.debug("fill iniciado");
            actor.attemptsTo(
                    EnterText.into(By.id("MainContent_txtVIN"), car.getVin()),
                    ClickOn.element(By.id("MainContent_BtnSearchVIN"))
            );

            actor.attemptsTo(
                    EnterTextIfEmpty.into(By.id("MainContent_txtYear"), String.valueOf(car.getYear())),
                    EnterTextIfEmpty.into(By.id("MainContent_txtMake"), String.valueOf(car.getYear())),
                    EnterTextIfEmpty.into(By.id("MainContent_txtModel"), String.valueOf(car.getYear()))
            );

            String plate = generators.insertRandomPlate(driver, wait);

            actor.attemptsTo(
                    EnterText.into(By.id("MainContent_txtPlateNumber"), plate)
            );

            WebElementFacade dropdown = BrowseTheWeb.as(actor).find(By.id("MainContent_ddlColor1"));
            int optionsSize = dropdown.thenFindAll("option").size();
            int randomIndex = new Random().nextInt(optionsSize - 1) + 1;

            actor.attemptsTo(
                    SelectByIndex.from(By.id("MainContent_ddlColor1"), randomIndex)
            );


            String contract = generators.generateContractNumber();

            actor.attemptsTo(
                    EnterText.into(By.id("MainContent_contracNumber"), contract),
                    EnterText.into(By.id("MainContent_txtLicenseNumber"), env.getLicence())
            );

            String saleDate = generators.selectRandomSaleDate(driver);
            String disbursementDate = generators.selectRandomReimbursementDate(driver);

            log.debug("desembolso----" + disbursementDate);


            actor.attemptsTo(
                    EnterText.into(By.id("MainContent_SaleDate"), saleDate),
                    EnterText.into(By.id("MainContent_DisbursementDate"), disbursementDate)
            );



                actor.attemptsTo(
                        EnterText.into(By.id("MainContent_txtLicense"), env.getConcesionario()),
                        ClickOn.element(By.id("MainContent_BtnSearchLicense"))
                );

                String concessionaryName = actor.asksFor(AttributeValue.of(By.id("MainContent_txtConcesionarioName"), "value"));
                String licenceName = actor.asksFor(AttributeValue.of(By.id("MainContent_txtLicenseDate"), "value"));


                if ((concessionaryName == null || concessionaryName.trim().isEmpty()) ||
                        (licenceName == null || licenceName.trim().isEmpty())) {
                    throw new FormException("valores de licencia de dealer null: ");
                }


            actor.attemptsTo(
                    ClickOn.element(By.id("PageFunctionsContent_ucRightFunctionBox_lvwFunctions_imbFunction_0")),
                    ClickOn.element(By.id("MainContent_btnConfirmationOwner"))
            );

            log.debug("fill finalizado");

        } catch (TimeoutException | NoSuchElementException | FormException  e) {
            throw new FormException("Error: " + e.getMessage());
        } catch (Exception e) {
            throw new FormException("falla al momento de autorrellenar formulario pre-venta: " + e.getMessage());
        }
    }
}
