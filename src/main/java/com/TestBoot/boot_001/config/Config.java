package com.TestBoot.boot_001.config;

import com.TestBoot.boot_001.utils.Generators;
import com.TestBoot.boot_001.utils.VinFileHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Date;

@Slf4j
@Configuration
@AllArgsConstructor
public class Config {

    @Autowired
    private VinFileHandler vinFileHandler;



    @Bean
    public Generators generators() {

        return new Generators(vinFileHandler);
    }
}

