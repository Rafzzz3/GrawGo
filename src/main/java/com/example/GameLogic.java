package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameLogic {    
    private int blackKills = 0;
    private int whiteKills = 0;

    // argument Board otrzymuje od klasy Game i ona dba o poprawnosc danych
    public List<int[]> suffocatedStones(Board board, int x, int y) {
        int size = board.getBoardSize();
        Stone targetColor = board.getStone(x, y);

        // Jak pusto, to nie ma kogo zabijać
        if (targetColor == Stone.EMPTY) return new ArrayList<>();

        List<int[]> group = new ArrayList<>(); // Tu zbieramy potencjalne ofiary
        Set<String> visited = new HashSet<>(); // Żeby nie kręcić się w kółko
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
    


    // Główny punkt wejścia: walidacja, ruch, rollback przy samobójstwie
    public MoveResult move(Board board, int x, int y, Stone color) {
        int size = board.getBoardSize();
        if (!isValidPosition(size, x, y)) {
            return new MoveResult(MoveCode.INVALID_POSITION, new int[0][], "Nieprawidłowa pozycja!");
        }
        if (board.getStone(x, y) != Stone.EMPTY) {
            return new MoveResult(MoveCode.OCCUPIED, new int[0][], "Pole już zajęte!");
        }

        board.setStone(x, y, color);
        int[][] captured = calculateKills(board, x, y, color);

        if (captured.length == 0) {
            List<int[]> suffocatedList = suffocatedStones(board, x, y);
            
            if (suffocatedList.size() > 0) {
                board.setStone(x, y, Stone.EMPTY); // Rollback
                return new MoveResult(MoveCode.SUICIDE, new int[0][], "Ruch niedozwolony: Samobójstwo!");
            }
        }

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


    // Helper też musi wiedzieć jaki jest rozmiar
    public boolean isValidPosition(int size, int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public int getBlackKills() {
        return blackKills;
    }

    public int getWhiteKills() {
        return whiteKills;
    }
}
