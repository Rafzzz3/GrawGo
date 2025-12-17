package com.example;

public class Turn {
    // Wykonanie ruchu z explicite kolorem – używane przez stany
    public boolean internalMove(Game game, int x, int y, Stone color) {
        MoveResult r = game.getLogic().move(game.getBoard(), x, y, color);

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
            default:
                game.setMessage("Nieznany błąd.");
                return false;
        }
    }

}
