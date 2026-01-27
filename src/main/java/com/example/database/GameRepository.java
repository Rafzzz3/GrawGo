package com.example.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

// magazyn do obslugi obiektow GameDocument o ID typu string
// spring przy uruchomieniu tworzy klase implementujaca ten interfejs (.save(), .findAll(), .delete())
public interface GameRepository extends MongoRepository<GameDocument, String> {
    List<GameDocument> findAllByOrderByPlayedAtDesc();
}
