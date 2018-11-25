package es.urjc.jjve.spaceinvaders;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
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

import java.io.File;

import es.urjc.jjve.spaceinvaders.R;
import es.urjc.jjve.spaceinvaders.SpaceInvadersActivity;
import es.urjc.jjve.spaceinvaders.controllers.ScoreManager;

public class HighScoreActivity extends AppCompatActivity implements OnClickListener  {

    private int score;
    private String fileScores;
    private Bitmap image;

    @Override   //Este método se carga el primero en cuanto se llama a la activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        takePicture();

        //Se inicializan los elementos de la pantalla
        View exit = findViewById(R.id.quitButton);
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
    }

    @Override   //Permite agregar funcionalidad de click a los objeto que tenga agregados
    public void onClick(View v) {

        //Botón de salir de la aplicación (quizás haya que cambiar de salir de la aplicación a volver a la pantalla de título)
        if(v.getId()== findViewById(R.id.quitButton).getId()) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
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

    public void takePicture(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePicture,1);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            this.image = imageBitmap;
            ImageView foto = findViewById(R.id.fotoLastGame);
            foto.setImageBitmap(this.image);

            //ToDo Reescalar el bitmap segun la pantalla
            //ToDo Guardar referencia de la imagen en el fichero de puntos -> galleryAddPic();
            //ToDo Mostrar el bitmap en la pantalla de puntuaciones
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        //File f = new File(mCurrentPhotoPath);//ToDo conocer el path the la foto
        //Uri contentUri = Uri.fromFile(f);
        //mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
