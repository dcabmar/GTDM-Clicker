package es.upv.etsit.aatt.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    /** Clase enumerada de tipos de operacion general */
    enum OPERACION { SUMAR, RESTAR, MULTIPLICAR, DIVIDIR, IGUAL}
    /** Operacion anterior pendiente de realizar */
    OPERACION OP_ANT;

    /** Clase enumerada de estados de la calculadora */
    enum ESTADOS {INIC, NUMERO, OPERACION}
    /** Estado en el que se está */
    ESTADOS estado;

    /** Acumulador de operaciones: operando1 */
    double acumulador;

    /** operando2 de las operaciones en format String para escribir en pantalla y double para hacer operaciones */
    String operando2_str;
    double operando2;

    /** TextView para escribir resultados y operandos en pantalla */
    TextView display;

    /** Para fijar formato en salida de pantalla */
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enlazamos el TextView en variable display para pantalla de calculadora
        display = (TextView)findViewById(R.id.display);

        // Formato de salida de los resulados
        df = new DecimalFormat("#.#############");

        // Inicialización de calculadora
        aINIC();
    }

    /**
     * Inicialización
     */
    private void aINIC() {
        operando2_str   = "";     // String de formación de operando2 vacío
        display.setText("0.");    // Visualización en TextView display
        operando2 = 0.;           // operando2 double
        acumulador = 0.;          // valor inicial del acumulador
        OP_ANT = OPERACION.IGUAL; // operacion_anterior pendiente: igual
        estado = ESTADOS.INIC;    // estamos en el estado INIC.
    }

    /**
     * Método de call-back ejecutado cuando se clica alguno de los TextView en los que se
     * ha programado el atributo android:onClick con alPulsarTecla.
     *
     * A partir del TextView tecla se extrae su id y se compara con todos los posibles id mediante la
     * clase R. Las comparaciones se hacen en una estructura switch, agrupándose los distintos cases
     * según funcionalidad común
     *
     * @param tecla TextView que ha sido clicado
     */
    public void alPulsarTecla(View tecla){

        int id = tecla.getId(); // id de la tecla pulsada

        // Comparación del id de la tecla pulsada con los id de dígitos decimales, punto decimal, operaciones, ...
        switch(id) {

            case R.id.b_punto:
            case R.id.b0:
            case R.id.b1:
            case R.id.b2:
            case R.id.b3:
            case R.id.b4:
            case R.id.b5:
            case R.id.b6:
            case R.id.b7:
            case R.id.b8:
            case R.id.b9:
                operando2_str += ((TextView)tecla).getText().toString();  // acumulación de digito o punto
                operando2 = miStringToDouble( operando2_str ); // conversión de String a double
                display.setText(operando2_str); // Visualización

                estado = ESTADOS.NUMERO; // estamos en estado NUMERO
                break;

            case R.id.b_mas:
            case R.id.b_menos:
            case R.id.b_por:
            case R.id.b_div:
            case R.id.b_igual:

                acumulador = operar(acumulador, OP_ANT, operando2); // opera acumulador y operando2 según la operacion anterior (OP_ANT) pendiente
                display.setText( miDoubleToString(acumulador) ); // visualiza el resultado (acumulador)
                // guarda operacion actual en la operacion anterior (OP_ANT)
                switch(id) {
                    case R.id.b_mas:   OP_ANT = OPERACION.SUMAR;       break;
                    case R.id.b_menos: OP_ANT = OPERACION.RESTAR;      break;
                    case R.id.b_por:   OP_ANT = OPERACION.MULTIPLICAR; break;
                    case R.id.b_div:   OP_ANT = OPERACION.DIVIDIR;     break;
                    case R.id.b_igual: OP_ANT = OPERACION.IGUAL;       break;
                }
                // tras acabar operación, se reinicializa operando2
                operando2_str = "";
                operando2 = 0.;

                estado = ESTADOS.OPERACION; // estamos en estado OPERACION
                break;

        }

    }

    /**
     * Devuelve el resultado de la operación entre dos operandos
     *
     * @param operando1 el primer operando double
     * @param operacion operación como valor enumerado: OPERACION.SUMAR, OPERACION.RESTAR, ...
     * @param operando2 el segundo operando double
     * @return operando1 OPERACION operando2
     *
     */
    private double operar (double operando1, OPERACION operacion, double operando2) {
        switch (operacion) {
            case SUMAR:       return operando1 + operando2;
            case RESTAR:      return operando1 - operando2;
            case MULTIPLICAR: return operando1 * operando2;
            case DIVIDIR:     return operando1 / operando2;
            case IGUAL:       return ( (operando2_str.equals(""))? operando1 : operando2 ); // Si operando2_str=="",
                                                                                            // no existe todavía operando2
        }
        return 0.; // indiferente
    }

    /**
     * Convierte a double un String
     *
     * @param numero_string String que representa a un número
     * @return un double desde un String
     */
    private double miStringToDouble(String numero_string) {
        return Double.parseDouble(numero_string);
    }

    /**
     * Devuelve un String con formato desde un double
     *
     * @param numero_double double a convertir a String
     * @return String convertido del parámetro
     */
    private String miDoubleToString(double numero_double) {
        return (df.format(numero_double));
    }

}