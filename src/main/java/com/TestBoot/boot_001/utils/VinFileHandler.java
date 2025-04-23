package com.TestBoot.boot_001.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.List;

@Slf4j
@Component
public class VinFileHandler {

    private static final String VIN_FILE_PATH = "C:\\Users\\jgraph\\Documents\\JAVA\\boot-001\\Vins.txt";
    private static final int VIN_THRESHOLD = 100;

    public String obtenerYConsumirVin() {
        try {
            Path path = Paths.get(VIN_FILE_PATH);

            // Leer todas las líneas del archivo
            List<String> lines = Files.readAllLines(path);

            if (lines.isEmpty()) {
                log.error("El archivo de VINs está vacío.");
                return null;
            }

            // Obtener la primera línea (VIN a usar)
            String vin = lines.get(0).trim();

            // Eliminar la primera línea del archivo
            List<String> remaining = lines.subList(1, lines.size());
            Files.write(path, remaining, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            // Verificar si quedan menos de 100 VINs
            if (remaining.size() < VIN_THRESHOLD) {
                log.warn("Quedan menos de 100 VINs en el archivo: {}", remaining.size());
            }

            return vin;

        } catch (IOException e) {
            log.error("Error al manejar el archivo de VINs", e);
            return null;
        }
    }
}
