package com.TestBoot.boot_001.config;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class WebDriverConfig {

    // Asegúrate de que la clave en el yml sea 'Chrome_driver.path'
    @Value("${Chrome_driver.path}") // Configura la ruta desde application.yml o application.properties
    private String path;


    @Bean
    public WebDriver webDriver() {
        log.info("path -----------: " + path);
        // Establece el path del ChromeDriver
        System.setProperty("webdriver.chrome.driver", path);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito"); // No guarda sesión
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless=new"); // Usa esto si no necesitas interfaz
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");

        // Devuelve una nueva instancia de ChromeDriver
        return new ChromeDriver(options);
    }
}
