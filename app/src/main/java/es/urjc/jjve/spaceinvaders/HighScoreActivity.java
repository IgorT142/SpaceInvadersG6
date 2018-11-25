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

import es.urjc.jjve.spaceinvaders.R;
import es.urjc.jjve.spaceinvaders.SpaceInvadersActivity;
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

        takePicture(); //Toma una foto para almacenarla en el fichero de highScores
        this.playerImageUri = getImageUri(this.getApplicationContext(),image);

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
        TreeMap<Integer,String> puntuaciones = sm.getScores();
        String scores = "";
        for(Map.Entry punts: puntuaciones.entrySet()){
            scores += punts.getKey() + "-" + punts.getValue() + "\n";
        }

        return scores;
    }

    //Llama al activity para hacer fotos, el resultado se recoge en onActivityResult
    public void takePicture(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePicture,1);
        }
    }
    //Es el resultado de tomar la foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Uri uri = (Uri) extras.get("data");
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

    //Genera un URI a partir de un bitmap
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
