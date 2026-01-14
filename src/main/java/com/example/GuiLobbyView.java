package com.example;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
public class GuiLobbyView {
    private Scene scene;
    private TextArea infoArea;
    private SocketClient socketClient;
    public GuiLobbyView(SocketClient socketClient) {
        this.socketClient = socketClient;
        VBox layoutBox = new VBox(10);
        layoutBox.setPadding(new Insets(10));
        Label titleLabel = new Label("Lobby gry");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        infoArea = new TextArea();
        infoArea.setEditable(false);
        infoArea.setPrefHeight(400);
        infoArea.setText("Oczekiwanie na połączenie");
        HBox buttonBox = new HBox(10);
        TextField inputField = new TextField("Podaj rozmiar pokoju");
        Button createButton = new Button("Create Room");
        Button joinButton = new Button("Join Room");
        Button readyButton = new Button("Ready");
        buttonBox.getChildren().addAll(inputField, createButton, joinButton, readyButton);
        layoutBox.getChildren().addAll(titleLabel, infoArea, buttonBox);
        createButton.setOnAction(e -> {
            socketClient.getClientSender().sendToGui("CREATE " + inputField.getText());
        });
        joinButton.setOnAction(e -> {
            socketClient.getClientSender().sendToGui("JOIN " + inputField.getText());
        });
        readyButton.setOnAction(e -> {
            socketClient.getClientSender().sendToGui("READY");
        });
        scene = new Scene(layoutBox, 400, 500); 
    }
    public Scene getScene() {
        return scene;
    }
    public void setMessage(String message) {
        infoArea.appendText("\n" + message);
    }
}
