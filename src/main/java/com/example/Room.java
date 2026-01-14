package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {
    private int id;
    private int readyCounter = 0;
    private Game game;
    private List<ClientHandler> players = new ArrayList<>();
    private boolean gameStarted = false;
    private Stone firstSeatColor;
    private final Random random = new Random();

    public Room(int id, int size) {
        this.id = id;
        this.game = new Game(size);
    }

    public void addPlayer(ClientHandler player) {
        if (players.isEmpty()) {
            // losuj kolor dla pierwszego gracza
            boolean blackFirst = random.nextBoolean();
            if (blackFirst) {
                player.setPlayerColor(Stone.BLACK);
                firstSeatColor = Stone.BLACK;
            } else {
                player.setPlayerColor(Stone.WHITE);
                firstSeatColor = Stone.WHITE;
            }
        } else {
            // drugi gracz dostaje przeciwny kolor
            if (firstSeatColor == Stone.BLACK) {
                player.setPlayerColor(Stone.WHITE);
            } else {
                player.setPlayerColor(Stone.BLACK);
            }
        }
        players.add(player);
    }
    public void removePlayer(ClientHandler player) {
        players.remove(player);
        if (players.isEmpty()) {
            readyCounter = 0;
            gameStarted = false;
        }
    }
    public int getId() {
        return id;
    }
    public int getPlayerCount() {
        return players.size();
    }
    public Game getGame() {
        return game;
    }
    public int getReadyCounter() {
        return readyCounter;
    }

    public void incrementReadyCounter() {
        readyCounter++;
    }

    public boolean isGameStarted() {
        if (readyCounter == 2) {
            gameStarted = true;
        }
        return gameStarted;
    }

    public List<ClientHandler> getPlayers() {
        return players;
    }
}
