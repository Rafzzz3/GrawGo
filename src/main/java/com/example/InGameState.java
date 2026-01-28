package com.example;

public class InGameState implements ClientHandlerState {
    private GameCommandInterpreter gameCommandInterpreter;
    private Room room;
    private Game game;

    public InGameState() {
        this.gameCommandInterpreter = new GameCommandInterpreter();
    }

    @Override
    public void handleMessage(ClientHandler player, String message) {
        room = player.getCurrentRoom();
        game = room.getGame();
        if (message.startsWith("FETCH")) {
            gameCommandInterpreter.interpret(game,message,player);
            return;
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

        if (room.isBotGame() && game.isTurn(room.getBotColor()) && !message.startsWith("SURRENDER")) {
            if (game.getLastMoveResult() != null && game.getLastMoveResult().code == MoveCode.GAME_OVER) {
                return;
            }

            try {
                Thread.sleep(1000);     // opoznienie dla bota
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Bot.getInstance().makeMove(game, room.getBotColor());
            handleMoveResult(player, game, room);
        }
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
                    if (room.isBotGame()) {
                        p.switchToMenuState();
                    } else {
                        p.switchToRoomState();
                    }
                }
            }

            if (result.code == MoveCode.OK || result.code == MoveCode.PASS) {
                for (ClientHandler p : currentRoom.getPlayers()) {
                    p.getServerSender().sendObject(game.getBoard());
                    if (result.code == MoveCode.PASS && player != p) {
                        p.getServerSender().sendMessage(LobbyMessageType.INFO + ": Przeciwnik spasował.");
                    }
                }
            } 

            if (result.code == MoveCode.GAME_OVER || result.code == MoveCode.SURRENDER) {
                for (ClientHandler p : currentRoom.getPlayers()) {
                    if (room.isBotGame()) {
                        p.switchToMenuState();
                    } else {
                        p.switchToRoomState();
                    }
                }
            }

            game.setLastMoveResult(null);
        }
    }
}
