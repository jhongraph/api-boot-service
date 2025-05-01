package com.TestBoot.boot_001.utils;

import com.TestBoot.boot_001.config.Env;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.List;

@Slf4j
@Component
public class VinFileHandler {

    Env env;
    @Autowired
    public VinFileHandler(Env env) {
        this.env = env;
    }

    public String obtenerYConsumirVin() {
        try {

            Path path = Paths.get(env.getVinBanckRoute());

            List<String> lines = Files.readAllLines(path);

            if (lines.isEmpty()) {
                log.error("El archivo de VINs está vacío.");
                return null;
            }

            String vin = lines.get(0).trim();

            List<String> remaining = lines.subList(1, lines.size());
            Files.write(path, remaining, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            if (remaining.size() < 500 && remaining.size() > 249) {
                log.warn("Quedan menos de 500 VINs en el archivo: {}", remaining.size());
            }

            if (remaining.size() < 250 && remaining.size() > 99) {
                log.warn("Quedan menos de 250 VINs en el archivo: {}", remaining.size());
            }

            if (remaining.size() < 100) {
                log.warn("Quedan menos de 100 VINs en el archivo: {}", remaining.size());
            }

            return vin;

        } catch (IOException e) {
            log.error("Error al manejar el archivo de VINs", e);
            return null;
        }
    }
    public boolean insertarVin(String vin) {
        try {
            Path path = Paths.get(env.getVinBanckRoute());

            List<String> lines = Files.readAllLines(path);
            if (lines.contains(vin)) {
                return false;
            }

            Files.write(path, (vin + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

            return true;

        } catch (IOException e) {
            return false;
        }
    }
}
