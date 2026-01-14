package com.example;
import javafx.scene.control.Button;
public class GuiRoomView {
    private SocketClient socketClient;
    private Room room;
    public GuiRoomView(SocketClient socketClient, Room room) {
        this.socketClient = socketClient;
        this.room = room;
        Button leaveButton = new Button("Leave room");
        Button readyButton = new Button("Ready");
        readyButton.setOnAction(e -> readyRoom());
        leaveButton.setOnAction(e -> leaveRoom());
    }
    public void leaveRoom() {
        socketClient.getClientSender().sendToGui("LEAVE");
    }
    public void readyRoom() {
        socketClient.getClientSender().sendToGui("READY");
    }
}
