package com.example;
import java.io.IOException;
import java.io.ObjectOutputStream;
public class ServerSender {
    private ObjectOutputStream output;
    public ServerSender(ObjectOutputStream output) {
        this.output = output;
    }
    public synchronized void sendMessage(String message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.err.println("Błąd podczas wysyłania wiadomości do klienta: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
