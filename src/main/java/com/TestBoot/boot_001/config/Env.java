package com.TestBoot.boot_001.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "utilities")
public class Env {

    private String user1;
    private String user2;
    private String user3;
    private String password1;
    private String password2;
    private String password3;
    private String licence;
    private String baseUrl;
    private String loginUrl;
    private  String homeUrl;
    private  String preventaUrl;
    private String pdfRoute;
    private String GeneratorVinkey;
    private String concesionario;
    private int ciclo;
    private int waitTimeOut;
    private int usersActives;
}
