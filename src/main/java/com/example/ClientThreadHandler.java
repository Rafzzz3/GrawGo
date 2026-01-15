/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
    Klasa obsługująca wątki klientów, umożliwiająca zarządzanie wieloma klientami jednocześnie.
 */
public class ClientThreadHandler {
    /** 
     * ExecutorService do zarządzania wątkami klientów. Ustawiony na stałą pulę wątków - max 10.
    */
    private ExecutorService executor = Executors.newFixedThreadPool(10);
    /**
     * Metoda handleClient() przyjmuje obiekt Runnable reprezentujący obsługę klienta
     * i przekazuje go do wykonania przez ExecutorService.
     * @param clientHandler Obiekt Runnable reprezentujący obsługę klienta.
     */
    public void handleClient(Runnable clientHandler) {
        executor.execute(clientHandler);
    }
}
