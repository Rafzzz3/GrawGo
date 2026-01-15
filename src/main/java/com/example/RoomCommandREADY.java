/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

/**
 * Klasa obsługująca polecenie READY do oznaczenia gracza jako gotowego do gry.
 */
public class RoomCommandREADY implements RoomCommandInterfaceExecutor {
    /** 
     * Metoda wykonująca polecenie READY do oznaczenia gracza jako gotowego do gry.
     * @param player Obiekt obsługujący klienta, który wysłał polecenie.
     * @param roomManager Menedżer pokoi gry.
     * @param args Argumenty polecenia w formie łańcucha znaków (nieużywane).
    */
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
