package com.example;
import java.io.IOException;
import java.io.ObjectInputStream;
public class ClientReceiver implements Runnable {
    private ObjectInputStream input;

    public ClientReceiver(ObjectInputStream input) {
        this.input = input;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object message = input.readObject();
                System.out.println("SERWER: " + message);
                System.out.flush();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Rozłaczono z serwerem lub wystąpił błąd: " + e.getMessage());
                break;
            }
        }
    }
    
}
