package com.example;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Label;
public class GuiRoomView {
    private SocketClient socketClient;
    private Scene scene;
    private int roomId;
    public GuiRoomView(SocketClient socketClient, int roomId) {
        this.socketClient = socketClient;
        this.roomId = roomId;
        VBox layoutBox = new VBox(10);
        Label titleLabel = new Label("Pokój nr " + roomId);
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight");
        Button leaveButton = new Button("Leave room");
        Button readyButton = new Button("Ready");
        readyButton.setOnAction(e -> readyRoom());
        leaveButton.setOnAction(e -> leaveRoom());
        layoutBox.getChildren().addAll(readyButton, leaveButton);
        scene = new Scene(layoutBox, 400, 200);
    }
    public Scene getScene() {
        return scene;
    }
    public void leaveRoom() {
        socketClient.getClientSender().sendToGui("LEAVE");
    }
    public void readyRoom() {
        socketClient.getClientSender().sendToGui("READY");
    }
}
