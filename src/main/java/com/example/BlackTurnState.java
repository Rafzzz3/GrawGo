/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */

package com.example;

/**
 * Klasa reprezentująca stan tury czarnego gracza w grze. Implementuje interfejs GameState.
 */
public class BlackTurnState extends Turn implements GameState {
    
    /**
     * Metoda obsługująca ruch w stanie tury czarnego gracza.
     * @param game Obiekt gry, w której działa stan tury czarnego gracza.
     * @param x Współrzędna x ruchu.
     * @param y Współrzędna y ruchu.
     * @return true jeśli ruch był poprawny i wykonany, false w przeciwnym wypadku.
     */
    @Override
    public boolean stateMove(Game game, int x, int y) {

        // Próbujemy postawić CZARNY
        boolean result = internalMove(game, x, y, Stone.BLACK);
        
        if (result) {
            // Przełączamy na gotową instancję stanu białego
            game.setState(game.getWhiteState());
        }
        return result;
    }
}