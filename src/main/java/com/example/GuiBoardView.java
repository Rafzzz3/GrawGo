/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
/**
    Klasa reprezentująca widok GUI planszy gry.
 */
public class GuiBoardView {    
    private int currentViewIndex = -1; // -1 to live
    private int headIndex = -1; // Najnowszy ruch w historii
    private boolean waitingForDelta = false;
    private boolean lastActionWasUndo = false;
    private boolean analysisMode = false;
    private int totalMoves;
    private Board currentBoard;
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
    private Button passButton;
    private Button surrenderButton;
    private Button returnButton;
    public GuiBoardView(SocketClient socketClient) {
        this.socketClient = socketClient;
        BorderPane layout = new BorderPane();
        passButton = new Button("Pass");
        surrenderButton = new Button("Surrender");
        returnButton = new Button("Return to menu");
        returnButton.setVisible(false);
        returnButton.setManaged(false);
        Button prevButton = new Button("<-");
        Button nextButton = new Button("->");
        drawingPanel = new GoDrawingPanel();

        passButton.setOnAction(e -> passTurn());
        surrenderButton.setOnAction(e -> surrenderGame());
        prevButton.setOnAction(e -> requestPrev());
        nextButton.setOnAction(e -> requestNext());
        returnButton.setOnAction(e -> returnToMenu());
        drawingPanel.setOnMoveListener(command -> {
            if (analysisMode) {
                return;
            }
            if (currentViewIndex != headIndex) { 
                 return;
            }
            socketClient.getClientSender().sendToGui(command);
        });
        layout.setLeft(passButton);
        layout.setRight(surrenderButton);
        layout.setCenter(drawingPanel);
        layout.setTop(returnButton);

        HBox historyBox = new HBox(10, prevButton, nextButton,returnButton);
        historyBox.setAlignment(Pos.CENTER);
        layout.setBottom(historyBox);

        scene = new Scene(layout, 600, 600);
    }

    private void requestPrev() {
        if (!waitingForDelta) {
            if (currentViewIndex < 0)  {
                return;
            }
            waitingForDelta = true;
            lastActionWasUndo = true;
            socketClient.getClientSender().sendToGui("FETCH_DELTA " + currentViewIndex);
        }
    }

    private void requestNext() {
        int limit;
        if (analysisMode) {
            limit = totalMoves - 1;
        }
        else {
            limit = headIndex;
        }
        if (!waitingForDelta && currentViewIndex < limit) {
            waitingForDelta = true;
            lastActionWasUndo = false;
            socketClient.getClientSender().sendToGui("FETCH_DELTA " + (currentViewIndex+1));
        }
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
        Platform.runLater(() -> {
            this.currentBoard = board;
            if (!analysisMode) {
                this.headIndex++;
                this.currentViewIndex = this.headIndex;
            }
            
            this.waitingForDelta = false;
            this.lastActionWasUndo = false;

            if (!analysisMode && currentViewIndex > headIndex) {
                 currentViewIndex = headIndex;
            }

            drawingPanel.updateBoard(board);
        });
    }

    /**
     * Metoda wywoływana przez GameApp -> ClientReceiver, gdy przyjdzie paczka z historią.
     */
    public void handleHistoryDelta(HistoryMove move) {
        waitingForDelta = false; // Odblokowujemy przyciski

        Platform.runLater(() -> {
            if (move == null) {
                System.out.println("Brak historii lub koniec zakresu.");
                return;
            }
            
            if (currentBoard == null) {
                return; 
            }

            if (lastActionWasUndo) {
                currentBoard.setStone(move.x, move.y, Stone.EMPTY);
                restoreCaptives(move);
                
                currentViewIndex--; 

            } else {
                // redo
                if (currentViewIndex >= headIndex) {
                     return;
                }
                currentBoard.setStone(move.x, move.y, move.playerColor);
                removeCaptives(move);
                
                currentViewIndex++;
            }

            drawingPanel.updateBoard(currentBoard);
            System.out.println("Widok historii: " + currentViewIndex + " / " + headIndex);
        });
    }

    private void restoreCaptives(HistoryMove move) {
        if (move.captured == null || move.captured.length == 0) return;

        Stone deadColor;
        if (move.playerColor == Stone.BLACK) {
            deadColor = Stone.WHITE;
        } else {
            deadColor = Stone.BLACK;
        }

        for (int[] pos : move.captured) {
            int realX = pos[0];
            int realY = pos[1];
            currentBoard.setStone(realX, realY, deadColor);
        }
    }

    private void removeCaptives(HistoryMove move) {
        if (move.captured == null || move.captured.length == 0) return;

        for (int[] pos : move.captured) {
            int realX = pos[0];
            int realY = pos[1];
            currentBoard.setStone(realX, realY, Stone.EMPTY);
        }
    }

    /**
     * Metoda resetująca stan widoku do wartości początkowych.
     * Należy ją wywołać przy rozpoczęciu nowej gry.
     */
    public void reset() {
        this.currentViewIndex = -1;
        this.headIndex = -1;
        returnButton.setVisible(false);
        returnButton.setManaged(false);
        this.waitingForDelta = false;
        this.lastActionWasUndo = false;
        this.currentBoard = null;
        this.analysisMode = false;
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
    public void setAnalysisMode(boolean isActive, int totalMoves) {
        this.analysisMode = isActive;
        this.totalMoves = totalMoves;
        passButton.setVisible(!isActive);
        surrenderButton.setVisible(!isActive);
        returnButton.setVisible(isActive);
        this.currentViewIndex = -1;
        this.headIndex = totalMoves - 1;
    }
    public void returnToMenu() {
        if (analysisMode) {
            socketClient.getClientSender().sendToGui("EXIT_ANALYZE");
        }
    }
}