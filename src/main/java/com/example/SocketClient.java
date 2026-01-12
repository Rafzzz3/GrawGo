package com.example;

import java.io.*;
import java.net.Socket;
import javafx.application.Application;
import javafx.application.Application;
//TO DO dokończyć socket clienta do komunikacji z serwerem
public class SocketClient {
    private Socket socket;
    private ClientSender clientSender;
    private ClientReceiver clientReceiver;
    private static SocketClient instance;
    // Nie wiem jeszcze ale może zamiast przekazywać wszędzie SocketClienta to zrobię singletona  
    public static void main(String[] args) {
        Application.launch(GameApp.class, args);
    }
    // To chyba jest do poprawki bo coś nie spina, ale zamysł jest taki żeby uruchamiać w osobnym wątku bo w FX 
    // Application.launch blokuje wątek główny i on robi wszystko i apka jest zamrożona
    public void connect() throws IOException {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 4444);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                clientSender = new ClientSender(out);
                clientReceiver = new ClientReceiver(in);
                new Thread(clientSender).start();
                new Thread(clientReceiver).start();
            } catch (IOException e) {
                System.out.println("Błąd połączenia z serwerem: " + e.getMessage());
            }
        }).start();
    }
    public void close() {
        try { 
            if (socket != null) {
                socket.close(); 
            }
        } catch (Exception e) {
            System.out.println("Błąd zamykania połączenia: " + e.getMessage());
        }
    }
}