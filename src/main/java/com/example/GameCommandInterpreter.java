package com.example;
import java.util.HashMap;
import java.util.Map;
// TO DO - zaimplementować komendy i dodać je do mapy commandList
public class GameCommandInterpreter {
    private Map<String, CommandInterfaceExecutor> commandList = new HashMap<>();
    public GameCommandInterpreter() {
        commandList.put("MOVE", new CommandMOVE());
    }
    public void interpret(Game game, String message, ClientHandler gracz) {
        String[] commandsMessage = message.trim().split(" ", 2);
        String commandName = commandsMessage[0];
        String args = ""; 
        if (commandsMessage.length > 1) {
            args = commandsMessage[1]; 
        }
        CommandInterfaceExecutor command = commandList.get(commandName);
        if (command != null) {
            command.execute(game,args,gracz);
        } else {
            System.out.println("Nieznana komenda: " + commandName);
        }
    }
}
