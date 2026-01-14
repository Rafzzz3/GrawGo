package com.example;

import java.io.*;
import java.net.Socket;
//TO DO dokończyć socket clienta do komunikacji z serwerem
public class SocketClient {
    private Socket socket;
    private ClientSender clientSender;
    private ClientReceiver clientReceiver;
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
    public ClientSender getClientSender() {
        return clientSender;
    }
    public ClientReceiver getClientReceiver() {
        return clientReceiver;
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