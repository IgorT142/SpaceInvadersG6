package es.urjc.jjve.spaceinvaders.view;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.TimerTask;

import es.urjc.jjve.spaceinvaders.R;

public class Temporizador extends TimerTask {

    private int contador;
    private MediaPlayer mediaPlayer;
    private Context context;


    Temporizador(int contador, MediaPlayer mediaPlayer, Context context) {
        this.contador = contador;
        this.mediaPlayer = mediaPlayer;
        this.context = context;

    }

    @Override
    public void run() {
        int cancion;
        contador = contador++;
        if(contador==0) {
            cancion = R.raw.doom;
            contador++;
        }else if(contador==1){
            cancion = R.raw.idontfeelsogoodmrstark;
            contador++;
        }else if(contador==2){
            cancion = R.raw.mktheme;
            contador++;
        }else if(contador==3){
            cancion = R.raw.zgotg;
            contador++;
        }else if(contador==4){
            cancion = R.raw.swduel;
            contador++;
        }else {
            cancion = R.raw.tauhubballad;
            contador = 0;
        }
        mediaPlayer = MediaPlayer.create(context.getApplicationContext(), cancion);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }
}
