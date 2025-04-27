package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.Tasks.FillFormAndContinue;
import com.TestBoot.boot_001.Tasks.Login;
import com.TestBoot.boot_001.Tasks.Signatures;
import com.TestBoot.boot_001.Tasks.UploadPdf;
import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.interactions.AttributeValue;
import com.TestBoot.boot_001.utils.Generators;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Open;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@Slf4j
public class Launcher implements CommandLineRunner {

    @Value("${chrome.driver.path}")
    private String path;

    private final Env env ;
    private final Generators  generators ;


    public Launcher(Env env, Generators generators) {
        this.env = env;
        this.generators = generators;
    }

    @Autowired


    public WebDriver initDriver(){

        System.setProperty("webdriver.chrome.driver", path);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--headless=new");
        options.addArguments("--user-data-dir=/tmp/chrome-profile-" + UUID.randomUUID());


        return new ChromeDriver(options);
    }

    @Override
    public void run(String... args) throws Exception {
        WebDriver driver = initDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));




        try{
            String username = "jovidio";
            String password = "123456";

            log.info("url: {},{}",env.getBaseUrl(),env.getLoginUrl());

        Actor actor = Actor.named("TEST");
        actor.can(BrowseTheWeb.with(driver));

        actor.attemptsTo(
                Open.url(env.getBaseUrl() + env.getLoginUrl()),

            // Aquí tus Tasks uno por uno
                Login.withCredentials(username, password),
                FillFormAndContinue.withRandomData(env, generators),
                Signatures.withRandomData(),
                UploadPdf.withRandomData(env)
        );

        String vin = actor.asksFor(AttributeValue.of(By.id("MainContent_txtVIN"), "value"));
        System.out.println("VIN capturado: " + vin);
    }catch (Exception e){
            log.error("error en la linea 44 de run: ", e);

        }
    }
}
