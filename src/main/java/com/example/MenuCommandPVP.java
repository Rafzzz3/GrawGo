package com.example;

public class MenuCommandPVP implements MenuCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler player, String args) {
        player.switchToLobbyState();
    }
}
