package com.example;

public class BlackTurnState extends Turn implements GameState {
    // dla bezpieczeństwa
    Stone turnColor = Stone.BLACK;

    @Override
    public boolean stateMove(Game game,Stone color, int x, int y) {
        if (color != turnColor) {
            game.setMessage("Obecnie tura czarnych.");
            return false;
        }
        // Próbujemy postawić CZARNY
        boolean result = internalMove(game, x, y, Stone.BLACK);
        
        if (result) {
            // Przełączamy na gotową instancję stanu białego
            game.setState(game.getWhiteState());
        }
        return result;
    }
}