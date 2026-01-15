/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

/**
 * Interfejs definiujący stan gry.
 */
public interface GameState {
    /**
     * Metoda obsługująca ruch w danym stanie gry.
     * @param game Obiekt gry.
     * @param x Współrzędna x pola, na które wykonywany jest ruch.
     * @param y Współrzędna y pola, na które wykonywany jest ruch.
     * @return true jeśli ruch został wykonany pomyślnie, false w przeciwnym wypadku.
     */
    boolean stateMove(Game game, int x, int y);
}