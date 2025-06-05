package com.TestBoot.boot_001.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogPayload {
    private String timestamp;
    private String type;
    private String service;
    private String message;
}
