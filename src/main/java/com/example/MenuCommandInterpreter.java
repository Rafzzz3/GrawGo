/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

import java.util.HashMap;
import java.util.Map;

import com.example.database.GameService;

/**
 * Klasa interpretująca polecenia związane z pokojami gry.
 */
public class MenuCommandInterpreter {
    /**
     * Mapa przechowująca dostępne komendy i ich wykonawców.
     */
    private Map<String, MenuCommandInterfaceExecutor> commands = new HashMap<>();

    /**
     * Konstruktor klasy RoomCommandInterpreter.
     * @param roomManager Menedżer pokoi gry.
     * @param player Obiekt obsługujący klienta.
     */
    public MenuCommandInterpreter(GameService gameService) {
        commands.put("PVP", new MenuCommandPVP());
        commands.put("PVE", new MenuCommandPVE());
        commands.put("ANALYZE", new MenuCommandANALYZE(gameService));
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
        MenuCommandInterfaceExecutor commandExecutor = commands.get(commandName);
        if (commandExecutor != null) {
            commandExecutor.execute(player, args);
        } else {
            player.getServerSender().sendMessage(LobbyMessageType.ERROR.name() +": Nieznana komenda pokoju: " + commandName);
        }
    }
}
