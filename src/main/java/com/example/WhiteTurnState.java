package com.example;

/**
 * Klasa reprezentująca stan tury białego gracza.
 */
public class WhiteTurnState extends Turn implements GameState {

    /**
     * Metoda obsługująca ruch w stanie tury białego gracza.
     * @param game Obiekt gry.
     * @param x Współrzędna x pola, na które wykonywany jest ruch.
     * @param y Współrzędna y pola, na które wykonywany jest ruch.
     * @return true jeśli ruch został wykonany pomyślnie, false w przeciwnym wypadku.
     */
    @Override
    public boolean stateMove(Game game, int x, int y) {
        // Próbujemy postawić BIAŁY
        boolean result = internalMove(game, x, y, Stone.WHITE);
        
        if (result) {
            game.setState(game.getBlackState());
        }
        return result;
    }
}