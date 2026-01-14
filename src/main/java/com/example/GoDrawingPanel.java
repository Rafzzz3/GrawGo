package com.example;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
public class GoDrawingPanel extends Canvas {
    private Board currentBoard;
    private static final int MARGIN = 20;
    public GoDrawingPanel(Board board) {
        super(500, 500);
        this.currentBoard = board;
    }
    public void updateBoard(Board board) {
        drawBoard(board);
    }
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
            
        }
        // TO DO: Metoda do obsługi kliknięcia w celu postawienia kamienia
    }
}
