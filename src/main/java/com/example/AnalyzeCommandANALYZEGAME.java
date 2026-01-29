package com.example;

import com.example.database.GameDocument;
import com.example.database.GameService;

public class AnalyzeCommandANALYZEGAME implements AnalyzeCommandInterfaceExecutor {
    private GameService gameService;
    public AnalyzeCommandANALYZEGAME(GameService gameService) {
        this.gameService = gameService;
    }
    @Override
    public void execute(ClientHandler player, String args) {
        String gameId = args.trim();
        GameDocument doc = gameService.getGame(gameId);
        if (doc != null) {
            Game game = new Game(doc.getSize(), null);
            game.setHistory(doc.getMoves());

            player.setAnalyzedGame(game);
            player.getServerSender().sendMessage("LOAD_GAME " + doc.getSize() + " " + doc.getMoves().size());
        }
        else {
            player.getServerSender().sendMessage("ERROR: Nie znaleziono gry");
        }
    }
}
