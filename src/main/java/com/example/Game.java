package com.example;

// Klasa to wzorzec projektowy Facade i moze (agreguje GameLogic i Board oraz GameState)
public class Game {
    private Board board;
    private GameLogic logic;
    private String response;
    private MoveResult lastMoveResult;
    private final GameState blackState;
    private final GameState whiteState;
    private GameState currentState;

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

    public void setMessage(String message) { this.response = message; }
    public String getMessage() { return response; }
    public void setState(GameState newState) { this.currentState = newState; }
    public void setLastMoveResult(MoveResult result) { this.lastMoveResult = result; }

    public GameState getBlackState() { return blackState; }
    public GameState getWhiteState() { return whiteState; }
    public Board getBoard() { return board; }
    public GameLogic getLogic() { return logic; }
    public MoveResult getLastMoveResult() { return lastMoveResult; }

    // Plansza Go w Unicode: ● (czarny), ○ (biały), linie ┌┬┐ ├┼┤ └┴┘
    // public String renderBoard() {
    //     Board board = getBoard();
    //     int size = board.getBoardSize();
    //     StringBuilder sb = new StringBuilder();

    //     // Nagłówek kolumn (1..size)
    //     sb.append("   ");
    //     for (int i = 0; i < size; i++) {
    //         sb.append(String.format("%-3d", (i + 1)));
    //     }
    //     sb.append('\n');

    //     for (int y = 0; y < size; y++) {
    //         // Numer wiersza z lewej (1..size)
    //         sb.append(String.format("%2d ", (y + 1)));

    //         // Linia z przecięciami i poziomymi łącznikami
    //         for (int x = 0; x < size; x++) {
    //             Stone stone = board.getStone(x, y);
    //             String symbol;

    //             if (stone == Stone.BLACK) {
    //                 symbol = "●";
    //             } else if (stone == Stone.WHITE) {
    //                 symbol = "○";
    //             } else {
    //                 if (x == 0 && y == 0) symbol = "┌"; // lewy gorny rog
    //                 else if (x == size - 1 && y == 0) symbol = "┐"; // prawy gorny rog
    //                 else if (x == 0 && y == size - 1) symbol = "└"; // lewy dolny rog
    //                 else if (x == size - 1 && y == size - 1) symbol = "┘"; // prawy dolny rog
    //                 else if (y == 0) symbol = "┬";  //gorna krawedz
    //                 else if (y == size - 1) symbol = "┴";   // dolna krawedz
    //                 else if (x == 0) symbol = "├";  // lewa krawedz
    //                 else if (x == size - 1) symbol = "┤";   // prawa krawaedz
    //                 else symbol = "┼";  // srodek
    //             }

    //             sb.append(symbol);
    //             if (x < size - 1) sb.append("──");
    //         }
    //         sb.append('\n');

    //         // Pionowe łączniki między wierszami
    //         if (y < size - 1) {
    //             sb.append("   ");
    //             for (int x = 0; x < size; x++) {
    //                 sb.append('│');
    //                 if (x < size - 1) sb.append("  ");
    //             }
    //             sb.append('\n');
    //         }
    //     }
    //     return sb.toString();
    // }
}