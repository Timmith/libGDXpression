package com.timmith.deadtropolis.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Citizen extends Sprite {

    Vector2 previousPos;

    public Citizen(Texture texture){
        super(texture);
        previousPos = new Vector2(getX(), getY());
    }

    public boolean hasMoved(){
        if(previousPos.x != getX() || previousPos.y != getY()) {
            previousPos.x = getX();
            previousPos.y = getY();
            return true;
        } //else
        return false;
    }

}
