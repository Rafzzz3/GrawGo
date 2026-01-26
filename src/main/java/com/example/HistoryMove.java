package com.example;

import java.io.Serializable;

// klasa Wrapper dla MoveResult (adapter?) do przechowywania historii ruchów
public class HistoryMove implements Serializable {
    public final int x;
    public final int y;
    public final Stone playerColor;
    public final MoveResult result;

    public HistoryMove(int x, int y, Stone playerColor, MoveResult result) {
        this.x = x;
        this.y = y;
        this.playerColor = playerColor;
        this.result = result;
    }
}
