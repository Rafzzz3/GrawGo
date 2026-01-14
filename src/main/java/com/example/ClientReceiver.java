package com.example;
import java.io.IOException;
import java.util.List;
import java.io.ObjectInputStream;
public class ClientReceiver implements Runnable {
    private ObjectInputStream input;
    private GuiListner listener;
    public ClientReceiver(ObjectInputStream input) {
        this.input = input;
    }
    public void setListener(GuiListner listener) {
        this.listener = listener;
    }
    @Override
    public void run() {
        while (true) {
            try {
                Object receivedObject = input.readObject();
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
                    } else {
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
