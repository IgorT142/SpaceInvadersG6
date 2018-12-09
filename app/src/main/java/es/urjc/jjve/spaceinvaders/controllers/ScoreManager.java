package es.urjc.jjve.spaceinvaders.controllers;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

public class ScoreManager {

    private final String FILE_PATH = "puntuaciones.src";

    public ScoreManager() {

    }

    //Método público que permite guardar las puntuaciones en una colección ordenada
    public void saveScore(Score punt) {
        TreeSet<Score> puntFichero = readFile();
        puntFichero.add(punt);
        saveFile(puntFichero);
    }

    @SuppressWarnings("WeakerAccess")
    public TreeSet<Score> getScores() {
        return readFile();
    }


    //Método para almacenar las puntuaciones en un archivo
    private void saveFile(TreeSet<Score> scores) {
        try {
            //Se busca el archivo en el almacenamiento externo del dispositivo
            File archivo = new File(Environment.getExternalStorageDirectory(), FILE_PATH);
            if (!archivo.exists()) {  //Si el archivo no existe se genera de cero
                boolean o = archivo.createNewFile();
                if(!o) Log.d("file","File cannot be created.");
            }

            //Se inicializan los buffers para escribir el archivo
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(archivo));
            BufferedWriter out = new BufferedWriter(outputStreamWriter);
            PrintWriter writer = new PrintWriter(out);

            //Se escriben todos las puntuaciones siguiendo el orden 'Nombre:Puntuación:URIimagen'
            for (Score score : scores) {
                writer.println(score.getName() + "|" + score.getScore() + "|" + score.getUri());
            }

            //Se cierran los buffers
            writer.close();
            out.close();
            outputStreamWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Lee los archivos de puntuaciones y los pasa a una colección de TreeMap
    private TreeSet<Score> readFile() {

        //Se inicializa el TreeMap
        TreeSet<Score> puntuaciones = new TreeSet<>(Collections.reverseOrder());
        try {
            //Busca el archivo en el almacenamiento externo del dispositivo
            File archivo = new File(Environment.getExternalStorageDirectory(), FILE_PATH);
            if (!archivo.exists()) {      //Si el archivo no existe se genera de cero
                boolean o = archivo.createNewFile();
                if(!o) Log.d("file","File cannot be created.");
            }

            //Prepara los buffers para leer el archivo
            InputStreamReader inputStream = new InputStreamReader(new FileInputStream(archivo));
            BufferedReader br = new BufferedReader(inputStream);

            //Lee el archivo línea a línea y las divide en dos teniendo en cuenta el carácter ":".
            // Después guarda la puntuación y el nombre en el TreeMap
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] cadena = linea.split("\\|");
                puntuaciones.add(new Score(cadena[0], Integer.parseInt(cadena[1]), Uri.parse(cadena[2])));
            }
            //Se cierran los buffers
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Por último, se devuelve el TreeMap con las puntuaciones
        return puntuaciones;

    }

    public ArrayList<Score> getList() {
        return new ArrayList<>(getScores());
    }
}
