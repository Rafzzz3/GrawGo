package com.example;

public class InAnalyzeState implements ClientHandlerState {
    private AnalyzeCommandInterpreter analyzeCommandInterpreter;
    public InAnalyzeState() {
        this.analyzeCommandInterpreter = new AnalyzeCommandInterpreter();
    }
    @Override 
    public void handleMessage(ClientHandler player, String message) {
        if ("EXIT".trim().equalsIgnoreCase(message)) {
            player.getServerSender().sendMessage("EXIT_ANALYZE");
        }
        analyzeCommandInterpreter.interpret(player, message);
    }
}
