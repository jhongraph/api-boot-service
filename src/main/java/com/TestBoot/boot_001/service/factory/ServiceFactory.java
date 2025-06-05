package com.TestBoot.boot_001.service.factory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class ServiceFactory {
    private final Map<String, ServiceExecute> services;

    public ServiceExecute getService(String name){
        ServiceExecute service = services.get(name);
        if (service == null) throw new IllegalArgumentException("SERVICE: [" + name + "]  DO NOT EXIST IN SYSTEM");
        return service;
    }
}
