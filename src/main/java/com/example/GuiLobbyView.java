/** 
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Optional;
/**
    Klasa reprezentująca widok GUI lobby gry.
 */
public class GuiLobbyView {
    /**
     * @param scene Obiekt Scene reprezentujący scenę GUI lobby gry.
     */
    private Scene scene;
    /**
     * @param roomList Lista pokoi dostępnych w lobby gry.
     */
    private ListView<String> roomList;
    /**
     * @param socketClient Obiekt SocketClient do komunikacji z serwerem.
     */
    private SocketClient socketClient;
    /**
     * Konstruktor klasy GuiLobbyView.
     * @param socketClient Obiekt SocketClient do komunikacji z serwerem.
     */
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
        Button refreshButton = new Button("Refresh List");
        buttonBox.getChildren().addAll(createButton, refreshButton);
        layoutBox.getChildren().addAll(titleLabel, buttonBox, roomList);
        createButton.setOnAction(e -> showCreateRoomDialog());
        refreshButton.setOnAction(e -> refreshRoomList());
        roomList.setOnMouseClicked(e -> handleRoomClicked());
        scene = new Scene(layoutBox, 800, 1000); 
    }
    /**
     * Zwraca obiekt Scene reprezentujący scenę GUI lobby gry.
     * @return obiekt sceny.
     */
    public Scene getScene() {
        return scene;
    }
    /**
     * Metoda setMessage() wyświetla komunikat w oknie popup.
     * @param message Komunikat do wyświetlenia.
     */
    public void setMessage(String message) {
        Platform.runLater(() -> {
            String prefix = message.split(":", 2)[0];
            String trimmedMessage = message.split(":", 2)[1].trim();
            Alert.AlertType type;
            switch (prefix) {
                case "ERROR":
                    type = Alert.AlertType.ERROR; 
                    break;
                case "INFO":
                    type = Alert.AlertType.INFORMATION;
                    break;
                case "ALERT":
                    type = Alert.AlertType.WARNING;
                    break;
                default:
                    type = Alert.AlertType.NONE;
                    break;
            }
            showPopup(type, trimmedMessage);
        });
    }
    /**
     * Metoda createRoom() wysyła wiadomość do serwera o utworzeniu nowego pokoju.
     * @param size Rozmiar planszy nowego pokoju.
     */
    public void createRoom(String size) {
        socketClient.getClientSender().sendToGui("CREATE " + size);
    }
    /**
     * Metoda showCreateRoomDialog() wyświetla okno dialogowe do wyboru rozmiaru planszy nowego pokoju.
     */
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
    /**
     * Metoda refreshRoomList() wysyła wiadomość do serwera o odświeżeniu listy pokoi.
     */
    public void refreshRoomList() {
        socketClient.getClientSender().sendToGui("LIST");
    }
    /**
     * Metoda updateRoomList() aktualizuje listę pokoi w GUI lobby gry.
     * @param rooms Lista pokoi do wyświetlenia.
     */
    public void updateRoomList(List<String> rooms) {
        Platform.runLater(() -> {
            roomList.getItems().clear();
            roomList.getItems().addAll(rooms);
        });
    }
    /**
     * Metoda handleRoomClicked() obsługuje kliknięcie na pokój w liście pokoi i wysyła wiadomość 
     * do serwera o dołączeniu do wybranego pokoju.
     */
    public void handleRoomClicked() {
        String selectedRoom = roomList.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            String roomId = selectedRoom.split(",")[0].split(":")[1].trim();
            socketClient.getClientSender().sendToGui("JOIN " + roomId);
            
            updateRoomList(roomList.getItems());
        }
    }
    /**
     * Metoda showPopup() wyświetla okno popup z określonym typem i komunikatem.
     * @param type Typ okna popup (np. INFORMATION, ERROR).
     * @param message Komunikat do wyświetlenia.
     */
    private void showPopup(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("");
        alert.setContentText(message);      
        alert.showAndWait();
    }
}
