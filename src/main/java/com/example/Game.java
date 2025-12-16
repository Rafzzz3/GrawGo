package com.example;

// TO-DO: zaimplementowac GameState
// TO-DO: w ktorym momencie sprawdzac liczbe uduszen (na koniec gry? po kazdym ruchu?)
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
    // narazie implementujemy tylko makeMove potem poprawić
    public synchronized boolean makeMove(int x, int y, StoneColor color) {
        if (!logic.isValidPosition(size, x, y)) {
            setMessage("Nieprawidłowa pozycja!");
            return false;
        }
        Stone stone = board.getStone(x, y);
        if (stone.getColor() != StoneColor.EMPTY) {
            setMessage("Pole już zajęte!");
            return false;
        }
        board.setStone(x, y, new Stone(color));
        if (logic.isSuffocated(board, x, y)) {
            setMessage("Ruch prowadzi do uduszenia kamienia!");
            board.setStone(x, y, new Stone(StoneColor.EMPTY));
            return false;
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