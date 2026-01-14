package com.example;

public class GameCommandPASS implements CommandInterfaceExecutor {
    @Override
    public void execute(Game game, String args, ClientHandler player) {
        MoveResult result = game.pass(player.getPlayerColor());
        
        game.setLastMoveResult(result);
    }
}