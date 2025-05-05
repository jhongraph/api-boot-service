package com.TestBoot.boot_001.utils;

import com.TestBoot.boot_001.config.Users;
import com.TestBoot.boot_001.pojos.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
@Data
@Slf4j
@AllArgsConstructor
public class GenerateUser {

    @Autowired
    private Users usersConfig;

    public User gettingUser() {
        Map<String, String> users = usersConfig.getUsers();

        User usersData = new User();

        if (users != null && !users.isEmpty()) {
            List<Map.Entry<String, String>> entries = new ArrayList<>(users.entrySet());

            Random random = new Random();
            Map.Entry<String, String> randomEntry = entries.get(random.nextInt(entries.size()));

            String user = randomEntry.getKey();
            String password = randomEntry.getValue();

            usersData.setUsername(user);
            usersData.setPassword(password);

            log.debug("user: {}, pass: {}", user, password);

        } else {
            log.warn("NO HAY USUARIOS DISPONIBLES.");
        }
        return usersData;
    }
}
