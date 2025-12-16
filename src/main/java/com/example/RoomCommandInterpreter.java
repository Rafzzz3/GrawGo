package com.example;

import java.util.HashMap;
import java.util.Map;

public class RoomCommandInterpreter {
    private Map<String, RoomCommandInterfaceExecutor> commands = new HashMap<>();
    public RoomCommandInterpreter(RoomManager roomManager, ClientHandler gracz) {
        commands.put("CREATE", new RoomCommandCREATE());
        commands.put("LIST", new RoomCommandLIST());
        commands.put("JOIN", new RoomCommandJOIN());
    }
    public void interpret(RoomManager roomManager, ClientHandler gracz, String commandLine) {
        String[] parts = commandLine.split(" ", 2);
        String commandName = parts[0];
        if (parts.length < 2) {
            parts = new String[] {parts[0], ""};
        }
        String args = parts[1];
        RoomCommandInterfaceExecutor commandExecutor = commands.get(commandName);
        if (commandExecutor != null) {
            commandExecutor.execute(gracz, roomManager, args);
        } else {
            gracz.getServerSender().sendMessage("Nieznana komenda pokoju: " + commandName);
        }
    }
}
