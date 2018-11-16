package es.urjc.jjve.spaceinvaders;

import android.app.Activity;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Space;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.Vector;

import es.urjc.jjve.spaceinvaders.controllers.ViewController;
import es.urjc.jjve.spaceinvaders.entities.PlayerShip;
import es.urjc.jjve.spaceinvaders.view.SpaceInvadersView;
// SpaceInvadersActivity es el punto de entrada al juego.
// Se va a encargar del ciclo de vida del juego al llamar
// los métodos de spaceInvadersView cuando sean solicitados por el OS.

/**
 * Clase dedicada a manejar eventos en la aplicación, tales como inicio, pausa y reanudar
 */
public class SpaceInvadersActivity extends Activity {

    // spaceInvadersView será la visualización del juego
    // También tendrá la lógica del juego → Lógica a través de controladores
    // y responderá a los toques a la pantalla (Event Handler)
    ViewController spaceInvadersController;
    SpaceInvadersView spaceView;
    private boolean underage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.swduel);
        mediaPlayer.start();
        // Obtener un objeto de Display para acceder a los detalles de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Cargar la resolución a un objeto de Point
        Point size = new Point();
        display.getSize(size);

        //Inicializar gameView y lo establece como la visualización
        spaceView = new SpaceInvadersView(this, size.x, size.y, getIntent().getExtras().getBoolean("underage"));
        setContentView(spaceView);
        this.spaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                spaceView.unpause();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                finish();
            }
        });
    }

    // Este método se ejecuta cuando el jugador empieza el juego
    @Override
    protected void onResume() {
        super.onResume();
        // Le dice al método de reanudar del gameView que se ejecute
        spaceView.resume();
    }

    // Este método se ejecuta cuando el jugador se sale del juego
    @Override
    protected void onPause() {
        super.onPause();

        // Le dice al método de pausa del gameView que se ejecute
        spaceView.pause();
    }

}