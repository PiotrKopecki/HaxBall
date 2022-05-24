package org.example;
import org.example.client.Client;

public class Main {
    public static int SCREEN_W = 1280;
    public static int SCREEN_H = 900;

    public static void main(String[] args){
        Client client = new Client();
        if(client.canStartTheGame()) {
            new Window(client);
        }
    }
}
