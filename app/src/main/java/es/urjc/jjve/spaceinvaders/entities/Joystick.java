package es.urjc.jjve.spaceinvaders.entities;

import android.graphics.Color;
import android.graphics.Paint;

public class Joystick {


    private float centerX;
    private float centerY;

    private int hatRadius;
    private int baseRadius;

    private Paint hatColor = new Paint(); //Color del joystick interior
    private Paint baseColor = new Paint(); //Color del joystick exterior
    private float hatY;
    private float hatX;
    private float distance;

    public Joystick(int x, int y, int radius) {

        hatColor.setColor(Color.argb(125, 193, 34, 36));
        hatColor.setStyle(Paint.Style.FILL);

        baseColor.setColor(Color.argb(105, 193, 197, 204));
        baseColor.setStyle(Paint.Style.FILL);

        this.centerX = x;
        this.centerY = y;
        double porcentaje = radius * 0.4;
        this.hatRadius = radius - (int) porcentaje;
        this.baseRadius = radius;
        initHat();
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

    public void setHatY(float y) {
        this.hatY=y;
    }

    public void setHatX(float x) {
        this.hatX=x;
    }

    public void setHat(float x, float y){
        this.distance = (float) Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

        if(distance<baseRadius){
            this.setHatX(x);
            this.setHatY(y);
        }else{
            float ratio = baseRadius / distance;
            hatX = centerX + (x - centerX) * ratio;

            hatY = centerY + (y - centerY) * ratio;
        }
    }

    public void initHat(){
        this.hatX= centerX;
        this.hatY= centerY;
    }

    public float getHatY() {
        return this.hatY;
    }

    public float getHatX() {
        return this.hatX;
    }
}
