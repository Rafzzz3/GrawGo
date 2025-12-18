package com.example;

public class RoomCommandCREATE implements RoomCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler player, RoomManager roomManager, String args) {
        if (args == null) {
            player.getServerSender().sendMessage("Użycie: CREATE <size>. Dozwolone: 9, 13, 19.");
            return;
        }
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            player.getServerSender().sendMessage("Użycie: CREATE <size>. Dozwolone: 9, 13, 19.");
            return;
        }

        int size;
        try {
            size = Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            player.getServerSender().sendMessage("Rozmiar musi być liczbą: 9, 13 lub 19.");
            return;
        }

        if (size != 9 && size != 13 && size != 19) {
            player.getServerSender().sendMessage("Nieprawidłowy rozmiar. Dozwolone: 9, 13, 19.");
            return;
        }

        Room room = roomManager.createRoom(size);
        player.setCurrentRoom(room);
        room.addPlayer(player);

        player.getServerSender().sendMessage("Utworzono pokój: " + room.getId() + " z planszą " + size + "x" + size);
        if (player.getPlayerColor() == Stone.BLACK) {
            player.getServerSender().sendMessage("Masz czarny kolor. Zaczynasz grę.");
        } else {
            player.getServerSender().sendMessage("Masz biały kolor. Drugi gracz zaczyna grę.");
        }
    }
}
