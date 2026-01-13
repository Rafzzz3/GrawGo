package com.example;
import java.io.IOException;
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
                    if (listener != null) {
                        listener.forMessage(message);
                    }
                } else if (receivedObject instanceof Board) {
                    Board board = (Board) receivedObject;
                    if (listener != null) {
                        listener.forBoard(board);
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
