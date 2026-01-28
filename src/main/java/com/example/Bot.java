package com.example;

import java.util.Random;

/**
 * Singleton zaimplementowany metodą "Double-Checked Locking".
 */
public class Bot {
    private static volatile Bot instance;
    
    private final Random random = new Random();

    private Bot() {}

    //Double-Checked Locking
    public static Bot getInstance() {
        if (instance == null) {
            synchronized (Bot.class) {
                if (instance == null) {
                    instance = new Bot();
                }
            }
        }
        return instance;
    }

    /**
     * Główna logika bota.
     */
    public void makeMove(Game game, Stone botColor) {
        int size = game.getBoard().getBoardSize();
        boolean moveMade = false;
        int attempts = 0;
        int maxAttempts = size * size * 2; 

        while (!moveMade && attempts < maxAttempts) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);

            if (game.getBoard().getStone(x, y) == Stone.EMPTY) {
                
                moveMade = game.putStone(x, y);
            }
            attempts++;
        }

        if (!moveMade) {
            System.out.println("BOT pasuje.");
            game.pass(botColor);
        } else {
            System.out.println("BOT wykonał ruch.");
        }
    }
}