package com.example;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
//
public class ClientHandler implements Runnable {
    private Socket socket;
    private Room currentRoom = null;
    private Stone playerColor;
    private RoomManager roomManager;
    private Game game;
    private ServerSender serverSender;
    private ObjectInputStream input;
    private RoomCommandInterpreter roomCommandInterpreter;
    private GameCommandInterpreter commandInterpreter;
    public ClientHandler( Socket socket, RoomManager roomManager) {
        this.socket = socket;
        this.roomManager = roomManager;
        this.commandInterpreter = new GameCommandInterpreter();
        this.roomCommandInterpreter = new RoomCommandInterpreter(roomManager, this);
    }
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
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }
    public Room getCurrentRoom() {
        return currentRoom;
    }

    public ServerSender getServerSender() {
        return serverSender;
    }
    public void setPlayerColor(Stone color) {
        this.playerColor = color;
    }
    public Stone getPlayerColor() {
        return this.playerColor;
    }
}