package com.TestBoot.boot_001.controllers;

import com.TestBoot.boot_001.pojos.PreSaleRequest;
import com.TestBoot.boot_001.service.factory.ExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RestController
public class ExecuteController {

    @Autowired
    private ExecutionService  launcher;

    @PostMapping("/execute")
    public ResponseEntity<String> execute (@RequestHeader("service") String nameService, @RequestBody PreSaleRequest request) throws InterruptedException {
        return   launcher.execute(nameService, request);
    }
}
