package com.TestBoot.boot_001.service.factory;

import com.TestBoot.boot_001.pojos.PreSaleRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class ExecutionService {
    @Autowired
    private final ServiceFactory factory;


    public ResponseEntity<String> execute(String serviceName, PreSaleRequest request) throws InterruptedException {
        try {

        ServiceExecute service = factory.getService(serviceName);
        service.execute(request);
            log.info("EJECUTANDO BOT PARA [{}] EXITOSAMENTE", serviceName);
        return ResponseEntity.ok("EJECUTANDO BOT PARA [" + serviceName +"] EXITOSAMENTE");
        }catch (Exception e){
            log.error("ERROR: {}, {}", e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.badRequest().body(String.format("ERROR: %s, %s", e.getClass().getSimpleName(), e.getMessage()));

        }
    }

}
