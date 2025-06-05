package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.ElementNotFoundException;
import com.TestBoot.boot_001.exception.LoginException;
import com.TestBoot.boot_001.exception.SeleniumTimeoutException;
import com.TestBoot.boot_001.utils.DomElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.TestBoot.boot_001.interactions.DomInteractions.waitAndClick;
import static com.TestBoot.boot_001.interactions.DomInteractions.waitAndType;

@Service
@Slf4j
public class LoginService {

    private final Env env;
    private final DomElement element;

    @Autowired
    public LoginService(Env env, DomElement element) {
        this.env = env;
        this.element = element;
    }



    public void login(String username, String password, WebDriver driver, WebDriverWait wait) {

        try {
            driver.get(env.getBaseUrl() + env.getLoginUrl());

            waitAndType(wait, By.id(element.getUsernameInput()), username);

            waitAndType(wait, By.id(element.getPasswordInput()), password);

            waitAndClick(wait, By.className(element.getSubmitLoginButton()));

            wait.until(ExpectedConditions.urlContains(env.getHomeUrl()));

            List<WebElement> buttons = driver.findElements(By.id(element.getTermConditionConfirmButton()));
            if (!buttons.isEmpty() && buttons.getFirst().isDisplayed()) {
                waitAndClick( wait, By.id(element.getTermConditionButton1()));
                waitAndClick( wait, By.id(element.getTermConditionButton2()));
                waitAndClick(wait, By.id(element.getTermConditionButton3()));
                waitAndClick( wait, By.id(element.getTermConditionButton4()));
                waitAndClick( wait, By.id(element.getTermConditionConfirmButton()));
            }

            waitAndClick(wait, By.id(element.getPreSaleButton()));

        } catch (TimeoutException | ElementNotFoundException e) {
            throw new SeleniumTimeoutException(e.getMessage());
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new LoginException(e.getMessage());
        }
    }


}
