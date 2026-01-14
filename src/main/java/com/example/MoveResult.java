package com.example;

import java.io.Serializable;

public class MoveResult implements Serializable {
    public final MoveCode code;
    public final int[][] captured;
    public final String message;

    public MoveResult(MoveCode code, int[][] captured, String message) {
        this.code = code;
        this.captured = captured;
        this.message = message;
    }
}