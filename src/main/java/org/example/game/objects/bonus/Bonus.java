package org.example.game.objects.bonus;

import lombok.Data;
import org.example.game.objects.Object;
import org.example.game.objects.players.Player;
import java.awt.*;
import java.util.Random;
@Data
public class Bonus extends Object {

    private float timeWhenDrawn;
    private String whoHasBonus;
    private int speedBoost;
    private boolean taken = false;

    public Bonus(float x, float y, Color color){
        super(x, y, 0.1f, color);
        this.setR(20);
        this.findNewPosition();
        timeWhenDrawn = 0;
        whoHasBonus = "none";
        speedBoost = 3;
    }

    public void findNewPosition(){
        Random number = new Random();
        float x = number.nextFloat(900 - this.getR()) + 234;
        float y = number.nextFloat(550 - this.getR()) + 100;
        this.setXCoord(x);
        this.setYCoord(y);
    }

    public boolean canBeDrawn(float currentTime, Player player1, Player player2){
        if(whoHasBonus.equals("none")){
            return true;
        }
        else{
            if(!taken){ //if someone took it set time when he did
                this.setTimeWhenDrawn(currentTime);
                taken = true;
            }
            else if(currentTime - timeWhenDrawn >= 1000*30 || currentTime <= 1){ //if 30 seconds passed draw a new one
                this.timeWhenDrawn = currentTime;
                if(player1.getColor() == Color.BLUE){
                    player1.setSpeed(player1.getSpeed() - this.getSpeedBoost());
                }
                else{
                    player2.setSpeed(player2.getSpeed() - this.getSpeedBoost());
                }
                this.setWhoHasBonus("none");
                this.findNewPosition();
                taken = false;
                return true;
            }
        }
        return false;
    }
    @Override
    public void move(Player player1, Player player2, Rectangle boundaries, Rectangle goalBoundaries, Bonus bonus) {

    }
}
