package com.example;
import java.util.List;
public interface GuiListner {
    void forMessage(String msg);
    void forBoard(Board board);
    void forLobbyList(List<String> lobbyList);
    void forMoveResult(MoveResult result);
    void forJoinedRoom(int roomId);
}
