package com.example;

import com.example.database.GameService;

public class InMenuState implements ClientHandlerState {
    private MenuCommandInterpreter menuCommandInterpreter;
    private RoomManager roomManager;
    public InMenuState(RoomManager roomManager, GameService gameService) {
        this.roomManager = roomManager;
        this.menuCommandInterpreter = new MenuCommandInterpreter(gameService);
    }
    @Override
    public void handleMessage(ClientHandler player, String message) {
        menuCommandInterpreter.interpret(roomManager,player,message);
    }
}
