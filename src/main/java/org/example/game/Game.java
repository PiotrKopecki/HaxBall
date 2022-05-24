package org.example.game;

import lombok.Data;
import org.example.client.Client;
import org.example.client.ReadFromServer;
import org.example.client.WriteToServer;
import org.example.game.objects.ball.Ball;
import org.example.game.objects.bonus.Bonus;
import org.example.game.objects.players.AlivePlayer;
import org.example.game.objects.players.Player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

import static org.example.Main.*;

@Data
public class Game extends JPanel implements ActionListener,KeyListener {
    private Player enemy;
    private Player me;
    private Ball ball;
    private Bonus bonus;
    private String score = "0 : 0";
    private final int BOX_WH = 75;
    private String gameFinished = "no";
    private float time = 0;
    private Image backgroundImage;
    private int id;

    public Game(Client client){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        String path = "assets/pitch_resized.png";
        backgroundImage = toolkit.getImage(path);
        this.id = client.getPlayerID();
        if(id == 1){
            me = new AlivePlayer((float)SCREEN_W - 110,(float)SCREEN_H/2 - 90, Color.BLUE);
            enemy = new AlivePlayer((float)SCREEN_W/4 - 210,(float)SCREEN_H/2 - 90, Color.RED);
        }
        else{
            me = new AlivePlayer((float)SCREEN_W/4 - 210,(float)SCREEN_H/2 - 90, Color.RED);
            enemy = new AlivePlayer((float)SCREEN_W - 110,(float)SCREEN_H/2 - 90, Color.BLUE);
        }
        ball = new Ball((float)SCREEN_W/2 ,(float)SCREEN_H/2 - 90,Color.WHITE);
        bonus = new Bonus(0,0,Color.ORANGE);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
        setFocusTraversalKeysEnabled(false);
        ReadFromServer rfs = new ReadFromServer(client.getIn(), this);
        WriteToServer wts = new WriteToServer(client.getOut(), this);
        Thread threadRFS = new Thread(rfs);
        Thread threadWTS = new Thread(wts);
        threadRFS.start();
        threadWTS.start();
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,SCREEN_W,SCREEN_H);
        g.drawImage(backgroundImage,0,0,null);
        printTime(g);
        printScore(g);
        printObjects(g);
        if(!gameFinished.equals("no")){
            printWinner(g);
        }
        g.dispose();
    }

    void printScore(Graphics g){
        g.setFont(new Font("Verdana", Font.BOLD, 60));
        int stringWidth = g.getFontMetrics().stringWidth(getScore());
        g.setColor(Color.white);
        g.drawString(getScore(),SCREEN_W/2 - stringWidth/2,(int)(720 + (SCREEN_H - 720)/4 + BOX_WH*0.75));
        g.setColor(Color.RED);
        g.fillRect(SCREEN_W/2 - BOX_WH - stringWidth/2 - 10,720 + (SCREEN_H - 720)/4,75,75);
        g.setColor(Color.BLUE);
        g.fillRect(SCREEN_W/2 + stringWidth/2 + 10,720 + (SCREEN_H - 720)/4,75,75);
    }

    void printObjects(Graphics g){
        g.setColor(getMe().getColor());
        int x = (int)(getMe().getXCoord());
        int y = (int)(getMe().getYCoord());
        int r = (int)(getMe().getR());
        g.fillOval(x-r,y-r,r*2,r*2);

        g.setColor(getEnemy().getColor());
        x = (int)(getEnemy().getXCoord());
        y = (int)(getEnemy().getYCoord());
        r = (int)(getEnemy().getR());
        g.fillOval(x-r,y-r,r*2,r*2);

        g.setColor(getBall().getColor());
        x = (int)(getBall().getXCoord());
        y = (int)(getBall().getYCoord());
        r = (int)(getBall().getR());
        g.fillOval(x-r,y-r,r*2,r*2);

        if(getBonus().getXCoord() != 0){
            g.setColor(getBonus().getColor());
            x = (int)(getBonus().getXCoord());
            y = (int)(getBonus().getYCoord());
            r = (int)(getBonus().getR());
            g.fillOval(x-r,y-r,r*2,r*2);
        }
    }

    void printTime(Graphics g){
        int tempTime = (int)time/1000;
        int minutes = 0;
        while(tempTime > 59){
            minutes++;
            tempTime -= 60;
        }
        String temp = "0";
        temp += Integer.toString(minutes);
        temp += ":";
        if(tempTime < 10){
            temp +="0";
        }
        temp += Integer.toString(tempTime);
        g.setFont(new Font("Verdana", Font.BOLD, 50));
        int stringWidth = g.getFontMetrics().stringWidth(temp);
        g.setColor(Color.white);
        g.drawString(temp,SCREEN_W/2 + 2*stringWidth,(int)(720 + (SCREEN_H - 720)/4 + BOX_WH*0.75));
    }

    public void printWinner(Graphics g){
        String temp = gameFinished;
        g.setFont(new Font("Verdana", Font.BOLD, 70));
        int stringWidth = g.getFontMetrics().stringWidth(temp);
        if(temp.equals("Its a draw")){
            g.setColor(Color.BLACK);
        }
        else if(temp.equals("Blue wins")){
            g.setColor(Color.BLUE);
        }
        else{
            g.setColor(Color.RED);
        }
        g.drawString(temp,SCREEN_W/2 - stringWidth/2,SCREEN_H/2 - stringWidth/2);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int button = e.getKeyCode();
        if(button == KeyEvent.VK_DOWN){
            me.setYVector(me.getSpeed());
        }
        if(button == KeyEvent.VK_UP){
            me.setYVector(-me.getSpeed());
        }
        if(button == KeyEvent.VK_RIGHT){
            me.setXVector(me.getSpeed());
        }
        if(button == KeyEvent.VK_LEFT){
            me.setXVector(-me.getSpeed());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int button = e.getKeyCode();
        if(button == KeyEvent.VK_DOWN){
            me.setYVector(0);
        }
        if(button == KeyEvent.VK_UP){
            me.setYVector(0);
        }
        if(button == KeyEvent.VK_RIGHT){
            me.setXVector(0);
        }
        if(button == KeyEvent.VK_LEFT){
            me.setXVector(0);
        }
    }





    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
