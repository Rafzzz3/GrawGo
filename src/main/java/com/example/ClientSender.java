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
    public void sendGui(String message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (Exception e) {
            System.out.println("Błąd wysyłania do klienta: " + e.getMessage());
        }
    }
    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (isRunning) {
                if (System.in.available() == 0) {
                    String message = scanner.nextLine();
                    sendGui(message);
                    if ("exit".equalsIgnoreCase(message)) {
                        isRunning = false;
                    }
                    Thread.sleep(100);
                }
            }
        } catch (Exception e) {
            System.out.println("Błąd wysyłania do klienta: " + e.getMessage());
        }
    }
    
}
