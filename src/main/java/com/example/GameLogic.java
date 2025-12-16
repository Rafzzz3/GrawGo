package com.example;

public class GameLogic {
    // TO-DO: implementacja lancuchow jesli bedize potrzeba

    // argument Board otrzymuje od klasy Game i ona dba o poprawnosc danych
    public boolean isSuffocated(Board board, int x, int y) {
        int size = board.getBoardSize();

        // dodatkowe zabezpieczenie na wypadek zlego uzycia
        if (!isValidPosition(size, x, y)) {
                return false; 
        }

        Stone stone = board.getStone(x, y);
        StoneColor color = stone.getColor();
        StoneColor oppositeColor;
        if (color == StoneColor.BLACK) {
            oppositeColor = StoneColor.WHITE;
        } else if (color == StoneColor.WHITE) {
            oppositeColor = StoneColor.BLACK;
        } else {
            return false; 
        }

        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        int possibleBreaths = 4;

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (isValidPosition(size, newX, newY)) {
                Stone neighbor = board.getStone(newX, newY);
                if (neighbor.getColor() == oppositeColor) {
                    possibleBreaths--;
                }
            } else {
                possibleBreaths--;
            }
        }
        
        if (possibleBreaths == 0) {
            return true;
        }
        return false; 
    }
    
    // Helper też musi wiedzieć jaki jest rozmiar
    public boolean isValidPosition(int size, int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }
}
