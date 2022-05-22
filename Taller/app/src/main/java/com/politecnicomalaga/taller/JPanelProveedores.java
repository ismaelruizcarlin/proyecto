package com.politecnicomalaga.taller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Handler;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class JPanelProveedores  extends JPanel {

    //ESTADO
    private JList miListaProveedores;

    //CONSTRUCTOR
    public JPanelProveedores() {
        super();

        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.add(new JLabel("Proveedores:"));
        String resultado = Controlador.getSingleton().getProveedores();

        //DefaultListModel<String> dataModel = new DefaultListModel<String>();

        ArrayList<String> datosProcesados = procesaDatos(resultado);

        //dataModel.addElement(element);

        miListaProveedores = new JList(datosProcesados.toArray());

        this.add(miListaProveedores);
    }


    //COMPORTAMIENTO
    private ArrayList<String> procesaDatos(String sHtml) {
        ArrayList<String> listaResultado = new ArrayList<>();

        if (!sHtml.isEmpty()) {
            sHtml = sHtml.substring(sHtml.indexOf("<p>"));
            String[] dividido = sHtml.split("\n");
            int i = 1;
            while (!dividido[i].isEmpty()) {
                listaResultado.add(dividido[i].substring(3, dividido[i].length() - 4));
                i++;
            }
        } else {
            listaResultado.add("No se pudo obtener lista proveedores. Fallo de conexión");
        }



        return listaResultado;
    }
}
