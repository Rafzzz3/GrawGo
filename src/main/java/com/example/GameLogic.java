package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameLogic {
    private Stone[][] previousBoard = null;

    private int blackKills = 0;
    private int whiteKills = 0;

    private int blackTerritory = 0;
    private int whiteTerritory = 0;


    // Główny punkt wejścia: walidacja, ruch, rollback przy samobójstwie
    public MoveResult move(Board board, int x, int y, Stone color) {
        int size = board.getBoardSize();
        if (!isValidPosition(size, x, y)) {
            return new MoveResult(MoveCode.INVALID_POSITION, new int[0][], "Nieprawidłowa pozycja!");
        }
        if (board.getStone(x, y) != Stone.EMPTY) {
            return new MoveResult(MoveCode.OCCUPIED, new int[0][], "Pole już zajęte!");
        }

        // backup planszy dla zasady KO
        Stone[][] preMoveBoard = deepCopyBoard(board);

        // ruch na brudno
        board.setStone(x, y, color);

        // dusimy sasiadow
        int[][] captured = calculateKills(board, x, y, color);

        // samobojstwo - rollback
        if (captured.length == 0) {
            List<int[]> suffocatedList = suffocatedStones(board, x, y);
            
            if (suffocatedList.size() > 0) {
                board.setStone(x, y, Stone.EMPTY); // Rollback
                return new MoveResult(MoveCode.SUICIDE, new int[0][], "Ruch niedozwolony: Samobójstwo!");
            }
        }

        // KO
        if (previousBoard != null && areBoardsEqual(previousBoard, board)) {
            restoreBoard(board, preMoveBoard); // Rollback
            return new MoveResult(MoveCode.KO, new int[0][], "Ruch niedozwolony: zasada KO!");
        }

        previousBoard = preMoveBoard;

        String msg;
        if (captured.length > 0) {
            StringBuilder sb = new StringBuilder("Uduszono: ");
            for (int i = 0; i < captured.length; i++) {
                int[] p = captured[i];
                sb.append("(").append(p[0]).append(", ").append(p[1]).append(")");
                if (i < captured.length - 1) sb.append(", ");
            }
            msg = sb.toString();
        } else {
            msg = "Ruch wykonany pomyślnie.";
        }

        return new MoveResult(MoveCode.OK, captured, msg);
    }


    private int[][] calculateKills(Board board, int x, int y, Stone color) {
        List<int[]> capturedList = new ArrayList<>();

        // sprawdzamy czy ruch udusil sasiada, zabieramy jenca i dodajemy punkt
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (isValidPosition(board.getBoardSize(), newX, newY)) {
                Stone neighbor = board.getStone(newX, newY);

                if (neighbor != Stone.EMPTY && neighbor != color) {
                    List<int[]> suffocatedList = suffocatedStones(board, newX, newY);

                    // Usuwamy uduszone kamienie z planszy
                    if (suffocatedList.size() > 0) {
                        for (int[] pos : suffocatedList) {
                            board.setStone(pos[0], pos[1], Stone.EMPTY);
                            capturedList.add(new int[]{pos[0]+1, pos[1]+1}); // +1 dla uzytkownika
                        }

                        if (color == Stone.BLACK) {
                            blackKills += suffocatedList.size();
                        } else {
                            whiteKills += suffocatedList.size();
                        }
                    }
                }
            }
        }

        return capturedList.toArray(new int[0][]);
    }



    // Na koniec gry, liczymy terytoria
    public void calculateTerritory(Board board) {
        int size = board.getBoardSize();
        boolean[][] visited = new boolean[size][size];
        
        // Resetujemy punkty przed liczeniem (ważne, jakby gracze wznawiali grę!)
        blackTerritory = 0;
        whiteTerritory = 0;

        // Przechodzimy przez całą planszę
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                // Jeśli trafiliśmy na puste pole, którego jeszcze nie odwiedziliśmy...
                if (board.getStone(x, y) == Stone.EMPTY && !visited[x][y]) {
                    floodAlg(board, x, y, visited);
                }
            }
        }
    }


    // Helper
    public boolean isValidPosition(int size, int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }



    private List<int[]> suffocatedStones(Board board, int x, int y) {
        int size = board.getBoardSize();
        Stone targetColor = board.getStone(x, y);

        // Jak pusto, to nie ma kogo zabijać
        if (targetColor == Stone.EMPTY) return new ArrayList<>();

        List<int[]> group = new ArrayList<>(); // Tu zbieramy potencjalne ofiary
        Set<String> visited = new HashSet<>();
        List<int[]> stack = new ArrayList<>(); // Nasz stos do DFS
        
        stack.add(new int[]{x, y});

        while (!stack.isEmpty()) {
            int[] curr = stack.remove(stack.size() - 1); // Pobierz ostatni (DFS)
            int currX = curr[0];
            int currY = curr[1];
            String key = currX + "," + currY;

            if (visited.contains(key)) continue;
            visited.add(key);
            group.add(new int[]{currX, currY}); // Dodajemy do grupy

            // Sprawdzamy sąsiadów
            int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
            for (int[] dir : directions) {
                int newX = currX + dir[0];
                int newY = currY + dir[1];

                if (isValidPosition(size, newX, newY)) {
                    Stone neighbor = board.getStone(newX, newY);

                    if (neighbor == Stone.EMPTY) {
                        return new ArrayList<>(); // Znaleźliśmy oddech, kończymy
                    } else if (neighbor == targetColor) {
                        stack.add(new int[]{newX, newY});
                    }
                }
            }
        }
        return group;  //Zwracamy listę do egzekucji.
    }
    

    // To jest nasz BFS (Flood Fill) sprawdzający jedną zamkniętą strefę
    private void floodAlg(Board board, int startX, int startY, boolean[][] visited) {
        int size = board.getBoardSize();

        int regionSize = 0;
        
        // czyje terytorium?
        boolean touchesBlack = false;
        boolean touchesWhite = false;

        List<int[]> queue = new ArrayList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            int[] curr = queue.remove(0);    // Pobierz piewrszy (BFS)

            regionSize++;

            int currX = curr[0];
            int currY = curr[1];

            // Sprawdzamy sąsiadów
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int newX = currX + dir[0];
                int newY = currY + dir[1];

                if (isValidPosition(size, newX, newY)) {
                    Stone neighbor = board.getStone(newX, newY);
                    if (neighbor == Stone.EMPTY) {
                        // Jeśli puste i nieodwiedzone -> dodajemy do kolejki (rozlewamy wodę)
                        if (!visited[newX][newY]) {
                            visited[newX][newY] = true;
                            queue.add(new int[]{newX, newY});
                        }
                    } else if (neighbor == Stone.BLACK) {
                        touchesBlack = true;
                    } else if (neighbor == Stone.WHITE) {
                        touchesWhite = true;
                    }
                }
            }
        }

        // punkty czarnego
        if (touchesBlack && !touchesWhite) {
            blackTerritory += regionSize;
        }
        // punkty bialego
        else if (touchesWhite && !touchesBlack) {
            whiteTerritory += regionSize;
        }
        // 0 punktów jeśli dotyka obu lub żadnego koloru
    }


    private Stone[][] deepCopyBoard(Board board) {
        int size = board.getBoardSize();
        Stone[][] copy = new Stone[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                copy[x][y] = board.getStone(x, y);
            }
        }
        return copy;
    }

    private boolean areBoardsEqual(Stone[][] board1, Board currentBoard) {
        int size = currentBoard.getBoardSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board1[i][j] != currentBoard.getStone(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    // przywracanie planszy z backupu
    private void restoreBoard(Board board, Stone[][] backup) {
        int size = board.getBoardSize();
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                if(board.getStone(i, j) != backup[i][j]) {
                    board.setStone(i, j, backup[i][j]);
                }
            }
        }
    }



    // Gettery
    public int getBlackKills() {
        return blackKills;
    }

    public int getWhiteKills() {
        return whiteKills;
    }

    public int getBlackTerritory() {
        return blackTerritory;
    }

    public int getWhiteTerritory() {
        return whiteTerritory;
    }
}
