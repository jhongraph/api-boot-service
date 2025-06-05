package com.TestBoot.boot_001.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Car {

    private String year = "";
    public String Make = "";
    private String model = "";
    private String vin = "";
}


