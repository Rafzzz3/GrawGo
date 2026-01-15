/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import java.io.Serializable;

/**
 * Klasa reprezentująca planszę gry.
 */
public class Board implements Serializable {
    private int board_size;
    private Stone[][] table;

    /**
     * Konstruktor klasy Board.
     * @param size Rozmiar planszy gry.
     */
    public Board(int size) {
        this.board_size = size;
        table = new Stone[board_size][board_size];
        initializeBoard();
    }

    /** 
     * Metoda inicjalizująca pustą planszę gry (ustawiając wszystkie pola na EMPTY).
    */
    private void initializeBoard() {
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                table[i][j] = Stone.EMPTY;
            }
        }
    }

    /** 
     * Metoda zwracająca stan pola na planszy gry.
     * @param row Wiersz pola.
     * @param col Kolumna pola.
     * @return Stan pola (kamień) na planszy gry.
    */
    public Stone getStone(int row, int col) {
        return table[row][col];
    }

    /** 
     * Metoda ustawiająca stan pola na planszy gry.
     * @param row Wiersz pola.
     * @param col Kolumna pola.
     * @param stone kamień do ustawienia na planszy gry.
    */
    public void setStone(int row, int col, Stone stone) {
        table[row][col] = stone;
    }

    /** 
     * Metoda zwracająca rozmiar planszy gry.
     * @return Rozmiar planszy gry.
    */
    public int getBoardSize() {
        return board_size;
    }

}