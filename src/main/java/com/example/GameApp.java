package com.example;
import java.net.Socket;

import javafx.application.Application;
import javafx.stage.Stage;
public class GameApp extends Application {
    private Stage mainStage;
    private GuiLobbyView lobbyView;
    private GuiBoardView boardView;
    private SocketClient socketClient;
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

}
