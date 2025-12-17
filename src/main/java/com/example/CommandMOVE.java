package com.example;

public class CommandMOVE implements CommandInterfaceExecutor {

    @Override
    public void execute(Game game, String args) {
        String[] parts = args.split(" ");
        if (parts.length != 3) {
            System.out.println("Nieprawidłowa liczba argumentów dla komendy MOVE.");
            return;
        }
        try {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            Stone color = Stone.valueOf(parts[2].toUpperCase());
            boolean success = game.putStone(color, x, y);
            System.out.println(game.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Nieprawidłowe współrzędne. Użyj liczb całkowitych.");
        } catch (IllegalArgumentException e) {
            System.out.println("Nieprawidłowy kolor kamienia. Dostępne kolory: BLACK, WHITE.");
        }
    }
    
}
