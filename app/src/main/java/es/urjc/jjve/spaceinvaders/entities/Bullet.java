package es.urjc.jjve.spaceinvaders.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import es.urjc.jjve.spaceinvaders.R;

/*En esta clase debo implementar el bitmap con la bala la cual es una pelota*/
public class Bullet {
    private float x;
    private float y;
    private boolean godBullet;


    private RectF rect;

    // En qué dirección se está disparando
    private static final int UP = 0;

    private static final int DOWN = 1;

    private int heading = -1;
    private static final float speed =  350;

    private static final int width =20;
    private int height;

    private Bitmap bitmapBullet;

    private boolean isActive;

    public Bullet(int screenY,Context context) {

        height = screenY / 20;
        isActive = false;
        godBullet = false;
        //incializamos el bitmap
        bitmapBullet = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.balita);
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

    public void shoot(float startX, float startY, int direction) {
        if (!isActive) {
            x = startX;
            y = startY;
            heading = direction;
            isActive = true;
        }
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

    public static int getDOWN() {
        return DOWN;
    }

    public Bitmap getBitmapBullet() {
        return bitmapBullet;
    }
}
