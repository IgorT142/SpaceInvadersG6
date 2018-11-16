package es.urjc.jjve.spaceinvaders;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import org.w3c.dom.Text;

import es.urjc.jjve.spaceinvaders.R;
import es.urjc.jjve.spaceinvaders.SpaceInvadersActivity;
import es.urjc.jjve.spaceinvaders.controllers.ScoreManager;
import es.urjc.jjve.spaceinvaders.controllers.ViewController;

public class HighScoreActivity extends AppCompatActivity implements OnClickListener  {

    private int score;
    private String fileScores;

    @Override   //Este método se carga el primero en cuanto se llama a la activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        //Se inicializan los elementos de la pantalla
        View exit = findViewById(R.id.quitButton);
        View reiniciar = findViewById(R.id.reiniciar);
        TextView highScoreField = findViewById(R.id.highScore);
        TextView yourScore = findViewById(R.id.yourScore);

        //Se obtienen las puntuaciones
        score = getIntent().getExtras().getInt("score");
        fileScores = cargarScores();

        //Se pintan las listas de puntuaciones
        yourScore.setText("Tu puntuación: " + score);
        highScoreField.setText(fileScores);

        //Se agregan el OnClickListener para que el botón funcione al pulsarlo
        exit.setOnClickListener(this);
        reiniciar.setOnClickListener(this);
    }

    @Override   //Permite agregar funcionalidad de click a los objeto que tenga agregados
    public void onClick(View v) {

        //Botón de salir de la aplicación (quizás haya que cambiar de salir de la aplicación a volver a la pantalla de título)
        if(v.getId()== findViewById(R.id.quitButton).getId()) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        //Boton para reiniciar el juego
        if(v.getId()== findViewById(R.id.reiniciar).getId()){
            Intent juegoNuevo = new Intent(getApplicationContext(),SpaceInvadersActivity.class);
            juegoNuevo.putExtra("underage",false);
            startActivity(juegoNuevo);

        }
    }

    public String cargarScores() {
        ScoreManager sm = new ScoreManager(this.getApplicationContext());
        int[] puntuaciones = sm.getScores();
        String scores = "";
        for(int i = 0; i < puntuaciones.length; i++){
            scores += puntuaciones[i] + "\n";
        }

        return scores;
    }
}
