package com.example.database;

import com.example.Game;
import com.example.HistoryMove;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

// zapisujemy do bazy jako dokument kolekcji "games"
@Document(collection = "games")
public class GameDocument {
    
    // pola dokumentu

    // klucz glowny
    @Id
    private String id;

    private LocalDateTime playedAt;
    private String winner;
    private int size;
    private List<HistoryMove> moves;

    public GameDocument() {}

    public GameDocument(int size, String winner, List<HistoryMove> moves) {
        this.playedAt = LocalDateTime.now();
        this.winner = winner;
        this.size = size;
        this.moves = moves;
    }

    // gettery dla zapisu
    public String getId() { return id; }
    public List<HistoryMove> getMoves() { return moves; }
    public int getSize() { return size; }
    public LocalDateTime getPlayedAt() { return playedAt; }
    public String getWinner() { return winner; }
}
