package com.example;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private Map<Integer, Room> rooms = new ConcurrentHashMap<>();
    private int currentRoomId  = 1;
    public Room createRoom() {
        int roomId =  currentRoomId;
        currentRoomId++;
        Room newRoom = new Room(roomId);
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