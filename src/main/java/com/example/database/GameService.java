package com.example.database;

import com.example.HistoryMove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// klasa z logika biznesowa do obslugi gier i zapisu/odczytu z bazy
@Service
public class GameService {
    
    // spring sam wstrzyknie implementacje repozytorium (Dependency Injection)
    @Autowired
    private GameRepository repository;

    // wrapper bazy danych do korzystania dla gry
    public void saveGame(int size, String winner, List<HistoryMove> history) {
        GameDocument document = new GameDocument(size, winner, history);
        
        repository.save(document);
        
        System.out.println("[Spring] Zapisano grę w bazie, ID: " + document.getId());
    }

    public List<GameDocument> getAllGames() {
        return repository.findAllByOrderByPlayedAtDesc();
    }
}
