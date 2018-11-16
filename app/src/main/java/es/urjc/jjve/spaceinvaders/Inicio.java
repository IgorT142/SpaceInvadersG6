package es.urjc.jjve.spaceinvaders;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.IOException;

import es.urjc.jjve.spaceinvaders.R;

public class Inicio extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View si = findViewById(R.id.si);
        View no = findViewById(R.id.no);
        si.setOnClickListener(this);
        no.setOnClickListener(this);

        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.gotg);
        mediaPlayer.start();


        // Esto permite permisos de escritura de la app
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId()== findViewById(R.id.si).getId()){
            Intent i = new Intent(getApplicationContext(),SpaceInvadersActivity.class);
            i.putExtra("underage",false);
            startActivity(i);
            //finish();
        }
        if(v.getId()== findViewById(R.id.no).getId()){
            Intent i = new Intent(getApplicationContext(),SpaceInvadersActivity.class);
            i.putExtra("underage",true);
            startActivity(i);
            //finish();
        }
    }
}
