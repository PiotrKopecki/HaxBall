package org.example.server;

import java.io.DataInputStream;
import java.io.IOException;

public class ReadFromClient implements Runnable{
    private final int playerID;
    private final DataInputStream dataInputStream;
    private Server server;

    public ReadFromClient(int pid, DataInputStream in, Server s){
        playerID = pid;
        dataInputStream = in;
        server = s;
    }
    @Override
    public void run() {
        try{
            while (true){
                if(playerID == 1){
                    server.getP1().setXVector(dataInputStream.readFloat());
                    server.getP1().setYVector(dataInputStream.readFloat());
                    server.getP1().move(server.getP1(), server.getP2(),
                            server.getBoundaries(), server.getGoalBoundaries(), server.getBonus());
                }
                else{
                    server.getP2().setXVector(dataInputStream.readFloat());
                    server.getP2().setYVector(dataInputStream.readFloat());
                    server.getP2().move(server.getP2(), server.getP1(),
                            server.getBoundaries(), server.getGoalBoundaries(), server.getBonus());
                }
                if(!server.getBonus().canBeDrawn(server.getTime(), server.getP1(), server.getP2())){
                    server.getBonus().setXCoord(0);
                    server.getBonus().setYCoord(0);
                }
                server.getBall().move(server.getP1(), server.getP2(),
                        server.getBoundaries(), server.getGoalBoundaries(), server.getBonus());
                server.addGoal(server.getBall().checkIfGoal());
                if(server.checkIfGameIsOver()){
                    server.newGame();
                }
            }
        } catch (IOException e) {
            System.out.println("There was a problem while trying to read player position");
        }
    }
}