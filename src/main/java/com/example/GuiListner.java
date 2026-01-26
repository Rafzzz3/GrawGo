/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import java.util.List;
/**
 * Interfejs nasłuchiwacza GUI do obsługi różnych zdarzeń i danych otrzymywanych z serwera, jak i zmiany w GUI.
 */
public interface GuiListner {
    /** 
     * Metoda forMessage() obsługuje otrzymaną wiadomość.
     * @param msg Otrzymana wiadomość.
    */
    void forMessage(String msg);
    /** 
     * Metoda forBoard() obsługuje otrzymaną planszę gry.
     * @param board Otrzymana plansza gry.
    */
    void forBoard(Board board);
    /** 
     * Metoda forLobbyList() obsługuje otrzymaną listę pokoi w lobby gry.
     * @param lobbyList Otrzymana lista pokoi.
    */
    void forLobbyList(List<String> lobbyList);
    /** 
     * Metoda forMoveResult() obsługuje otrzymany wynik ruchu.
     * @param result Otrzymany wynik ruchu.
    */
    void forMoveResult(MoveResult result);
    void forJoinedRoom(int roomId);
    void forExitRoom();
    void forHistoryMove(HistoryMove move);
}
