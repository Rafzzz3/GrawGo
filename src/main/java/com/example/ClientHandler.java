/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.example.database.GameService;
/**
    Klasa reprezentująca obsługę klienta na serwerze.
    Odpowiada za komunikację z klientem, przetwarzanie komend oraz zarządzanie stanem gry i pokoju 
    w zależności od otrzymanych poleceń.
 */
public class ClientHandler implements Runnable {
    /**
     * @param socket Socket używany do komunikacji z klientem.
     */
    private Socket socket;
    /**
     * @param state odpowiedzialny za stan klienta. Domyślnie gracz jest w menu.
     */
    private ClientHandlerState currentState;
    /**
     * @param currentRoom Obiekt Room reprezentujący pokój, do którego należy klient.
     */
    private Room currentRoom = null;
    /**
     * @param playerColor Kolor kamienia przypisany do klienta.
     */
    private Stone playerColor;
    /**
     * @param roomManager Obiekt RoomManager zarządzający pokojami na serwerze.
     */
    private RoomManager roomManager;
    /**
     * @param game Obiekt Game reprezentujący aktualną grę w pokoju.
     */
    private Game game;
    /**
     * @param serverSender Obiekt ServerSender odpowiedzialny za wysyłanie danych do klienta.
     */
    private ServerSender serverSender;
    /**
     * @param input Obiekt ObjectInputStream używany do odbierania danych od klienta.
     */
    private ObjectInputStream input;
    private AnalyzeCommandInterpreter analyzeCommandInterpreter;
    /**
     * @param roomCommandInterpreter Obiekt RoomCommandInterpreter do interpretacji komend związanych z pokojem.
     */
    private RoomCommandInterpreter roomCommandInterpreter;
    /**
     * @param commandInterpreter Obiekt GameCommandInterpreter do interpretacji komend związanych z grą.
     */
    private GameCommandInterpreter commandInterpreter;
    private final ClientHandlerState menuState;
    private final ClientHandlerState lobbyState;
    private final ClientHandlerState roomState;
    private final ClientHandlerState gameState;
    private final ClientHandlerState analyzeState;
    private GameService gameService;
    /**
     * Konstruktor klasy ClientHandler.
     * @param socket Socket używany do komunikacji z klientem.
     * @param roomManager Obiekt RoomManager zarządzający pokojami na serwerze.
     */
    public ClientHandler( Socket socket, RoomManager roomManager, GameService gameService) {
        this.socket = socket;
        this.roomManager = roomManager;
        this.gameService = gameService;
        
        this.lobbyState = new InLobbyState(roomManager, this);
        this.roomState = new InRoomState(this, roomManager);
        this.gameState = new InGameState();
        this.analyzeState = new InAnalyzeState();
        this.menuState = new InMenuState(roomManager);

        this.currentState = menuState;
    }
    /**
     * Metoda run() uruchamia pętlę odbierającą komendy od klienta i przetwarzającą je.
     * W zależności od stanu pokoju i gry, komendy są interpretowane odpowiednio przez
     * RoomCommandInterpreter lub GameCommandInterpreter.
     */
    @Override
    public void run() {
        try {
            this.serverSender = new ServerSender(new ObjectOutputStream(socket.getOutputStream()));
            input = new ObjectInputStream(socket.getInputStream());
            serverSender.sendMessage("Witaj na serwerze: ");
            while (!socket.isClosed()) {
                String clientcommand = (String) input.readObject();
                System.out.println("Otrzymano odpowiedź od gracza: " + clientcommand);
                currentState.handleMessage(this,clientcommand);

            }
        } catch (EOFException e) {
            System.out.println("Gracz rozłączył się.");
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Błąd odczytu z klienta: " + e.getMessage());
        } finally {
            if (currentRoom != null) {
                currentRoom.removePlayer(this);
                if (currentRoom.getPlayers().isEmpty()) {
                    roomManager.removeRoom(currentRoom.getId());
                    System.out.println("Usunięto pokój o ID: " + currentRoom.getId());
                }
                else {
                    for (ClientHandler player : currentRoom.getPlayers()) {
                        player.getServerSender().sendMessage(LobbyMessageType.INFO+": Przeciwnik rozłączył się.");
                    }
                }
            }   
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Błąd zamknięcia gniazda: " + e.getMessage());
            }
        }
    }
    /**
     * Ustawia aktualny pokój klienta.
     * @param room Obiekt Room reprezentujący pokój.
     */
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }
    /**
     * Zwraca aktualny pokój klienta.
     * @return Obiekt Room reprezentujący pokój.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }
    /**
     * Zwraca obiekt ServerSender odpowiedzialny za wysyłanie danych do klienta.
     * @return Obiekt ServerSender.
     */
    public ServerSender getServerSender() {
        return serverSender;
    }
    /**
     * Ustawia kolor kamienia przypisany do klienta.
     * @param color Kolor kamienia.
     */
    public void setPlayerColor(Stone color) {
        this.playerColor = color;
    }
    /**
     * Zwraca kolor kamienia przypisany do klienta.
     * @return Kolor kamienia.
     */
    public Stone getPlayerColor() {
        return this.playerColor;
    }
    public void switchToLobbyState() {
        this.currentState = this.lobbyState;
    }
    public void switchToRoomState() {
        this.currentState = this.roomState;
    }
    public void switchToGameState() {
        this.currentState = this.gameState;
    }
    public void switchToAnalyzeState() {
        this.currentState = this.analyzeState;
    }
    public void switchToMenuState() {
        this.currentState = this.menuState;
    }
}