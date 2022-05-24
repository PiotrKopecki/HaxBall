package org.example.game.objects;

import lombok.Data;
import org.example.game.objects.bonus.Bonus;
import org.example.game.objects.players.Player;

import java.awt.*;

@Data
public abstract class Object {

    private float xCoord;
    private float yCoord;
    private float xVector;
    private float yVector;
    private float r = 30;
    private float deltaT;
    private Color color;

    public abstract void move(Player player1, Player player2, Rectangle boundaries, Rectangle goalBoundaries, Bonus bonus);
    public Object(){}
    public Object(float x, float y, float deltaT, Color color) {
        xCoord = x;
        yCoord = y;
        xVector = 0;
        yVector = 0;
        this.deltaT = deltaT;
        this.color = color;
    }

    public boolean checkHit(Object object){
        return distance(object) <= r+object.getR();
    }

    public double distance(Object object){
        return Math.sqrt(Math.pow(xCoord- object.xCoord, 2)+Math.pow(yCoord- object.yCoord, 2));
    }

    public double distanceFromPoint(float x, float y){
        return Math.sqrt(Math.pow(xCoord-x, 2)+Math.pow(yCoord-y, 2));
    }

    public boolean between(float left, float middle, float right){
        return ((left < middle && middle < right) || (left > middle && middle > right));
    }

    public float getSignOfNumber(float number){
        return (number != 0)? number/Math.abs(number) : 0;
    }

}
