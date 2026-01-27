package com.example;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;
public class GuiAnalyzeView {
    private SocketClient socketClient;
    private Scene scene;
    private ListView<String> gameList;
    public GuiAnalyzeView(SocketClient socketClient) {
        this.socketClient = socketClient;
        VBox layoutBox = new VBox(10);
        layoutBox.setPadding(new Insets(10));
        Label titleLabel = new Label("Analizuj grę");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        gameList = new ListView<>();
        gameList.setPrefHeight(400);
        gameList.setPrefWidth(400);
        layoutBox.getChildren().addAll(titleLabel, gameList);
        gameList.setOnMouseClicked(e -> handleGameClicked());
        scene = new Scene(layoutBox, 800, 1000); 
    }
    public Scene getScene() {
        return scene;
    } 
    public void updateGameList(List<String> games) {
        Platform.runLater(() -> {
            gameList.getItems().clear();
            gameList.getItems().addAll(games);
        });
    }
    public void handleGameClicked() {
        String selectedGame = gameList.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            String gameId = selectedGame.split(",")[0].split(":")[1].trim();
            socketClient.getClientSender().sendToGui("ANALYZE_GAME:" + gameId);
        }
    }

}
