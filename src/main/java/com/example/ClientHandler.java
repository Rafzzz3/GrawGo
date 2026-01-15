package com.example;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
    /**
     * @param roomCommandInterpreter Obiekt RoomCommandInterpreter do interpretacji komend związanych z pokojem.
     */
    private RoomCommandInterpreter roomCommandInterpreter;
    /**
     * @param commandInterpreter Obiekt GameCommandInterpreter do interpretacji komend związanych z grą.
     */
    private GameCommandInterpreter commandInterpreter;
    /**
     * Konstruktor klasy ClientHandler.
     * @param socket Socket używany do komunikacji z klientem.
     * @param roomManager Obiekt RoomManager zarządzający pokojami na serwerze.
     */
    public ClientHandler( Socket socket, RoomManager roomManager) {
        this.socket = socket;
        this.roomManager = roomManager;
        this.commandInterpreter = new GameCommandInterpreter();
        this.roomCommandInterpreter = new RoomCommandInterpreter(roomManager, this);
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
            serverSender.sendMessage("Witaj na serwerze. Dostępne komendy pokoju to: CREATE, LIST, JOIN.");
            while (!socket.isClosed()) {
                String clientcommand = (String) input.readObject();
                System.out.println("Otrzymano odpowiedź od gracza: " + clientcommand);
                if (clientcommand.trim().equals("LEAVE")) {
                    roomCommandInterpreter.interpret(roomManager, this, clientcommand);
                    continue;
                }
                if (currentRoom == null || !currentRoom.isGameStarted()) {
                    roomCommandInterpreter.interpret(roomManager, this, clientcommand);
                // } else if (currentRoom.isGameStarted()) {
                //     game = currentRoom.getGame();
                //     if (!game.isTurn(getPlayerColor())) {
                //         serverSender.sendMessage("Nie twoja tura. Czekaj na ruch przeciwnika.");
                //         continue;
                //     }
                //     commandInterpreter.interpret(game, clientcommand, this);
                //     // odeślij wynik ostatniej komendy gry do klienta
                //     if (game != null) {
                //         String message = game.getMessage();
                //         if (message != null && !message.isEmpty()) {
                //             serverSender.sendMessage(message);
                //         }
                //         for (ClientHandler player : currentRoom.getPlayers()) {
                //             player.getServerSender().sendObject(game.getBoard());
                //         }
                //     }
                // }
                } else if (currentRoom.isGameStarted()) {
                    game = currentRoom.getGame();
                    
                    // 1. Sprawdzenie tury
                    if (!game.isTurn(getPlayerColor())) {
                        MoveResult notTurnResult = new MoveResult(
                            MoveCode.NOT_YOUR_TURN, 
                            new int[0][], 
                            "Nie twoja tura. Czekaj na ruch przeciwnika."
                        );
                        serverSender.sendObject(notTurnResult);
                        continue; // Przerywamy pętlę
                    }

                    // 2. Wykonanie ruchu
                    commandInterpreter.interpret(game, clientcommand, this);

                    // 3. Wysłanie wyniku ruchu
                    if (game != null && game.getLastMoveResult() != null) {
        
                        serverSender.sendObject(game.getLastMoveResult());
                        if (game.getLastMoveResult().code == MoveCode.SURRENDER || game.getLastMoveResult().code == MoveCode.GAME_OVER) {
                            for (ClientHandler player : currentRoom.getPlayers()) {
                                if (player != this) { 
                                    player.getServerSender().sendObject(game.getLastMoveResult());
                                }
                            }
                        }                        
                        if (game.getLastMoveResult().code == MoveCode.OK || game.getLastMoveResult().code == MoveCode.PASS) {
                            for (ClientHandler player : currentRoom.getPlayers()) {
                                player.getServerSender().sendObject(game.getBoard());
                                if (game.getLastMoveResult().code == MoveCode.PASS && player != this) {
                                    player.getServerSender().sendMessage(LobbyMessageType.INFO + ": Przeciwnik spasował.");
                                }
                            }
                        }
                        
                        game.setLastMoveResult(null);
                    }
                }
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
                        player.getServerSender().sendMessage(LobbyMessageType.INFO+" Przeciwnik rozłączył się.");
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
}