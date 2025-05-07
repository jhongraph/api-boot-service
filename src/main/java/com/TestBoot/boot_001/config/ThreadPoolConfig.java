package com.TestBoot.boot_001.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Autowired
    private final Env env;

    @Getter
    private int CurrencyPossible;

    @Getter
    @Setter
    private boolean alreadyNotified;

    public ThreadPoolConfig(Env env) {
        this.env = env;
    }

    @Bean(name = "Executor")
    public ThreadPoolTaskExecutor taskExecutor() {

        // Obtener el número de tareas solicitadas por el usuario (recibido por variable de entorno)
        int requestedTasks = env.getConcurrency();

        // Obtener información sobre los recursos disponibles en la máquina
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        long maxMemory = Runtime.getRuntime().maxMemory();

        // Definir cuánta RAM se necesita por tarea (en este caso, 300 MB por tarea)
        long memoryPerTask = 300 * 1024 * 1024;

        // Calcular la cantidad máxima de tareas según la RAM disponible
        long maxTasksByMemory = maxMemory / memoryPerTask;

        // Calcular la cantidad máxima de tareas según la CPU (basado en los núcleos, considerando 2 tareas por nucleo)
        int maxTasksByCpu = availableProcessors * 2;

        // Calcular cuántas tareas se pueden ejecutar en función de la RAM y la CPU
        int maxTasks = (int)Math.min(maxTasksByMemory, maxTasksByCpu);

        // Comparar el número de tareas solicitadas con la capacidad máxima(esto dara el maximo de tareas que se pueden ejecutar dentro del rango solicitado)
        int tasksToExecute = Math.min(requestedTasks, maxTasks);

        this.CurrencyPossible = tasksToExecute;
        this.alreadyNotified = false;

        return getThreadPoolTaskExecutor(tasksToExecute);
    }

    private static ThreadPoolTaskExecutor getThreadPoolTaskExecutor(int corePoolSize) {

            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(corePoolSize);  // Usar el número calculado de hilos
            executor.setMaxPoolSize(corePoolSize); // maximo de tareas permitidas
            executor.setQueueCapacity(0); // no permite colas, rechaza las demás tareas
            executor.setThreadNamePrefix("user-");
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
            executor.setWaitForTasksToCompleteOnShutdown(true);
            executor.setAwaitTerminationSeconds(30);
            executor.initialize();
            return executor;
    }
}