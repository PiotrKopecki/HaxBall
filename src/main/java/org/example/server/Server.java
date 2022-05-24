package org.example.server;

import lombok.Data;
import org.example.game.objects.ball.Ball;
import org.example.game.objects.bonus.Bonus;
import org.example.game.objects.players.AlivePlayer;
import org.example.game.objects.players.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import static org.example.Main.SCREEN_H;
import static org.example.Main.SCREEN_W;

@Data
public class Server implements ActionListener {
    private ServerSocket ss;
    private int numberOfPlayers;
    private final int maxPlayers;
    private Socket player1;
    private Socket player2;
    private ReadFromClient player1Read;
    private ReadFromClient player2Read;
    private WriteToClient player1Write;
    private WriteToClient player2Write;
    private Player p1, p2;
    private Ball ball;
    private Rectangle goalBoundaries;
    private Rectangle boundaries;
    private Bonus bonus;
    private String score = "0 : 0";
    private String gameFinished = "no";
    private float time = 0;
    private Timer timer;
    private float timeWhenGoalScored = 0;
    private float timeWhenGameEnded = 0;

    public Server(){
        numberOfPlayers = 0;
        maxPlayers = 2;
        ball = new Ball((float)SCREEN_W/2 ,(float)SCREEN_H/2 - 90, Color.WHITE);
        boundaries = new Rectangle(78, 54, 1123, 613);
        goalBoundaries = new Rectangle(78, 275, 1123, 170);
        bonus = new Bonus(0,0,Color.ORANGE);
        p1 = new AlivePlayer((float)SCREEN_W - 110,(float)SCREEN_H/2 - 90, Color.BLUE);
        p2 = new AlivePlayer((float)SCREEN_W/4 - 210,(float)SCREEN_H/2 - 90, Color.RED);
        timer = new Timer(10, this);
        try{
            ss = new ServerSocket(45371);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was a problem while creating Server");
        }
    }

    public void acceptConnections(){
        try{
            System.out.println("Waiting for players...");
            while (numberOfPlayers < maxPlayers){
                DataInputStream in;
                DataOutputStream out;
                if(numberOfPlayers == 0){
                    player1 = ss.accept();
                    in  = new DataInputStream(player1.getInputStream());
                    out  = new DataOutputStream(player1.getOutputStream());
                    player1Read = new ReadFromClient(1, in, this);
                    player1Write = new WriteToClient(1, out, this);
                }
                else{
                    player2 = ss.accept();
                    in  = new DataInputStream(player2.getInputStream());
                    out  = new DataOutputStream(player2.getOutputStream());
                    player2Read = new ReadFromClient(2, in, this);
                    player2Write = new WriteToClient(2, out, this);
                }
                numberOfPlayers++;
                out.writeInt(numberOfPlayers);
                System.out.println("Player number: " + numberOfPlayers + " has connected!");
                if(numberOfPlayers == 2){
                    out = new DataOutputStream(player1.getOutputStream());
                    out.writeBoolean(true);
                    out = new DataOutputStream(player2.getOutputStream());
                    out.writeBoolean(true);
                    Thread readThread1 = new Thread(player1Read);
                    Thread writeThread1 = new Thread(player1Write);
                    Thread readThread2 = new Thread(player2Read);
                    Thread writeThread2 = new Thread(player2Write);
                    readThread1.start();
                    readThread2.start();
                    writeThread1.start();
                    writeThread2.start();
                    timer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There was a problem while accepting client");
        }
    }

    public void changeScore(){
        String s;
        s = Integer.toString(p2.getPoints());
        score = s;
        score += " : ";
        s = Integer.toString(p1.getPoints());
        score += s;
    }

    public void addGoal(String whichPlayer){
        if(whichPlayer == null || (time - timeWhenGoalScored <= 500)){
            return;
        }
        timeWhenGoalScored = time;
        if(whichPlayer.equals("blue")){
            p1.setPoints(p1.getPoints() + 1);
        }
        else if(whichPlayer.equals("red")){
            p2.setPoints(p2.getPoints() + 1);
        }
        if(checkIfGameIsOver()){
            newGame();
        }
        else{
            newRound();
        }
        changeScore();
    }

    public boolean checkIfGameIsOver(){
        if(p1.getPoints() >= 3){
            gameFinished = "Blue wins";
            timeWhenGameEnded = time;
            return true;
        }
        else if(p2.getPoints() >= 3){
            gameFinished = "Red wins";
            timeWhenGameEnded = time;
            return true;
        }
        else if(time >= 1000*60*3){
            if(p1.getPoints() > p2.getPoints()){
                gameFinished = "Blue wins";
            }
            else if(p2.getPoints() > p1.getPoints()){
                gameFinished = "Red wins";
            }
            else{
                gameFinished = "Its a draw";
            }
            timeWhenGameEnded = time;
            return true;
        }
        if(time - timeWhenGameEnded >= 1000 && timeWhenGameEnded != 0){
            gameFinished = "no";
            time = 0;
            timeWhenGameEnded = 0;
        }
        return false;
    }

    public void newRound(){
        p1.setXCoord((float)SCREEN_W - 110);
        p1.setYCoord((float)SCREEN_H/2 - 90);
        p2.setXCoord((float)SCREEN_W/4 - 210);
        p2.setYCoord((float)SCREEN_H/2 - 90);
        p1.setXVector(0);
        p1.setYVector(0);
        p2.setXVector(0);
        p2.setYVector(0);
        p1.setSpeed(p1.getStartingSpeed());
        p2.setSpeed(p2.getStartingSpeed());
        ball = new Ball((float)SCREEN_W/2 ,(float)SCREEN_H/2 - 90,Color.WHITE);
        bonus = new Bonus(0,0,Color.ORANGE);
        timeWhenGoalScored = 0;
    }

    public void newGame(){
        p1 = new AlivePlayer((float)SCREEN_W - 110,(float)SCREEN_H/2 - 90, Color.BLUE);
        p2 = new AlivePlayer((float)SCREEN_W/4 - 210,(float)SCREEN_H/2 - 90, Color.RED);
        p1.setXVector(0);
        p1.setYVector(0);
        p2.setXVector(0);
        p2.setYVector(0);
        ball = new Ball((float)SCREEN_W/2 ,(float)SCREEN_H/2 - 90,Color.WHITE);
        bonus = new Bonus(0,0,Color.ORANGE);
        boundaries = new Rectangle(78, 54, 1123, 613);
        goalBoundaries = new Rectangle(78, 275, 1123, 170);
        timer.restart();
        score = "0 : 0";
        timeWhenGoalScored = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time += timer.getDelay();
    }

    public static void main(String[] args){
        Server server = new Server();
        server.acceptConnections();
    }
}
