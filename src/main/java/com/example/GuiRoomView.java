/** 
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Label;
/**
    Klasa reprezentująca widok GUI pokoju gry.
 */
public class GuiRoomView {
    /**
     * Obiekt SocketClient do komunikacji z serwerem.
     * @param socketClient 
     */
    private SocketClient socketClient;
    /**
     * Obiekt Scene reprezentujący scenę GUI pokoju gry.
     * @param scene scena GUI pokoju gry.
     */
    private Scene scene;
    /**
     * Identyfikator pokoju.
     * @param roomId 
     */
    private int roomId;
    /**
     * Konstruktor klasy GuiRoomView.
     * @param socketClient Obiekt SocketClient do komunikacji z serwerem.
     * @param roomId Identyfikator pokoju.
     */
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
    /**
     * Zwraca obiekt Scene reprezentujący scenę GUI pokoju gry.
     * @return obiekt sceny.
     */
    public Scene getScene() {
        return scene;
    }
    /**
     * Metoda leaveRoom() wysyła wiadomość do serwera o opuszczeniu pokoju.
     */
    public void leaveRoom() {
        socketClient.getClientSender().sendToGui("LEAVE");
    }
    /**
     * Metoda readyRoom() wysyła wiadomość do serwera o gotowości gracza.
     */
    public void readyRoom() {
        socketClient.getClientSender().sendToGui("READY");
    }
}
