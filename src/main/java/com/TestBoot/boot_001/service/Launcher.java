package com.TestBoot.boot_001.service;

import com.TestBoot.boot_001.config.Env;
import com.TestBoot.boot_001.config.ThreadPoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Launcher implements CommandLineRunner {

    private final Env env;
    private final ExecuteFullTask fullTask;
    private final ThreadPoolConfig threadConfig;

    @Autowired
    public Launcher(ExecuteFullTask fullTask, Env env, ThreadPoolConfig threadConfig) {
        this.fullTask = fullTask;
        this.env = env;
        this.threadConfig = threadConfig;
    }

    @Override
    public void run(String... args) {
        log.info("CANTIDAD DE CONCURRENCIAS SOLICITADAS {}", env.getConcurrency());
        log.info("TAREAS EJECUTADAS CONSIDERANDO LA CAPACIDAD [{}] ", threadConfig.getCurrencyPossible());
        for (int i = 0; i < env.getConcurrency(); i++) {
            fullTask.ejecutarTarea();
        }
    }
}
