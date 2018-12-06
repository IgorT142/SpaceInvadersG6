package es.urjc.jjve.spaceinvaders;

import android.app.ActivityManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java.io.File;

import es.urjc.jjve.spaceinvaders.R;
import es.urjc.jjve.spaceinvaders.SpaceInvadersActivity;
import es.urjc.jjve.spaceinvaders.controllers.ScoreManager;
import es.urjc.jjve.spaceinvaders.controllers.ViewController;
import es.urjc.jjve.spaceinvaders.view.CustomPagerAdapter;
import es.urjc.jjve.spaceinvaders.view.PageFragment;
import es.urjc.jjve.spaceinvaders.view.ViewPagerAdapter;

public class HighScoreActivity extends AppCompatActivity {

    private BitmapFactory.Options options;
    private ViewPager viewPager;
    private FragmentStatePagerAdapter adapter;
    private final static int[] resourceIDs = new int[]{R.drawable.invader1, R.drawable.invader2,
            R.drawable.invader12, R.drawable.invader22, R.drawable.special_invader};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        List<PageFragment> scores = new ArrayList<>();

        //ToDo Añadir instancias de pageFragments con los scores leidos de fichero

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomPagerAdapter(this,scores));


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
}
