package com.example;

public class RoomCommandCREATE implements RoomCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler gracz, RoomManager roomManager, String args) {
        if (args == null) {
            gracz.getServerSender().sendMessage("Użycie: CREATE <size>. Dozwolone: 9, 13, 19.");
            return;
        }
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            gracz.getServerSender().sendMessage("Użycie: CREATE <size>. Dozwolone: 9, 13, 19.");
            return;
        }

        int size;
        try {
            size = Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            gracz.getServerSender().sendMessage("Rozmiar musi być liczbą: 9, 13 lub 19.");
            return;
        }

        if (size != 9 && size != 13 && size != 19) {
            gracz.getServerSender().sendMessage("Nieprawidłowy rozmiar. Dozwolone: 9, 13, 19.");
            return;
        }

        Room room = roomManager.createRoom(size);
        gracz.setCurrentRoom(room);
        room.addPlayer(gracz);

        gracz.getServerSender().sendMessage("Utworzono pokój: " + room.getId() + " z planszą " + size + "x" + size);
        gracz.getServerSender().sendMessage(room.getGame().renderBoard());
    }
}
