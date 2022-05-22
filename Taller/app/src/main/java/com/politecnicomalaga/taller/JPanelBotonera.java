package com.politecnicomalaga.taller;

import com.sun.tools.javac.Main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;


public class JPanelBotonera extends JPanel {
    //ESTADO
    //Para el panel menú botonera
    protected JButton botonAlta;
    protected JButton botonCobro;
    protected JButton botonCobrados;
    protected JButton botonProveedores;

    MainFrame miMainFrame;

    private static final int GAP = 30;

    //COMPORTAMIENTO
    public JPanelBotonera(MainFrame referenciaAlFrame){
        super();

        miMainFrame = referenciaAlFrame;
        setLayout(new FlowLayout(FlowLayout.CENTER, GAP,GAP));

        botonAlta = new JButton("Alta Trabajo") ;
        botonCobro = new JButton("Cobro Trabajo");
        botonCobrados = new JButton("Trabajos ya cobrados");
        botonProveedores = new JButton("Proveedores");
        this.add(botonAlta);
        this.add(botonCobro);
        this.add(botonCobrados);
        this.add(botonProveedores);

        //Zona código botones
        botonAlta.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                miMainFrame.apagarPaneles();
                miMainFrame.activaPaneles(MainFrame.PANELES.PANEL_ALTATRABAJOS);
            }
        });

        botonCobro.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                miMainFrame.apagarPaneles();
                miMainFrame.activaPaneles(MainFrame.PANELES.PANEL_TRABAJOSPORCOBRAR);
                miMainFrame.cargarTrabajosPorCobrar();
            }
        });

        botonCobrados.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                miMainFrame.apagarPaneles();
                miMainFrame.activaPaneles(MainFrame.PANELES.PANEL_TRABAJOSCOBRADOS);
                miMainFrame.cargarTrabajosCobrados();
            }
        });

        botonProveedores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                miMainFrame.apagarPaneles();
                miMainFrame.activaPaneles(MainFrame.PANELES.PANEL_PROVEEDORES);
            }
        });

    }


}
