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

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> socketClient.getClientSender().sendToGui("LIST"));

        Button returnButton = new Button("Back to Menu");
        returnButton.setOnAction(e -> socketClient.getClientSender().sendToGui("EXIT"));


        gameList = new ListView<>();
        gameList.setPrefHeight(400);
        gameList.setPrefWidth(400);
        layoutBox.getChildren().addAll(titleLabel, gameList, refreshButton, returnButton);
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
        if (selectedGame != null && selectedGame.contains("ID: ")) {
            try {
                int start = selectedGame.indexOf("ID: ") + 4; 
                int end = selectedGame.indexOf(" ", start);
                if (end == -1) {
                    end = selectedGame.length();
                }
                
                String gameId = selectedGame.substring(start, end).trim();
                if (gameId.isEmpty()) {
                    return;
                }
                socketClient.getClientSender().sendToGui("ANALYZEGAME " + gameId);
                
            } catch (Exception e) {
                System.out.println("Błąd parsowania ID gry: " + e.getMessage());
            }
        }
    }

}
