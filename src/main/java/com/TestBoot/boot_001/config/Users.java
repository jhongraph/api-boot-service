package com.TestBoot.boot_001.config;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "users")
public class Users {
    private Map<String, String> users;

}
