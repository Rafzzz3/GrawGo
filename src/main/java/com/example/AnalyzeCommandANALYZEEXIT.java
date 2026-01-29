package com.example;

public class AnalyzeCommandANALYZEEXIT implements AnalyzeCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler player, String args) {
        player.setAnalyzedGame(null);       
        player.getServerSender().sendMessage("EXIT_ANALYZE");
        player.switchToMenuState();
    }
}
