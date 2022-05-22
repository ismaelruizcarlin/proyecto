package com.politecnicomalaga.taller;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Vehiculo implements Comparable<Vehiculo> {

    //ESTADO
    protected String sMatricula;
    protected String sModelo;
    protected String sPropietario;
    protected String sDNIPropietario;

    //COMPORTAMIENTO
    //Constructor

    public Vehiculo(String sMatricula, String sModelo, String sPropietario, String sDNIPropietario) {
        this.sMatricula = sMatricula;
        this.sModelo = sModelo;
        this.sPropietario = sPropietario;
        this.sDNIPropietario = sDNIPropietario;
    }

    public Vehiculo(String sCSV) {
        // dato1;dato2,dato3;;dato4
        // -> listaParametros[2] -> ""
        String[] listaParametros = sCSV.split(";");
        this.sMatricula = listaParametros[0];
        this.sModelo = listaParametros[1];
        this.sPropietario = listaParametros[2];
        this.sDNIPropietario = listaParametros[3];
    }

    public Vehiculo() {

    }

    //Resto Comportamientos

    public String getsMatricula() {
        return sMatricula;
    }

    public String getsModelo() {
        return sModelo;
    }

    public String getsPropietario() {
        return sPropietario;
    }

    public String getsDNIPropietario() {
        return sDNIPropietario;
    }

    @Override
    public String toString() {
        return  sMatricula + ';' + sModelo + ';' + sPropietario + ';' + sDNIPropietario;
    }

    @Override
    public int compareTo(Vehiculo vehiculo) {
        return sMatricula.compareTo(vehiculo.getsMatricula());
    }

    public void setsMatricula(String sMatricula) {
        this.sMatricula = sMatricula;
    }

    public void setsModelo(String sModelo) {
        this.sModelo = sModelo;
    }

    public void setsPropietario(String sPropietario) {
        this.sPropietario = sPropietario;
    }

    public void setsDNIPropietario(String sDNIPropietario) {
        this.sDNIPropietario = sDNIPropietario;
    }
}
