package com.example;

public class RoomCommandJOIN implements RoomCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler gracz, RoomManager roomManager, String args) {
        try {
            Room room = roomManager.findRoom(Integer.parseInt(args));
            String message;
            if (room == null) {
                message = "Pokój o podanym ID nie istnieje.";
            }
            else if (room.getPlayerCount() >= 2) {
                message = "Pokój jest pełny.";
            }
            else {
                message = "Dołączyłeś do pokoju: " + room.getId();
                gracz.setCurrentRoom(room);
                room.addPlayer(gracz);
            }
            gracz.getServerSender().sendMessage(message);
        }
        catch (Exception e) {
            System.out.println("Nie udało się dołączyć do pokoju: " + e.getMessage());
        }
    }
    
}
