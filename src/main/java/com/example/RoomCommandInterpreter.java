/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa interpretująca polecenia związane z pokojami gry.
 */
public class RoomCommandInterpreter {
    /**
     * Mapa przechowująca dostępne komendy i ich wykonawców.
     */
    private Map<String, RoomCommandInterfaceExecutor> commands = new HashMap<>();

    /**
     * Konstruktor klasy RoomCommandInterpreter.
     * @param roomManager Menedżer pokoi gry.
     * @param player Obiekt obsługujący klienta.
     */
    public RoomCommandInterpreter(RoomManager roomManager, ClientHandler player) {
        commands.put("CREATE", new RoomCommandCREATE());
        commands.put("LIST", new RoomCommandLIST());
        commands.put("JOIN", new RoomCommandJOIN());
        commands.put("READY", new RoomCommandREADY());
        commands.put("LEAVE", new RoomCommandLEAVE());
    }

    /**
     * Metoda interpretująca i wykonująca polecenie związane z pokojem gry.
     * @param roomManager Menedżer pokoi gry.
     * @param player Obiekt obsługujący klienta, który wysłał polecenie.
     * @param message Polecenie w formie łańcucha znaków.
     */
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
