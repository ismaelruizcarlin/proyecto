package com.politecnicomalaga.taller;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Controlador {

    /////////////////////////////////////////////////////////////////////////////////////
    //
    // ESTADO
    //
    /////////////////////////////////////////////////////////////////////////////////////

    private static final String FICHEROCOBRADOS_TT = "ttcobrados.csv";
    private static final String FICHEROPORCOBRAR_TT = "ttcobrar.csv";

    //Instancia UNICA. Este es el "singleton"
    private static Controlador singleton;

    //Control de los trabajos
    ControlTrabajos miControlTrabajosPorCobrar;
    ControlTrabajos miControlTrabajosCobrados;

    
    /////////////////////////////////////////////////////////////////////////////////////
    //
    // Comportamiento
    //
    /////////////////////////////////////////////////////////////////////////////////////
    
    private Controlador() {
        miControlTrabajosPorCobrar = new ControlTrabajos();
        miControlTrabajosCobrados = new ControlTrabajos();
        cargarTrabajosPorCobrar();
        cargarTrabajosCobrados();
    }

    //Método para recuperar los datos desde fichero de texto local en CSV. Cobrados
    private void cargarTrabajosCobrados() {
        String sCSV = "";
        FileReader fr; //Lector de ficheros, modo texto.
        Scanner sc; //Nos sirve para pedir líneas completas. En vez de char a char...
        byte lines=0;

        try {
            fr = new FileReader(FICHEROCOBRADOS_TT); //Abrimos el fichero con los datos TEXTO.
            sc = new Scanner(fr);  //Le damos el fichero al scanner, el sabe "escanear" int, String, líneas completas...

            while(sc.hasNextLine()) {  //Tenemos una línea más??? Si hay una línea más en el fichero todavía, devuelve true
                sCSV = sc.nextLine();   //Pues dame esa línea: nextLine lee la línea hasta encontrar un "\n"
                miControlTrabajosCobrados.addTrabajo(sCSV);  //Esta parte es nuestra. Le damos a nuestro controlador el CSV para
                                                             //que se encargue de crear el objeto de verdad y añadirlo a su colección
                lines++;   //Para poder usar el "debugger" y comprobar cuantas líneas llevamos
            }

            fr.close();  //Cerramos el fichero. Así el S.O. puede darselo a otro proceso.


        } catch(IOException e) { //Ups, algo ha ido mal
            //Añadir aquí algún JDialog para informar al usuario... algo ha ido mal.
            // en el objeto e (Excepción generada) tenemos el comportamiento getMessage. Muy útil para
            //dar info al usuario de lo ocurrido. Es típico poner en un dialogo lo que devuelve e.getMessage()
            e.printStackTrace();  //Soltamos ahora mismo el problema en la consola... el usuario ¡vive felíz!
        }

    }

    //Método para recuperar los datos desde fichero de texto local en CSV. No cobrados
    private void cargarTrabajosPorCobrar() {
        String sCSV = "";
        FileReader fr;
        Scanner sc;
        byte lines=0;

        try {
            fr = new FileReader(FICHEROPORCOBRAR_TT);
            sc = new Scanner(fr);

            while(sc.hasNextLine()) {
                sCSV = sc.nextLine();
                miControlTrabajosPorCobrar.addTrabajo(sCSV);
                lines++;
            }

            fr.close();


        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    //Acceso al controlador. Instancia única "singleton"
    public static Controlador getSingleton() {
            if (singleton == null) {
                    singleton = new Controlador();
            }
            return singleton;
    }


    //Alta de un nuevo trabajo en el taller
    public void altaTrabajo(String sCSV) {

        //Añadimos a la estructura en la RAM
        miControlTrabajosPorCobrar.addTrabajo(sCSV);

        //Grabamos el trabajo en el fichero. Sólo a nivel académico
        //NO USAR en proyectos reales, la parte de XML. Tened en cuenta que crea cabeceras
        //por cada objeto. Sin embargo, el fichero CSV generado, si funciona, añadiendo de uno
        //en uno los objetos en forma de líneas CSV por el final (método escritura APPEND)
        grabarTrabajoTaller(sCSV,FICHEROPORCOBRAR_TT);

        //Ejemplo de exportación desde la memoria RAM (nuestros objetos) hacia fichero XML
        //Con TODOS los trabajos en forma de árbol XML
        grabarTodosLosTrabajosTaller();

    }

    //Método para exportación de nuestros datos de los trabajos de taller a XML
    private void grabarTodosLosTrabajosTaller() {
        Writer wCobrados=null;  //Un writer es una cosa muy genérica. Es un interface de IO JAVA.
                                // Muy utilizado para "abstraernos" del tipo de escritura a realizar (con buffer, sin buffer
                                // , a sockets, a puertos TCP/IP, ficheros en NAS...
        Writer wPorCobrar=null;
        try {

            //Creamos un objeto ejemplo de la clase a serializar
            ControlTrabajos cobrados = this.miControlTrabajosCobrados;
            ControlTrabajos porcobrar = this.miControlTrabajosPorCobrar;

            //Un JAXBContext analiza la clase que le pasemos para
            //conocer la estructura de los objetos a almacenar
            JAXBContext contexto = JAXBContext.newInstance(cobrados.getClass());

            //Un Marshaller es un “jefe”. Literalmente un “Mariscal”
            Marshaller marshaller = contexto.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //El “jefe” les “ordena” a las partes del objeto TrabajoTaller que “formen”
            // que se “organicen” para “desfilar” (serialización) hacia el Stream
            // de salida que deseemos

            //En los writers, ponemos FileWriters, que son adaptadores del interfaz Writer para
            //fichero en local de tipo texto (perfectos para txt, xml, html, etc...)
            wPorCobrar = new FileWriter(FICHEROPORCOBRAR_TT +"_todos.xml",false);
            wCobrados = new FileWriter(FICHEROCOBRADOS_TT +"_todos.xml",false);
            marshaller.marshal(cobrados,wCobrados);  //Al jefe le damos el objeto y el escritor, y el se encarga de
                                                     //hacer desfilar/marchar (to marshal) el objeto hacia el escritor.
                                                     // ¡Maravilloso!
            marshaller.marshal(porcobrar,wPorCobrar);

            //Escribimos el resultado en el escritor, podríamos enviarlo
            // a un fichero, socket, consola...
        } catch (Exception e) {  //Algo fue mal...Siempre con ficheros, puede haber problemas
            //Añadir "info para el usuario"
            e.printStackTrace();
        } finally {  //Esto siempre se ejecuta al final, para no dejar ficheros "colgando" abiertos en el S.O. (Sistema operativo)
            if (wCobrados!= null)
                try {
                    wCobrados.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (wPorCobrar!= null)
                try {
                    wPorCobrar.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
    }

    //Método para añadir de uno en uno los trabajos de taller a los ficheros, CSV y XML
    //CUIDADO: en XML este diseño genera XML mal formados por escritura de cabeceras.
    //Esta generado así adrede para QUE LO PODÁIS VER. No usar en la realidad

    private void grabarTrabajoTaller(String sCSV, String sFichero) {
        //Abrir para escritura el fichero
        FileWriter fw=null;  //Este lo usamos para el CSV
        Writer w=null;       //Este para el XML
        //NOTA: fijaros que puedo declarar objetos de un adaptador (FileWriter) o de un interfaz (Writer). Ambos me van
        //bien aquí. ¿Cuál usar? Cuestión de estilo de programación. Usar Writer es más "genérico" y más versatil, pero se
        //pierde especificidad. Nada es bueno bueno ni malo malo...


        //Parte escritura CSV
        try {
            fw = new FileWriter(sFichero,true);
            //Adicionar al final la línea tal cual y un "\n"
            fw.write(sCSV+"\n");
            fw.flush();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if (fw!= null)
                try {
                    fw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }


        //parte escritura XML
        try {

            //Creamos un objeto ejemplo de la clase a serializar
            TrabajoTaller tt = new TrabajoTaller(sCSV);

            //Un JAXBContext analiza la clase que le pasemos para
            //conocer la estructura de los objetos a almacenar
            JAXBContext contexto = JAXBContext.newInstance(tt.getClass());

            //Un Marshaller es un “jefe”. Literalmente un “Mariscal”
            Marshaller marshaller = contexto.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //El “jefe” les “ordena” a las partes del objeto TrabajoTaller que “formen”
            // que se “organicen” para “desfilar” (serialización) hacia el Stream
            // de salida que deseemos

            marshaller.marshal(tt, System.out);
            w = new FileWriter(sFichero+".xml",true);
            marshaller.marshal(tt,w);

            //Escribimos el resultado en la consola, podríamos enviarlo
            // a un fichero, socket...
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (w!= null)
                try {
                    w.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

    }

    //Petición a los controladores de colecciones que nos den info para la vista
    //son dos métodos en local y un tercero que conecta usando la librería OKhttp a nuestro servidor en remoto
    public String[] getListaTrabajosPorCobrar() {
        return miControlTrabajosPorCobrar.getListaTrabajosVista();
    }

    public String[] getListaTrabajosCobrados(){
        return miControlTrabajosCobrados.getListaTrabajosVista();
    }

    public String getProveedores() {
        String resultado = "";
        String sURL = "http://localhost:8080/TrabajoTallerServidor/Servidor";

        //Conexión al servidor
        OkHttpClient cliente = new OkHttpClient();

        //construimos la peticion
        Request peticion = new Request.Builder()
                .url(sURL)
                .get()
                .build();

        try {
            Response respuesta = cliente.newCall(peticion).execute();
            resultado = respuesta.body().string();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return resultado;
    }


    //método para gestionar el cambio de trabajo sin cobrar a trabajo cobrado. En local, RAM
    public void cobrarTrabajo(String sCSV, String sHorasReales) {
        miControlTrabajosCobrados.addTrabajo(miControlTrabajosPorCobrar.cobrar(sCSV,sHorasReales));

        actualizarFicherosCSV();
    }

    private void actualizarFicherosCSV() {
        //Aquí tenemos que escribir, sobreescribiendo, todos los trabajos por cobrar y los cobrados
        //Esta funcionalidad a diferencia de la escritura XML, que se ha programado para "dar un ejemplo"
        //sí tiene la función de dar persistencia, por ahora, a nuestra gestión del taller.

        //Se hace todito todito igual que en grabarTodosLosTrabajosTaller

        Writer wCobrados=null;  //Un writer es una cosa muy genérica. Es un interface de IO JAVA.
        // Muy utilizado para "abstraernos" del tipo de escritura a realizar (con buffer, sin buffer
        // , a sockets, a puertos TCP/IP, ficheros en NAS...
        Writer wPorCobrar=null;
        try {

            //Creamos un objeto ejemplo de la clase a serializar
            ControlTrabajos cobrados = this.miControlTrabajosCobrados;
            ControlTrabajos porcobrar = this.miControlTrabajosPorCobrar;


            //En los writers, ponemos FileWriters, que son adaptadores del interfaz Writer para
            //fichero en local de tipo texto (perfectos para txt, xml, html, etc...)
            wPorCobrar = new FileWriter(FICHEROPORCOBRAR_TT ,false); //Append a false, sobreescribe
            wCobrados = new FileWriter(FICHEROCOBRADOS_TT ,false);

            for (int i = 0;i<cobrados.getListaTrabajos().size();i++)
                wCobrados.write(cobrados.getListaTrabajos().get(i).toString()+"\n");

            for (int i = 0;i<porcobrar.getListaTrabajos().size();i++)
                wPorCobrar.write(porcobrar.getListaTrabajos().get(i).toString()+"\n");

            //Escribimos el resultado en el escritor, podríamos enviarlo
            // a un fichero, socket, consola...
        } catch (Exception e) {  //Algo fue mal...Siempre con ficheros, puede haber problemas
            //Añadir "info para el usuario"
            e.printStackTrace();
        } finally {  //Esto siempre se ejecuta al final, para no dejar ficheros "colgando" abiertos en el S.O. (Sistema operativo)
            if (wCobrados!= null)
                try {
                    wCobrados.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (wPorCobrar!= null)
                try {
                    wPorCobrar.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

    }




}
