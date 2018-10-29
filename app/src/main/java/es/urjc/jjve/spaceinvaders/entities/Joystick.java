package es.urjc.jjve.spaceinvaders.entities;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class Joystick {


    private float centerX;
    private float centerY;

    private int hatRadius;
    private int baseRadius;

    private Paint hatColor = new Paint(); //Color del joystick interior
    private Paint baseColor = new Paint(); //Color del joystick exterior

    public Joystick(int x, int y, int radius) {

        hatColor.setColor(Color.argb(125, 193, 34, 36));
        hatColor.setStyle(Paint.Style.FILL);

        baseColor.setColor(Color.argb(105, 193, 197, 204));
        baseColor.setStyle(Paint.Style.FILL);

        this.centerX = x;
        this.centerY = y;
        this.hatRadius = radius;
        this.baseRadius = radius + 200;
    }


    public float getX() {
        return centerX;
    }

    public float getY() {
        return centerY;
    }

    public float getBaseRadius() {
        return baseRadius;
    }

    public Paint getBaseColor() {
        return baseColor;
    }

    public float getHatRadius() {
        return hatRadius;
    }

    public Paint getHatColor() {
        return hatColor;
    }
}
