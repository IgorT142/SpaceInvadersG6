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
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.MediaStore;
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
import java.util.Timer;
import java.util.TimerTask;

import es.urjc.jjve.spaceinvaders.PlayerNameActivity;
import es.urjc.jjve.spaceinvaders.R;
import es.urjc.jjve.spaceinvaders.controllers.ScoreManager;
import es.urjc.jjve.spaceinvaders.controllers.ViewController;
import es.urjc.jjve.spaceinvaders.entities.Bullet;
import es.urjc.jjve.spaceinvaders.entities.DefenceBrick;
import es.urjc.jjve.spaceinvaders.entities.Invader;
import es.urjc.jjve.spaceinvaders.entities.Joystick;
import es.urjc.jjve.spaceinvaders.entities.PlayerShip;

/**
 * Clase utilizada para mostrar la interfaz del juego y manejar eventos dentro del juego, movimiento y disparo
 */
public class SpaceInvadersView extends SurfaceView implements Runnable {

    Context context;

    private Thread gameThread = null;
    private boolean paused = true;
    private volatile boolean playing;
    private long timeThisFrame;
    private long fps = 20;

    // Our SurfaceHolder to lock the surface before we draw our graphics
    private SurfaceHolder ourHolder;

    // Draw variables
    private Canvas canvas;
    private Paint paint;

    // The size of the screen in pixels
    private int screenX;
    private int screenY;

    private Joystick joystick;  //Joystick gameObjetct that we will draw later
    private Rect BotonDisparo;

    private ViewController controller;

    private final static int SPECIAL_TIMER = 550;
    int currentTime =0;


    /*
     * CONTRUCTOR
     */
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

    /*
     * THREAD MANAGEMENT
     */
    @Override
    public void run() {
        while (playing) {
            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.nanoTime();

            if (!paused) {
                if (!controller.updateEntities(fps)) {
                    //Intenta acceder al highscore si se ha perdido
                    Intent i = new Intent(context.getApplicationContext(), PlayerNameActivity.class);
                    i.putExtra("score", controller.getScore());
                    context.startActivity(i);
                }
                if(currentTime>SPECIAL_TIMER){
                    currentTime=0;
                    this.controller.specialInvader(context);
                }
                controller.updateGame();
                controller.removeBullets();
            }

            // Calculate the fps this frame.
            // We can then use the result to time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
            currentTime += 1;
        }
    }

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

    public void unpause() {
        this.paused = false;
    }
    /*
     * EVENT MANAGEMENT
     */
    @Override
    public boolean onTouchEvent( MotionEvent event) {
            if(BotonDisparo.contains((int) event.getX(),(int) event.getY())){   //Comprueba el jugador pulsa el boton de disparo
                controller.notifyShoot();
            }
            if (event.getAction() != event.ACTION_UP) {
                if(event.getX()<screenX/2) {
                    this.joystick.setHat(event.getX(),event.getY());
                    this.controller.shipMovement(event.getX()-joystick.getX(),event.getY()-joystick.getY());

                }else{
                    this.joystick.initHat();
                }
            } else {
                this.joystick.initHat();
                this.controller.shipMovement(0,0);
            }
        return true;
    }

    /*
     * DRAW MANAGEMENT
     */

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

    public void unlockCanvas() {
        ourHolder.unlockCanvasAndPost(canvas);
    }

    public void initPaintGameObject() {
        paint.setColor(Color.argb(255, 249, 129, 0));
    }

    public void changeColor() {
        //ToDo Add random paint generation to be called when a bullet impacts a screen limit

    }

    public void drawJoystick() {
        canvas.drawCircle(joystick.getHatX(), joystick.getHatY(), joystick.getHatRadius(), joystick.getHatColor());
        canvas.drawCircle(joystick.getX(), joystick.getY(), joystick.getBaseRadius(), joystick.getBaseColor());
    }

    public void drawBackground() {
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

        if (surfValid) {
            canvas.drawBitmap(bitmap, x, y, paint);
        }
    }

    public void drawGameObject(String text, int x, int y) {
        paint.setTextSize(40);
        canvas.drawText(text, x, y, paint);

    }


    public void drawButton(){       //Crea un rect para el boton de disparo y lo pone como atributo de la clase para las comprobaciones.
        Rect rectangle;
        Paint color;
        rectangle = new Rect(getWidth()-110,getHeight()-150,getWidth()-10,getHeight()-90);  //Rectangulo por coordenadas
        color = new Paint();
        color.setARGB(120,102,102,102);
        Paint colorTexto = new Paint();
        colorTexto.setTextSize(15);
        colorTexto.setARGB(150,255,255,255);
        canvas.drawRect(rectangle,color);
        canvas.drawText("O",getWidth()-60,getHeight()-120,colorTexto);
        this.BotonDisparo = rectangle;
    }
    int songCount = R.raw.doom;
    public void iniciarMusica(final Activity activityContext){  //Metodo para iniciar la música del juego y cambiarla cada 20 segundos
        //Declare the timer
        Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {
                                  @Override
                                  public void run() {
                                      final int i = songCount++;
                                      MediaPlayer mediaPlayer = MediaPlayer.create(activityContext.getApplicationContext(), i);
                                      mediaPlayer.start();
                                      mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                          @Override
                                          public void onCompletion(MediaPlayer mediaPlayer) {
                                              mediaPlayer.release();
                                          }
                                      });
                                  }
                              },
                500,
                21000);
    }
}