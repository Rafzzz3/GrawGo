package com.example;

public class CommandMOVE implements CommandInterfaceExecutor {

    @Override
    public void execute(Game game, String args, ClientHandler player) {
        String[] parts = args.trim().split("\\s+");
        if (parts.length != 2) {
            game.setMessage("Nieprawidłowa liczba argumentów dla komendy MOVE.");
            return;
        }
        try {
            int ux = Integer.parseInt(parts[0]); // klient podaje 1..size
            int uy = Integer.parseInt(parts[1]);

            // konwersja na 0-based dla logiki gry
            int x = ux - 1;
            int y = uy - 1;
            game.putStone(x, y);
        } catch (NumberFormatException e) {
            game.setMessage("Nieprawidłowe współrzędne. Użyj liczb całkowitych.");
        } catch (IllegalArgumentException e) {
            game.setMessage("Nieprawidłowe współrzędne. Spróbuj ponownie.");
        }
    }
}
