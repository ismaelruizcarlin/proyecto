package com.politecnicomalaga.taller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TrabajoTaller implements Comparable<TrabajoTaller>{
    //ESTADO
    protected Vehiculo miVehiculo;
    protected String fechaEntrada;
    protected String sDiagnostico;
    protected String sResolucion;
    protected float fHorasTrabajoPrevistas;
    protected float fHorasTrabajoRealizadas;

    //COMPORTAMIENTO

    //Constructores
    public TrabajoTaller() {

    }

    public TrabajoTaller(Vehiculo miVehiculo, String sDiagnostico, String sResolucion,float fHorasTrabajoPrevistas) {
        this.sDiagnostico = sDiagnostico;
        this.sResolucion = sResolucion;
        this.fHorasTrabajoPrevistas = fHorasTrabajoPrevistas;
        this.fHorasTrabajoRealizadas = 0;
        this.miVehiculo = miVehiculo;
        fechaEntrada = Calendar.getInstance().toString(); //Se fija la hora de entrada del vehículo a la hora/día de creación del objeto
    }

    //Constructor para el caso que necesitemos "dar de alta" un vehículo en una
    //fecha previa al momento actual
    public TrabajoTaller(Vehiculo miVehiculo, String miFecha,String sDiagnostico, String sResolucion,float fHorasTrabajoPrevistas) {
        this.sDiagnostico = sDiagnostico;
        this.sResolucion = sResolucion;
        this.fHorasTrabajoPrevistas = fHorasTrabajoPrevistas;
        this.fHorasTrabajoRealizadas = 0;
        this.miVehiculo = miVehiculo;
        fechaEntrada = miFecha;
    }

    public TrabajoTaller(String sCSV) {
        String[] listaParametros = sCSV.split(";");
        miVehiculo = new Vehiculo(listaParametros[0]+';'+listaParametros[1]+';'+listaParametros[2]+';'+listaParametros[3]);

        fechaEntrada = listaParametros[4];

        this.sDiagnostico = listaParametros[5];
        this.sResolucion = listaParametros[6];
        this.fHorasTrabajoPrevistas = Float.valueOf(listaParametros[7]);
        this.fHorasTrabajoRealizadas = Float.valueOf(listaParametros[8]);
    }

    //Resto de comportamiento
    public Vehiculo getMiVehiculo() {
        return miVehiculo;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public String getsDiagnostico() {
        return sDiagnostico;
    }

    public String getsResolucion() {
        return sResolucion;
    }

    public float getfHorasTrabajoPrevistas() {
        return fHorasTrabajoPrevistas;
    }

    public float getfHorasTrabajoRealizadas() {
        return fHorasTrabajoRealizadas;
    }

    public void setfHorasTrabajoRealizadas(float fHorasTrabajoRealizadas) {
        this.fHorasTrabajoRealizadas = fHorasTrabajoRealizadas;
    }

    @Override
    public String toString() {

        return miVehiculo.toString() + ';' +
                fechaEntrada +  ';' +
                sDiagnostico + ';' +
                sResolucion + ';' +
                String.valueOf(fHorasTrabajoPrevistas) + ';' +
                String.valueOf(fHorasTrabajoRealizadas);
    }

    @Override
    public int compareTo(TrabajoTaller trabajoTaller) {
        return (trabajoTaller.getFechaEntrada()+trabajoTaller.getMiVehiculo().getsMatricula()).compareTo(this.fechaEntrada+this.miVehiculo.getsMatricula());
    }

    public void setMiVehiculo(Vehiculo miVehiculo) {
        this.miVehiculo = miVehiculo;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public void setsDiagnostico(String sDiagnostico) {
        this.sDiagnostico = sDiagnostico;
    }

    public void setsResolucion(String sResolucion) {
        this.sResolucion = sResolucion;
    }

    public void setfHorasTrabajoPrevistas(float fHorasTrabajoPrevistas) {
        this.fHorasTrabajoPrevistas = fHorasTrabajoPrevistas;
    }
}
