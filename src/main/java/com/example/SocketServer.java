package com.example;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
// Tu jest już chyba wszystko gotowe do uruchomienia serwera, potem jesczze sprawdzę czy 
// opłaca się robić playerId żeby rozróżniać graczy i żeby był doublelock
public class SocketServer {
    private int port;
    private RoomManager roomManager;
    private ClientThreadHandler clientThreadHandler = new ClientThreadHandler();
    public SocketServer(int port) {
        this.port = port;
        this.roomManager = new RoomManager();
    }
    public void listen() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Serwer działa na porcie: " + port);
            while (true) {
                try  {
                    Socket gracz = serverSocket.accept();
                    System.out.println("Podłączono nowego gracza");
                    ClientHandler clientHandler = new ClientHandler( gracz,  roomManager);
                    clientThreadHandler.handleClient(clientHandler);
                }
                catch (IOException e) {
                    System.out.println("Błąd podczas obsługi gracza: " + e.getMessage());
                }
            }
 
        } catch (IOException ex) {
            System.out.println("Błąd serwera: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        int port = 4444;
        SocketServer server = new SocketServer(port);
        server.listen();
    }
}