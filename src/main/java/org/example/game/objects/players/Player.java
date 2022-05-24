package org.example.game.objects.players;

import lombok.Data;
import org.example.game.objects.Object;

import java.awt.*;

import static org.example.Main.SCREEN_W;

@Data
public abstract class Player extends Object {

    private int points = 0;
    private int speed = 5;

    public Player(float x, float y, Color color){
        super(x, y, 0.1f, color);
    }

    public int getStartingSpeed(){
        return 5;
    }
    public int checkX(int x){
        if(x < getR())
            return (int)getR();
        else if(x > 1295 - 2*getR())
            return (int)(1295-2*getR());
        return x;
    }
    public int checkY(int y){
        if(y < getR())
            return (int)getR();
        else if(y > 720 - getR())
            return (int)(720 - getR());
        return y;
    }
    public void checkNotHittingSoccerGoal(int x, int y){
        if(y < 450 && y > 245 && x > 1180){
            setXCoord(1180);
        }
        if(y < 450 && y > 245 && x < SCREEN_W/4 - 215){
            setXCoord(SCREEN_W/4 - 215);
        }
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

}
