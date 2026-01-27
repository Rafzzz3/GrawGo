package com.example;

public class InRoomState implements ClientHandlerState {
    private RoomCommandInterpreter roomCommandInterpreter;
    private RoomManager roomManager;
    public InRoomState(ClientHandler player, RoomManager roomManager) {
        this.roomCommandInterpreter = new RoomCommandInterpreter(roomManager, player);
    }
    @Override
    public void handleMessage(ClientHandler player, String message) {
        roomCommandInterpreter.interpret(roomManager, player, message);
    }
}
