package com.example;
import java.util.HashMap;
import java.util.Map;
// TO DO - zaimplementować komendy i dodać je do mapy commandList
public class CommandInterpreter {
    private Map<String, CommandManager> commandList = new HashMap<>();
    public void interpret(String message) {
        String[] commandsMessage = message.trim().split(" ", 2);
        String commandName = commandsMessage[0];
        String args = ""; 
        if (commandsMessage.length > 1) {
            args = commandsMessage[1]; 
        }
        // to będzie nam wyciągało odpowiednią komendę
        CommandManager command = commandList.get(commandName);
        System.out.println("Wykonana komenda: " + commandName);
        // Jak zaimplementujemy komendy to odkomentować
        // if (command != null) {
        //     command.execute(args);
        // } else {
        //     System.out.println("Nieznana komenda: " + commandName);
        // }
    }
}
