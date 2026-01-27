/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

/**
 * Klasa obsługująca polecenie JOIN do dołączenia do istniejącego pokoju gry.
 */
public class RoomCommandJOIN implements RoomCommandInterfaceExecutor {
    /**
     * Metoda wykonująca polecenie JOIN do dołączenia do istniejącego pokoju gry.
     * @param player Obiekt obsługujący klienta, który wysłał polecenie.
     * @param roomManager Menedżer pokoi gry.
     * @param args Argumenty polecenia w formie łańcucha znaków (ID pokoju).
     */
    @Override
    public void execute(ClientHandler player, RoomManager roomManager, String args) {
        try {
            Room room = roomManager.findRoom(Integer.parseInt(args));
            String message;
            if (room.getPlayerCount() >= 2) {
                message = LobbyMessageType.ALERT.name() + ": Pokój jest pełny.";
            }
            else {
                message = LobbyMessageType.INFO.name() + ": Dołączyłeś do pokoju: " + room.getId();
                player.switchToRoomState();
                player.setCurrentRoom(room);
                room.addPlayer(player);

                if (player.getPlayerColor() == Stone.BLACK) {
                    message += "\nMasz czarny kolor. Zaczynasz grę.";
                } else {
                    message += "\nMasz biały kolor. Drugi gracz zaczyna grę.";
                }
            }
            player.getServerSender().sendMessage(message);
            player.getServerSender().sendMessage("JOINED_ROOM " + room.getId());
        }
        catch (Exception e) {
            player.getServerSender().sendMessage(LobbyMessageType.ERROR.name() + ": Nie udało się dołączyć do pokoju: " + e.getMessage());
        }
    }
    
}
