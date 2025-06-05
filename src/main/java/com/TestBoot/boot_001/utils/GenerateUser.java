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

@Component
@Data
@Slf4j
@AllArgsConstructor
public class GenerateUser {

    @Autowired
    private Users usersConfig;

    public void gettingUser(User userData) {
        int currency = userData.getCurrency();

        Map<String, String> users = usersConfig.getUsers();

        if (users != null && !users.isEmpty()) {
            List<Map.Entry<String, String>> entries = new ArrayList<>(users.entrySet());


            Map.Entry<String, String> randomEntry = entries.get(currency);

            String user = randomEntry.getKey();
            String password = randomEntry.getValue();

            userData.setUsername(user);
            userData.setPassword(password);
            userData.setCurrency(currency == entries.size() - 1 ? 0 : currency + 1);


        } else {
            log.warn("NO HAY USUARIOS DISPONIBLES.");
        }
    }
}
