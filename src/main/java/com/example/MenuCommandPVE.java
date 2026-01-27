package com.example;

public class MenuCommandPVE implements MenuCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler player, String args) {
        player.switchToGameState();
    }
}
