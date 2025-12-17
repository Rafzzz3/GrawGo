package com.example;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private int id;
    private Game game;
    private List<ClientHandler> players = new ArrayList<>();
    private boolean gameStarted = false;
    public Room(int id) {
        this.id = id;
        this.game = new Game(19);
    }
    public void addPlayer(ClientHandler player) {
        if (players.size() < 2) {
            players.add(player);
        }
        else {
            System.out.println("Pokój pełny, nie można dodać więcej graczy.");
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
