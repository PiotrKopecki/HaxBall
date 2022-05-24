package org.example.server;

import java.io.DataOutputStream;
import java.io.IOException;

public class WriteToClient implements Runnable{
    private final int playerID;
    private final DataOutputStream dataOutputStream;
    private final Server server;

    public WriteToClient(int pid, DataOutputStream out, Server s){
        playerID = pid;
        dataOutputStream = out;
        server = s;
    }

    @Override
    public void run() {
        try{
            while (true){
                if(playerID == 1){
                    dataOutputStream.writeFloat(server.getP2().getXCoord());
                    dataOutputStream.writeFloat(server.getP2().getYCoord());
                    dataOutputStream.writeFloat(server.getP1().getXCoord());
                    dataOutputStream.writeFloat(server.getP1().getYCoord());
                    dataOutputStream.writeInt(server.getP1().getSpeed());
                }
                else{
                    dataOutputStream.writeFloat(server.getP1().getXCoord());
                    dataOutputStream.writeFloat(server.getP1().getYCoord());
                    dataOutputStream.writeFloat(server.getP2().getXCoord());
                    dataOutputStream.writeFloat(server.getP2().getYCoord());
                    dataOutputStream.writeInt(server.getP2().getSpeed());
                }
                dataOutputStream.writeFloat(server.getBall().getXCoord());
                dataOutputStream.writeFloat(server.getBall().getYCoord());
                dataOutputStream.writeUTF(server.getScore());
                dataOutputStream.writeUTF(server.getGameFinished());
                dataOutputStream.writeFloat(server.getTime());
                dataOutputStream.writeFloat(server.getBonus().getXCoord());
                dataOutputStream.writeFloat(server.getBonus().getYCoord());
                dataOutputStream.flush();
                try{
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("There was a problem while trying to put thread to sleep");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}