package es.urjc.jjve.spaceinvaders;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import es.urjc.jjve.spaceinvaders.controllers.ViewController;
import es.urjc.jjve.spaceinvaders.view.SpaceInvadersView;
// SpaceInvadersActivity es el punto de entrada al juego.
// Se va a encargar del ciclo de vida del juego al llamar
// los métodos de spaceInvadersView cuando sean solicitados por el OS.

/**
 * Clase dedicada a manejar eventos en la aplicación, tales como inicio, pausa y reanudar
 */
public class SpaceInvadersActivity extends Activity  {

    // spaceInvadersView será la visualización del juego
    // También tendrá la lógica del juego → Lógica a través de controladores
    // y responderá a los toques a la pantalla (Event Handler)
    ViewController spaceInvadersController;
    SpaceInvadersView spaceView;
    private boolean underage;

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
        spaceView = new SpaceInvadersView(this,size.x,size.y);
        spaceInvadersController = new ViewController(this, size.x, size.y,spaceView);
        spaceInvadersController.setUnderage(getIntent().getExtras().getBoolean("underage"));
        spaceView.setObserver(spaceInvadersController);


        setContentView(spaceView);


    }



    // Este método se ejecuta cuando el jugador empieza el juego
    @Override
    protected void onResume() {
        super.onResume();

        // Le dice al método de reanudar del gameView que se ejecute
        spaceInvadersController.resume();
    }

    // Este método se ejecuta cuando el jugador se sale del juego
    @Override
    protected void onPause() {
        super.onPause();

        // Le dice al método de pausa del gameView que se ejecute
        spaceInvadersController.pause();
    }

    public void bulletsOn(boolean on){
        this.underage = underage;
    }

    public void moveUp(View view){
        if(view instanceof SpaceInvadersView){
            this.spaceView.moveShip(3);
        }
    }

    public void moveLeft(View view){
        if(view instanceof SpaceInvadersView){
            this.spaceView.moveShip(1);
        }
    }

    public void moveRight(View view){
        if(view instanceof SpaceInvadersView){
            this.spaceView.moveship(2);
        }
    }

    public void moveDown(View view){
        if(view instanceof SpaceInvadersView){
            this.spaceView.moveship(3);
        }
    }


}