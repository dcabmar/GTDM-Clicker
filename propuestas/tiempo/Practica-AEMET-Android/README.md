



<!-- markdownlint-disable MD033 -->



<div style="background-color:white; color:black">

<h3 align="center">Aplicaciones y Usabilidad</h3>

<img src="./figuras/UPVcolor300.png" align="left" height="40">
<img src="./figuras/DCOM.png" align="right" height="40">

<img src="./figuras/Teleco.png"       align="left" style="clear:left; padding-top:10px" height="40">

<img src="./figuras/GTDM.png"       align="right" style="clear:right; padding-top: 10px" height="40">

<h1 align="center"><b>JSON y API web en Android: AEMET</b></h1>
<h3 align="center"><b>Práctica</b></h3>
<h2 align="center"><b></b></h2>

<h4 align="center"><b>À. Alcaraz Bellido</b><br>
<b>F. J. Martínez Zaldívar</b></h4>

<h3 align="center">Grado en Tecnología Digital y Multimedia</h3>
<h3 align="center">ETSIT-UPV</h3>






# 1. Introducción y objetivos

Los servicios web son utilizados hoy en día en gran cantidad de
aplicaciones. En la presente práctica nos planteamos como objetivos ser
capaces de, empleando el entorno de programación de Android, utilizar
estos servicios elaborando peticiones de tipo API REST, recibir los
resultados y extraer de los mismos la información en la que estemos
interesados.

Para ello, utilizaremos un ejemplo de entre los múltiples que podemos
encontrar en la red: el servicio OpenData de AEMET, la Agencia Estatal
de Meteorología. Pediremos la predicción meteorológica para ciertos
intervalos o instantes horarios de un municipio en concreto y
extraeremos ciertas propiedades de dicha predicción, escribiéndolas en
pantalla.

Esta práctica está basada en una anterior en al que se realizaba estos
mismos procesos pero en el entorno de Java ejecutado en un PC. Veremos
que aunque la mecánica es la misma, existen matices que van a
diferenciar sutilmente una práctica de otra.

Los denominadores comunes son:

-   Utilización de servicios web: creación de petición API-REST y
    recolección de resultados.

-   Empleo de certificados digitales

-   Manipulación de objetos y arrays JSON

Respecto a las diferencias, utilizaremos una clase distinta en Android para manejar los objetos y arrays JSON, así como otra para realizar las peticiones HTTP. El acceso a un recurso que pueda implicar un tiempo de respuesta potencialmente largo (como una petición HTTP) requiere en Android realizar dicho acceso en un hilo de ejecución distinto al de la interfaz de usuario, por lo que en esta práctica, a diferencia de la realizada con Java, obligatoriamente tendremos que trabajar con *hilos*.

## 1.1. Formación de grupos


Los grupos para realizar esta práctica serán de uno o dos alumnos,
intentando mantener los de la práctica anterior. Si por algún motivo
hubiera algún cambio, este debe comunicarse al profesorado. Se sugiere
que se lean detenidamente todas las indicaciones dadas en la presente
memoria para poder llevar hasta el final la realización de esta
práctica.

## 1.2. Hardware&#x2014;software necesario y comunicaciones 


El hardware y software necesario consistirá en:

- Ordenador personal de cualquier arquitectura: Windows, OS-X o Linux

- IDE Android Studio

- Capacidad de virtualización de procesadores activada o terminal
    Android real

Respecto a comunicaciones será necesario tener evidentemente conexión a
Internet.



# 2. Acceso al servicio web AEMET OpenData


Aquí hacemos referencia a la sección "Servicio AEMET OpenData" de la
práctica 8 de Aplicaciones y Usabilidad, siendo completamente válidos los aspectos allí
comentados.

En el contexto de Android no tenemos disponible la clase `OkHttpClient`
para acceder a los servicios web de AEMET, luego tendremos que emplear
alguna clase alternativa con la que consigamos los mismos objetivos. Una
alternativa es `HttpURLConnection`. Un pequeño ejemplo de uso de esta
clase se muestra a continuación en forma de método o función que, a
partir de una URL introducida como un parámetro de la clase `String` en
la llamada, devuelve un `String` con el resultado de la petición
API-REST. Se sugiere que se entienda perfectamente el funcionamiento del
siguiente código o *snippet* y que se utilice oportunamente en la
solución.

```java
/** 
 * La peticion del argumento es recogida y devuelta por el método API_REST
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
``` 

