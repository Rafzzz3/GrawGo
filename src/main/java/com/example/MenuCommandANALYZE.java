package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.database.GameDocument;
import com.example.database.GameService;

public class MenuCommandANALYZE implements MenuCommandInterfaceExecutor {
    private GameService gameService;
    public MenuCommandANALYZE(GameService gameService) {
        this.gameService = gameService;
    }
    public void execute(ClientHandler player, String args) {
        player.switchToAnalyzeState();
        player.getServerSender().sendMessage("ENTER_ANALYZE");
        List<GameDocument> games = gameService.getAllGames();
        List<String> gameDescriptions = new ArrayList<>();
        for (GameDocument game : games) {
            String desc = "ID: " + game.getId() + " Data: " + game.getPlayedAt().toString().substring(0, 16) + "  Wygrał: " + game.getWinner();
            gameDescriptions.add(desc);
        }
        player.getServerSender().sendObject(gameDescriptions);
    }
}
