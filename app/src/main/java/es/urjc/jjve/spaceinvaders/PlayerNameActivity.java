package es.urjc.jjve.spaceinvaders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import java.util.Comparator;
import java.util.TreeMap<int,String>; //Valor la clave porque el comparator ordena por clave

public class PlayerNameActivity extends AppCompatActivity implements OnClickListener{

    public TreeMap namePunt = new TreeMap(Comparator.<int>naturalOrder()); //TreeMap ordenado por clave

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre_jugador);

        EditText nombre = findViewById(R.id.nombre);

        String namePlayer = "";
        namePlayer = String.valueOf(nombre.getText()); //Nombre introducido guardado en namePlayer

        namePunt.put(0, namePlayer); //el 0 debería ser la puntuación
    }

    @Override
    public void onClick(View v) {

    }
}
