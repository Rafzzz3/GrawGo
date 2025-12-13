package com.example;
import java.io.ObjectOutputStream;
// TO DO - zaimplementować wysyłanie wiadomości do klienta i zrobienia klasy do odbioru wiadomości od klienta w osobnej klasie
public class ClientSender implements Runnable {
    private ObjectOutputStream output;
    private String message;

    public ClientSender(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void run() {
        try {
            output.writeObject(message);
            output.flush();

        } catch (Exception e) {
            System.out.println("Błąd wysyłania do klienta: " + e.getMessage());
        }
    }
    
}
