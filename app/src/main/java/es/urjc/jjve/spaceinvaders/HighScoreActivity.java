package es.urjc.jjve.spaceinvaders;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.urjc.jjve.spaceinvaders.controllers.Score;
import es.urjc.jjve.spaceinvaders.controllers.ScoreManager;
import es.urjc.jjve.spaceinvaders.view.CustomPagerAdapter;
import es.urjc.jjve.spaceinvaders.view.PageFragment;

public class HighScoreActivity extends AppCompatActivity {

    private BitmapFactory.Options options;
    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private List<Score> scores;

    private int score;
    private String nombre;
    private Uri playerImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        //Se obtienen las puntuaciones
        score = getIntent().getExtras().getInt("score");
        nombre = getIntent().getExtras().getString("nombre");
        playerImageUri = (Uri) getIntent().getExtras().get("uri");

        //Se guarda el resultado actual en el archivo y carga el archivo en la lista
        ScoreManager sm = new ScoreManager(this.getApplicationContext());
        sm.saveScore(new Score(nombre,score,playerImageUri,this.getApplicationContext()));
        scores = sm.getList();

        List<Fragment> fragments = getFragments();

        adapter = new CustomPagerAdapter(getSupportFragmentManager(), fragments);

        ViewPager pager = (ViewPager)findViewById(R.id.viewPager);

        pager.setAdapter(adapter);



    }

    private View.OnClickListener onClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0) {
                    //next page
                    if (viewPager.getCurrentItem() < viewPager.getAdapter().getCount() - 1) {
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
    }



    private View.OnClickListener onChagePageClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(i);
            }
        };
    }


    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<Fragment>();

        //ToDo For each score instantiate a new fragment, with the respective name score and picture

        int i=0;
        for(Score score:scores) {
            PageFragment newFragment = PageFragment.newInstance("Fragment: "+i);
            newFragment.initFragment(score.getName(), score.getScore(), score.getBitmap());
            fList.add(newFragment);
            i++;
        }
        fList.add(PageFragment.newInstance("Fragment 2"));
        fList.add(PageFragment.newInstance("Fragment 3"));
        return fList;

    }

}
