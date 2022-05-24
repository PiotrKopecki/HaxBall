package org.example.game.objects.players;

import org.example.game.objects.bonus.Bonus;
import java.awt.*;

public class AlivePlayer extends Player{
    public AlivePlayer(float x, float y, Color color) {
        super(x, y, color);
    }

    public String colorToString(){
        if(this.getColor() == Color.BLUE){
            return "blue";
        }
        return "red";
    }
    public void checkIfTakenBonus(Bonus bonus){
        if(distance(bonus) <= getR()+bonus.getR() && bonus.getWhoHasBonus().equals("none")){ //taken bonus
            bonus.setWhoHasBonus(colorToString());
            setSpeed(getSpeed() + bonus.getSpeedBoost());
        }
    }
    @Override
    public void move(Player player1, Player player2, Rectangle boundaries, Rectangle goalBoundaries, Bonus bonus) {
        checkIfTakenBonus(bonus);
        setXCoord(checkX((int) (getXCoord() + getXVector())));
        setYCoord(checkY((int) (getYCoord() + getYVector())));
        checkNotHittingSoccerGoal((int)getXCoord(),(int)getYCoord());
    }
}
