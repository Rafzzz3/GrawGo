package com.example;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {
    // TO-DO: implementacja lancuchow jesli bedize potrzeba
    private int blackPoints = 0;
    private int whitePoints = 0;

    // argument Board otrzymuje od klasy Game i ona dba o poprawnosc danych
    public boolean isSuffocated(Board board, int x, int y) {
        int size = board.getBoardSize();

        // dodatkowe zabezpieczenie na wypadek zlego uzycia
        if (!isValidPosition(size, x, y)) {
                return false; 
        }

        Stone color = board.getStone(x, y);
        Stone oppositeColor;
        if (color == Stone.BLACK) {
            oppositeColor = Stone.WHITE;
        } else if (color == Stone.WHITE) {
            oppositeColor = Stone.BLACK;
        } else {
            return false; 
        }

        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        int possibleBreaths = 4;

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (isValidPosition(size, newX, newY)) {
                if (board.getStone(newX, newY) == oppositeColor) {
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

    private int[][] makeMove(Board board, int x, int y, Stone color) {
        board.setStone(x, y, color);
        List<int[]> capturedList = new ArrayList<>();

        // sprawdzamy czy ruch udusil sasiada, zabieramy jenca i dodajemy punkt
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (isValidPosition(board.getBoardSize(), newX, newY)) {
                Stone neighbor = board.getStone(newX, newY);
                if (neighbor != Stone.EMPTY && neighbor != color && isSuffocated(board, newX, newY)) {
                    board.setStone(newX, newY, Stone.EMPTY);
                    if (color == Stone.BLACK) {
                        blackPoints++;
                    } else {
                        whitePoints++;
                    }
                    capturedList.add(new int[]{newX+1, newY+1}); // +1 dla uzytkownika
                }
            }
        }

        return capturedList.toArray(new int[0][]);
    }
    


    // Główny punkt wejścia: walidacja, ruch, rollback przy samobójstwie
    public MoveResult move(Board board, int x, int y, Stone color) {
        int size = board.getBoardSize();
        if (!isValidPosition(size, x, y)) {
            return new MoveResult(MoveCode.INVALID_POSITION, new int[0][], "Nieprawidłowa pozycja!");
        }
        if (board.getStone(x, y) != Stone.EMPTY) {
            return new MoveResult(MoveCode.OCCUPIED, new int[0][], "Pole już zajęte!");
        }

        int[][] captured = makeMove(board, x, y, color);

        if (captured.length == 0 && isSuffocated(board, x, y)) {
            board.setStone(x, y, Stone.EMPTY); // rollback
            return new MoveResult(MoveCode.SUICIDE, new int[0][], "Ruch niedozwolony: Samobójstwo!");
        }

        String msg;
        if (captured.length > 0) {
            StringBuilder sb = new StringBuilder("Uduszono: ");
            for (int i = 0; i < captured.length; i++) {
                int[] p = captured[i];
                sb.append("(").append(p[0]).append(", ").append(p[1]).append(")");
                if (i < captured.length - 1) sb.append(", ");
            }
            msg = sb.toString();
        } else {
            msg = "Ruch wykonany pomyślnie.";
        }

        return new MoveResult(MoveCode.OK, captured, msg);
    }


    // Helper też musi wiedzieć jaki jest rozmiar
    public boolean isValidPosition(int size, int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public int getBlackPoints() {
        return blackPoints;
    }

    public int getWhitePoints() {
        return whitePoints;
    }
}
