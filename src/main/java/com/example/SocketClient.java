package com.example;

import java.io.*;
import java.net.Socket;
public class SocketClient {
    public static void main(String[] args) {
        int port = 4444; 
        System.out.println("Łączenie z serwerem " + ": " + port);
        try (Socket currentSocket = new Socket("localhost", port)) {
            System.out.println("Połączono! exit to wyjście.");
            ObjectOutputStream out = new ObjectOutputStream(currentSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(currentSocket.getInputStream());
            
            Thread receiverThread = new Thread(new ClientReceiver(in));
            Thread senderThread = new Thread(new ClientSender(out));

            senderThread.start();
            receiverThread.start();
            senderThread.join();

        } catch (IOException | InterruptedException e) {
            System.err.println("Błąd klienta: " + e.getMessage());
            e.printStackTrace();
        }
    }
}