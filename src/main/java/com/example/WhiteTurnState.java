package com.example;

public class WhiteTurnState extends Turn implements GameState {
    Stone turnColor = Stone.WHITE;

    @Override
    public boolean stateMove(Game game, Stone color, int x, int y) {
        if (color != turnColor) {
            game.setMessage("Obecnie tura białych.");
            return false;
        }
        // Próbujemy postawić BIAŁY
        boolean result = internalMove(game, x, y, Stone.WHITE);
        
        if (result) {
            game.setState(game.getBlackState());
        }
        return result;
    }
}