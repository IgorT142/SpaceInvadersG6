package es.urjc.jjve.spaceinvaders;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.textclassifier.TextLinks;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import es.urjc.jjve.spaceinvaders.controllers.Score;
import es.urjc.jjve.spaceinvaders.controllers.ScoreManager;

public class PlayerNameActivity extends AppCompatActivity implements OnClickListener {

    private int score;
    private Bitmap image;
    private String nombre;
    private boolean photo = false;

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
        if (v.getId() == findViewById(R.id.buttonAccept).getId()) {

            //Seleccionamos la caja de texto y asignamos su valor a una variable
            TextInputEditText aceptar = (TextInputEditText) findViewById(R.id.editText);
            this.nombre = aceptar.getText().toString();


            takePicture(); //Toma una foto para almacenarla en el fichero de highScores

        }
    }

    //Llama al activity para hacer fotos, el resultado se recoge en onActivityResult
    public void takePicture() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            setResult(RESULT_OK, takePicture);
            startActivityForResult(takePicture, 1);
        }
    }

    //Es el resultado de tomar la foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //Uri uri = (Uri) extras.get("data");
            this.image = imageBitmap;


            photo = true;

            Intent i = new Intent(getApplicationContext(), HighScoreActivity.class);
            System.out.println("Holi");
            i.putExtra("score", score);
            i.putExtra("nombre",nombre);
            i.putExtra("uri",getImageUri(this.getApplicationContext(),image));
            startActivity(i);

            //ToDo Reescalar el bitmap segun la pantalla
            //ToDo Guardar referencia de la imagen en el fichero de puntos -> galleryAddPic();
            //ToDo Mostrar el bitmap en la pantalla de puntuaciones
        }
    }

    //Genera un URI a partir de un bitmap
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "titulo", null);
        return Uri.parse(path);
    }
}
