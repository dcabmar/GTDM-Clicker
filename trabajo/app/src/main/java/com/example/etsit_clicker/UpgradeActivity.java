package com.example.etsit_clicker;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.app.Activity;

public class UpgradeActivity extends AppCompatActivity {
    private Button botonVolver;
    private Button click1;
    private Button click2;
    private Button click3;
    private Button gen;
    private Button v;
    private Button T;
    private Button C;
    public  int extra=0;
    int sobras;
    public boolean c1=false;
    public boolean c2=false;
    public boolean c3=false;
    private boolean vol=false;
    private boolean GE=false;
    private boolean TFG=false;
    private boolean carrera=false;
    private TextView puntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        Bundle b = getIntent().getExtras();
        int creditos = b.getInt("puntos");
        c1=b.getBoolean("teoria");
        c2=b.getBoolean("practica");
        c3=b.getBoolean("proyectos");
        extra=b.getInt("Extra");
        vol=b.getBoolean("Vol");
        GE=b.getBoolean("GE");
        carrera=b.getBoolean("C");
        TFG=b.getBoolean("TFG");
        sobras=creditos;
        botonVolver = findViewById(R.id.boton_volver);
        click1 = findViewById(R.id.button);
        click2 = findViewById(R.id.button2);
        click3 = findViewById(R.id.button3);
        puntos = findViewById(R.id.points);
        gen = findViewById(R.id.button6);
        v=findViewById(R.id.button7);
        T=findViewById(R.id.button5);
        C=findViewById(R.id.button4);
        actualizarPuntos();
        click1.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                int c=1000;
                if(sobras>=c && c1==false){
                extra+=10;
                sobras-=c;
                actualizarPuntos();
                c1=true;
            }}
        });
        click2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c=4000;
                if(sobras>=c && c2==false){
                    extra+=20;
                    sobras-=c;
                    actualizarPuntos();
                    c2=true;}
            }
        });
        click3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c=12000;
                if(sobras>=c && c3==false){extra+=30;
                    sobras-=c;
                    actualizarPuntos();
                    c3=true;}
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c=10000;
                if (sobras>=c && vol==false) {
                    vol = true;
                    sobras-=c;
                    actualizarPuntos();
                }
            }
        });
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c=500;
                if (sobras>=c && GE==false) {
                    GE = true;
                    sobras-=c;
                    actualizarPuntos();
                }
            }
        });
        T.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c=40000;
                if (sobras>=c && TFG==false) {
                    TFG = true;
                    carrera=true;
                    sobras-=c;
                    actualizarPuntos();
                }
            }
        });
        C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carrera==true) {
                    setContentView(R.layout.win);
                }
            }
        });
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vuelve a la actividad principal
                Intent intentoRetorno = new Intent();
                Bundle b = new Bundle();
                b.putInt("sobra", sobras);
                b.putBoolean("teoria",c1);
                b.putBoolean("practicas",c2);
                b.putBoolean("proyectos",c3);
                b.putInt("Extra",extra);
                b.putBoolean("GE",GE);
                b.putBoolean("Vol",vol);
                b.putBoolean("C",carrera);
                b.putBoolean("TFG",TFG);
                intentoRetorno.putExtras(b);
                setResult( Activity.RESULT_OK, intentoRetorno );
                finish();
            }
        });
    }
    private void actualizarPuntos() {
        puntos.setText(String.valueOf(sobras)+" Creditos");
    }
}