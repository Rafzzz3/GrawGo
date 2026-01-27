package com.example;

public class InGameState implements ClientHandlerState {
    private GameCommandInterpreter gameCommandInterpreter;
    private Room room;
    private Game game;
    @Override
    public void handleMessage(ClientHandler player, String message) {
        room = player.getCurrentRoom();
        game = room.getGame();
        if (message.startsWith("FETCH")) {
            gameCommandInterpreter.interpret(game,message,player);
        }
        if (!game.isTurn(player.getPlayerColor())) {
            MoveResult notTurnResult = new MoveResult(
                MoveCode.NOT_YOUR_TURN, 
                new int[0][], 
            "Nie twoja tura. Czekaj na ruch przeciwnika."
            );
            player.getServerSender().sendObject(notTurnResult);
            return;
        }
        gameCommandInterpreter.interpret(game, message, player);
        handleMoveResult(player, game, room);
    }
    private void handleMoveResult(ClientHandler player, Game game, Room currentRoom) {
        if (game != null && game.getLastMoveResult() != null) {
            MoveResult result = game.getLastMoveResult();
            player.getServerSender().sendObject(result);
            if (result.code == MoveCode.SURRENDER || result.code == MoveCode.GAME_OVER) {
                for (ClientHandler p : currentRoom.getPlayers()) {
                    if (p != player) { 
                        p.getServerSender().sendObject(result);
                    }
                }
            }                        
            if (result.code == MoveCode.OK || result.code == MoveCode.PASS) {
                for (ClientHandler p : currentRoom.getPlayers()) {
                    player.getServerSender().sendObject(game.getBoard());
                    if (result.code == MoveCode.PASS && player != p) {
                        p.getServerSender().sendMessage(LobbyMessageType.INFO + ": Przeciwnik spasował.");
                    }
                }
            } 
            game.setLastMoveResult(null);
        }
    }
}
