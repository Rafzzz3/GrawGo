package com.example;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
public class GuiMainMenuView {
    private SocketClient socketClient;
    private Scene scene;
    public GuiMainMenuView(SocketClient socketClient) {
        this.socketClient = socketClient;
        VBox layoutBox = new VBox(10);
        Label titleLabel = new Label("Main Menu");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        Button playPlayerButton  = new Button("Play vs Player");
        Button playBotButton = new Button("Play vs Bot");
        Button analyzeGameButton = new Button("Analyze Game");
        layoutBox.getChildren().addAll(titleLabel, playPlayerButton, playBotButton, analyzeGameButton);
        playPlayerButton.setOnAction(e -> playVsPlayer());
        playBotButton.setOnAction(e -> playVsBot());
        analyzeGameButton.setOnAction(e -> analyzeGame());
        scene = new Scene(layoutBox, 400, 400); 
    }
    public Scene getScene() {
        return scene;
    } 
    private void playVsPlayer() {
        socketClient.getClientSender().sendToGui("PVP");
    }
    private void playVsBot() {
        socketClient.getClientSender().sendToGui("PVE");
    }
    private void analyzeGame() {
        socketClient.getClientSender().sendToGui("ANALYZE");
    }

}
