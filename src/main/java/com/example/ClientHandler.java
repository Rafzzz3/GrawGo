package com.example;
import java.io.IOException;
import java.net.Socket;
// to do Zaimplementować ClientHandler
public class ClientHandler implements Runnable {
    private Socket socket;
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}