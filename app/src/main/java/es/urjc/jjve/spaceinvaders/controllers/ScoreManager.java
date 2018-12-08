package es.urjc.jjve.spaceinvaders.controllers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

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
    public void saveScore(Score punt) {
        TreeSet<Score> puntFichero = readFile();
        puntFichero.add(punt);
        saveFile(puntFichero);
    }

    public TreeSet<Score> getScores() {
        return readFile();
    }


    //Método para almacenar las puntuaciones en un archivo
    private void saveFile(TreeSet<Score> scores) {
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

            //Se escriben todos las puntuaciones siguiendo el orden 'Nombre:Puntuación:URIimagen'
            for(Score score:scores){
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
                String[] cadena = linea.split("\\|");
                puntuaciones.add(new Score(cadena[0],Integer.parseInt(cadena[1]),Uri.parse(cadena[2])));
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

    public List<Score> getList(){
        return new LinkedList<>(getScores());
    }
}
