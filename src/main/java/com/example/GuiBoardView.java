/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
/**
    Klasa reprezentująca widok GUI planszy gry.
 */
public class GuiBoardView {     
    /**
     * @param scene Obiekt Scene reprezentujący scenę GUI planszy gry.
     */
    private Scene scene;
    /**
     * @param socketClient Obiekt SocketClient do komunikacji z serwerem.
     */
    private SocketClient socketClient;
    /**
     * @param drawingPanel Panel do rysowania planszy gry.
     */
    private GoDrawingPanel drawingPanel;
    /**
     * Konstruktor klasy GuiBoardView.
     * @param socketClient Obiekt SocketClient do komunikacji z serwerem.
     */
    public GuiBoardView(SocketClient socketClient) {
        this.socketClient = socketClient;
        BorderPane layout = new BorderPane();
        Button passButton = new Button("Pass");
        Button surrenderButton = new Button("Surrender");
        drawingPanel = new GoDrawingPanel();
        passButton.setOnAction(e -> passTurn());
        surrenderButton.setOnAction(e -> surrenderGame());
        drawingPanel.setOnMoveListener(command -> socketClient.getClientSender().sendToGui(command));
        layout.setLeft(passButton);
        layout.setRight(surrenderButton);
        layout.setCenter(drawingPanel);
        scene = new Scene(layout, 600, 600);
    }
    /**
     * Zwraca obiekt Scene reprezentujący scenę GUI planszy gry.
     * @return obiekt sceny.
     */
    public Scene getScene() {
        return scene;
    }
    /**
     * Metoda passTurn() wysyła wiadomość do serwera o spasowaniu tury.
     */
    public void passTurn() {
        socketClient.getClientSender().sendToGui("PASS");
    }
    /**
     * Metoda surrenderGame() wysyła wiadomość do serwera o poddaniu się w grze.
     */
    public void surrenderGame() {
        socketClient.getClientSender().sendToGui("SURRENDER");
    }
    /**
     * Metoda updateBoard() aktualizuje widok planszy gry na podstawie otrzymanego obiektu Board.
     * @param board Obiekt Board reprezentujący stan planszy gry.
     */
    public void updateBoard(Board board) {
        Platform.runLater(() -> drawingPanel.updateBoard(board));
    }
    /**
     * Metoda handleMoveResult() obsługuje wynik ruchu i wyświetla odpowiednie popupy w zależności od kodu błędu.
     * @param result Obiekt MoveResult reprezentujący wynik ruchu.
     */
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
    /**
     * Metoda showPopup() wyświetla okno popup z określonym typem i komunikatem.
     * @param alertType Typ okna popup.
     * @param title Tytuł okna popup.
     * @param content Komunikat do wyświetlenia.
     */
    private void showPopup(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}