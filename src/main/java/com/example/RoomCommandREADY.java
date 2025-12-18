package com.example;

public class RoomCommandREADY implements RoomCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler player, RoomManager roomManager, String args) {
        Room room = player.getCurrentRoom();
        if (room == null) {
            player.getServerSender().sendMessage("Nie jesteś w żadnym pokoju.");
            return;
        }
        room.incrementReadyCounter();
        player.getServerSender().sendMessage("Gotowy do gry.");
        if (room.isGameStarted()) {
            for (ClientHandler gracz : room.getPlayers()) {
                gracz.getServerSender().sendMessage("Wszyscy gracze są gotowi. Gra się rozpoczyna!");
            }
        }
    }
    
}
