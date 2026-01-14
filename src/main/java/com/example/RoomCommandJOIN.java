package com.example;

public class RoomCommandJOIN implements RoomCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler player, RoomManager roomManager, String args) {
        try {
            Room room = roomManager.findRoom(Integer.parseInt(args));
            String message;
            if (room == null) {
                message = "Pokój o podanym ID nie istnieje.";
            }
            else if (player.getCurrentRoom() != null && room.getId() == player.getCurrentRoom().getId()) {
                message = "Już jesteś w tym  pokoju.";
            }
            else if (room.getPlayerCount() >= 2) {
                message = "Pokój jest pełny.";
            }
            else {
                message = "Dołączyłeś do pokoju: " + room.getId();
                player.setCurrentRoom(room);
                room.addPlayer(player);
                if (player.getPlayerColor() == Stone.BLACK) {
                    message += "\nMasz czarny kolor. Zaczynasz grę.";
                } else {
                    message += "\nMasz biały kolor. Drugi gracz zaczyna grę.";
                }
            }
            player.getServerSender().sendMessage(message);
        }
        catch (Exception e) {
            player.getServerSender().sendMessage("Nie udało się dołączyć do pokoju: " + e.getMessage());
        }
    }
    
}
