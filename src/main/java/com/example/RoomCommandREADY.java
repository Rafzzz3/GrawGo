package com.example;

public class RoomCommandREADY implements RoomCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler player, RoomManager roomManager, String args) {
        Room room = player.getCurrentRoom();
        // if (room == null) {
        //     player.getServerSender().sendMessage(LobbyMessageType.ERROR.name() + ": Nie jesteś w żadnym pokoju.");
        //     return;
        // }
        room.incrementReadyCounter();
        player.getServerSender().sendMessage(LobbyMessageType.INFO.name() + ": Gotowy do gry.");
        if (room.isGameStarted()) {
            for (ClientHandler gracz : room.getPlayers()) {
                gracz.getServerSender().sendObject(room.getGame().getBoard());
            }
        }
    }
    
}
