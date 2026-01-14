package com.example;

import java.util.HashMap;
import java.util.Map;

public class RoomCommandInterpreter {
    private Map<String, RoomCommandInterfaceExecutor> commands = new HashMap<>();
    public RoomCommandInterpreter(RoomManager roomManager, ClientHandler player) {
        commands.put("CREATE", new RoomCommandCREATE());
        commands.put("LIST", new RoomCommandLIST());
        commands.put("JOIN", new RoomCommandJOIN());
        commands.put("READY", new RoomCommandREADY());
        commands.put("LEAVE", new RoomCommandLEAVE());
    }
    public void interpret(RoomManager roomManager, ClientHandler player, String message) {
        String[] commandMessage = message.trim().split(" ", 2);
        String commandName = commandMessage[0].trim().toUpperCase();
        String args = "";
        if (commandMessage.length > 1) {
            args = commandMessage[1];
        }
        RoomCommandInterfaceExecutor commandExecutor = commands.get(commandName);
        if (commandExecutor != null) {
            commandExecutor.execute(player, roomManager, args);
        } else {
            player.getServerSender().sendMessage(LobbyMessageType.ERROR.name() +": Nieznana komenda pokoju: " + commandName);
        }
    }
}
