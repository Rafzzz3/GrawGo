/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import java.io.IOException;
import java.util.List;
import java.io.ObjectInputStream;
/**
    Klasa reprezentująca odbiorcę klienta, odpowiedzialnego za odbieranie wiadomości i obiektów z serwera.
 */
public class ClientReceiver implements Runnable {
    /**
     * @param input Obiekt ObjectInputStream używany do odbierania danych z serwera.
     */
    private ObjectInputStream input;
    /**
     * @param listener Obiekt GuiListner do obsługi odebranych wiadomości i obiektów.
     */
    private GuiListner listener;
    /**
     * Konstruktor klasy ClientReceiver.
     * @param input Obiekt ObjectInputStream używany do odbierania danych z serwera.
     */
    public ClientReceiver(ObjectInputStream input) {
        this.input = input;
    }
    /**
     * Metoda setListener() ustawia obiekt GuiListner do obsługi odebranych wiadomości i obiektów.
     * @param listener Obiekt GuiListner.
     */
    public void setListener(GuiListner listener) {
        this.listener = listener;
    }
    /**
     * Metoda run() uruchamia pętlę odbierającą dane z serwera i wywołującą odpowiednie metody
     * na obiekcie GuiListner w zależności od typu odebranych danych.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Object receivedObject = input.readObject();
                if (receivedObject == null) {
                    System.out.println("SERWER: Otrzymano null (brak historii/koniec zakresu)");
                    if (listener != null) {
                        listener.forHistoryMove(null); 
                    }
                    continue;
                }
                if (receivedObject instanceof String) {
                    String message = (String) receivedObject;
                    if (message.startsWith("JOINED_ROOM ")) {
                        try {
                                int roomId = Integer.parseInt(message.split(" ")[1]);
                                if (listener != null) {
                                    listener.forJoinedRoom(roomId);
                                }
                            } catch (Exception e) {
                                System.out.println("Błąd parsowania ID pokoju: " + e.getMessage());
                            }
                    } 
                    else if (message.startsWith("EXIT_ROOM")) {
                        if (listener != null) {
                            listener.forExitRoom();
                        }
                    } 
                    else if (message.startsWith("ENTER_LOBBY")) {
                        if (listener != null) {
                            listener.forEnterLobby();
                        }
                    }
                    else if (message.startsWith("ENTER_ANALYZE")) {
                        if (listener != null) {
                            listener.forEnterAnalyze();
                        }
                    }
                    else if (message.startsWith("LOAD_GAME")) {
                        String[] parts = message.split(" ");
                        int size = Integer.parseInt(parts[1]);
                        int totalMoves = Integer.parseInt(parts[2]);
                        if (listener != null) {
                            listener.forLoadGame(size, totalMoves);
                        }
                    }
                    else {
                        if (listener != null) {
                            listener.forMessage(message);
                        }
                    }
                } else if (receivedObject instanceof Board) {
                    Board board = (Board) receivedObject;
                    if (listener != null) {
                        listener.forBoard(board);
                    }
                } else if (receivedObject instanceof MoveResult) {
                    MoveResult result = (MoveResult) receivedObject;
                    if (listener != null) {
                        listener.forMoveResult(result);
                    }
                } else if (receivedObject instanceof List) {
                    List<String> lobbyList = (List<String>) receivedObject;
                    if (listener != null) {
                        listener.forLobbyList(lobbyList);
                    }
                } else if (receivedObject instanceof HistoryMove) {
                    HistoryMove historyMove = (HistoryMove) receivedObject;
                    if (listener != null) {
                        listener.forHistoryMove(historyMove);
                    }
                }
                System.out.println("SERWER:");
                System.out.println(receivedObject);
                System.out.flush();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Rozłaczono z serwerem lub wystąpił błąd: " + e.getMessage());
                break;
            }
        }
    }
    
}
