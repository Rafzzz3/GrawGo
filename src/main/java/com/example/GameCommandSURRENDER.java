/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

/** 
 * Klasa odpowiedzialna za wykonanie polecenia poddania się w grze.
*/
public class GameCommandSURRENDER implements CommandInterfaceExecutor {
    /** 
     * Metoda wykonująca polecenie poddania się w grze.
     * @param game Obiekt gry, na którym wykonywane jest polecenie poddania się.
     * @param args Argumenty polecenia w formie łańcucha znaków (nieużywane).
     * @param player Obiekt obsługujący klienta, który wysłał polecenie.
    */
    @Override
    public void execute(Game game, String args, ClientHandler player) {
        MoveResult result = game.surrender(player.getPlayerColor());
        
        game.setLastMoveResult(result);
    }
}