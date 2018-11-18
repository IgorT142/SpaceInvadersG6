package es.urjc.jjve.spaceinvaders.controllers;

import android.content.Context;
import android.content.Intent;
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
import java.util.Map;
import java.util.TreeMap;

import es.urjc.jjve.spaceinvaders.R;

public class ScoreManager {

    private final String FILE_PATH = "puntuaciones.src";
    private final int MAX_SCORES = 10;

    private BufferedReader br;
    private Context context;

    public ScoreManager(Context context) {
        this.context = context;
    }

    //Método público que permite guardar las puntuaciones en una colección ordenada
    public void saveScore(int score,String name) {
        TreeMap<Integer,String> puntFichero = readFile();
        puntFichero.put(score,name);
        saveFile(puntFichero);
    }

    public TreeMap<Integer, String> getScores() {
        return readFile();
    }


    //Método para almacenar las puntuaciones en un archivo
    private void saveFile(TreeMap<Integer,String> scores) {

        try {
            //Se busca el archivo en el almacenamiento externo del dispositivo
            File archivo = new File(Environment.getExternalStorageDirectory(),FILE_PATH);
            if(!archivo.exists()){  //Si el archivo no existe se genera de cero
                archivo.createNewFile();
            }

            //Se inicializan los buffers para escribir el archivo
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(archivo));
            BufferedWriter out = new BufferedWriter(outputStreamWriter);
            PrintWriter writer = new PrintWriter(out);

            //Se escriben todos las puntuaciones siguiendo el orden 'Puntuacion:Nombre'
            for(Map.Entry score:scores.entrySet()){
                writer.println(score.getKey() + ": " + score.getValue());
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
    private TreeMap<Integer,String> readFile() {

        //Se inicializa el TreeMap
        TreeMap<Integer,String> puntuaciones = new TreeMap<>();
        try {
            //Busca el archivo en el almacenamiento externo del dispositivo
            File archivo = new File(Environment.getExternalStorageDirectory(),FILE_PATH);
            if(!archivo.exists()){      //Si el archivo no existe se genera de cero
                archivo.createNewFile();
            }

            //Prepara los buffers para leer el archivo
            InputStreamReader inputStream = new InputStreamReader(new FileInputStream(archivo));
            br = new BufferedReader(inputStream);

            //Lee el archivo línea a línea y las divide en dos teniendo en cuenta el carácter ":".
            // Después guarda la puntuación y el nombre en el TreeMap
            String linea = null;
            while ((linea = br.readLine()) != null) {
                String[] cadena = linea.split(":");
                puntuaciones.put(Integer.parseInt(cadena[0]),cadena[1]);
            }

            //Se cierran los buffers
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Por último, se devuelve el TreeMap con las puntuaciones
            return puntuaciones;
        }
    }


}
