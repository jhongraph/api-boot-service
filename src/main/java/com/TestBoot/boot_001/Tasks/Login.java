package com.TestBoot.boot_001.Tasks;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.interactions.EnterText;
import com.TestBoot.boot_001.interactions.ClickOn;
import com.TestBoot.boot_001.interactions.WaitUntilUrlContains;
import com.TestBoot.boot_001.exception.LoginException;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.core.annotations.findby.By;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class Login implements Task {

    private final String username;
    private final String password;
    @Autowired
    private  Env env;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static Login withCredentials(String username, String password) {
        return Tasks.instrumented(Login.class, username, password);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        try {
log.debug("login iniciado");
            actor.attemptsTo(
                    EnterText.into(By.id("LoginUser_UserName"), username),
                    EnterText.into(By.id("LoginUser_Password"), password),
                    ClickOn.element(By.className("iniciar-sesin-1"))
            );

            WebElementFacade termsModal = BrowseTheWeb.as(actor).find(By.id("ucTermsConditions_btnSubmit"));
            if (termsModal.isCurrentlyVisible()) {
                actor.attemptsTo(
                        ClickOn.element(By.id("ucTermsConditions_gvTermConditionsBullets_chkChecked_0")),
                        ClickOn.element(By.id("ucTermsConditions_gvTermConditionsBullets_chkChecked_1")),
                        ClickOn.element(By.id("ucTermsConditions_gvTermConditionsBullets_chkChecked_2")),
                        ClickOn.element(By.id("ucTermsConditions_gvTermConditionsBullets_chkChecked_3")),
                        ClickOn.element(By.id("ucTermsConditions_btnSubmit"))
                );
            }

            actor.attemptsTo(
                    ClickOn.element(By.id("MainContent_navMenu_btnCarPartialRegistration"))
            );

            log.debug("login finalizado");


        } catch (Exception e) {
            throw new LoginException("Error during login: " + e.getMessage(), e);
        }
    }
}
