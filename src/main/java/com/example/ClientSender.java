/**
 * @authors @Rafzzz3 i @paw08i
 * @version 1.0
 */
package com.example;
import java.io.ObjectOutputStream;
/**
    Klasa reprezentująca nadawcę klienta, odpowiedzialnego za wysyłanie wiadomości do GUI.
 */
public class ClientSender implements Runnable {
    /**
     * Obiekt ObjectOutputStream używany do wysyłania danych do GUI.
     */
    private ObjectOutputStream output;
    /**
     * Konstruktor klasy ClientSender.
     * @param output Obiekt ObjectOutputStream używany do wysyłania danych do GUI.
     */
    public ClientSender(ObjectOutputStream output) {
        this.output = output;
    }
    /**
     * Metoda wysyłająca wiadomość do GUI.
     * @param message Wiadomość do wysłania.
     */
    public void sendToGui(String message) {
        try {
            output.writeObject(message);
            output.flush();
            
        } catch (Exception e) {
            System.out.println("Błąd wysyłania do klienta: " + e.getMessage());
        }
    }
    /**
     * Metoda run() jest pusta, ponieważ ClientSender działa na żądanie.
     */
    @Override
    public void run() {
        
    }
    
    
}
