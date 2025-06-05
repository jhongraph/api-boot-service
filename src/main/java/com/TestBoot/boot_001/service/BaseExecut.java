package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.exception.*;
import com.TestBoot.boot_001.pojos.PreSaleRequest;
import com.TestBoot.boot_001.utils.DomElement;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static com.TestBoot.boot_001.interactions.DomInteractions.waitAndClick;
import static com.TestBoot.boot_001.interactions.DomInteractions.waitAndType;

public abstract class BaseExecut {

    private final Env env;
    private final DomElement element;
    @Value("${driver.path}")
    private String path;


    @Autowired
    public BaseExecut(Env env, DomElement element) {
        this.env = env;
        this.element = element;
    }

    public WebDriver initDriver() {

        System.setProperty("webdriver.edge.driver", path);

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--incognito");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--headless=new");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-data-dir=/tmp/chrome-profile-" + UUID.randomUUID());

        return new EdgeDriver(options);
    }



    public void login(String username, String password, String urlBase, String sesionBtn,  WebDriver driver, WebDriverWait wait) {

        try {
            driver.get(urlBase + env.getLoginUrl());

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

            waitAndClick(wait, By.id(sesionBtn));

        } catch (Exception e) {
            throw new LoginException(e.getMessage(), e);
        }
    }


    @Async("concurrencyExecutor")
    public abstract void executeTask(PreSaleRequest request, CountDownLatch latch);



}
