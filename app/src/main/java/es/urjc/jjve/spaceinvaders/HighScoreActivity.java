package es.urjc.jjve.spaceinvaders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.urjc.jjve.spaceinvaders.controllers.Score;
import es.urjc.jjve.spaceinvaders.controllers.ScoreManager;
import es.urjc.jjve.spaceinvaders.view.CustomPagerAdapter;
import es.urjc.jjve.spaceinvaders.view.PageFragment;

public class HighScoreActivity extends AppCompatActivity implements OnClickListener {

    private List<Score> scores;


    @Override   //Este método se carga el primero en cuanto se llama a la activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        //Se inicializan los elementos de la pantalla
        View exit = findViewById(R.id.quitButton);
        View reiniciar = findViewById(R.id.reiniciar);
        reiniciar.setVisibility(View.INVISIBLE);


        //Se obtienen las puntuaciones
        int score = Objects.requireNonNull(getIntent().getExtras()).getInt("score");
        String nombre = getIntent().getExtras().getString("nombre");
        Uri playerImageUri = (Uri) getIntent().getExtras().get("uri");

        ScoreManager sm = new ScoreManager();
        sm.saveScore(new Score(nombre, score, playerImageUri));

        scores = sm.getList();

        List<Fragment> fragments = getFragments();
        FragmentPagerAdapter adapter = new CustomPagerAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = findViewById(R.id.viewPager);
        pager.setAdapter(adapter);


        if (score >= 500) {
            reiniciar.setVisibility(View.VISIBLE);
        }
        //fileScores = cargarScores();


        //Se agregan el OnClickListener para que el botón funcione al pulsarlo
        exit.setOnClickListener(this);
        reiniciar.setOnClickListener(this);
    }


    @Override   //Permite agregar funcionalidad de click a los objeto que tenga agregados
    public void onClick(View v) {

        //Botón de salir de la aplicación (quizás haya que cambiar de salir de la aplicación a volver a la pantalla de título)
        if (v.getId() == findViewById(R.id.quitButton).getId()) {
            Intent inicio = new Intent(getApplicationContext(), Inicio.class);
            startActivity(inicio);
            this.finishActivity(0);
        }
        //Boton para reiniciar el juego
        if (v.getId() == findViewById(R.id.reiniciar).getId()) {
            Intent juegoNuevo = new Intent(getApplicationContext(), SpaceInvadersActivity.class);
            juegoNuevo.putExtra("underage", false);
            startActivity(juegoNuevo);
            this.finishActivity(0);
        }
    }

    /*private View.OnClickListener onClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0) {
                    //next page
                    if (viewPager.getCurrentItem() < Objects.requireNonNull(viewPager.getAdapter()).getCount() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                } else {
                    //previous page
                    if (viewPager.getCurrentItem() > 0) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }
                }
            }
        };
    }*/


    /*
    private View.OnClickListener onChagePageClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(i);
            }
        };
    }*/
    /*
    //Carga las puntuaciones almacenadas en el fichero
    public String cargarScores() {
        ScoreManager sm = new ScoreManager();
        TreeSet<Score> puntuaciones = sm.getScores();
        String scores = "";

        //Se genera las puntuaciones a mostrar "Nombre:Puntuación:URI"
        for (Score punts : puntuaciones) {
            scores += punts.getName() + "-" + punts.getScore() + "\n";
        }

        return scores;
    }*/

    private List<Fragment> getFragments() {

        List<Fragment> fList = new ArrayList<>();

        int i = 0;
        for (Score score : scores) {
            PageFragment newFragment = PageFragment.newInstance("Fragment: " + i);
            Bitmap bit = getBitmapFromUri(score.getUri());
            newFragment.initFragment(score.getName(), score.getScore(), bit);
            fList.add(newFragment);
            i++;
        }
        return fList;

    }

    //Genera un URI como el método anterior pero maneja datos e imágenes mucho mayores
    public Bitmap getBitmapFromUri(Uri uri) {
        Bitmap bitmap = null;
        try {
            InputStream input = this.getContentResolver().openInputStream(uri);

            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            //noinspection deprecation
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            assert input != null;
            input.close();

            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
                return null;
            }

            int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

            double ratio = (originalSize > 100) ? (originalSize / 100) : 1.0;

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
            //noinspection deprecation
            bitmapOptions.inDither = true; //optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
            input = this.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);

            assert input != null;
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;


    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

}
