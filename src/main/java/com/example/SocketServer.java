/** 
 *  @authors @Rafzzz3 i @paw08i
 *  @version 1.0
 */
package com.example;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.config.MongoConfig;
import com.example.database.GameService;
/**
    Klasa reprezentująca serwer socketowy, który nasłuchuje na określonym porcie i obsługuje połączenia od klientów.
    Serwer działa w nieskończonej pętli, akceptując nowych klientów i tworząc dla nich ich własnych ClientHandler'ów.
 */
public class SocketServer {
    /**
     * Port, na którym serwer nasłuchuje połączeń od klientów.
     */
    private int port;
    /** 
     * Manager pokoi - zarządza pokojami gier i graczami.
    */
    private RoomManager roomManager;
    /** 
     * Handler wątków klientów - zarządza wątkami obsługującymi komunikację z klientami.
    */
    private ClientThreadHandler clientThreadHandler = new ClientThreadHandler();
    /**
     * Konstruktor klasy SocketServer.
     * @param port Port, na którym serwer będzie nasłuchiwał połączeń.
     */

    private GameService gameService;

    public SocketServer(int port, GameService gameService) {
        this.port = port;
        this.gameService = gameService;
        this.roomManager = new RoomManager(gameService);
    }
    /**
     * Metoda uruchamiająca serwer i nasłuchująca na połączenia od klientów.
     * Po zaakceptowaniu połączenia tworzy nowy ClientHandler dla każdego klienta
     * i przekazuje go do ClientThreadHandler w celu obsługi komunikacji.
     */
    public void listen() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Serwer działa na porcie: " + port);
            while (true) {
                try  {
                    Socket gracz = serverSocket.accept();
                    System.out.println("Podłączono nowego gracza");
                    ClientHandler clientHandler = new ClientHandler( gracz,  roomManager, this.gameService);
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
    /**
     * Metoda główna uruchamiająca serwer.
     * @param args Argumenty linii poleceń (nieużywane).
     */
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(MongoConfig.class);
        GameService gameService = context.getBean(GameService.class);

        int port = 4444;
        SocketServer server = new SocketServer(port, gameService);
        server.listen();
    }
}