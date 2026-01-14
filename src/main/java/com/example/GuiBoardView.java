package com.example;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextArea;
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
        layout.setCenter(drawingPanel);
        scene = new Scene(layout, 600, 600);
    }
    public Scene getScene() {
        return scene;
    }
    public void setMessage(String message) {
        infoArea.appendText("\n" + message);
    }
    public void updateBoard(Board board) {
        drawingPanel.updateBoard(board);
    }
}
