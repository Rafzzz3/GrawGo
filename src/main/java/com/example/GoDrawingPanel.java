/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import java.util.function.Consumer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/**
 * Klasa reprezentująca panel do rysowania planszy gry Go.
 */
public class GoDrawingPanel extends Canvas {
    /** 
     * @param currentBoard Obiekt Board reprezentujący aktualny stan planszy gry.
     */
    private Board currentBoard;
    /** 
     * Stała określająca margines planszy.
     */
    private static final int MARGIN = 20;
    /** 
     * @param onMoveListener Listener wywoływany po wykonaniu ruchu.
     */
    private Consumer<String> onMoveListener;
    /** 
     * Konstruktor klasy GoDrawingPanel.
    */
    public GoDrawingPanel() {
        super(500, 500);
        this.setOnMouseClicked(event -> clickToPlaceStone(event.getX(), event.getY()));
    }
    /** 
     * Metoda aktualizująca widok planszy gry na podstawie otrzymanego obiektu Board.
     * @param board Obiekt Board reprezentujący stan planszy gry.
    */
    public void updateBoard(Board board) {
        currentBoard = board;
        drawBoard(board);
    }
    /** 
     * Metoda ustawiająca listener wywoływany po wykonaniu ruchu.
     * @param listener Listener do ustawienia.
    */
    public void setOnMoveListener(Consumer<String> listener) {
        this.onMoveListener = listener;
    }
    /** 
     * Metoda rysująca planszę gry na podstawie otrzymanego obiektu Board.
     * @param board Obiekt Board reprezentujący stan planszy gry.
    */
    private void drawBoard(Board board) {
        if (currentBoard == null) {
            return;
        }
        GraphicsContext gc = getGraphicsContext2D();
        int size = currentBoard.getBoardSize();
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, getWidth(), getHeight());
        double width = getWidth() - 2 * MARGIN;
        double cellSize = width / (size - 1);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        for (int i = 0; i <= size; i++) {
            double pos = MARGIN + i * cellSize;
            gc.strokeLine(MARGIN, pos, getWidth() - MARGIN, pos);
            gc.strokeLine(pos, MARGIN, pos, getHeight() - MARGIN);
        }
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Stone stone = board.getStone(row, col);
                if (stone != Stone.EMPTY) {
                    double x = MARGIN + col * cellSize;
                    double y = MARGIN + row * cellSize;
                    if (stone == Stone.BLACK) {
                        gc.setFill(Color.BLACK);
                    } else if (stone == Stone.WHITE) {
                        gc.setFill(Color.WHITE);
                    }
                    gc.fillOval(x - cellSize / 2 + 1, y - cellSize / 2 + 1, cellSize - 2, cellSize - 2);
                    gc.setStroke(Color.BLACK);
                    gc.strokeOval(x - cellSize / 2 + 1, y - cellSize / 2 + 1, cellSize - 2, cellSize - 2);
                }
            }
        }
    }
    /** 
     * Metoda obsługująca kliknięcie na planszy w celu umieszczenia kamienia.
     * @param x Współrzędna X kliknięcia.
     * @param y Współrzędna Y kliknięcia.
    */
    private void clickToPlaceStone(double x, double y) {
        if (currentBoard == null) {
            return;
        }
        int size = currentBoard.getBoardSize();
        double width = getWidth() - 2 * MARGIN;
        double cellSize = width / (size - 1);
        int col = (int) Math.round((x - MARGIN) / cellSize);
        int row = (int) Math.round((y - MARGIN) / cellSize);
        if (row >= 0 && row < size && col >= 0 && col < size) {
            String command = "MOVE " + (row+1) + " " + (col+1);

            if (onMoveListener != null) {
                onMoveListener.accept(command);
            }
            
        }
    }
}
