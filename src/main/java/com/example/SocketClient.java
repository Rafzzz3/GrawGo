package com.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
// TO DO - zaimplementować klienta do komunikacji z serwerem ale to po wzrobieniu TO DO z ClientSendera, ja to zrobię jak coś
public class SocketClient {
    public static void main(String[] args) {
        int port = 4444; 
        System.out.println("Łączenie z serwerem " + ": " + port);
        try (Socket currentSocket = new Socket("localhost", port)) {
            System.out.println("Połączono! exit to wyjście.");

            ObjectOutputStream out = new ObjectOutputStream(currentSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(currentSocket.getInputStream());
            
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String message = scanner.nextLine();
                if ("exit".equals(message)) {
                    break;
                }
                out.writeObject(message);
                out.flush();
                Object response = in.readObject();
                System.out.println(response);
            }
            out.close();
            in.close();
            scanner.close();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Błąd klienta: " + e.getMessage());
            e.printStackTrace();
        }
    }
}