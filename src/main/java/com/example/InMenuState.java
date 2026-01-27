package com.example;


public class InMenuState implements ClientHandlerState {
    private MenuCommandInterpreter menuCommandInterpreter;
    private RoomManager roomManager;
    public InMenuState(RoomManager roomManager) {
        this.roomManager = roomManager;
        this.menuCommandInterpreter = new MenuCommandInterpreter();
    }
    @Override
    public void handleMessage(ClientHandler player, String message) {
        menuCommandInterpreter.interpret(roomManager,player,message);
    }
}
