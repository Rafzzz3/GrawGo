package com.example;

public class GameCommandSURRENDER implements CommandInterfaceExecutor {
    @Override
    public void execute(Game game, String args, ClientHandler player) {
        MoveResult result = game.surrender(player.getPlayerColor());
        
        game.setLastMoveResult(result);
    }
}