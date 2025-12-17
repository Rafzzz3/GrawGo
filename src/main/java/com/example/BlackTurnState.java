package com.example;

public class BlackTurnState extends Turn implements GameState {
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