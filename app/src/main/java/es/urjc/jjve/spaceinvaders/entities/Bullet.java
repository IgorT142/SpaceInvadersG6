package es.urjc.jjve.spaceinvaders.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;

import es.urjc.jjve.spaceinvaders.R;

/*En esta clase debo implementar el bitmap con la bala la cual es una pelota*/
public class Bullet {
    private float x;
    private float y;
    private boolean godBullet = false;
    private Bitmap bitmapBullet;//Variable para implentar el bitmapBullet


    private RectF rect;

    // En qué dirección se está disparando
    public final int UP = 0;
    public final int DOWN = 1;

    int heading = -1;
    float speed =  350;

    private int width =20;
    private int height;


    private boolean isActive;

    public Bullet(int screenY,Context context) {

        height = screenY / 20;
        isActive = false;
        godBullet = false;
        //incializamos el bitmap
        bitmapBullet = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.balita);//cambiar este bitmap
        rect = new RectF();
    }

    /*prueba de metodo de redimension de bitmap
    public Bitmap redimensionarImagen(Bitmap bitmap, float newWidth, float newHeight){
        int width=bitmapBullet.getWidth();
        int height=bitmapBullet.getHeight();
        float scaleWidth=((float)newWidth)/width;
        float scaleHeigth=((float)newHeight)/height;
        //creamos una matriz para la manipulacion de la imagen
        Matrix matrix=new Matrix();
        matrix.postScale(scaleWidth,scaleHeigth);
        return bitmap.createBitmap(bitmapBullet,0,0,matrix,false);
    }*/

    public RectF getRect(){
        return  rect;
    }

    public boolean getStatus(){
        return isActive;
    }

    public void setInactive(){
        isActive = false;
    }

    public void setGodBullet() {
        this.godBullet = true;
    }
    public boolean getGodBullet(){
        return this.godBullet;
    }

    public float getImpactPointY(){
        if (heading == DOWN){
            return y + height;
        }else{
            return  y;
        }

    }

    public boolean shoot(float startX, float startY, int direction) {
        if (!isActive) {
            x = startX;
            y = startY;
            heading = direction;
            isActive = true;
            return true;
        }
        // La bala ya está activa
        return false;
    }

    public void update(long fps){

        // Solo se mueve para arriba o abajo
        if(heading == UP){
            y = y - speed / fps;
        }else{
            y = y + speed / fps;
        }
        // Actualiza rect
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;
    }

    public void changeDir() {
        this.heading = 1-this.heading;
    }

    public Bitmap getBitmapBullet() {
        return bitmapBullet;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
