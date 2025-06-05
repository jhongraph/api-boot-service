package com.TestBoot.boot_001.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PreSaleRequest {

    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("concurrency")
    private int concurrency;
    @JsonProperty("processQuantity")
    private int processQuantity;
    @JsonProperty("UrlBase")
    private String UrlBase;

}
