package com.example;

import java.util.ArrayList;
import java.util.List;

public class RoomCommandLEAVE implements RoomCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler player, RoomManager roomManager, String args) {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom != null) {
            List<ClientHandler> others = new ArrayList<>(currentRoom.getPlayers());
            others.remove(player);
            currentRoom.removePlayer(player);
            player.setCurrentRoom(null);
            player.getServerSender().sendMessage("EXIT_ROOM");
            for (ClientHandler opponent : others) {
                opponent.getServerSender().sendMessage(LobbyMessageType.INFO.name() + ": Przeciwnik opuścił pokój. Koniec gry.");
                currentRoom.removePlayer(opponent);
                opponent.setCurrentRoom(null);
                opponent.getServerSender().sendMessage("EXIT_ROOM");
            }
            if (currentRoom.getPlayerCount() == 0) {
                roomManager.removeRoom(currentRoom.getId());   
            }
            player.getServerSender().sendMessage("EXIT_ROOM");
        } 
    }
    
}
