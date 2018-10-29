package es.urjc.jjve.spaceinvaders.view;

import es.urjc.jjve.spaceinvaders.HighScoreActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import es.urjc.jjve.spaceinvaders.R;
import es.urjc.jjve.spaceinvaders.controllers.ViewController;
import es.urjc.jjve.spaceinvaders.entities.Bullet;
import es.urjc.jjve.spaceinvaders.entities.DefenceBrick;
import es.urjc.jjve.spaceinvaders.entities.Invader;
import es.urjc.jjve.spaceinvaders.entities.Joystick;
import es.urjc.jjve.spaceinvaders.entities.PlayerShip;

/**
 * Clase utilizada para mostrar la interfaz del juego y manejar eventos dentro del juego, movimiento y disparo
 */

public class SpaceInvadersView extends SurfaceView implements Runnable, View.OnTouchListener {


    Context context;

    private Thread gameThread = null;


    private boolean paused = true;
    private volatile boolean playing;

    private long timeThisFrame;

    private long fps = 20;


    // Our SurfaceHolder to lock the surface before we draw our graphics
    private SurfaceHolder ourHolder;

    private Button upBtn;
    private Button dwnBtn;
    private Button lftBtn;
    private Button rgtBtn;


    // A Canvas and a Paint object
    private Canvas canvas;
    private Paint paint;


    // The size of the screen in pixels
    private int screenX;
    private int screenY;


    private ViewController controller;

    // How menacing should the sound be?
    private long menaceInterval = 1000;
    // Which menace sound should play next
    private boolean uhOrOh;
    // When did we last play a menacing sound
    private long lastMenaceTime = System.currentTimeMillis();
    private Joystick joystick;


    public SpaceInvadersView(Context context, int x, int y, boolean underage) {


        super(context);


        // Make a globally available copy of the context so we can use it in another method
        this.context = context;

        // Initialize ourHolder and paint objects
        ourHolder = this.getHolder();
        paint = new Paint();


        screenX = x;
        screenY = y;
        // The next line of code asks the
        // SurfaceView class to set up our object.
        this.joystick = new Joystick(screenX / 10, screenY - (screenY / 8), screenX / 14);

        controller = new ViewController(this.context, screenX, screenY, this);
        controller.setUnderage(underage);

        this.initPaintGameObject();
    }


    public void drawBackground() {


        // Lock the canvas ready to draw
        // Draw the background color
        try {
            if (canvas != null)
                canvas.drawColor(Color.argb(255, 0, 0, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawGameObject(RectF rect) {


        canvas.drawRect(rect, paint);


    }


    public void drawGameObject(Bitmap bitmap, float x, float y) {
        // Make sure our drawing surface is valid or we crash
        Surface surface = ourHolder.getSurface();

        boolean surfValid = surface.isValid();

        if (surfValid) { // ITS FALSE ALWAYS why?????????????
            // Lock the canvas ready to draw


            // Choose the brush color for drawing

            // Now draw the Game Object
            canvas.drawBitmap(bitmap, x, y, paint);


            // Draw everything to the screen
        }
    }

    public void drawGameObject(String text, int x, int y) {


        paint.setTextSize(40);

        canvas.drawText(text, x, y, paint);


    }


    // If SpaceInvadersActivity is paused/stopped
    // shutdown our thread.
//    public void pause() {
//        playing = false;
//        try {
//            gameThread.join();
//        } catch (InterruptedException e) {
//            Log.e("Error:", "joining thread");
//        }
//
//    }

//    // If SpaceInvadersActivity is started then
//    // start our thread.
//    public void resume() {
//        playing = true;
//        gameThread = new Thread(this);
//        gameThread.start();
//    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.

    @Override
    public boolean onHoverEvent(MotionEvent motionEvent) {

        return true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent motionEvent) {
//
//        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
//
//            // Player has touched the screen
//            case MotionEvent.ACTION_DOWN:
//
//                /**
//                 * Checks if the touch event happened in the upper half of the screen
//                 */
//                if(motionEvent.getY() > screenY - screenY / 8) {
//
//                    if (motionEvent.getX() > screenX / 2) {
//                        controller.notifyMovement(2);
//                    } else {
//                        controller.notifyMovement(1);
//
//
//                    }
//
//                }else {
//                    // Shots fired
//                   controller.notifyShoot();
//                }
//                break;
//
//            // Player has removed finger from screen
//            case MotionEvent.ACTION_UP:
//
//                controller.notifyMovement(0);
//
//                break;
//
//        }
//
//        return true;
//    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    public void lockCanvas() {

        try {
            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
            } else {
                System.out.println(ourHolder.getSurface().isValid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void initPaintGameObject() {
        paint.setColor(Color.argb(255, 249, 129, 0));
    }


    public void unlockCanvas() {
        ourHolder.unlockCanvasAndPost(canvas);
    }


    public void moveship(int i) {

        this.controller.moveShip(i);
    }

    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.nanoTime();
///////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (!paused) {
                if (!controller.updateEntities(fps)) {

                    Intent i = new Intent(context.getApplicationContext(), HighScoreActivity.class); //Intenta acceder al highscore si se ha perdido
                    i.putExtra("score", 100);
                    context.startActivity(i);
                    //controller.initGame(this.context);
                }
                controller.updateGame();
                controller.removeBullets();
            }


            //ToDo show start again button if updateEntities returns false


            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            // We will do something new here towards the end of the project
            // Play a sound based on the menace level
//            if(!paused) {
//                if ((startFrameTime - lastMenaceTime) > menaceInterval) {
//                    if (uhOrOh) {
//                        // Play Uh
//                        soundPool.play(uhID, 1, 1, 0, 0, 1);
//
//                    } else {
//                        // Play Oh
//                        soundPool.play(ohID, 1, 1, 0, 0, 1);
//                    }
//
//                    // Reset the last menace time
//                    lastMenaceTime = System.currentTimeMillis();
//                    // Alter value of uhOrOh
//                    uhOrOh = !uhOrOh;
//                }
//            }
//            // Reset the menace level
//            menaceInterval = 1000;


        }
    }

    public void changeColor() {
        //ToDo Add random paint generation to be called when a bullet impacts a screen limit
    }

    public void unpause() {
        this.paused = false;
    }

    public void setShip(int i) {
        //this.controller.setShip(i);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.equals(this)) {
            if (event.getAction() != event.ACTION_UP) {
                //drawJoystick(event.getX(), event.getY());
            } else {
                //drawJoystick(joystick.getX(),joystick.getY());
            }
        }
        return true;
    }

    public void drawJoystick(float x, float y) {
        //canvas=ourHolder.lockCanvas();
        canvas.drawCircle(x, y, joystick.getBaseRadius(), joystick.getBaseColor());
        canvas.drawCircle(joystick.getX(), joystick.getY(), joystick.getHatRadius(), joystick.getHatColor());
        //ourHolder.unlockCanvasAndPost(canvas);
    }

    public void drawJoystick() {
        canvas.drawCircle(joystick.getX(), joystick.getY(), joystick.getHatRadius(), joystick.getHatColor());
        canvas.drawCircle(joystick.getX(), joystick.getY(), joystick.getBaseRadius(), joystick.getBaseColor());
    }
}