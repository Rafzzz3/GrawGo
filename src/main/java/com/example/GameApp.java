package com.example;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.util.List;
public class GameApp extends Application implements GuiListner {
    private Stage mainStage;
    private GuiLobbyView lobbyView;
    private GuiBoardView boardView;
    private GuiRoomView roomView;
    private SocketClient socketClient;
    private boolean gameStarted = false;
    public static void main(String[] args) {
        launch(args);
    }
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
    @Override
    public void forLobbyList(List<String> lobbyList) {
        Platform.runLater(() -> {
            lobbyView.updateRoomList(lobbyList);
        });
    }
    @Override
    public void forMoveResult(MoveResult result) {
        Platform.runLater(() -> {
            if (gameStarted && boardView != null) {
                boardView.handleMoveResult(result);
            }
        });
    }
    @Override
    public void forJoinedRoom(int roomId) {
        Platform.runLater(() -> {
            roomView = new GuiRoomView(socketClient, roomId);
            mainStage.setScene(roomView.getScene());
            mainStage.setTitle("Gra w go pokój nr. " + roomId);
        });
    }
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