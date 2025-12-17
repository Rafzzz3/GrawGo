package com.example;

// TO-DO: zaimplementowac GameState
// Klasa to wzorzec projektowy Facade i moze (agreguje GameLogic i Board oraz GameState - trzeba zaimplementować)
public class Game {
    private Board board;
    private GameLogic logic;
    private String response;
    private final GameState blackState;
    private final GameState whiteState;
    private GameState currentState;

    // odkomentować kiedy zaimplementujemy GameState
    // private GameState state;

    public Game(int size) {
        this.board = new Board(size);
        this.logic = new GameLogic();
        this.blackState = new BlackTurnState();
        this.whiteState = new WhiteTurnState();
        this.currentState = blackState;
    }

    // Punkt wjescia z zewnatrz
    public synchronized boolean putStone(int x, int y) {
        return currentState.stateMove(this, x, y);
    }
    public synchronized boolean isTurn(Stone color) {
        if (color == null) {
            return false;
        }
        if (currentState == blackState && color == Stone.BLACK) {
            return true;
        }
        if (currentState == whiteState && color == Stone.WHITE) {
            return true;
        }
        return false;
    }
    public synchronized void switchTurn() {
        if (currentState == blackState) {
            currentState = whiteState;
        } else {
            currentState = blackState;
        }
    }
    public void setMessage(String message) { this.response = message; }
    public String getMessage() { return response; }
    public void setState(GameState newState) {
        this.currentState = newState;
    }
    public GameState getBlackState() { return blackState; }
    public GameState getWhiteState() { return whiteState; }
    public Board getBoard() { return board; }
    public GameLogic getLogic() { return logic; }
}