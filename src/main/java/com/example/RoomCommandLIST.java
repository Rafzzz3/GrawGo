package com.example;

import java.util.ArrayList;
import java.util.List;
public class RoomCommandLIST implements RoomCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler player, RoomManager roomManager, String args) {
        ArrayList<Room> rooms = roomManager.getRoomList();
        List<String> roomDescriptions = new ArrayList<>();
        for (Room room : rooms) { 
            String roomInfo = "Pokój ID: " + room.getId() + ", Rozmiar: " + room.getGame().getBoard().getBoardSize() +  "Graczy: " + room.getPlayers().size() + "/" + "2";
            roomDescriptions.add(roomInfo);
        }

        player.getServerSender().sendObject(roomDescriptions);
    }

}
