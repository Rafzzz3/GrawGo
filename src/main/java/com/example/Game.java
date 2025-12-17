package com.example;

// TO-DO: zaimplementowac GameState
// Klasa to wzorzec projektowy Facade i moze (agreguje GameLogic i Board oraz GameState - trzeba zaimplementować)
public class Game {
    private Board board;
    private GameLogic logic;
    private String response;

    // odkomentować kiedy zaimplementujemy GameState
    // private GameState state;

    public Game(int size) {
        this.board = new Board(size);
        this.logic = new GameLogic();
    }

    public synchronized boolean makeMove(int x, int y, Stone color) {
        MoveResult r = logic.move(board, x, y, color);

        switch (r.code) {
            case OK:
                setMessage(r.message);
                return true;
            case INVALID_POSITION:
                setMessage("Nieprawidłowa pozycja!");
                return false;
            case OCCUPIED:
                setMessage("Pole już zajęte!");
                return false;
            case SUICIDE:
                setMessage("Ruch niedozwolony: Samobójstwo!");
                return false;
            default:
                setMessage("Nieznany błąd.");
                return false;
        }
    }

    public void setMessage(String message) { this.response = message; }
    public String getMessage() { return response; }
}