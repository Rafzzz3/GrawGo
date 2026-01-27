/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.database.GameService;
/**
    Klasa reprezentująca pokój gry, zarządzająca stanem gry i kolorami graczy.
 */
public class Room {
    /** 
     * @param id Identyfikator pokoju.
    */
    private int id;
    /** 
     * @param readyCounter Licznik gotowych graczy.
    */
    private int readyCounter = 0;
    /** 
     * @param game Obiekt gry reprezentujący stan gry w pokoju.
    */
    private Game game;
    /** 
     * @param players Lista graczy w pokoju.
    */
    private List<ClientHandler> players = new ArrayList<>();
    /** 
     * @param gameStarted Flaga wskazująca, czy gra w pokoju się rozpoczęła.
    */
    private boolean gameStarted = false;
    /** 
     * @param firstSeatColor Kolor pierwszego gracza.
    */
    private Stone firstSeatColor;
    /** 
     * @param random Obiekt Random do losowania kolorów graczy.
    */
    private final Random random = new Random();
    private GameService gameService;
    /**
     * Konstruktor klasy Room.
     * @param id Identyfikator pokoju.
     * @param size Rozmiar planszy gry.
     */
    public Room(int id, int size, GameService gameService) {
        this.id = id;
        this.game = new Game(size, gameService);
        this.gameService = gameService;
    }
    /** 
     * Metoda dodająca gracza do pokoju i przypisująca mu kolor.
     * Pierwszemu graczowi przypisywany jest losowo kolor czarny lub biały,
     * a drugiemu graczowi przypisywany jest przeciwny kolor.
     * @param player Gracz do dodania do pokoju.
    */
    public void addPlayer(ClientHandler player) {
        if (players.isEmpty()) {
            // losuj kolor dla pierwszego gracza
            boolean blackFirst = random.nextBoolean();
            if (blackFirst) {
                player.setPlayerColor(Stone.BLACK);
                firstSeatColor = Stone.BLACK;
            } else {
                player.setPlayerColor(Stone.WHITE);
                firstSeatColor = Stone.WHITE;
            }
        } else {
            // drugi gracz dostaje przeciwny kolor
            if (firstSeatColor == Stone.BLACK) {
                player.setPlayerColor(Stone.WHITE);
            } else {
                player.setPlayerColor(Stone.BLACK);
            }
        }
        players.add(player);
    }
    /** 
     * Metoda usuwająca gracza z pokoju.  Jeśli pokój stanie się pusty po usunięciu gracza, licznik gotowych graczy 
     * jest resetowany, a gra jest oznaczana jako nie rozpoczęta.
     * @param player Gracz do usunięcia z pokoju.
    */
    public void removePlayer(ClientHandler player) {
        players.remove(player);
        if (players.isEmpty()) {
            readyCounter = 0;
            gameStarted = false;
        }
    }
    /** 
     * Zwraca identyfikator pokoju.
     * @return Identyfikator pokoju.
    */
    public int getId() {
        return id;
    }
    /** 
     * Zwraca liczbę graczy w pokoju.
     * @return Liczba graczy w pokoju.
    */
    public int getPlayerCount() {
        return players.size();
    }
    /** 
     * Zwraca obiekt gry reprezentujący stan gry w pokoju.
     * @return Obiekt gry.
    */
    public Game getGame() {
        return game;
    }
    /** 
     * Zwraca licznik gotowych graczy.
     * @return Licznik gotowych graczy.
    */
    public int getReadyCounter() {
        return readyCounter;
    }
    /** 
     * Inkrementuje licznik gotowych graczy o 1.
    */
    public void incrementReadyCounter() {
        readyCounter++;
    }
    /** 
     * Sprawdza, czy gra w pokoju się rozpoczęła.
     * Gra rozpoczyna się, gdy licznik gotowych graczy osiągnie 2.
     * @return true, jeśli gra się rozpoczęła, false w przeciwnym razie.
    */
    public boolean isGameStarted() {
        if (readyCounter == 2) {
            gameStarted = true;
        }
        return gameStarted;
    }
    /** 
     * Zwraca listę graczy w pokoju.
     * @return Lista graczy.
    */ 
    public List<ClientHandler> getPlayers() {
        return players;
    }
}
