package es.urjc.jjve.spaceinvaders;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import es.urjc.jjve.spaceinvaders.controllers.ScoreManager;

public class PlayerNameActivity extends AppCompatActivity implements OnClickListener{

    private int score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre_jugador);
        score = getIntent().getExtras().getInt("score");
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== findViewById(R.id.si).getId()){
            Intent i = new Intent(getApplicationContext(),HighScoreActivity.class);
            i.putExtra("score",score);
            startActivity(i);

            ScoreManager sm = new ScoreManager(this.getApplicationContext());
            sm.saveScore(score);
        }
    }
}
