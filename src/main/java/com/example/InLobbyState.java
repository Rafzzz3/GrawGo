package com.example;

public class InLobbyState implements ClientHandlerState {
    private RoomCommandInterpreter roomCommandInterpreter;
    private RoomManager roomManager;
    public InLobbyState(RoomManager roomManager, ClientHandler player) {
        this.roomManager = roomManager;
        this.roomCommandInterpreter = new RoomCommandInterpreter(roomManager, player);
    }
    @Override
    public void handleMessage(ClientHandler player, String message) {
        roomCommandInterpreter.interpret(roomManager, player, message);
    }
}
