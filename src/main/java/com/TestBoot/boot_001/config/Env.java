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

    private String user;
    private String password;
    private String licence;
    private String base_url;
    private String login_url;
    private  String home_url;
    private  String preventa_url;
    private String pdf_route;
    private int ciclo;
    private int wait_time_out;
}
