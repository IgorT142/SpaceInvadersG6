package es.urjc.jjve.spaceinvaders;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import es.urjc.jjve.spaceinvaders.controllers.ScoreManager;

public class PlayerNameActivity extends AppCompatActivity implements OnClickListener{

    private int score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre_jugador);
        score = getIntent().getExtras().getInt("score");

        View aceptar = findViewById(R.id.buttonAccept);     //Se busca cual es el botón de aceptar
        aceptar.setOnClickListener(this);                   //Se le añade el OnClickListener para poder pulsarlo.
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== findViewById(R.id.buttonAccept).getId()){

            //Seleccionamos la caja de texto y asignamos su valor a una variable
            TextInputEditText aceptar = (TextInputEditText) findViewById(R.id.editText);
            String nombre =  aceptar.getText().toString();

            //Se crea el archivo con el nombre y la nueva puntuación
            ScoreManager sm = new ScoreManager(this.getApplicationContext());
            sm.saveScore(score,nombre);

            //Se pasa al siguiente activity
            Intent i = new Intent(getApplicationContext(),HighScoreActivity.class);
            i.putExtra("score",score);
            startActivity(i);
        }
    }
}
