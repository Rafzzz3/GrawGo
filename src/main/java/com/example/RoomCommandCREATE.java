/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

public class RoomCommandCREATE implements RoomCommandInterfaceExecutor {
    /**
     * Metoda wykonująca polecenie CREATE do utworzenia nowego pokoju gry.
     * @param player Obiekt obsługujący klienta, który wysłał polecenie.
     * @param roomManager Menedżer pokoi gry.
     * @param args Argumenty polecenia w formie łańcucha znaków (rozmiar planszy).
     */
    @Override
    public void execute(ClientHandler player, RoomManager roomManager, String args) {
        if (args == null) {
            player.getServerSender().sendMessage(LobbyMessageType.ALERT.name() +": Użycie: CREATE <size>. Dozwolone: 9, 13, 19.");
            return;
        }
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            player.getServerSender().sendMessage(LobbyMessageType.ALERT.name() +": Użycie: CREATE <size>. Dozwolone: 9, 13, 19.");
            return;
        }

        int size;
        try {
            size = Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            player.getServerSender().sendMessage(LobbyMessageType.ERROR.name() +": Rozmiar musi być liczbą: 9, 13 lub 19.");
            return;
        }

        if (size != 9 && size != 13 && size != 19) {
            player.getServerSender().sendMessage(LobbyMessageType.ERROR.name() +": Nieprawidłowy rozmiar. Dozwolone: 9, 13, 19.");
            return;
        }

        Room room = roomManager.createRoom(size);
        player.setCurrentRoom(room);
        player.setState(ClientState.ROOM);
        room.addPlayer(player);
        player.getServerSender().sendMessage(LobbyMessageType.INFO.name() + ": Utworzono pokój o ID: " + room.getId() + " z rozmiarem planszy: " + size);
        player.getServerSender().sendMessage("JOINED_ROOM " + room.getId());

        if (player.getPlayerColor() == Stone.BLACK) {
            player.getServerSender().sendMessage(LobbyMessageType.INFO.name() + ": Masz czarny kolor. Zaczynasz grę.");
        } else {
            player.getServerSender().sendMessage(LobbyMessageType.INFO.name() + ": Masz biały kolor. Drugi gracz zaczyna grę.");
        }
    }
}