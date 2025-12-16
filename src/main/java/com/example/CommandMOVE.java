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
            // StoneColor color = StoneColor.valueOf(parts[3].toUpperCase());

            // boolean success = game.makeMove(x, y, color);
            // if (success) {
            //     System.out.println("Ruch wykonany pomyślnie.");
            // } else {
            //     System.out.println("Ruch nieudany: " + game.getMessage());
            // }
        } catch (NumberFormatException e) {
            System.out.println("Nieprawidłowe współrzędne. Użyj liczb całkowitych.");
        } catch (IllegalArgumentException e) {
            System.out.println("Nieprawidłowy kolor kamienia. Dostępne kolory: BLACK, WHITE.");
        }
    }
    
}
