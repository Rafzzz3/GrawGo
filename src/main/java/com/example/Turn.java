package com.example;

/**
 * Klasa bazowa dla stanów tury w grze.
 */
public class Turn {
    /**
     * Metoda wewnętrzna obsługująca ruch w grze.
     * @param game Obiekt gry.
     * @param x Współrzędne pola, na które wykonywany jest ruch.
     * @param y Współrzędne pola, na które wykonywany jest ruch.
     * @param color Kolor kamienia gracza wykonującego ruch.
     * @return true jeśli ruch został wykonany pomyślnie, false w przeciwnym wypadku.
     */
    public boolean internalMove(Game game, int x, int y, Stone color) {
        MoveResult r = game.getLogic().move(game.getBoard(), x, y, color);
        game.addToHistory(x, y, color, r);

        game.setLastMoveResult(r);

        switch (r.code) {
            case OK:
                game.setMessage(r.message);
                return true;
            case INVALID_POSITION:
                game.setMessage(r.message); // "Nieprawidłowa pozycja!"
                return false;
            case OCCUPIED:
                game.setMessage(r.message); // "Pole już zajęte!"
                return false;
            case SUICIDE:
                game.setMessage(r.message); // "Ruch niedozwolony: Samobójstwo!"
                return false;
            case KO:
                game.setMessage(r.message); // "Ruch niedozwolony: zasada KO!"
                return false;
            default:
                game.setMessage("Nieznany błąd.");
                return false;
        }
    }

}
