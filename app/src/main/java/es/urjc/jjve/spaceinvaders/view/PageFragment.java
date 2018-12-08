package es.urjc.jjve.spaceinvaders.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import es.urjc.jjve.spaceinvaders.R;

public class PageFragment extends Fragment {

    private String name;
    private int score;
    private Bitmap bitmap;


    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final PageFragment newInstance(String message) {

        PageFragment f = new PageFragment();

        Bundle bdl = new Bundle(1);

        bdl.putString(EXTRA_MESSAGE, message);

        f.setArguments(bdl);

        return f;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String message = getArguments().getString(EXTRA_MESSAGE);

        View v = inflater.inflate(R.layout.fragment_page, container, false);

        TextView name = (TextView)v.findViewById(R.id.playerName);
        TextView score = (TextView)v.findViewById(R.id.playerScore);
        ImageView picture = (ImageView)v.findViewById(R.id.playerImage);

        name.setText(this.name);
        score.setText(String.valueOf(this.score));
        picture.setImageBitmap(this.bitmap);

        return v;
    }

    public void initFragment(String name, int score, Bitmap image){
        this.name=name;
        this.score=score;
        this.bitmap=image;
    }

}
