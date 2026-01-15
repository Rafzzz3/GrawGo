/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
    Klasa zarządzająca pokojami gier i graczami.
    Umożliwia tworzenie, wyszukiwanie i usuwanie pokoi oraz pobieranie listy dostępnych pokoi.
 */
public class RoomManager {
    /** 
     * Mapa przechowująca pokoje gier, gdzie kluczem jest identyfikator pokoju.
     * @param rooms Mapa pokoi gier.
    */
    private Map<Integer, Room> rooms = new ConcurrentHashMap<>();
    /** 
     * Obecny identyfikator pokoju, używany do nadawania unikalnych ID nowo tworzonym pokojom.
     * @return obecny identyfikator pokoju.
    */
    private int currentRoomId  = 1;
    /**
     * Tworzy nowy pokój gry o określonym rozmiarze i zwraca go.
     * @param size Rozmiar planszy gry.
     * @return Nowy pokój gry.
     */
    public Room createRoom(int size) {
        int roomId =  currentRoomId;
        currentRoomId++;
        Room newRoom = new Room(roomId, size);
        rooms.put(roomId, newRoom);
        return newRoom;
    }

    public Room findRoom(int id) {
        return rooms.get(id);
    }

    public void removeRoom(int id) {
        rooms.remove(id);
    }

    public ArrayList<Room> getRoomList() {
        return new ArrayList<Room>(rooms.values());
    }
}