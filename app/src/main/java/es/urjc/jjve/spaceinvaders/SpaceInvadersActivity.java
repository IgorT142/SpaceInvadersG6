package es.urjc.jjve.spaceinvaders;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.util.Objects;

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
    SpaceInvadersView spaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtener un objeto de Display para acceder a los detalles de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Cargar la resolución a un objeto de Point
        Point size = new Point();
        display.getSize(size);

        //Inicializar gameView y lo establece como la visualización
        spaceView = new SpaceInvadersView(this, size.x, size.y, Objects.requireNonNull(getIntent().getExtras()).getBoolean("underage"));
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

        spaceView.iniciarMusica();
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