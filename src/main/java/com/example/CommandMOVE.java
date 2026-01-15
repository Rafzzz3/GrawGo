/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

/** 
 * Klasa odpowiedzialna za wykonanie polecenia ruchu w grze.
 */
public class CommandMOVE implements CommandInterfaceExecutor {

    /** 
     * Metoda wykonująca polecenie ruchu w grze.
     * @param game Obiekt gry, na którym wykonywane jest polecenie ruchu.
     * @param args Argumenty polecenia w formie łańcucha znaków (współrzędne ruchu).
     * @param player Obiekt obsługujący klienta, który wysłał polecenie.
    */
    @Override
    public void execute(Game game, String args, ClientHandler player) {
        String[] parts = args.trim().split("\\s+");
        if (parts.length != 2) {
            game.setMessage("Nieprawidłowa liczba argumentów dla komendy MOVE.");
            return;
        }
        try {
            int ux = Integer.parseInt(parts[0]);
            int uy = Integer.parseInt(parts[1]);
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
