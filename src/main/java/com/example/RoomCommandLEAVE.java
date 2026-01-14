package com.example;

public class RoomCommandLEAVE implements RoomCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler player, RoomManager roomManager, String args) {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom != null) {
            currentRoom.removePlayer(player);
            player.setCurrentRoom(null);
            player.getServerSender().sendMessage(LobbyMessageType.INFO.name() +": Opuszczono pokój o ID: " + currentRoom.getId());
            if (currentRoom.getPlayerCount() == 0) {
                roomManager.removeRoom(currentRoom.getId());
            }
        } else {
            player.getServerSender().sendMessage(LobbyMessageType.ALERT.name() +": Nie jesteś w żadnym pokoju.");
        }
    }
    
}
