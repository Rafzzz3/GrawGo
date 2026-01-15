/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasa odpowiedzialna za interpretację poleceń gry.
 */
public class GameCommandInterpreter {
    /** 
     * Mapa przechowująca dostępne komendy i ich wykonawców.
    */
    private Map<String, CommandInterfaceExecutor> commandList = new HashMap<>();
    /** 
     * Konstruktor inicjalizujący interpreter poleceń z dostępnymi komendami.
    */
    public GameCommandInterpreter() {
        commandList.put("MOVE", new CommandMOVE());
        commandList.put("PASS", new GameCommandPASS());
        commandList.put("SURRENDER", new GameCommandSURRENDER());
    }

    /** 
     * Metoda interpretująca i wykonująca polecenie gry.
     * @param game Obiekt gry, na którym wykonywane jest polecenie.
     * @param message Polecenie w formie łańcucha znaków.
     * @param player Obiekt obsługujący klienta, który wysłał polecenie.
    */
    public void interpret(Game game, String message, ClientHandler player) {
        String[] commandsMessage = message.trim().split(" ", 2);
        String commandName = commandsMessage[0].toUpperCase();
        String args = ""; 
        if (commandsMessage.length > 1) {
            args = commandsMessage[1]; 
        }
        CommandInterfaceExecutor command = commandList.get(commandName);
        if (command != null) {
            command.execute(game,args,player);
        } else {
            System.out.println("Nieznana komenda: " + commandName);
        }
    }
}
