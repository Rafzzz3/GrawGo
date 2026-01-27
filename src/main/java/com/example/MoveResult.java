/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

import java.io.Serializable;

/**
 * Klasa reprezentująca wynik ruchu w grze.
 */
public class MoveResult implements Serializable {
    /**
     * Kod wyniku ruchu.
     */
    public MoveCode code;
    /**
     * Tablica współrzędnych uduszonych kamieni.
     */
    public int[][] captured;
    /**
     * Komunikat opisujący wynik ruchu.
     */
    public String message;
    
    public MoveResult() {}

    /**
     * Konstruktor klasy MoveResult.
     * @param code Kod wyniku ruchu.
     * @param captured Tablica współrzędnych uduszonych kamieni.
     * @param message Komunikat opisujący wynik ruchu.
     */
    public MoveResult(MoveCode code, int[][] captured, String message) {
        this.code = code;
        this.captured = captured;
        this.message = message;
    }
}