package com.TestBoot.boot_001.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Configuration
@EnableAsync
@Data
public class ThreadPoolConfig {

    @Autowired
    private final Env env;

    private int CurrencyPossible;

    private boolean alreadyNotifiedExecutorException;

    private boolean alreadyNotifiedVinWarn;

    private final AtomicInteger runningTasks = new AtomicInteger(0);

    public ThreadPoolConfig(Env env) {
        this.env = env;
    }

    @Bean(name = "launcherExecutor")
    public Executor launcherExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("Launcher-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "concurrencyExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {

        int maxTasks = maxTasks();

        this.alreadyNotifiedExecutorException = false;
        this.alreadyNotifiedVinWarn = false;

        return getThreadPoolTaskExecutor(maxTasks);
    }

    private int maxTasks(){

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        long maxMemory = Runtime.getRuntime().maxMemory();

        long memoryPerTask = 300 * 1024 * 1024;

        long maxTasksByMemory = maxMemory / memoryPerTask;

        int maxTasksByCpu = availableProcessors * 2;


        return  (int) Math.min(maxTasksByMemory, maxTasksByCpu);
    }

    private static ThreadPoolTaskExecutor getThreadPoolTaskExecutor(int corePoolSize) {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(corePoolSize);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("user-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
}