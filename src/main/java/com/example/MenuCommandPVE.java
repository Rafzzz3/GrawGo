package com.example;

public class MenuCommandPVE implements MenuCommandInterfaceExecutor {
    @Override
    public void execute(ClientHandler player, String args) {
        int size = Integer.parseInt(args.trim());

        player.getRoomManager();
        Room room = player.getRoomManager().createRoom(size);
        room.setBotGame(true);
        room.addPlayer(player);
        player.setCurrentRoom(room);
        // player.switchToRoomState();

        player.getServerSender().sendMessage(LobbyMessageType.INFO.name() + ": Rozpoczęto grę z BOTEM na planszy " + size + "x" + size);
        player.getServerSender().sendMessage("JOINED_ROOM " + room.getId());
        
        room.incrementReadyCounter();
        room.incrementReadyCounter();

        player.switchToGameState();
        player.getServerSender().sendObject(room.getGame().getBoard());
        
        // ruch bota
        if (player.getPlayerColor() == Stone.WHITE) {
            player.getServerSender().sendMessage(LobbyMessageType.INFO.name() + ": Masz BIAŁE.");

            Bot.getInstance().makeMove(room.getGame(), Stone.BLACK);
            
            Game game = room.getGame();
            if (game.getLastMoveResult() != null) {
                player.getServerSender().sendObject(game.getLastMoveResult());
                player.getServerSender().sendObject(game.getBoard()); 
                game.setLastMoveResult(null);
            }
        // ruch gracza
        } else {
            player.getServerSender().sendMessage(LobbyMessageType.INFO.name() + ": Masz CZARNE. Twój ruch.");
        }
    }
}
