package com.example;

public interface GameState {
    boolean stateMove(Game game, Stone color, int x, int y);
}