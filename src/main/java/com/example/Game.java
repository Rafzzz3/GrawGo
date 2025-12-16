package com.example;

// TO-DO: zaimplementowac GameState
// Klasa to wzorzec projektowy Facade i moze (agreguje GameLogic i Board oraz GameState - trzeba zaimplementować)
public class Game {
    private Board board;
    private GameLogic logic;
    private int size;
    private String response;

    // odkomentować kiedy zaimplementujemy GameState
    // private GameState state;

    public Game(int size) {
        this.board = new Board(size);
        this.logic = new GameLogic();
        this.size = size;
        // this.state = new GameState();
    }

    public synchronized boolean makeMove(int x, int y, Stone color) {
        if (!logic.isValidPosition(size, x, y)) {
            setMessage("Nieprawidłowa pozycja!");
            return false;
        }
        Stone stone = board.getStone(x, y);
        if (stone != Stone.EMPTY) {
            setMessage("Pole już zajęte!");
            return false;
        }

        int[][] captured = logic.makeMove(board, x, y, color);
        if (captured.length == 0 && logic.isSuffocated(board, x, y)) {
            board.setStone(x, y, Stone.EMPTY);
            setMessage("Ruch niedozwolony: Samobójstwo!");
            return false;
        }

        if (captured.length > 0) {
            StringBuilder sb = new StringBuilder("Uduszono: ");
            for (int i = 0; i < captured.length; i++) {
                int[] p = captured[i];
                sb.append("(").append(p[0]).append(", ").append(p[1]).append(")").append(", ");
            }
            sb.setLength(sb.length() - 2); // usuwa ostatni przecinek i spację
            setMessage(sb.toString());
        } else {
            setMessage("Ruch wykonany pomyślnie.");
        }

        return true;
    }

    public void setMessage(String message) {
        this.response = message;
    }
    public String getMessage() {
        return response;
    }
}