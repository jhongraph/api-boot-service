//package com.TestBoot.boot_001.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@Slf4j
//public class WebDriverConfig {
//
//    @Value("${chrome.driver.path}")
//    private String path;
//
//    @Bean
//    public WebDriver webDriver() {
//        log.info("path -----------: {}", path);
//        System.setProperty("webdriver.chrome.driver", path);
//
//        ChromeOptions options = new ChromeOptions();
////        options.addArguments("--incognito");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--disable-extensions");
////        options.addArguments("--headless=new");
//        options.addArguments("--disable-gpu");
//        options.addArguments("--remote-allow-origins=*");
//
//
//        return new ChromeDriver(options);
//    }
//
////    @Bean(name = "driver1")
////    public WebDriver driver1() {
////        return buildDriver();
////    }
////
////    @Bean(name = "driver2")
////    public WebDriver driver2() {
////        return buildDriver();
////    }
//
//}
