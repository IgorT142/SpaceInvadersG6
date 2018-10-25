package es.urjc.jjve.spaceinvaders.entities;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class Joystick  {


    private float centerX;
    private float centerY;

    private int hatRadius;
    private int baseRadius;
    private static final Paint HAT_COLOR= new Paint(Color.argb(60,193, 197, 204)) ;

    private static final Paint BASE_COLOR= new Paint(Color.argb(30,193, 197, 204)) ;

    public Joystick(int x, int y, int radius) {
        this.centerX = x;
        this.centerY=y;
        this.hatRadius=radius;
        this.baseRadius = radius +2;
    }


    public float getX() {
        return centerX;
    }

    public float getY(){
        return centerY;
    }

    public float getBaseRadius() {
        return baseRadius;
    }

    public Paint getBaseColor() {
        return BASE_COLOR;
    }

    public float getHatRadius() {
        return hatRadius;
    }

    public Paint getHatColor() {
        return HAT_COLOR;
    }
}
