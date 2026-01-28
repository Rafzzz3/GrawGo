package com.example;

import java.io.Serializable;

// klasa Wrapper dla MoveResult (adapter?) do przechowywania historii ruchów
public class HistoryMove implements Serializable {
    public int x;
    public int y;
    public Stone playerColor;
    public int[][] captured;

    public HistoryMove() {}

    public HistoryMove(int x, int y, Stone playerColor, MoveResult result) {
        this.x = x;
        this.y = y;
        this.playerColor = playerColor;
        this.captured = result.captured;
    }
}
