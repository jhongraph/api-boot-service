package com.TestBoot.boot_001.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventPayload {
    private String type;             // "event"
    private String service;          // "pre-sale", "ocr", etc.
    private String eventName;        // "STATUS_UPDATE", "PROCESS_FINISHED", etc.
    private String description;      // Mensaje del evento
    private LocalDateTime timestamp; // Hora
}