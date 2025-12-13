package com.example;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
//
public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private CommandInterpreter commandInterpreter;
    public ClientHandler(Socket socket, CommandInterpreter commandInterpreter) {
        this.socket = socket;
        this.commandInterpreter = commandInterpreter;
    }
    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            while (!socket.isClosed()) {
                String clientcommand = (String) input.readObject();
                commandInterpreter.interpret(clientcommand);
                String response = "Odpowiedź serwera: " + clientcommand;
                output.writeObject(response);   
                output.flush();
            }
        } catch (EOFException e) {
            System.out.println("Gracz rozłączył się.");
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Błąd odczytu z klienta: " + e.getMessage());
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("Błąd zamknięcia zasobów: " + e.getMessage());
            }
        }
    }
}