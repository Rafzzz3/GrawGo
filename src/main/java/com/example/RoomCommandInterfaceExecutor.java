/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

/**
 * Interfejs definiujący wykonawcę poleceń w kontekście pokoju gry.
 */
public interface RoomCommandInterfaceExecutor {
    /**
     * Metoda wykonująca polecenie w kontekście pokoju gry.
     * @param player Obiekt obsługujący klienta, który wysłał polecenie.
     * @param roomManager Menedżer pokoi gry.
     * @param args Argumenty polecenia w formie łańcucha znaków.
     */
    void execute(ClientHandler player, RoomManager roomManager, String args);
}
