package com.example;

public class MenuCommandANALYZE implements MenuCommandInterfaceExecutor {
    public void execute(ClientHandler player, String args) {
        player.switchToAnalyzeState();
    }
}