A partir de aquí, habría que realizar el oportuno análisis JSON y
extraer las propiedades en las que estamos interesados. Recuérdese de la
práctica de AEMET realizada con Java en un PC, que habrá que efectuar
dos llamadas encdadenadas para obtener el JSON final.

# 3 JSON y el paquete `org.json` de Java en Android


De la misma forma que ocurría con el paquete `okhttp3`, el paquete
`javax.json` no lo vamos a encontrar en Android. Alternativamente, el
paquete de Android `org.json` puede cumplir perfectamente su cometido,
teniendo una funcionalidad similar, aunque con algunas diferencias, a la
del citado y ya conocido paquete `javax.json`.

Por ejemplo, podemos crear directamente un array u objeto JSON a partir
del string que lo representa con:

```java
JSONArray array = new JSONArray(string_JSON_Array);
```
o bien
```java
JSONObject objeto = new JSONObject(string_JSON_Object);
```

A partir de un `JSONArray` o `JSONObject`, pueden extraerse objetos,
arrays, strings, enteros, booleanos, etc.:

- Objetos: `.getJSONObject(<SELECTOR>)`

- Arrays: `.getJSONArray(<SELECTOR>)`

- Enteros: `.getInt(<SELECTOR>)`

- Reales: `.getDouble(<SELECTOR>)`

- Strings: `.getString(<SELECTOR>)`

- Booleanos: `.getBoolean(<SELECTOR>)`

- ...

donde `<SELECTOR>` hace referencia a un entero (índice), si estamos accediendo a un
elemento de un array o a un *string* si estamos accediendo a una propiedad
de un objeto mediante su nombre. Se sugiere que se busque documentación en Internet para conocer algún detalle o ejemplo de funcionamiento.

# 4. Instalación de certificados digitales

Tal y como ocurría con la práctica de AEMET con Java en un PC, será
necesario que el certificado digital (o la cadena oportuna de
certificados digitales) esté instalado en algún tipo de almacén al que
acuda el sistema cuando se pretenda acceder a un recurso web mediante el
protocolo `HTTPS`, lo cual es el caso. En la práctica anterior,
descargamos y formamos este almacén. En esta, muy probablemente
tendremos ya instalado de manera correcta el certificado necesario en el
almacén propio de Android, por lo que no habrá que hacer nada al respecto.



# 5. Clase `Thread`


## 5.1. Ejemplos

La clase `Thread` de Java me permite, al extenderla, crear hilos de ejecución en los cuales ejecutar nuestro código de manera paralela al que podríamos considerar como hilo principal. Esto puede realizarse de múltiples formas. Veamos algún ejemplo:

```java
class Hilo extends Thread {
  
  String nombre;  
  
  Hilo(String nombre) { // constructor
      this.nombre = nombre;
  }
  
  @Override public void run() {
      // Ejecución de código en hilo paralelo
  } // run

} // Hilo


...


class EjemploHilo {

   public static void main(String [] args) {
       Hilo hilo = new Hilo("Pepe");  // creación del objeto de la clase Hilo
       hilo.start();                  // ejecución del método run a través de start.
       System.out.println("Hola");    
   } // main

} // EjemploHilo
```

