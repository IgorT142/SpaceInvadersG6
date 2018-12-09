package es.urjc.jjve.spaceinvaders.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    public static PageFragment newInstance(String message) {

        PageFragment f = new PageFragment();

        Bundle bdl = new Bundle(1);

        bdl.putString(EXTRA_MESSAGE, message);

        f.setArguments(bdl);

        return f;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //String message = getArguments().getString(EXTRA_MESSAGE);

        View v = inflater.inflate(R.layout.fragment_page, container, false);

        TextView name = v.findViewById(R.id.playerName);
        TextView score = v.findViewById(R.id.playerScore);
        ImageView picture = v.findViewById(R.id.playerImage);

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
