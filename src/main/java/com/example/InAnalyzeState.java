package com.example;

import com.example.database.GameService;

public class InAnalyzeState implements ClientHandlerState {
    private AnalyzeCommandInterpreter analyzeCommandInterpreter;
    public InAnalyzeState(GameService gameService) {
        this.analyzeCommandInterpreter = new AnalyzeCommandInterpreter(gameService);
    }
    @Override 
    public void handleMessage(ClientHandler player, String message) {
        if ("EXIT".trim().equalsIgnoreCase(message)) {
            player.getServerSender().sendMessage("EXIT_ANALYZE");
            player.setAnalyzedGame(null);
            player.switchToMenuState();
            return;
        }
        analyzeCommandInterpreter.interpret(player, message);
    }
}
