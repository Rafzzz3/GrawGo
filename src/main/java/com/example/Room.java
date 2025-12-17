package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {
    private int id;
    private Game game;
    private List<ClientHandler> players = new ArrayList<>();
    private boolean gameStarted = false;
    private Stone firstSeatColor;
    private final Random random = new Random();

    public Room(int id) {
        this.id = id;
        this.game = new Game(19);
    }

    public void addPlayer(ClientHandler player) {
        if (players.size() == 0) {
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

    public int getId() {
        return id;
    }
    public int getPlayerCount() {
        return players.size();
    }
    public Game getGame() {
        return game;
    }
    public boolean isGameStarted() {
        return gameStarted;
    }
    public void setGameStarted() {
        for (ClientHandler player : players) {
            if (!player.isReady()) {
                return;
            }
        }
        gameStarted = true;
    }
    public List<ClientHandler> getPlayers() {
        return players;
    }
}
