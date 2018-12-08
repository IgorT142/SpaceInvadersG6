package es.urjc.jjve.spaceinvaders.view;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.util.Timer;

import es.urjc.jjve.spaceinvaders.PlayerNameActivity;
import es.urjc.jjve.spaceinvaders.R;
import es.urjc.jjve.spaceinvaders.controllers.ViewController;
import es.urjc.jjve.spaceinvaders.entities.Joystick;

/**
 * Clase utilizada para mostrar la interfaz del juego y manejar eventos dentro del juego, movimiento y disparo
 */
@SuppressLint("ViewConstructor")
public class SpaceInvadersView extends SurfaceView implements Runnable {

    Context context;

    private Thread gameThread = null;
    private boolean paused = true;
    private volatile boolean playing;
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
    int currentTime = 0;

    private MediaPlayer mediaPlayer;
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
                if (!controller.updateEntities(fps*2)) {  //Controla la velocidad del juego
                    //Intenta acceder al highscore si se ha perdido
                    Intent i = new Intent(context.getApplicationContext(), PlayerNameActivity.class);
                    i.putExtra("score", controller.getScore());
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    context.startActivity(i);
                }
                if (currentTime > SPECIAL_TIMER) {
                    currentTime = 0;
                    this.controller.specialInvader(context);
                }
                controller.updateGame();
                controller.removeBullets();
            }

            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
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
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (BotonDisparo.contains((int) event.getX(), (int) event.getY())) {   //Comprueba el jugador pulsa el boton de disparo
            controller.notifyShoot();
        }
        if (event.getAction() != MotionEvent.ACTION_UP) {
            if (event.getX() < screenX / 2) {
                this.joystick.setHat(event.getX(), event.getY());
                this.controller.shipMovement(event.getX() - joystick.getX(), event.getY() - joystick.getY());

            } else {
                this.joystick.initHat();
            }
        } else {
            this.joystick.initHat();
            this.controller.shipMovement(0, 0);
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

    //Crea un rect para el boton de disparo y lo pone como atributo de la clase para las comprobaciones.
    public void drawButton() {
        Rect rectangle;
        Paint color;
        rectangle = new Rect(getWidth() - 180, getHeight() - 250, getWidth() - 10, getHeight() - 90);  //Rectangulo por coordenadas
        color = new Paint();
        color.setARGB(120, 102, 102, 102);
        Paint colorTexto = new Paint();
        colorTexto.setTextSize(15);
        colorTexto.setARGB(150, 255, 255, 255);
        canvas.drawRect(rectangle, color);
        canvas.drawText("SHOOT", getWidth() - 125, getHeight() - 180, colorTexto);
        this.BotonDisparo = rectangle;
    }

    //Metodo para iniciar la música del juego y cambiarla cada 20 segundos
    public void iniciarMusica() {
        mediaPlayer = new MediaPlayer();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new Temporizador(0,mediaPlayer,context),500,21000);
    }
}