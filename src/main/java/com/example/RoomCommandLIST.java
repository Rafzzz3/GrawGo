/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

import java.util.ArrayList;
import java.util.List;
/**
 * Klasa obsługująca polecenie LIST do wyświetlenia listy dostępnych pokoi gry.
 */
public class RoomCommandLIST implements RoomCommandInterfaceExecutor {
    /**
     * Metoda wykonująca polecenie LIST do wyświetlenia listy dostępnych pokoi gry.
     * @param player Obiekt obsługujący klienta, który wysłał polecenie.
     * @param roomManager Menedżer pokoi gry.
     * @param args Argumenty polecenia w formie łańcucha znaków (nieużywane).
     */
    @Override
    public void execute(ClientHandler player, RoomManager roomManager, String args) {
        ArrayList<Room> rooms = roomManager.getRoomList();
        List<String> roomDescriptions = new ArrayList<>();
        for (Room room : rooms) { 
            String roomInfo = "Pokój ID: " + room.getId() + ", Rozmiar: " + room.getGame().getBoard().getBoardSize() +  " Graczy: " + room.getPlayers().size() + "/" + "2";
            roomDescriptions.add(roomInfo);
        }

        player.getServerSender().sendObject(roomDescriptions);
    }

}
