package com.TestBoot.boot_001.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    private String year = "";
    public String Make = "";
    private String model = "";
    private String vin = "";
}


