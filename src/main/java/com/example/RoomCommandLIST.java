package com.example;

import java.util.ArrayList;

public class RoomCommandLIST implements RoomCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler gracz, RoomManager roomManager, String args) {
        ArrayList<Room> rooms = roomManager.getRoomList();
        
        StringBuilder roomList = new StringBuilder("Dostępne pokoje:\n");
        for (Room room : rooms) {
            roomList.append("Pokój ID: ").append(room.getId())
                    .append(", Gracze: ").append(room.getPlayerCount()).append("\n");
        }

        gracz.getServerSender().sendMessage(roomList.toString());
    }

}
