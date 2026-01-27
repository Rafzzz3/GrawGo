package com.example;

public class GameCommandFETCHDATA implements CommandInterfaceExecutor {
    @Override
    public void execute(Game game, String args, ClientHandler player) {
        String[] parts = args.trim().split("");
        int index = Integer.parseInt(parts[0]);
        HistoryMove delta = game.getHistoryDelta(index);
        player.getServerSender().sendObject(delta);
    }
}