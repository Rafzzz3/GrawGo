/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import java.io.*;
import java.net.Socket;
/**
    Klasa reprezentująca klienta socketowego, który łączy się z serwerem,
    wysyła i odbiera dane za pomocą strumieni obiektów.
 */
public class SocketClient {
    /**
     * @param socket Socket używany do komunikacji z serwerem.
     */
    private Socket socket;
    /**
     * @param clientSender Obiekt odpowiedzialny za wysyłanie danych do serwera.
     * @param clientReceiver Obiekt odpowiedzialny za odbieranie danych od serwera
     */
    private ClientSender clientSender;
    private ClientReceiver clientReceiver;
    /**
     * Metoda łącząca klienta z serwerem na określonym hoście i porcie.
     * Inicjalizuje strumienie do wysyłania i odbierania danych oraz uruchamia
     * odpowiednie wątki do obsługi komunikacji.
     * @throws IOException Jeśli wystąpi błąd podczas nawiązywania połączenia.
     */
    public void connect() throws IOException {
        try {
            socket = new Socket("localhost", 4444);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            clientSender = new ClientSender(out);
            clientReceiver = new ClientReceiver(in);
            Thread senderThread = new Thread(clientSender);
            Thread receiverThread = new Thread(clientReceiver);

            senderThread.setDaemon(true);
            receiverThread.setDaemon(true);
            
            senderThread.start();
            receiverThread.start();
        } catch (IOException e) {
            System.out.println("Błąd połączenia z serwerem: " + e.getMessage());
        }
    }
    /**
     * Zwraca obiekt ClientSender odpowiedzialny za wysyłanie danych do serwera.
     * @return obiekt ClientSender.
     */
    public ClientSender getClientSender() {
        return clientSender;
    }
    /**
     * Zwraca obiekt ClientReceiver odpowiedzialny za odbieranie danych od serwera.
     * @return Obiekt ClientReceiver.
     */
    public ClientReceiver getClientReceiver() {
        return clientReceiver;
    }
    /**
     * Funkcja close() zamyka połączenie z serwerem.
     */
    public void close() {
        try { 
            if (socket != null) {
                socket.close(); 
            }
        } catch (Exception e) {
            System.out.println("Błąd zamykania połączenia: " + e.getMessage());
        }
    }
}