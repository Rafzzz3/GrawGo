package com.example;

public class WhiteTurnState extends Turn implements GameState {
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