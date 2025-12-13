package com.example;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ClientThreadHandler {
    // to określa maxymalną liczbę graczy w puli
    private ExecutorService executor = Executors.newFixedThreadPool(10);
    public void handleClient(Runnable clientHandler) {
        executor.execute(clientHandler);
    }
}
