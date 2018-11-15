package es.urjc.jjve.spaceinvaders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class PlayerNameActivity extends AppCompatActivity implements OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre_jugador);
    }

    @Override
    public void onClick(View v) {

    }
}
