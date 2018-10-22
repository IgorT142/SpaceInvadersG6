package es.urjc.jjve.spaceinvaders;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
// SpaceInvadersActivity es el punto de entrada al juego.
// Se va a encargar del ciclo de vida del juego al llamar
// los métodos de spaceInvadersView cuando sean solicitados por el OS.

public class SpaceInvadersActivity extends Activity {

    // spaceInvadersView será la visualización del juego
    // También tendrá la lógica del juego
    // y responderá a los toques a la pantalla
    SpaceInvadersView spaceInvadersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener un objeto de Display para acceder a los detalles de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        // Cargar la resolución a un objeto de Point
        Point size = new Point();
        display.getSize(size);

        // Inicializar gameView y lo establece como la visualización
        spaceInvadersView = new SpaceInvadersView(this, size.x, size.y);
        setContentView(spaceInvadersView);

    }

    // Este método se ejecuta cuando el jugador empieza el juego
    @Override
    protected void onResume() {
        super.onResume();

        // Le dice al método de reanudar del gameView que se ejecute
        spaceInvadersView.resume();
    }

    // Este método se ejecuta cuando el jugador se sale del juego
    @Override
    protected void onPause() {
        super.onPause();

        // Le dice al método de pausa del gameView que se ejecute
        spaceInvadersView.pause();
    }
}