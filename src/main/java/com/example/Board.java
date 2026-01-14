package com.example;
import java.io.Serializable;
// Tu też już chyba wszystko gotowe do planszy
// ten Serializable jest potrzebny żebyśmy mogli wysłać planszę do Klient receivera który wyświetli planszę
public class Board implements Serializable {
    private int board_size;
    private Stone[][] table;

    public Board(int size) {
        this.board_size = size;
        table = new Stone[board_size][board_size];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                table[i][j] = Stone.EMPTY;
            }
        }
    }

    public Stone getStone(int row, int col) {
        return table[row][col];
    }

    public void setStone(int row, int col, Stone stone) {
        table[row][col] = stone;
    }
    public int getBoardSize() {
        return board_size;
    }

}