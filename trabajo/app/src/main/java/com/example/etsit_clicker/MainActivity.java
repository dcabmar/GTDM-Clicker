package com.example.etsit_clicker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private int contador=0;
    private int puntos=0;
    private int mejora=50;
    private Button botonMejora;
    private TextView textoContador;
    private TextView textoPuntos;

    private boolean teo=false;
    private boolean prac=false;
    private boolean proy=false;
    private boolean vol=false;
    private boolean GE=false;
    private boolean TFG=false;
    private boolean carrera=false;
    int COD_PETICION = 7;
    int extra=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        botonMejora = findViewById(R.id.boton_mejora);
        textoContador = findViewById(R.id.texto_contador);
        textoPuntos = findViewById(R.id.texto_puntos);
        Thread temporizadorThread = new Thread(() -> {

            while (true) {
                if(GE==true){

                try {
                    // Sumar 5 a la variable

                    puntos += 5;


                    // Esperar 5 segundos
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            }}
        });

        // Iniciar el hilo del temporizador
        temporizadorThread.start();

        actualizarContador();
        actualizarPuntos();

        botonMejora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Comprueba si hay suficientes puntos para comprar la mejora
                if (puntos >= calcularCostoMejora()) {
                    // Resta los puntos necesarios
                    puntos -= calcularCostoMejora();
                    // Incrementa la mejora en 1
                    contador++;
                    mejora=calcularCostoMejora();
                    // Actualiza el contador y los puntos
                    actualizarContador();

                    actualizarPuntos();
                }
            }
        });
        Button inversion = (Button) findViewById(R.id.boton_otra_actividad);
        inversion.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpgradeActivity.class);
                Bundle b = new Bundle();
                b.putInt("puntos",puntos);
                b.putBoolean("teoria",teo);
                b.putBoolean("practica",prac);
                b.putBoolean("proyectos",proy);
                b.putBoolean("Vol",vol);
                b.putBoolean("GE",GE);
                b.putBoolean("TFG",TFG);
                b.putInt("Extra",extra);
                b.putBoolean("C",carrera);

                intent.putExtras(b);
                startActivityForResult(intent,COD_PETICION);
            }
        });
    }
    @Override
    protected void onActivityResult(int codigoPeticion, int codigoResultado, Intent intentoVuelta) {
        if (codigoPeticion == COD_PETICION) {
            if (codigoResultado == Activity.RESULT_OK) {
                Bundle b = intentoVuelta.getExtras();
                puntos = b.getInt("sobra");
                teo=b.getBoolean("teoria");
                prac=b.getBoolean("practicas");
                proy=b.getBoolean("proyectos");
                extra=b.getInt("Extra");
                vol=b.getBoolean("Vol");
                GE=b.getBoolean("GE");
                carrera=b.getBoolean("C");
                TFG=b.getBoolean("TFG");
                actualizarPuntos();


            }
        }
    }

    // Calcula el costo de la mejora actual
    private int calcularCostoMejora() {
        return 50 * (int) Math.pow(2, contador);
    }

    // Actualiza el texto del contador
    private void actualizarContador() {
        textoContador.setText("Precio: "+String.valueOf(mejora));
    }

    // Actualiza el texto de los puntos
    private void actualizarPuntos() {
        textoPuntos.setText(String.valueOf(puntos)+" creditos");
    }

    // Método para el botón de clic para aumentar el contador
    public void botonClic(View view) {
        if (vol == true) {
            puntos += ((1 + contador * 3) + extra)*2; // Aumenta en 1 más 2 por cada 2 mejoras compradas

        } else {
            puntos += (1 + contador * 3) + extra; // Aumenta en 1 más 2 por cada 2 mejoras compradas

        }
        actualizarContador();
        actualizarPuntos();
    }



}