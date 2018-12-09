package es.urjc.jjve.spaceinvaders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class Inicio extends AppCompatActivity implements OnClickListener {

    MediaPlayer media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View si = findViewById(R.id.si);
        View no = findViewById(R.id.no);
        si.setOnClickListener(this);
        no.setOnClickListener(this);
        /*
            OPCIONAL
        */
        //Para la música de introducción
        media = new MediaPlayer();
        MediaPlayer.create(getApplicationContext(), R.raw.zgotg);
        media.start();
        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer media) {
                media.release();
            }
        });


        // Esto permite permisos de escritura de la app
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        //Esto permite permisos del uso de la cámara de la app
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == findViewById(R.id.si).getId()) {
            Intent i = new Intent(getApplicationContext(), SpaceInvadersActivity.class);
            i.putExtra("underage", false);
            startActivity(i);
            media.stop();
            //finish();
        }
        if (v.getId() == findViewById(R.id.no).getId()) {
            Intent i = new Intent(getApplicationContext(), SpaceInvadersActivity.class);
            i.putExtra("underage", true);
            startActivity(i);
            //finish();
        }
    }
}
