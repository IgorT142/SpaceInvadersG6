package es.urjc.jjve.spaceinvaders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

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
