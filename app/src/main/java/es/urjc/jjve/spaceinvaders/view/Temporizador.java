package es.urjc.jjve.spaceinvaders.view;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.TimerTask;

import es.urjc.jjve.spaceinvaders.R;

public class Temporizador extends TimerTask {

    private int contador;
    private MediaPlayer mediaPlayer;
    private Context context;

    private int doom = R.raw.doom;
    private int spider = R.raw.idontfeelsogoodmrstark;
    private int mk = R.raw.mktheme;
    private int zgotg = R.raw.zgotg;
    private int swduel = R.raw.swduel;
    private int tau = R.raw.tauhubballad;

    public Temporizador(int contador, MediaPlayer mediaPlayer, Context context) {
        this.contador = contador;
        this.mediaPlayer = mediaPlayer;
        this.context = context;

    }

    @Override
    public void run() {
        int cancion = 0;
        contador = contador++;
        if(contador==0) {
            cancion = doom;
            contador++;
        }else if(contador==1){
            cancion = spider;
            contador++;
        }else if(contador==2){
            cancion = mk;
            contador++;
        }else if(contador==3){
            cancion = zgotg;
            contador++;
        }else if(contador==4){
            cancion = swduel;
            contador++;
        }else {
            cancion = tau;
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
