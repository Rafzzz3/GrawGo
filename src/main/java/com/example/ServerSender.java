/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import java.io.IOException;
import java.io.ObjectOutputStream;
/**
    Klasa reprezentująca nadawcę serwera, odpowiedzialnego za wysyłanie wiadomości i obiektów do klienta.
 */
public class ServerSender {
    /**
     * Obiekt ObjectOutputStream używany do wysyłania danych do klienta.
     */
    private ObjectOutputStream output;
    /**
     * Konstruktor klasy ServerSender.
     * @param output Obiekt ObjectOutputStream używany do wysyłania danych do klienta.
     */
    public ServerSender(ObjectOutputStream output) {
        this.output = output;
    }
    /**
     * Metoda wysyłająca wiadomość do klienta.
     * @param message
     */
    public synchronized void sendMessage(String message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.err.println("Błąd podczas wysyłania wiadomości do klienta: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Metoda wysyłająca obiekt do klienta.
     * @param obj Obiekt do wysłania.
     */
    public synchronized void sendObject(Object obj) {
        try {
            output.writeObject(obj);
            output.reset();
            output.flush();
        } catch (IOException e) {
            System.err.println("Błąd podczas wysyłania obiektu do klienta: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