lo cual podemos observar en la [figura 1](#fig:thread)

<div id="fig:thread">
<p align="center"><br/>
<img src="figuras/thread.svg" width="600" >
</p>
<p align="center"><b>Figura 1</b>: creación de un hilo paralelo</p><br/>
</div>


Pueden plantearse otras alternativas más *compactas*:

```java

class EjemploHilo2 {

   public static void main(String [] args) {

        new Thread(){
            @Override public void run () { // Esto se ejecuta ya en el hilo alternativo
                // Codigo en hilo alternativo
                System.out.println("Hola soy Manolito");
            }
        }.start();
       
   } // main

} // EjemploHilo2
```


O bien empleando la interfaz `Runnable` y utilizando clases anónimas:



```java

class EjemploHilo3 {

   public static void main(String [] args) {

      new Thread(new Runnable(){
            @Override public void run () { // Esto se ejecuta ya en el hilo alternativo
                // Codigo en hilo alternativo
                 System.out.println("Hola soy Pepito");
            }
        }).start();
       
   } // main

} // EjemploHilo3

```

En cualquier caso se obtienen idénticos resultados ([figura 1](#fig:thread)).

En Android, el hilo que podemos considerar como *principal* es el hilo en el que se realizan todas las operaciones e interacciones con la interfaz gráfica; a dicho hilo
*principal* también se le denonmina hilo de la *interfaz gráfica* o hilo de la UI (*User Interface*).
En ocasiones, aunque pueda resultar paradójico, estándo en un hilo alternativo al principal, puede interesar realizar alguna tarea que se ejecute en el hilo principal o de la UI, por ejemplo alguna actualización de la misma (actualizar un texto, un gráfico...). Android dispone de herramientas para ello, como por ejemplo la utilización del métod `runOnUiThread` perteneciente a la clase `Activity` cuyo método `run` es ejecutado en el hilo de la interfaz de usuario (UI) se ejecute `runOnUiThread` donde se ejecute. Por ejemplo:

```java
class EjemploHilo4 {

   TextView texto;

   public static void main(String [] args) {

       texto = (TextView) findViewById(R.id......);
       texto.setText("Contenido de texto"); // esto se ha ejecutado supuestamente en el hilo de la UI

       new Thread(new Runnable(){
           @Override public run () { // esto ya se ejecuta en un hilo alternativo
               
               // Codigo en hilo alternativo
               
               String saludo = "hola";

               runOnUiThread(new Runnable() {  // seguimos en un hilo alternativo distinto al principal (UI)
                   @Override public run() {
                     texto.setText(saludo);  // esto se ejecuta ya en el hilo asociado a la UI
                   } // run
                } // Runnable
               ); // runOnUiThread

           } // run
       }).start();
       
   } // main

} // EjemploHilo4
```


Si intentáramos interactuar con la UI (como en el ejemplo anterior, actualizando un `TextView`) desde un hilo distinto al hilo de la UI o hilo principal, se produciría un error en tiempo de ejecución.


## 5.2. Procesos temporalmente costosos en Android

Al problema de la acualización de la interfaz de usuario desde un hilo distinto al _principal_ o hilo de la UI _(User Interface)_, hay un problema añadido que todavía no hemos considerado: la petición
HTTP(S) no debemos ponerla en el mismo hilo de ejecución que el hilo en
el que se está ejecutando la interfaz gráfica con el usuario, ya que el
tiempo de respuesta del servidor puede ser relativamente largo, y si
tenemos bloqueada la ejecución de dicho hilo, mientras tanto, el móvil
no podría atender a ninguna interacción que el usuario hiciera, por lo
que aparecerá como bloqueado. Ello implica que será necesario realizar
dicha petición HTTP en un hilo distinto al que se ejecuta la UI. Existen numerosas soluciones para ello, proponiendo emplear hilos 
extendiendo la clase `Thread`.

En la [figura 2](#fig:hilos) se muestra el funcionamiento simbólico de la aplicación
Android:

<div id="fig:hilos">
<p align="center"><br/>
<img src="figuras/hilos.svg" width="800" >
</p>
<p align="center"><b>Figura 2</b>: hilo alternativo al de la UI</p><br/>
</div>




Un resumen de los detalles más importantes a tener en cuenta en la ejecución del hilo alternativo es el siguiente:

- Hay que realizar, tal y como se hizo con la práctica de AEMET con Java de escritorio, dos peticiones HTTPS, esperando sendos resultados JSON.

- Sugerencia: créese una clase que extienda a la clase `Thread` donde se ejecuten las peticiones (o bien créese una clase anónima donde se realicen los procesos requeridos).

- En cualquier caso, arránquese el hilo alternativo desde `onCreate()`.

- Realícense las dos peticiones (método `API_REST(...)` ya proporcionado) HTTPS encadenadas y tras la segunda, invóquese a `runOnUiThread(...)` para escribir los resultados, esta vez, en el hilo de la UI, tal y como se indica en la [figura 2](fig:hilos).

- Complétese el método que escribe los resultados en pantalla.

Se ha proporionado una plantilla del proyecto con cierta estructura inicial para que el grupo pueda abordar fácil y rápidamente los objetivos de la práctica.

# 6. Otros detalles importantes

## 6.1. Permisos

Si el código estuviera ya finalizado y en este instante se ejecutara,
muy probablemente no funcionaría a no ser que hubieramos tenido en
cuenta un detalle importante: hay que acceder a Internet y para ello la
aplicación *debe pedir permiso* para ello. Para solucionar este problema
se sugiere que se busque la solución en la documentación de Android;
para ello, se tendrá que modificar el fichero `AndroidManifest.xml`
añadiendo oportunamente cierto elemento XML.

## 6.2. Tratamiento y control de errores


Las principales fuentes de error que podemos encontrarnos en nuestra
aplicación son, por una parte, por un código de respuesta del protocolo
HTTP que diera lugar a un error, lo cual podría controlarse en la
sentencia `else` de la rutina anterior `API_REST`:

```java
if (responseCode == HttpsURLConnection.HTTP_OK) \{
   ...
} else {
   Log.d(TAG, "responseCode: " + responseCode);
   return null; // retorna null anticipadamente si hay algun problema
}
```




por otra, por alguna excepción como `MalformedURLException`,
`IOException` y `ProtocolException`, lo cual es controlable en el bloque
`catch` del mismo método:


```java
} catch(Exception e) { 
    // Posibles excepciones: MalformedURLException, IOException y ProtocolException
    e.printStackTrace();
    Log.d(TAG, "Error conexión HTTP:" + e.toString());
    return null; // retorna null anticipadamente si hay algun problema
}
```   



En cualquier caso, el denominador común que se ha adoptado es que la
rutina finaliza y retorna un `null`.

Si ocurre esta situación, la intención es generar un `Toast` o pequeña
advertencia o mensaje temporal que suele aparecer en la parte inferior
de la pantalla. Puede conseguirse con la siguiente instrucción:

```java
Toast.makeText( getApplicationContext(), "Problemas en el servicio web de AEMET", 
                Toast.LENGTH_LONG ).show();
```


Ya que está íntimamente relacionado con la UI, esta llamada se debe
efectuar en el hilo de la UI utilizando usando, por ejemplo, `runOnUiThread` tal y como se describió en subapartados anteriores &#x2014;cuando el *string*
`respuesta` fuera `null`&#x2014;.

Por último, también puede ocurrir una excepción al decodificar la
respuesta JSON con la excepción `JSONException`, debiendo *cazarla* allá
donde sea oportuno.


Ténganse muy en cuenta estos detalles y añada el código que controle todas estas circunstancias para avisar al usuario de las vicisitudes que vayan ocurriendo.


# 7. Desarrollo de la práctica


Un resumen de las acciones a realizar puede ser el siguiente:

- Clónese el repositorio desde `Android Studio`.

- En el directorio clonado aparecerá el presente fichero denominado `README.md`

- Recuérdese los detalles del funcionamiento de las API de AEMET
    OpenData


- Complétese el fichero `MainActivity.java` del proyecto Android,
    siguiendo las indicaciones que aparecen en el propio fichero y las
    dadas en la presente memoria

- Complétese el fichero de recursos `activity_main.xml` del proyecto
    Android con los elementos `View` oportunos para indicar las
    magnitudes y los valores meteorológicos exigidos:

- Gestiónese el permiso de acceso a Internet oportunamente

- Obténgase la predicción meteorológica para el día siguiente de:

    - Temperatura máxima y mínima en el día

    - Probabilidad de precipitación en tanto por cien entre las 12 y
        las 18 h

    - Dirección y velocidad del viento entre las 12 y las 18 h

    - Estado del cielo entre las 12 y las 18 h

    - ... y si desea algún párametro adicional, puede ser buscado dentro del JSON entregado en alguna prueba preliminar.

- ...en alguna de las siguientes poblaciones de España:

    - Peleas de Abajo, provincia de Zamora.

    - Guarromán, provincia de Jaén

    - Malcocinado, provincia de Badajoz

Deben contrastarse los resultados con los obtenidos visualmente de
manera directa de la página web de AEMET.

> **Importante:**
Cuando se esté elaborando el código, extrayendo los parámetros meteorológicos desde el JSON, debe prestarse especial atención a la estructura del mismo (secuencia de arrays/objetos),  así como a los tipos de los valores de las propiedades: en algunas ocasiones, algunas propiedades deberían tener un tipo numérico y no de tipo _string_, cuando no lo es así por algún tipo de criterio de diseño, por lo que debe prestarse especial atención a este detalle visualizando previamente todo el JSON devuelto en la segunda API REST, verificando el tipo de cada propiedad.








# 8. Resultados a entregar

Debe actualizarse el proyecto Android del repositorio local en el remoto de GitHub. Esto puede llevarse a cabo mediantes las herramientas gráficas de apoyo a git que contiene Android Studio, o de manera convencional, en cualquier terminal tal y como se ha ido haciendo hasta ahora; terminal por cierto, que puede ser el que nos proporciona el propio Android Studio. Recuérdese que en cualquier caso, la mecánica consistía en añadir al *index* aquellos ficheros que se desee que formen parte del siguiente commit:

```bash
git add -A // se añadirían todos los ficheros nuevos, modificados y borrados de todo el directorio de trabajo
```

Creación de un commit:

```bash
git commit -m "Mensaje"
```

Esta operación add-commit puede realizarse cuantas veces lo considere oportuno cada integrante del grupo.
Se sugiere que se vayan realizando  regularmente *commits* para ir guardando en el
respositorio local las instantáneas del directorio de trabajo que os
parezcan oportunas, así como realizar una sincronización del repositorio
local con el remoto en GitHub, para salvaguardar una copia de seguridad
del repositorio en la nube; si se usaron nombres *estándar* para servidores y ramas, una posibilidad es:

```bash
git push origin master
```



A lo largo del desarrollo de la práctica, el grupo es completamente
libre de actualizar el repositorio local o remoto como lo estime
oportuno, con las ramas o los *commits* que estimen necesarios, pero
respetando los *commits* y *tags* explicitados, como puntos de control. La forma de trabajar
se denomina genéricamente *workflow* y convendría aclararla antes de comenzar, es decir, repartir las tareas y utilizar las herramientas que el propio GitHub proporciona para llevar a cabo la comunicación entre los integrantes del grupo, como las *issues*. Se valorará que haya una intensa interacción con el repositorio remoto.


Si el grupo realiza  consultas através de las *issues* de GitHub, recuérdese antes,
actualizar el repositorio remoto en GitHub e indicar como *assignees*
tanto al profesor, si es que así interesara, como a todos los integrantes del grupo para que
lleguen notificaciones vía email de que hay una *issue* pendiente de ser
contestada.


## 8.1. Etiquetado

Cada alumno integrante del grupo deberá etiquetar individualmente tanto el último *commit* de su sesión de prácticas como el de la práctica en sí (si queda algún detalle pendiente, dentro de los tres días lectivos siguientes). Las etiquetas tendrán el formato `prN.sesion.fin.<USUARIO_DE_GITHUB>` y `prN.fin.<USUARIO_DE_GITHUB>` respectivamente, siendo `N` el número de la práctica. Estos *commits* deben estar ubicados obligatoriamente en la rama `master`.



Ello puede implicar que si los miembros del grupo hacen la práctica en la misma sesión y la finalizan en dicha sesión, el  *commit* en cuestión tendrá las 4 etiquetas. Si en ese mismo caso, quedan detalles pendientes por finalizar durante los tres siguientes días lectivos, cuando el considerado como último *commit* de la práctica esté en el repositorio, ambos alumnos etiquetarán dicho *commit*, a ser posible en el mismo instante.

Siguiendo con la casuística, si ambos miembros hacen la práctica en días distintos, entonces cada vez que un integrante finalice su sesión de prácticas, etiquetará su último *commit* con `prN.sesion.fin.<USUARIO_DE_GITHUB>`. Cuando se dé la práctica por finalizada en cierto último *commit*, este deberá ser etiquetado por ambos con la etiqueta `prN.fin.<USUARIO_DE_GITHUB>`, a ser posible el mismo día.

Las etiquetas debe ponerlas cada alumno a título individual (un alumno no podrá etiquetar un *commit* por el otro, ya que la autoría de las etiquetas aparece reflejada en el repositorio). Ello obligará a que el alumno en cuestión tenga que tener sincronizado su repositorio local con el remoto en el momento de realizar el etiquetado.

**MUY IMPORTANTE**: se tiende a confundir mensajes de commit con etiquetas. Es decir, cuando se hace un commit, se añade un mensaje:

```bash
git commit -m "Mensaje"
```

`"Mensaje"` **NO** es la etiqueta del commit. Para etiquetar un commit, recuérdese que se debe emplear el comando `tag` oportunamente. Recuérdese que con 

```bash
git tag <ETIQUETA> <REFERENCIA>
```

Se etiqueta con el nombre `<ETIQUETA>` el *commit* cuyo hash, *commit* relativo a puntero o hash es `<REFERENCIA>`. Por ejemplo:

```bash
git tag prN.sesion.fin.fjmz-UPV master
```

etiqueta el commit apuntado por el puntero de rama `master` con la etiqueta `prN.sesion.fin.fjmz-UPV`.


Y con

```bash
git push origin --tags
```

se suben exclusivamente las etiquetas al repositorio remoto ubicado en el servidor `origin`.


Recuérdese que Android Studio posee herramientas gráficas para llevar a cabo todos estos procesos de gestión del repositorio  local y remoto. Utilícese el que le resulte más cómodo a los integrantes del grupo.



</div>


