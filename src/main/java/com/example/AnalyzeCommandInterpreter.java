package com.example;

import java.util.HashMap;
import java.util.Map;

public class AnalyzeCommandInterpreter {
    private Map<String, AnalyzeCommandInterfaceExecutor> commandList = new HashMap<>();
    public AnalyzeCommandInterpreter() {
        commandList.put("ANALYZEGAME", new AnalyzeCommandANALYZEGAME());
    }
    public void interpret(ClientHandler player, String message) {
        String[] commandsMessage = message.trim().split(" ", 2);
        String commandName = commandsMessage[0].toUpperCase();
        String args = ""; 
        if (commandsMessage.length > 1) {
            args = commandsMessage[1]; 
        }
        AnalyzeCommandInterfaceExecutor command = commandList.get(commandName);
        if (command != null) {
            command.execute(player,args);
        } else {
            System.out.println("Nieznana komenda: " + commandName);
        }
    }
}
