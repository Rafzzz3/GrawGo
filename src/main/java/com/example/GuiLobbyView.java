package com.example;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
public class GuiLobbyView {
    private Scene scene;
    private ListView<String> roomList;
    private SocketClient socketClient;
    public GuiLobbyView(SocketClient socketClient) {
        this.socketClient = socketClient;
        VBox layoutBox = new VBox(10);
        layoutBox.setPadding(new Insets(10));
        Label titleLabel = new Label("Lobby gry");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        roomList = new ListView<>();
        roomList.setPrefHeight(400);
        roomList.setPrefWidth(400);
        HBox buttonBox = new HBox(10);
        TextField inputField = new TextField("Podaj rozmiar pokoju");
        Button createButton = new Button("Create Room");
        Button readyButton = new Button("Ready");
        Button refreshButton = new Button("Refresh List");
        buttonBox.getChildren().addAll(inputField, roomList, createButton, readyButton, refreshButton);
        layoutBox.getChildren().addAll(titleLabel, buttonBox);
        createButton.setOnAction(e -> createRoom(inputField.getText()));
        refreshButton.setOnAction(e -> refreshRoomList());
        roomList.setOnMouseClicked(e -> handleRoomClicked());
        scene = new Scene(layoutBox, 800, 1000); 
    }
    public Scene getScene() {
        return scene;
    }
    public void setMessage(String message) {
        Platform.runLater(() -> {
            String prefix = message.split(":", 2)[0];
            String trimmedMessage = message.split(":", 2)[1].trim();
            Alert.AlertType type;
            switch (prefix) {
                case "ALERT":
                    type = Alert.AlertType.ERROR; 
                    break;
                case "INFO":
                    type = Alert.AlertType.INFORMATION;
                    break;
                default:
                    type = Alert.AlertType.NONE;
                    break;
            }
            showPopup(type, message);
        });
    }
    public void createRoom(String size) {
        socketClient.getClientSender().sendToGui("CREATE " + size);
    }
    public void refreshRoomList() {
        socketClient.getClientSender().sendToGui("LIST");
    }
    public void updateRoomList(List<String> rooms) {
        Platform.runLater(() -> {
            roomList.getItems().clear();
            roomList.getItems().addAll(rooms);
        });
    }
    public void handleRoomClicked() {
        String selectedRoom = roomList.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            String roomId = selectedRoom.split(",")[0].split(":")[1].trim();
            socketClient.getClientSender().sendToGui("JOIN " + roomId);
            updateRoomList(roomList.getItems());
        }
    }
    private void showPopup(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("");
        alert.setContentText(message);      
        alert.showAndWait();
    }
}
