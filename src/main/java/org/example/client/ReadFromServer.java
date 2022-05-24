package org.example.client;

import org.example.game.Game;

import java.io.DataInputStream;
import java.io.IOException;

public class ReadFromServer implements Runnable{
    private final DataInputStream dataInputStream;
    private final Game game;

    public ReadFromServer(DataInputStream in, Game g){
        game = g;
        dataInputStream = in;
    }

    @Override
    public void run() {
        try{
            while (true){
                game.getEnemy().setXCoord(dataInputStream.readFloat());
                game.getEnemy().setYCoord(dataInputStream.readFloat());
                game.getMe().setXCoord(dataInputStream.readFloat());
                game.getMe().setYCoord(dataInputStream.readFloat());
                game.getMe().setSpeed(dataInputStream.readInt());
                game.getBall().setXCoord(dataInputStream.readFloat());
                game.getBall().setYCoord(dataInputStream.readFloat());
                game.setScore(dataInputStream.readUTF());
                game.setGameFinished(dataInputStream.readUTF());
                float t = dataInputStream.readFloat();
                if(game.getGameFinished().equals("no")){
                    game.setTime(t);
                }
                game.getBonus().setXCoord(dataInputStream.readFloat());
                game.getBonus().setYCoord(dataInputStream.readFloat());
                game.repaint();
            }
        } catch (IOException e) {
            System.out.println("There was a problem while trying to read enemy position");
        }
    }
}