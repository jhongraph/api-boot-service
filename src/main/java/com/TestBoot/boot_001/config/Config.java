package com.TestBoot.boot_001.config;

import com.TestBoot.boot_001.utils.Generators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Date;

@Slf4j
@Configuration
public class Config {


    @Bean
    public Generators generators() {

        return new Generators();
    }
}

