package org.example.client;

import org.example.game.Game;

import java.io.DataOutputStream;
import java.io.IOException;

public class WriteToServer implements Runnable{
    private final DataOutputStream dataOutputStream;
    private Game game;

    public WriteToServer(DataOutputStream out, Game g){
        game = g;
        dataOutputStream = out;
    }

    @Override
    public void run() {
        try{
            while(true){
                if(game.getGameFinished().equals("no")){
                    dataOutputStream.writeFloat(game.getMe().getXVector());
                    dataOutputStream.writeFloat(game.getMe().getYVector());
                }
                else{
                    dataOutputStream.writeFloat(0);
                    dataOutputStream.writeFloat(0);
                }
                dataOutputStream.flush();
                try{
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("There was a problem while trying to put thread to sleep");
                }
            }
        }catch(IOException ex){
            System.out.println("There was a problem in WTS run method");
        }
    }
}
