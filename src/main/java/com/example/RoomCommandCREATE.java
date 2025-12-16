package com.example;

public class RoomCommandCREATE implements RoomCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler gracz, RoomManager roomManager, String args) {
        Room room = roomManager.createRoom();
        gracz.setCurrentRoom(room);
        room.addPlayer(gracz);
        gracz.getServerSender().sendMessage("Utworzono pokój: " + room.getId());
    }
    
}
