package com.TestBoot.boot_001.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "utilities")
public class Env {

    private List<String> users;
    private String licence;
    private String baseUrl;
    private String loginUrl;
    private  String homeUrl;
    private  String preventaUrl;
    private String pdfRoute;
    private String GeneratorVinkey;
    private String concesionario;
    private String vinBanckRoute;
    private int ciclo;
    private int waitTimeOut;
    private int concurrency;
}
