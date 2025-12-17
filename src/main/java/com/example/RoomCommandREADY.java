package com.example;

public class RoomCommandREADY implements RoomCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler gracz, RoomManager roomManager, String args) {
        Room room = gracz.getCurrentRoom();
        if (room == null) {
            gracz.getServerSender().sendMessage("Nie jesteś w żadnym pokoju.");
            return;
        }
        room.incrementReadyCounter();
        gracz.getServerSender().sendMessage("Gotowy do gry.");
        if (room.isGameStarted()) {
            for (ClientHandler player : room.getPlayers()) {
                player.getServerSender().sendMessage("Wszyscy gracze są gotowi. Gra się rozpoczyna!");
            }
        }
    }
    
}
