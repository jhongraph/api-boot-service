package com.TestBoot.boot_001.service.preSale;

import com.TestBoot.boot_001.config.ThreadPoolConfig;
import com.TestBoot.boot_001.pojos.PreSaleRequest;
import com.TestBoot.boot_001.pojos.User;
import com.TestBoot.boot_001.service.factory.ServiceExecute;
import com.TestBoot.boot_001.utils.GenerateUser;
import com.TestBoot.boot_001.utils.WebSocketSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;

@AllArgsConstructor
@Slf4j
@Service("pre-sale")
public class PreSaleExecuteService implements ServiceExecute {

    @Autowired
    private final PreSaleService preSale;
    @Autowired
    private final ThreadPoolConfig threadConfig;
    @Autowired
    private final GenerateUser generateUser;
    @Autowired
    private final WebSocketSender sender;


    @Override
    @Async("launcherExecutor")
    public void execute(PreSaleRequest request) throws InterruptedException {

        String service = "pre-sale";
        String message;
        String startMessage = "PROCESO INICIADO EXITOSAMENTE";
        String finishMessage = "PROCESO FINALIZADO";

        int currencyActives = threadConfig.taskExecutor().getActiveCount();
        int maxCurrencyGlobal = threadConfig.taskExecutor().getMaxPoolSize();
        int concurrencyPossible = maxCurrencyGlobal - currencyActives;
        int concurrencyLatch = Math.min(concurrencyPossible, request.getConcurrency());
        CountDownLatch latch = new CountDownLatch(concurrencyLatch);

        User userData = new User();

        Thread.currentThread().setName("PRE-SALE-BOT");

        sender.sendLog("info", service, "CANTIDAD DE CONCURRENCIAS SOLICITADAS [" + request.getConcurrency() + "]");
        for (int i = 0; i < request.getConcurrency(); i++) {
            generateUser.gettingUser(userData);
            try {

                sender.sendEvent(service, startMessage, "PROCESS_START");
                sender.sendLog("info", service, startMessage);

                preSale.executeTask(request, latch);


            } catch (RejectedExecutionException e) {
                if (!threadConfig.isAlreadyNotifiedExecutorException()) {
                    sender.sendLog("warn", service, "INSUFICIENCIA DE RECURSOS, EJECUTANDO CONCURRENCIAS POSIBLES [" + concurrencyPossible + "].");
                    threadConfig.setAlreadyNotifiedExecutorException(true);
                }
            }
        }
        latch.await();
        message = """
                
                ==============================================
                    HA FINALIZADO LA EJECUCION DEL BOT!
                ==============================================
                """;

        sender.sendEvent(service, finishMessage, "PROCESS_FINISH");
        sender.sendLog("info", service, message);


    }
}
