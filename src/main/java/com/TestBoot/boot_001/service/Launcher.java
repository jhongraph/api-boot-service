package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.config.ThreadPoolConfig;
import com.TestBoot.boot_001.pojos.User;
import com.TestBoot.boot_001.utils.GenerateUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;

@Component
@Slf4j
public class Launcher implements CommandLineRunner {

    private final Env env;
    private final ExecuteFullTask fullTask;
    private final ThreadPoolConfig threadConfig;
    private final GenerateUser generateUser;


    @Autowired
    public Launcher(ExecuteFullTask fullTask, Env env, ThreadPoolConfig threadConfig, GenerateUser generateUser) {
        this.fullTask = fullTask;
        this.env = env;
        this.threadConfig = threadConfig;
        this.generateUser = generateUser;
    }

    @Override
    public void run(String... args) throws InterruptedException {
        User userData = new User();
        CountDownLatch latch = new CountDownLatch(threadConfig.getCurrencyPossible());


        log.info("CANTIDAD DE CONCURRENCIAS SOLICITADAS {}", env.getConcurrency());
        for (int i = 0; i < env.getConcurrency(); i++) {
            generateUser.gettingUser(userData);
            try {

                fullTask.executeTask(userData.getUsername(), userData.getPassword(), latch);

            } catch (RejectedExecutionException e) {
                if (!threadConfig.isAlreadyNotifiedExecutorException()) {
                    log.warn("INSUFICIENCIA DE RECURSOS, EJECUTANDO CONCURRENCIAS POSIBLES [{}].", threadConfig.getCurrencyPossible());
                    threadConfig.setAlreadyNotifiedExecutorException(true);
                }
            }
        }
        latch.await();
        log.info("""
                
                ==============================================
                    HA FINALIZADO LA EJECUCION DE BOOT!
                ==============================================
                """);
        System.exit(1);
    }
}
