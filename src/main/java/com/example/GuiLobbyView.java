package com.example;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Optional;
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
        Button createButton = new Button("Create Room");
        Button readyButton = new Button("Ready");
        Button refreshButton = new Button("Refresh List");
        buttonBox.getChildren().addAll(createButton, readyButton, refreshButton);
        layoutBox.getChildren().addAll(titleLabel, buttonBox, roomList);
        createButton.setOnAction(e -> showCreateRoomDialog());
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
            showPopup(type, trimmedMessage);
        });
    }
    public void createRoom(String size) {
        socketClient.getClientSender().sendToGui("CREATE " + size);
    }
    private void showCreateRoomDialog() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("19", "9", "13", "19");
        dialog.setTitle("Nowy Pokój");
        dialog.setHeaderText("Wybierz rozmiar planszy:");
        dialog.setContentText("Rozmiar:");

        Optional<String> wynik = dialog.showAndWait();
        if (wynik.isPresent()) {
            String rozmiar = wynik.get();
            createRoom(rozmiar);
        }
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
