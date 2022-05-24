package org.example.game.objects.ball;

import lombok.Data;
import org.example.game.objects.Object;
import org.example.game.objects.bonus.Bonus;
import org.example.game.objects.players.Player;

import java.awt.*;

@Data
public class Ball extends Object {
    private float speed = (float)Math.sqrt(64);
    public Ball(float x, float y, Color color){
        super(x, y, 0.1f, color);
        this.setR(20);
    }

    public void playerCollision(Player player){
        float deltaY = player.getYCoord() - getYCoord();
        float deltaX = getXCoord() - player.getXCoord();
        float xSign = getSignOfNumber(deltaX);// 1 or -1 or 0
        float ySign = getSignOfNumber(-deltaY);// 1 or -1 or 0
        if(deltaX != 0) {
            float a = (deltaY) / (deltaX);//a = (y2-y1)/(x2-x1)
            setXVector((float)(xSign*speed/Math.sqrt(Math.pow(a, 2)+1)));
            setYVector(ySign*Math.abs(getXVector()*a));
        }else{
            setXVector(0);
            setYVector(-speed*ySign);
        }
    }

    public void bothPlayersCollision(Player player1, Player player2){
        if(player1.getXCoord() == player2.getXCoord()){//ball has to go up or down
            setXVector((float)Math.sqrt(2)*speed);
            setYVector(0);
            if(getXCoord() < player1.getXCoord())
                setXVector(-1*getXVector());
        }else if(player1.getYCoord() == player2.getYCoord()){
            setXVector(0);
            setYVector((float)Math.sqrt(2)*speed);
            if(getYCoord() < player1.getYCoord())
                setYVector(-1*getYVector());
        }else{
            float xS = (player1.getXCoord()+player2.getXCoord())/2;
            float yS = (player1.getYCoord()+player2.getYCoord())/2;
            double dist = distanceFromPoint(xS, yS);
            float deltaX = getXCoord() - xS;
            float deltaY = getYCoord() - yS;
            setXVector(deltaX*(float)Math.sqrt(2)*speed/(float)dist);
            setYVector(deltaY*(float)Math.sqrt(2)*speed/(float)dist);
        }
    }

    public void checkCollisions(Player player1, Player player2, Rectangle boundaries, Rectangle goalBoundaries){
        if(isPlayerHit(player1) && isPlayerHit(player2))
            bothPlayersCollision(player1, player2);
        else if(isPlayerHit(player1))
            playerCollision(player1);
        else if(isPlayerHit(player2))
            playerCollision(player2);
        checkWallHit(boundaries, goalBoundaries);
    }

    public boolean isPlayerHit(Object object){
        return distance(object) <= getR()+object.getR();
    }

    public void checkWallHit(Rectangle boundaries, Rectangle goalBoundaries){
        if(getYCoord() - getR() < boundaries.y || getYCoord() + getR() > boundaries.y + boundaries.height){//up & down walls
            if ((getYCoord() - getR() < boundaries.y))
                setYCoord(boundaries.y + getR());
            else
                setYCoord(boundaries.y + boundaries.height - getR());

            setYVector(-1*getYVector());
        }

        if(getXCoord() - getR() < boundaries.x || getXCoord() + getR() > boundaries.x + boundaries.width){//left & right walls
            if(!between(goalBoundaries.y, getYCoord(), goalBoundaries.y+goalBoundaries.height)){
                if ((getXCoord() - getR() < boundaries.x))
                    setXCoord(boundaries.x + getR());
                else
                    setXCoord(boundaries.x + boundaries.width - getR());

                setXVector(-1*getXVector());
            }
        }
    }

    public String checkIfGoal(){
        if(getYCoord() > 280 && getYCoord() < 480 && getXCoord() <= 78){
            return "blue";
        }
        else if(getYCoord() > 280 && getYCoord() < 480 && getXCoord() >= 1201){
            return "red";
        }
        return null;
    }

    public void ridOfCollision(Player enemy, Rectangle boundaries, Rectangle goalBoundaries){
        int vectorY = Float.compare(enemy.getYCoord(), getYCoord());
        int vectorX = Float.compare(enemy.getXCoord(), getXCoord());

        while(checkHit(enemy)){
            setYCoord(getYCoord()-vectorY);
            setXCoord(getXCoord()-vectorX);
            enemy.setYCoord(enemy.getYCoord()+vectorY);
            enemy.setXCoord(enemy.getXCoord()+vectorX);
            checkWallHit(boundaries, goalBoundaries);
            enemy.checkWallHit(boundaries, goalBoundaries);
        }
    }

    @Override
    public void move(Player player1, Player player2, Rectangle boundaries, Rectangle goalBoundaries, Bonus bonus) {
        checkCollisions(player1, player2, boundaries, goalBoundaries);
        ridOfCollision(player1, boundaries, goalBoundaries);
        ridOfCollision(player2, boundaries, goalBoundaries);
        setXCoord(getXCoord() + getXVector());
        setYCoord(getYCoord() + getYVector());
        setXVector((float)0.96*getXVector());
        setYVector((float)0.96*getYVector());
        checkWallHit(boundaries, goalBoundaries);
    }
}