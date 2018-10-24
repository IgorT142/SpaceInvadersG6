package es.urjc.jjve.spaceinvaders;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
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

        /*
            OPCIONAL
         */
        //Para la música de introducción
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.tauhubballad);
        mediaPlayer.start();
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
