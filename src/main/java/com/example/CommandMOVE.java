package com.example;

public class CommandMOVE implements CommandInterfaceExecutor {

    @Override
    public void execute(Game game, String args, ClientHandler gracz) {
        String[] parts = args.trim().split("\\s+");
        if (parts.length != 2) {
            System.out.println("Nieprawidłowa liczba argumentów dla komendy MOVE.");
            return;
        }
        try {
            int ux = Integer.parseInt(parts[0]); // klient podaje 1..size
            int uy = Integer.parseInt(parts[1]);

            // konwersja na 0-based dla logiki gry
            int x = ux - 1;
            int y = uy - 1;

            boolean success = game.putStone(x, y);
            System.out.println(game.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Nieprawidłowe współrzędne. Użyj liczb całkowitych.");
        } catch (IllegalArgumentException e) {
            System.out.println("Nieprawidłowy kolor kamienia. Dostępne kolory: BLACK, WHITE.");
        }
    }
}
