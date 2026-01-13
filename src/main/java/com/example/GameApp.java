package com.example;
import java.net.Socket;

import javafx.application.Application;
import javafx.stage.Stage;
public class GameApp extends Application implements GuiListner {
    private Stage mainStage;
    private GuiLobbyView lobbyView;
    private GuiBoardView boardView;
    private SocketClient socketClient;
    private boolean gameStarted = false;
    private static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage mainStage) {
        this.socketClient = new SocketClient();
        this.mainStage = mainStage;
        lobbyView = new GuiLobbyView(socketClient);
        boardView = new GuiBoardView(socketClient);
        try {
            socketClient.connect();
        } catch (Exception e) {
            System.out.println("Błąd podczas łączenia z serwerem: " + e.getMessage());
            return;
        }
        mainStage.setTitle("Gra Go");
        mainStage.setScene(lobbyView.getScene());
        mainStage.setOnCloseRequest(e -> {
            socketClient.close();
            System.exit(0);
        });
        mainStage.show();
    }
    @Override
    public void forMessage(String message) {
        if (gameStarted) {
            boardView.setMessage(message);
            return;
        }
        else {
            lobbyView.setMessage(message);
        }
    }
    @Override
    public void forBoard(Board board) {
        if (!gameStarted) {
            gameStarted = true;
            mainStage.setScene(boardView.getScene());
            mainStage.sizeToScene();
        }
        // Tu trzeba jeszcze wysłać board do boardView żeby aktualizować wygląd boarda;
    }
}
