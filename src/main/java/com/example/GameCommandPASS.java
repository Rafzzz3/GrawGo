/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

/** 
 * Klasa odpowiedzialna za wykonanie polecenia spasowania w grze.
*/
public class GameCommandPASS implements CommandInterfaceExecutor {
    /** 
     * Metoda wykonująca polecenie spasowania w grze.
     * @param game Obiekt gry, na którym wykonywane jest polecenie spasowania.
     * @param args Argumenty polecenia w formie łańcucha znaków (nieużywane).
     * @param player Obiekt obsługujący klienta, który wysłał polecenie.
    */
    @Override
    public void execute(Game game, String args, ClientHandler player) {
        MoveResult result = game.pass(player.getPlayerColor());
        
        game.setLastMoveResult(result);
    }
}