package es.urjc.jjve.spaceinvaders;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import java.io.File;
import java.util.TreeSet;

import es.urjc.jjve.spaceinvaders.R;
import es.urjc.jjve.spaceinvaders.SpaceInvadersActivity;
import es.urjc.jjve.spaceinvaders.controllers.Score;
import es.urjc.jjve.spaceinvaders.controllers.ScoreManager;
import es.urjc.jjve.spaceinvaders.controllers.ViewController;

public class HighScoreActivity extends AppCompatActivity implements OnClickListener  {

    private int score;
    private String fileScores;
    private Bitmap image;
    private Uri playerImageUri;

    @Override   //Este método se carga el primero en cuanto se llama a la activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        //Se inicializan los elementos de la pantalla
        View exit = findViewById(R.id.quitButton);
        View reiniciar = findViewById(R.id.reiniciar);
        reiniciar.setVisibility(View.INVISIBLE);

        TextView highScoreField = findViewById(R.id.highScore);
        TextView yourScore = findViewById(R.id.yourScore);

        //Se obtienen las puntuaciones
        score = getIntent().getExtras().getInt("score");
        if (score>=500){
            reiniciar.setVisibility(View.VISIBLE);
        }
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
            Intent inicio = new Intent(getApplicationContext(),Inicio.class);
            startActivity(inicio);
            this.finishActivity(0);
        }
        //Boton para reiniciar el juego
        if(v.getId()== findViewById(R.id.reiniciar).getId()){
            Intent juegoNuevo = new Intent(getApplicationContext(),SpaceInvadersActivity.class);
            juegoNuevo.putExtra("underage",false); //TODO investigar esto
            startActivity(juegoNuevo);
            this.finishActivity(0);
        }
    }

    //Carga las puntuaciones almacenadas en el fichero
    public String cargarScores() {
        ScoreManager sm = new ScoreManager(this.getApplicationContext());
        TreeSet<Score> puntuaciones = sm.getScores();
        String scores = "";

        //Se genera las puntuaciones a mostrar "Nombre:Puntuación:URI"
        for(Score punts: puntuaciones){
            scores += punts.getName() + "-" + punts.getScore() + "\n";
        }

        return scores;
    }

    // Genera un bitmap a partir de un URI
    private Bitmap getBitmapFromUri(Uri contentUri) {
        String path = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            path = cursor.getString(columnIndex);
        }
        cursor.close();
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

}
