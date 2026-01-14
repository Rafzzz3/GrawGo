package com.example;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class GuiBoardView {     
    private Scene scene;
    private SocketClient socketClient;
    private GoDrawingPanel drawingPanel;
    private TextArea infoArea;
    public GuiBoardView(SocketClient socketClient) {
        this.socketClient = socketClient;
        BorderPane layout = new BorderPane();
        Button passButton = new Button("Pass");
        Button surrenderButton = new Button("Surrender");
        layout.setTop(infoArea);
        drawingPanel = new GoDrawingPanel();
        passButton.setOnAction(e -> passTurn());
        surrenderButton.setOnAction(e -> surrenderGame());
        drawingPanel.setOnMoveListener(command -> socketClient.getClientSender().sendToGui(command));
        layout.setLeft(passButton);
        layout.setRight(surrenderButton);
        layout.setCenter(drawingPanel);
        scene = new Scene(layout, 600, 600);
    }
    public Scene getScene() {
        return scene;
    }
    public void passTurn() {
        socketClient.getClientSender().sendToGui("PASS");
    }
    public void surrenderGame() {
        socketClient.getClientSender().sendToGui("SURRENDER");
    }
    public void updateBoard(Board board) {
        Platform.runLater(() -> drawingPanel.updateBoard(board));
    }
    public void handleMoveResult(MoveResult result) {
        // Logika wyświetlania popupów w zależności od kodu błędu
        Platform.runLater(() -> {
            switch (result.code) {
                case SUICIDE:
                    showPopup(AlertType.WARNING, "Samobójstwo!", result.message);
                    break;
                case KO:
                    showPopup(AlertType.WARNING, "Zasada KO", result.message);
                    break;
                case OCCUPIED:
                    showPopup(AlertType.ERROR, "Pole zajęte", result.message);
                    break;
                case INVALID_POSITION:
                    showPopup(AlertType.ERROR, "Błąd", result.message);
                    break;
                case NOT_YOUR_TURN:
                    showPopup(AlertType.INFORMATION, "Czekaj!", result.message);
                    break;
                case OK:
                    break;
                case PASS:
                    showPopup(AlertType.INFORMATION, "Pass", result.message);
                    break;
                case SURRENDER:
                    showPopup(AlertType.INFORMATION, "Poddanie się", result.message);
                    socketClient.getClientSender().sendToGui("LEAVE");
                    break;
                case GAME_OVER:
                    showPopup(AlertType.INFORMATION, "Koniec gry", result.message);
                    socketClient.getClientSender().sendToGui("LEAVE");
                    break;
            }
        });
    }

    private void showPopup(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}