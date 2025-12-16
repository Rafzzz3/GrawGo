package com.example;

// TO-DO: zaimplementowac GameState
// TO-DO: w ktorym momencie sprawdzac liczbe uduszen (na koniec gry? po kazdym ruchu?)
// Klasa to wzorzec projektowy Facade i moze (agreguje GameLogic i Board oraz GameState - trzeba zaimplementować)
public class Game {
    private Board board;
    private GameLogic logic;
    private int size;
    // odkomentować kiedy zaimplementujemy GameState
    // private GameState state;

    public Game(int size) {
        this.board = new Board(size);
        this.logic = new GameLogic();
        this.size = size;
        // this.state = new GameState();
    }

    // jeszcze nie wiem czy nie powinny throwowac wyjątków zamiast printa
    public synchronized boolean makeMove(int x, int y, StoneColor color) {
        if (!logic.isValidPosition(size, x, y)) {
            System.out.println("Nieprawidłowa pozycja!");
            return false;
        }
        Stone stone = board.getStone(x, y);
        if (stone.getColor() != StoneColor.EMPTY) {
            System.out.println("Pole już zajęte!");
            return false;
        }
        board.setStone(x, y, new Stone(color));
        if (logic.isSuffocated(board, x, y)) {
            System.out.println("Ruch prowadzi do uduszenia kamienia!");
            board.setStone(x, y, new Stone(StoneColor.EMPTY));
            return false;
        }
        return true;
    }
}