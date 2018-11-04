package es.urjc.jjve.spaceinvaders.controllers;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import es.urjc.jjve.spaceinvaders.R;

public class ScoreManager {

    private final String FILE_PATH = "puntuaciones.txt";
    private final int MAX_SCORES = 10;

    private FileWriter fw;
    private PrintWriter pw;
    private BufferedReader br;

    private int[] scores;
    private Context context;

    public ScoreManager(Context context) {
        this.context = context;
    }

    public void saveScore(int score) {

        int[] puntFichero = readFile();
        int[] puntConActual = new int[MAX_SCORES + 1];

        // Rellena el array de puntuaciones temporal con el obtenido en el fichero
        for (int i = 0; i < puntFichero.length; i++) {
            puntConActual[i] = puntFichero[i];
        }

        puntConActual[puntFichero.length] = score; //Añade la nueva puntuación al final del fichero.

        //ordena la puntuación incluyendo la nueva
        int temp = 0;
        for (int i = 0; i < puntConActual.length; i++) {
            for (int j = 1; j < (puntConActual.length - i); j++) {
                if (puntConActual[j - 1] < puntConActual[j]) {
                    temp = puntConActual[j - 1];
                    puntConActual[j - 1] = puntConActual[j];
                    puntConActual[j] = temp;
                }
            }
        }

        //Añade la puntuación recién ordenada al array que se guardará, eliminando la última posición
        for (int i = 0; i < puntFichero.length; i++) {
            puntFichero[i] = puntConActual[i];
        }

        scores = puntFichero;
        saveFile(scores);
    }

    public int[] getScores() {
        return readFile();
    }


    private void saveFile(int[] scores) {
        try {

            File archivo = new File(Environment.getExternalStorageDirectory(),"puntuaciones.txt");
            if(!archivo.exists()){
                archivo.createNewFile();
            }

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(archivo));
            BufferedWriter out = new BufferedWriter(outputStreamWriter);

            String puntos = "";
            for (int i = 0; i < MAX_SCORES; i++) {
                puntos += scores[i] + "\n";
            }

            out.write(puntos);
            out.newLine();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[] readFile() {

        int[] puntuaciones = null;

        try {
            File archivo = new File(Environment.getExternalStorageDirectory(),"puntuaciones.txt");
            if(!archivo.exists()){
                archivo.createNewFile();
            }

            InputStreamReader inputStream = new InputStreamReader(new FileInputStream(archivo));
            br = new BufferedReader(inputStream);

            List<String> listaPuntuaciones = new LinkedList<>();
            String linea = null;

            while ((linea = br.readLine()) != null) {
                listaPuntuaciones.add(linea);
            }


            puntuaciones = new int[MAX_SCORES];
            Iterator<String> it = listaPuntuaciones.iterator();

            int i = 0;
            while (it.hasNext()) {
                puntuaciones[i] = Integer.parseInt(it.next());
                i++;
            }

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return puntuaciones;
        }
    }


}
