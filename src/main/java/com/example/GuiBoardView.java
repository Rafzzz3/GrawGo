package com.example;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;

public class GuiBoardView {     
    private Scene scene;
    private SocketClient socketClient;
    private GoDrawingPanel drawingPanel;
    private TextArea infoArea;
    public GuiBoardView(SocketClient socketClient) {
        this.socketClient = socketClient;
        BorderPane layout = new BorderPane();
        infoArea = new TextArea();
        infoArea.setEditable(false);
        layout.setTop(infoArea);
        drawingPanel = new GoDrawingPanel();
        drawingPanel.setOnMoveListener(command -> socketClient.getClientSender().sendToGui(command));
        layout.setCenter(drawingPanel);
        scene = new Scene(layout, 600, 600);
    }
    public Scene getScene() {
        return scene;
    }
    public void setMessage(String message) {
        Platform.runLater(() -> infoArea.appendText("\n" + message));
    }
    public void updateBoard(Board board) {
        Platform.runLater(() -> drawingPanel.updateBoard(board));
    }

    public void handleMoveResult(MoveResult result) {
        // Logika wyświetlania popupów w zależności od kodu błędu
        Platform.runLater(() -> {
            switch (result.code) {
                case SUICIDE:
                    showPopup(AlertType.WARNING, "Samobójstwo!", "Nie możesz wykonać ruchu, który pozbawiłby Cię wszystkich oddechów (chyba że coś zabijasz).");
                    break;
                case KO:
                    showPopup(AlertType.WARNING, "Zasada KO", "Nie możesz wykonać ruchu, który przywróciłby planszę do stanu sprzed chwili.");
                    break;
                case OCCUPIED:
                    showPopup(AlertType.ERROR, "Pole zajęte", "Tu już stoi kamień, ziomeczku.");
                    break;
                case INVALID_POSITION:
                    showPopup(AlertType.ERROR, "Błąd", "Nieprawidłowa pozycja.");
                    break;
                case NOT_YOUR_TURN:
                    showPopup(AlertType.INFORMATION, "Czekaj!", "Teraz tura przeciwnika. Nie pchaj się! 🛑");
                    break;
                case OK:
                    if (result.message != null && !result.message.isEmpty() && !result.message.equals("Ruch wykonany pomyślnie.")) {
                        setMessage(result.message);
                    }
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