package com.TestBoot.boot_001.utils;

import com.TestBoot.boot_001.pojos.EventPayload;
import com.TestBoot.boot_001.pojos.LogPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendEvent(String service, String message, String eventName) {
        EventPayload eventPayload = new EventPayload();
        eventPayload.setTimestamp(LocalDateTime.now());
        eventPayload.setType("event");
        eventPayload.setService(service);
        eventPayload.setEventName(eventName);
        eventPayload.setDescription(message);

        messagingTemplate.convertAndSend("/topic/service/events", eventPayload);
    }


    public void sendLog(String type, String service, String message) {
        LogPayload payload = new LogPayload(LocalDateTime.now().toString(), "log", service, message);
        messagingTemplate.convertAndSend("/topic/service/logs", payload);

        if (type.equalsIgnoreCase("info")) log.info(message);
        if (type.equalsIgnoreCase("debug")) log.debug(message);
        if (type.equalsIgnoreCase("warn")) log.warn(message);
        if (type.equalsIgnoreCase("error")) log.error(message);

    }
}
