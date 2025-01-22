package es.upv.etsit.aatt.aemet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String cod_mun = "49148"; // Peleas de Abajo (Zamora)
        //cod_mun = "23039"; // Guarroman (Jaen)
        //cod_mun = "06077"; // Mal Cocinado (Badajoz)
        String url = "https://opendata.aemet.es...";

        ServiciosWebEncadenados servicioWeb = new ServiciosWebEncadenados(url);
        servicioWeb.start();

    }



    // LLeva a cabo dos peticiones de servicios web encadenadas
    class ServiciosWebEncadenados extends Thread {

        String url_inicial;

        // constructor
        ServiciosWebEncadenados(String url_inicial) {
            this.url_inicial = url_inicial;
        }

        // tarea a ejecutar en hilo paralelo e independiente
        @Override public void run(){

            // Gestiónese oportunamente las excepciones

            // Primera peticion
            String respuesta = API_REST(url_inicial);
            ...

            // Segunda peticion
            final String respuesta2 = API_REST(segunda_url);
            ...

            // Impresión de resultados en el hilo de la UI (User Interface thread): runOnUiThread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    printParametros(respuesta2);
                }
            });


        } // run


    } // ServiciosWebEncadenados




    // Imprime parámetros meteorológicos en pantalla: esto ya se ejecutará en la UI
    public void printParametros(String respuesta2) {
      ...

    }


    /** La peticion del argumento es recogida y devuelta por el método API_REST
     *  Método ya completado y supuestamente correcto
     *
     * @param uri String con la URL de la API
     * @return String con la respuesta (JSON)
     *
     *  */
    public String API_REST(String uri){

        StringBuffer response = null;

        try {
            URL url = new URL(uri);
            Log.d(TAG, "URL: " + uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Detalles de HTTP
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Codigo de respuesta: " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        // Presumiblemente, la codificación de la respuesta es ISO-8859-15
                        new InputStreamReader(conn.getInputStream() , "ISO-8859-15" ));
                String output;
                response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();
            } else {
                Log.d(TAG, "responseCode: " + responseCode);
                return null; // retorna null anticipadamente si hay algun problema

            }
        } catch(Exception e) { // Posibles excepciones: MalformedURLException, IOException y ProtocolException
            e.printStackTrace();
            Log.d(TAG, "Error conexión HTTP:" + e.toString());
            return null;
        }

        return new String(response); // de StringBuffer -response- pasamos a String

    } // API_REST


} // MainActivity