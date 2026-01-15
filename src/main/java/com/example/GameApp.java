/**
 * @authors @Rafzzz3
 */
package com.example;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.util.List;
/** 
 * Główna klasa GameApp odpowiadająca za widok GUI klienta. Pzełącza widok między okienkami.
 */
public class GameApp extends Application implements GuiListner {
    /**
     * @param mainStage Główna scena.
     */
    private Stage mainStage;
    /**
     * @param lobbyview Obiekt GuiLobbyView odpowiedzialny za widok gui w lobby
     */
    private GuiLobbyView lobbyView;
    /**
     * @param boardView Obiekt GuiBoardView odpowiedzialny za widok gui w trakcie gry
     */
    private GuiBoardView boardView;
    /**
     * @param roomView Obiekt GuiRoomView odpowiedzialny za widok gui w pokoju gry
     */
    private GuiRoomView roomView;
    /**
     * @param socketClient Obiekt SocketClient odpowiedzialny za komunikację z serwerem
     */
    private SocketClient socketClient;
    /**
     * Flaga określająca czy gra została już rozpoczęta
     */
    private boolean gameStarted = false;
    /**
     * Metoda główna, punkt wejścia do aplikacji
     * @param args Argumenty wiersza poleceń
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * Metoda uruchamiająca aplikację JavaFX. Inicjalizuje okno główne, łączy się z serwerem
     * i wyświetla widok lobby.
     * @param mainStage Główna scena aplikacji
     */
    @Override
    public void start(Stage mainStage) {
        this.socketClient = new SocketClient();
        this.mainStage = mainStage;
        try {
            socketClient.connect();
        } catch (Exception e) {
            System.out.println("Błąd podczas łączenia z serwerem: " + e.getMessage());
            return;
        }
        lobbyView = new GuiLobbyView(socketClient);
        boardView = new GuiBoardView(socketClient);
        try {
            if (socketClient.getClientReceiver() != null) {
                socketClient.getClientReceiver().setListener(this);
            }
        } catch (Exception e) {
            System.out.println("Błąd podczas łączenia z serwerem: " + e.getMessage());
            return;
        }
        mainStage.setTitle("Gra Go");
        mainStage.setScene(lobbyView.getScene());
        lobbyView.refreshRoomList();
        mainStage.setOnCloseRequest(e -> {
            socketClient.close();
            System.exit(0);
        });
        mainStage.show();
    }
    /**
     * Obsługuje wiadomości otrzymane od serwera. Jeśli gra nie została rozpoczęta,
     * wyświetla wiadomość w widoku lobby i odświeża listę pokojów.
     * @param message Wiadomość od serwera
     */
    @Override
    public void forMessage(String message) {
        Platform.runLater(() -> {
            if (gameStarted) {
            }
            else {
                lobbyView.setMessage(message);
                lobbyView.refreshRoomList();
            }
        });
    }
    /**
     * Obsługuje aktualizacje planszy gry. Przełącza widok z lobby na widok gry
     * i aktualizuje stan planszy.
     * @param board Obiekt planszy do wyświetlenia
     */
    @Override
    public void forBoard(Board board) {
        Platform.runLater(() -> {
            if (!gameStarted) {
                gameStarted = true;
                mainStage.setScene(boardView.getScene());
                mainStage.sizeToScene();
            }
            boardView.updateBoard(board);
        });
    }
    /**
     * Obsługuje aktualizację listy dostępnych pokojów w lobby.
     * @param lobbyList Lista pokojów dostępnych na serwerze
     */
    @Override
    public void forLobbyList(List<String> lobbyList) {
        Platform.runLater(() -> {
            lobbyView.updateRoomList(lobbyList);
        });
    }
    /**
     * Obsługuje wynik ruchu wykonanego w grze. Aktualizuje widok planszy
     * jeśli gra jest aktywna.
     * @param result Wynik ruchu zawierający informacje o poprawności ruchu
     */
    @Override
    public void forMoveResult(MoveResult result) {
        Platform.runLater(() -> {
            if (gameStarted && boardView != null) {
                boardView.handleMoveResult(result);
            }
        });
    }
    /**
     * Obsługuje wejście do pokoju gry. Tworzy widok pokoju i przełącza scenę.
     * @param roomId Identyfikator pokoju do którego gracz dołączył
     */
    @Override
    public void forJoinedRoom(int roomId) {
        Platform.runLater(() -> {
            roomView = new GuiRoomView(socketClient, roomId);
            mainStage.setScene(roomView.getScene());
            mainStage.setTitle("Gra w go pokój nr. " + roomId);
        });
    }
    /**
     * Obsługuje wyjście z pokoju gry. Resetuje stan gry i powraca do widoku lobby.
     */
    @Override
    public void forExitRoom() {
        Platform.runLater(() -> {
            gameStarted = false;
            mainStage.setScene(lobbyView.getScene());
            mainStage.setTitle("Gra Go - Lobby");
            lobbyView.refreshRoomList();
        });
    }
}