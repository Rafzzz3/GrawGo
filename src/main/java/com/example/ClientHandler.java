package com.example;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
//
public class ClientHandler implements Runnable {
    private Socket socket;
    private Room currentRoom = null;
    private RoomManager roomManager;
    private Game game;
    private ServerSender serverSender;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private RoomCommandInterpreter roomCommandInterpreter;
    private GameCommandInterpreter commandInterpreter;
    public ClientHandler( Socket socket, RoomManager roomManager) {
        this.socket = socket;
        this.roomManager = roomManager;
        this.commandInterpreter = new GameCommandInterpreter();
        this.roomCommandInterpreter = new RoomCommandInterpreter(roomManager, this);
    }
    @Override
    public void run() {
        try {
            this.serverSender = new ServerSender(new ObjectOutputStream(socket.getOutputStream()));
            input = new ObjectInputStream(socket.getInputStream());
            serverSender.sendMessage("Witaj na serwerze. Dostępne komendy pokoju to: CREATE, LIST, JOIN.");
            while (!socket.isClosed()) {
                String clientcommand = (String) input.readObject();
                System.out.println("Otrzymano odpowiedź od gracza: " + clientcommand);
                if (currentRoom == null) {
                    roomCommandInterpreter.interpret(roomManager, this, clientcommand);
                } else {
                    game = currentRoom.getGame();
                    commandInterpreter.interpret(game, clientcommand);
                }
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
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }
    public ServerSender getServerSender() {
        return serverSender;
    }
}