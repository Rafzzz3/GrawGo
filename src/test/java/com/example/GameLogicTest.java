package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class GameLogicTest {

    private GameLogic gameLogic;
    private Board board;
    private int size = 9;

    @BeforeEach
    void setUp() {
        gameLogic = new GameLogic();
        board = new Board(size);
    }


    //   0 1 2
    // 0 . B .
    // 1 B W .
    // 2 . B .
    // Ruch czarnego na (1,2); zbijamy W
    @Test
    void testCaptureSingleStone() {
        // Arrange
        board.setStone(1, 1, Stone.WHITE);
        board.setStone(0, 1, Stone.BLACK);
        board.setStone(1, 0, Stone.BLACK);
        board.setStone(2, 1, Stone.BLACK);

        // Act
        MoveResult result = gameLogic.move(board, 1, 2, Stone.BLACK);

        // Assert
        assertEquals(MoveCode.OK, result.code);
        assertEquals(Stone.EMPTY, board.getStone(1, 1), "Biały kamień powinien zniknąć");
        assertEquals(1, result.captured.length);
    }


    //   0 1 2
    // 0 W W B
    // 1 B B .
    // Ruch czarnego na (0,2); zbijamy oba W
    @Test
    void testCaptureGroupOnEdge() {
        // Arrange
        board.setStone(0, 0, Stone.WHITE);
        board.setStone(0, 1, Stone.WHITE);
        board.setStone(1, 0, Stone.BLACK);
        board.setStone(1, 1, Stone.BLACK);
        
        // Act
        MoveResult result = gameLogic.move(board, 0, 2, Stone.BLACK);

        // Assert
        assertEquals(MoveCode.OK, result.code);
        assertEquals(Stone.EMPTY, board.getStone(0, 0));
        assertEquals(Stone.EMPTY, board.getStone(0, 1));
        assertEquals(2, result.captured.length, "Dwa kamienie powinny zostać zbite");
    }

    //   0 1
    // 0 . B
    // 1 B .
    // Ruch białego na (0,0); samobójstwo
    @Test
    void testSuicideMove() {
        // Arrange
        board.setStone(1, 0, Stone.BLACK);
        board.setStone(0, 1, Stone.BLACK);
        
        // Act
        MoveResult result = gameLogic.move(board, 0, 0, Stone.WHITE);

        // Assert
        assertEquals(MoveCode.SUICIDE, result.code);
        assertEquals(Stone.EMPTY, board.getStone(0, 0));
    }

    //   0 1 2
    // 0 . W B
    // 1 W B .
    // 2 B . .
    // Ruch czarnego na (0,0) dusi białe, brak samobójstwa
    @Test
    void testSuicideThatKillsIsValid() {
        // Arrange
        board.setStone(0, 2, Stone.BLACK);
        board.setStone(1, 1, Stone.BLACK);
        board.setStone(2, 0, Stone.BLACK);
        board.setStone(0, 1, Stone.WHITE);
        board.setStone(1, 0, Stone.WHITE);
        
        // Act
        MoveResult result = gameLogic.move(board, 0, 0, Stone.BLACK);
        
        // Assert
        assertEquals(MoveCode.OK, result.code);
        assertEquals(Stone.BLACK, board.getStone(0, 0));
        assertEquals(Stone.EMPTY, board.getStone(0, 1));
        assertEquals(2, result.captured.length);
    }


    //   1 2 3
    // 1 . B .
    // 2 B W (B)
    // 3 . B .
    // Czarny zbił białego na (2,2), więc Biały nie może postawić na (2,2) w następnym ruchu; KO
    @Test
    void testKoRule() {
        // Arrange
        board.setStone(2, 1, Stone.BLACK);
        board.setStone(1, 2, Stone.BLACK);
        board.setStone(3, 2, Stone.BLACK);
        board.setStone(2, 4, Stone.WHITE);
        board.setStone(1, 3, Stone.WHITE);
        board.setStone(3, 3, Stone.WHITE);
        board.setStone(2, 2, Stone.WHITE);
        
        // Act 1 (czarny bije)
        MoveResult r1 = gameLogic.move(board, 2, 3, Stone.BLACK);
        assertEquals(MoveCode.OK, r1.code);
        
        // Act 2 (biały próbuje odbić)
        MoveResult r2 = gameLogic.move(board, 2, 2, Stone.WHITE);
        
        // Assert
        assertEquals(MoveCode.KO, r2.code);
        assertEquals(Stone.EMPTY, board.getStone(2, 2));
    }

    //   0 1
    // 0 . B
    // 1 B B
    // Liczenie terytorium (1 pole)
    @Test
    void testTerritoryCounting() {
        // Arrange
        board.setStone(0, 1, Stone.BLACK);
        board.setStone(1, 0, Stone.BLACK);
        board.setStone(1, 1, Stone.BLACK);
        board.setStone(0, 2, Stone.WHITE);
        board.setStone(1, 2, Stone.WHITE);
        board.setStone(2, 2, Stone.WHITE);
        board.setStone(2, 1, Stone.WHITE);
        board.setStone(2, 0, Stone.WHITE);
        
        // Act
        gameLogic.calculateTerritory(board);
        
        // Assert
        assertEquals(1, gameLogic.getBlackTerritory());
    }

    // Biały próbuje postawić kamień na zajętym polu
    @Test
    void testOccupiedPosition() {
        // Arrange
        board.setStone(5, 5, Stone.BLACK);
        
        // Act
        MoveResult result = gameLogic.move(board, 5, 5, Stone.WHITE);
        
        // Assert
        assertEquals(MoveCode.OCCUPIED, result.code);
    }
}