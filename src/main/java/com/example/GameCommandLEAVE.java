package com.example;

public class GameCommandLEAVE implements CommandInterfaceExecutor {
    @Override
    public void execute(Game game, String args, ClientHandler player) {
        new RoomCommandLEAVE().execute(player, player.getRoomManager(), args);
    }
}