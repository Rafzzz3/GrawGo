package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.database.GameDocument;
import com.example.database.GameService;
public class AnalyzeCommandInterpreter {
    private Map<String, AnalyzeCommandInterfaceExecutor> commandList = new HashMap<>();
    private GameCommandFETCHDATA fetchExecutor = new GameCommandFETCHDATA();
    private GameService gameService;
    public AnalyzeCommandInterpreter(GameService gameService) {
        this.gameService = gameService;
        commandList.put("ANALYZEGAME", new AnalyzeCommandANALYZEGAME(gameService));
        commandList.put("EXIT_ANALYZE", new AnalyzeCommandANALYZEEXIT());
    }
    public void interpret(ClientHandler player, String message) {
        String[] commandsMessage = message.trim().split(" ", 2);
        String commandName = commandsMessage[0].toUpperCase();
        String args = ""; 
        if (commandsMessage.length > 1) {
            args = commandsMessage[1]; 
        }
        if ("FETCH_DELTA".equals(commandName)) {
            Game game = player.getAnalyzedGame();
            if (game != null) {
                fetchExecutor.execute(game, args, player);
            }
            return;
        }
        if ("LIST".equals(commandName)) {
            List<GameDocument> games = gameService.getAllGames();
            List<String> gameDescriptions = new ArrayList<>();
            for (GameDocument game : games) {
                String desc = "ID: " + game.getId() + " Data: " + game.getPlayedAt().toString().substring(0, 16) + "  Wygrał: " + game.getWinner();
                gameDescriptions.add(desc);
            }
            player.getServerSender().sendObject(gameDescriptions);
            return;
        }
        AnalyzeCommandInterfaceExecutor command = commandList.get(commandName);
        if (command != null) {
            command.execute(player,args);
        } else {
            System.out.println("Nieznana komenda: " + commandName);
        } 
        
    }
}
