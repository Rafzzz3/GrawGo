/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

/**
 * Interfejs definiujący wykonawcę poleceń gry.
 */
public interface CommandInterfaceExecutor {
    /** 
     * Metoda wykonująca polecenie gry.
     * @param game Obiekt gry, na którym wykonywane jest polecenie.
     * @param args Argumenty polecenia w formie łańcucha znaków.
     * @param player Obiekt obsługujący klienta, który wysłał polecenie.
    */
    void execute(Game game, String args, ClientHandler player);
} 
