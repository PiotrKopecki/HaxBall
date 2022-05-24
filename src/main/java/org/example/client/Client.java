package org.example.client;
import lombok.Data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@Data
public class Client {
    private Socket socket;
    private int playerID;
    private DataInputStream in;
    private DataOutputStream out;

    public Client(){
        try{
            socket = new Socket("localhost", 45371);
            in  = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was a problem while connecting client");
        }
    }

    public boolean canStartTheGame(){
        while(true){
            try{
                boolean start = in.readBoolean();
                if(start == true){
                    break;
                }

            } catch (IOException e) {
            }
        }
        return true;
    }
}
