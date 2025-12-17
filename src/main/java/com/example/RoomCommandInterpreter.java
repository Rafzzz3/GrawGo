package com.example;

import java.util.HashMap;
import java.util.Map;

public class RoomCommandInterpreter {
    private Map<String, RoomCommandInterfaceExecutor> commands = new HashMap<>();
    public RoomCommandInterpreter(RoomManager roomManager, ClientHandler gracz) {
        commands.put("CREATE", new RoomCommandCREATE());
        commands.put("LIST", new RoomCommandLIST());
        commands.put("JOIN", new RoomCommandJOIN());
        commands.put("READY", new RoomCommandREADY());
    }
    public void interpret(RoomManager roomManager, ClientHandler gracz, String message) {
        String[] commandMessage = message.trim().split(" ", 2);
        String commandName = commandMessage[0].trim().toUpperCase();
        String args = "";
        if (commandMessage.length > 1) {
            args = commandMessage[1];
        }
        RoomCommandInterfaceExecutor commandExecutor = commands.get(commandName);
        if (commandExecutor != null) {
            commandExecutor.execute(gracz, roomManager, args);
        } else {
            gracz.getServerSender().sendMessage("Nieznana komenda pokoju: " + commandName);
        }
    }
}
