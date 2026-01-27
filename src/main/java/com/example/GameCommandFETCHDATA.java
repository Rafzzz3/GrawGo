package com.example;

public class GameCommandFETCHDATA implements CommandInterfaceExecutor {
    @Override
    public void execute(Game game, String args, ClientHandler player) {
        int index = Integer.parseInt(args);
        HistoryMove delta = game.getHistoryDelta(index);
        player.getServerSender().sendObject(delta);
    }
}