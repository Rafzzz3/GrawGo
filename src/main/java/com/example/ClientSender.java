package com.example;
import java.io.ObjectOutputStream;
// TO DO - zaimplementować wysyłanie wiadomości do klienta i zrobienia klasy do odbioru wiadomości od klienta w osobnej klasie
public class ClientSender implements Runnable {
    private ObjectOutputStream output;

    public ClientSender(ObjectOutputStream output) {
        this.output = output;
    }
    public void sendToGui(String message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (Exception e) {
            System.out.println("Błąd wysyłania do klienta: " + e.getMessage());
        }
    }
    @Override
    public void run() {
        
    }
    
    
}
