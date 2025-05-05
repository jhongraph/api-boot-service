package com.TestBoot.boot_001.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Component
public class ThreadPoolMonitor {

    private final ThreadPoolTaskExecutor executor;

    public ThreadPoolMonitor(@Qualifier("Executor") ThreadPoolTaskExecutor executor) {
        this.executor = executor;
    }

    @Scheduled(fixedRate = 5000)
    public void monitorThreadPool() {
        try {
            log.info("""
                            
                            === Thread Pool Status ===\
                            
                            Core Pool Size: {}
                            Current Pool Size: {}
                            Active Threads: {}
                            Max Pool Size: {}
                            Completed Tasks: {}
                            Queue Size: {}""",
                    executor.getCorePoolSize(),
                    executor.getPoolSize(),
                    executor.getActiveCount(),
                    executor.getMaxPoolSize(),
                    executor.getThreadPoolExecutor().getCompletedTaskCount(),
                    executor.getThreadPoolExecutor().getQueue().size());
        } catch (Exception e) {
            log.error("Error monitoring thread pool: {}", e.getMessage());
        }
    }
}