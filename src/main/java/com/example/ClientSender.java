package com.example;
import java.io.ObjectOutputStream;
import java.util.Scanner;
// TO DO - zaimplementować wysyłanie wiadomości do klienta i zrobienia klasy do odbioru wiadomości od klienta w osobnej klasie
public class ClientSender implements Runnable {
    private ObjectOutputStream output;
    private boolean isRunning = true;

    public ClientSender(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (isRunning) {
                String message = scanner.nextLine();
                output.writeObject(message);
                output.flush();
                if ("exit".equalsIgnoreCase(message)) {
                    isRunning = false;
                }
            }
        } catch (Exception e) {
            System.out.println("Błąd wysyłania do klienta: " + e.getMessage());
        }
    }
    
}
