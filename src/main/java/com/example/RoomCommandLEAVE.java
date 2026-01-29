/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa obsługująca polecenie LEAVE do opuszczenia pokoju gry.
 */
public class RoomCommandLEAVE implements RoomCommandInterfaceExecutor {
    /** 
     * Metoda wykonująca polecenie LEAVE do opuszczenia pokoju gry.
     * @param player Obiekt obsługujący klienta, który wysłał polecenie.
     * @param roomManager Menedżer pokoi gry.
     * @param args Argumenty polecenia w formie łańcucha znaków (nieużywane).
    */
    @Override
    public void execute(ClientHandler player, RoomManager roomManager, String args) {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom != null) {
            List<ClientHandler> others = new ArrayList<>(currentRoom.getPlayers());
            others.remove(player);
            currentRoom.removePlayer(player);
            player.setCurrentRoom(null);
            player.switchToMenuState();
            player.getServerSender().sendMessage("EXIT_ROOM");

            for (ClientHandler opponent : others) {
                opponent.getServerSender().sendMessage(LobbyMessageType.INFO.name() + ": Przeciwnik opuścił pokój. Koniec gry.");
                currentRoom.removePlayer(opponent);
                opponent.setCurrentRoom(null);
                opponent.switchToLobbyState();
                opponent.getServerSender().sendMessage("EXIT_ROOM");
            }

            if (currentRoom.getPlayerCount() == 0) {
                roomManager.removeRoom(currentRoom.getId());   
            }

            player.getServerSender().sendMessage("EXIT_ROOM");
        } 
    }
    
}
