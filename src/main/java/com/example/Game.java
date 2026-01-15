/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

/**
 * Klasa reprezentująca grę Go, zarządzająca stanem gry, planszą oraz logiką gry (wzorzec projektowy Facade).
 */
public class Game {
    /** 
     * @param board Obiekt Board reprezentujący planszę gry.
    */
    private Board board;
    /** 
     * @param logic Obiekt GameLogic reprezentujący logikę gry.
    */
    private GameLogic logic;
    /** 
     * @param response Wiadomość zwrotna gry.
    */
    private String response;
    /** 
     * @param lastMoveResult Wynik ostatniego ruchu.
    */
    private MoveResult lastMoveResult;
    /** 
     * @param consecutivePasses Liczba kolejnych spasowań.
    */
    private int consecutivePasses = 0;
    /** 
     * @param gameFinished Flaga wskazująca, czy gra została zakończona.
    */
    private boolean gameFinished = false;
    /** 
     * @param blackState Stan tury czarnego gracza.
    */
    private final GameState blackState;
    /** 
     * @param whiteState Stan tury białego gracza.
    */
    private final GameState whiteState;
    /** 
     * @param currentState Aktualny stan gry.
    */
    private GameState currentState;

    /** 
     * Konstruktor klasy Game.
     * @param size Rozmiar planszy gry.
    */
    public Game(int size) {
        this.board = new Board(size);
        this.logic = new GameLogic();
        this.blackState = new BlackTurnState();
        this.whiteState = new WhiteTurnState();
        this.currentState = blackState;
    }


    /** 
     * Metoda umieszczająca kamień na planszy gry.
     * @param x Współrzędna x ruchu.
     * @param y Współrzędna y ruchu.
     * @return true jeśli ruch był poprawny i wykonany, false w przeciwnym wypadku.
    */
    public synchronized boolean putStone(int x, int y) {
        if (gameFinished) {
            return false;
        }

        boolean result = currentState.stateMove(this, x, y);
        if (result) {
            resetPassCounter();
        }

        return result;
    }

    /** 
     * Metoda obsługująca ruch "pass" (poddanie tury) w grze.
     * @param playerColor Kolor kamienia gracza wykonującego ruch.
     * @return Obiekt MoveResult zawierający wynik ruchu.
    */
    public synchronized MoveResult pass(Stone playerColor) {
        if (!isTurn(playerColor)) {
            return new MoveResult(MoveCode.NOT_YOUR_TURN, new int[0][], "Nie twoja tura!");
        }

        consecutivePasses++;

        // koniec gry
        if (consecutivePasses >= 2) {
            gameFinished = true;
            
            // Liczymy punkty
            logic.calculateTerritory(board);
            int blackScore = logic.getBlackTerritory() + logic.getBlackKills();
            int whiteScore = logic.getWhiteTerritory() + logic.getWhiteKills();
            
            // Budujemy wiadomość końcową
            String scoreMsg = "KONIEC GRY (2x PASS).\n" + 
                              "Wynik:\n" + 
                              "⚫ Czarny: " + blackScore + " (Teren: " + logic.getBlackTerritory() + ", Jeńcy: " + logic.getBlackKills() + ")\n" +
                              "⚪ Biały: " + whiteScore + " (Teren: " + logic.getWhiteTerritory() + ", Jeńcy: " + logic.getWhiteKills() + ")";
            
            return new MoveResult(MoveCode.GAME_OVER, new int[0][], scoreMsg);
        }

        if (currentState == blackState) {
            setState(whiteState);
        } else {
            setState(blackState);
        }

        return new MoveResult(MoveCode.PASS, new int[0][], "Gracz " + playerColor + " spasował.");
    }

    /** 
     * Metoda obsługująca poddanie się gracza w grze.
     * @param playerColor Kolor kamienia gracza poddającego się.
     * @return Obiekt MoveResult zawierający wynik poddania się.
    */
    public synchronized MoveResult surrender(Stone playerColor) {
        gameFinished = true;
        String winner;

        if (playerColor == Stone.BLACK) {
            winner = "Czarny";
        } else {
            winner = "Biały";
        }
        
        return new MoveResult(MoveCode.SURRENDER, new int[0][], "Gracz " + playerColor + " poddał się! Wygrywa " + winner + ".");
    }

    /** 
     * Metoda sprawdzająca, czy jest tura gracza o podanym kolorze.
     * @param color Kolor kamienia gracza.
     * @return true jeśli jest tura gracza o podanym kolorze, false w przeciwnym wypadku.
    */
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

    /** 
     * Metoda ustawiająca wiadomość zwrotną gry.
     * @param message Wiadomość do ustawienia.
    */
    public void setMessage(String message) { this.response = message; }
    public String getMessage() { return response; }
    /** 
     * Metoda ustawiająca aktualny stan gry.
     * @param newState Nowy stan gry do ustawienia.
    */
    public void setState(GameState newState) { this.currentState = newState; }
    /** 
     * Metoda ustawiająca wynik ostatniego ruchu.
     * @param result Obiekt MoveResult do ustawienia jako wynik ostatniego ruchu.
    */
    public void setLastMoveResult(MoveResult result) { this.lastMoveResult = result; }


    /**
     * Metoda zwracająca stan gracza czarnego.
     * @return Stan gracza czarnego.
     */
    public GameState getBlackState() { return blackState; }
    /**
     * Metoda zwracająca stan gracza białego.
     * @return Stan gracza białego.
     */
    public GameState getWhiteState() { return whiteState; }
    /** 
     * Metoda zwracająca planszę gry.
     * @return Obiekt Board reprezentujący planszę gry.
    */
    public Board getBoard() { return board; }
    /** 
     * Metoda zwracająca logikę gry.
     * @return Obiekt GameLogic reprezentujący logikę gry.
    */
    public GameLogic getLogic() { return logic; }
    /**
     * Metoda zwracająca wynik ostatniego ruchu.
     * @return Obiekt MoveResult reprezentujący wynik ostatniego ruchu.
     */
    public MoveResult getLastMoveResult() { return lastMoveResult; }

    /** 
     * Metoda resetująca licznik kolejnych spasowań.
    */
    public synchronized void resetPassCounter() {
        this.consecutivePasses = 0;
    }

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