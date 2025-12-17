package com.example;

public class RoomCommandREADY implements RoomCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler gracz, RoomManager roomManager, String args) {
        Room room = gracz.getCurrentRoom();
        if (room == null) {
            gracz.getServerSender().sendMessage("Nie jesteś w żadnym pokoju.");
            return;
        }
        gracz.setReady(true);
        gracz.getServerSender().sendMessage("Gotowy do gry.");
        room.setGameStarted();
        if (room.isGameStarted()) {
            for (ClientHandler player : room.getPlayers()) {
                player.getServerSender().sendMessage("Wszyscy gracze są gotowi. Gra się rozpoczyna!");
            }
        }
    }
    
}
