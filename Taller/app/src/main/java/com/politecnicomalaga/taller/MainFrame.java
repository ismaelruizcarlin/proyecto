package com.politecnicomalaga.taller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

public class MainFrame extends JFrame {
	
    /////////////////////////////////////////////////////////////////////////////////////
    //
    // ESTADO
    //
    /////////////////////////////////////////////////////////////////////////////////////

    public static enum PANELES {PANEL_PROVEEDORES, PANEL_ALTATRABAJOS,PANEL_TRABAJOSPORCOBRAR,PANEL_TRABAJOSCOBRADOS};

    //Pixeles a usar en los Layouts como "hueco" entre componentes
    private static final byte GAP = 50;

    //Enlace al controlador
    protected Controlador miControlador;

    /////////////////////////////////////////////////
    //  Paneles y componentes para la GUI
    /////////////////////////////////////////////////

    // Paneles para dividir la pantalla en zonas    
    protected JPanel panelBase;
    protected JPanelBotonera panelBotonera;
    protected JPanel panelFormularios;
    protected JPanel panelFormularioAlta;
    protected JPanel panelFormularioCobro;
    protected JPanel panelFormularioCobrados;
    protected JPanelProveedores panelProveedores;

    //Zona Scrollings y visibilidad
    protected JScrollPane panelFormularioAltaScroll;
    protected JScrollPane panelFormularioCobroScroll;
    protected JScrollPane panelFormularioCobradosScroll;
    protected JScrollPane panelProveedoresScroll;


    //Para los formularios
    //Alta
    protected JTextField matricula;
    protected JTextField DNI;
    protected JTextField modelo;
    protected JTextField propietario;
    protected JTextArea diagnostico;
    protected JTextArea solucion;
    protected JTextField horasPrevistas;
    protected JButton botonAceptarAlta;

    //Formulario de cobro
    protected JList miListaCobros;
    protected JLabel labelTrabajosPorCobrar;
    protected JTextField horasReales;
    protected JButton botonCobrarTrabajo;

    //Formulario trabajos cobrados
    protected JLabel labelTrabajosCobrados;
    protected JList miListaCobrados;



    //Para mostrar resultado de las operaciones...
    protected JLabel lEstado;




    /////////////////////////////////////////////////////////////////////////////////////
    //
    // COMPORTAMIENTO
    //
    /////////////////////////////////////////////////////////////////////////////////////
    
    //Constructor. Se encarga de crear los paneles y colocarlos, crear los componentes
    // y colocarlos
    
    public MainFrame(String title) {
        super(title);  //Para que se muestre el título


        //Bordes para los paneles
        Border blackline, loweredetched;	 	       	 
        blackline = BorderFactory.createLineBorder(Color.black);		       
        loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

	//Recogemos referencia al controlador
    	 miControlador = Controlador.getSingleton();
    	 
    	//1. Crear el frame.		
		
            //asignamos el tamaño a nuestra ventana, y hacemos que se cierre cuando nos pulsan
            //la X de cerrar la ventana

        this.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //2. Creamos los paneles
        panelBase = new JPanel();
        panelBase.setLayout(new BoxLayout(panelBase,BoxLayout.Y_AXIS));
        this.add(new JScrollPane(panelBase)); //Añadimos el panel al frame. Ahora ponemos un "intermediario", el scrollpane, para
                                              //control del scrolling de la pantalla

        //Primera parte. Menú de botones
        panelBotonera = new JPanelBotonera(this);
        panelBotonera.setBorder(blackline);
        panelBase.add(panelBotonera);
        panelBase.add(Box.createRigidArea(new Dimension(500,5)));


        //Segunda parte, formularios
        panelFormularios = new JPanel(new FlowLayout(FlowLayout.CENTER, GAP,GAP));
        panelFormularios.setBorder(blackline);

        panelBase.add(panelFormularios);

        //Creamos los paneles pero nos los añadimos, por lo que no se pintan.
        panelFormularioAlta = new JPanel();
        panelFormularioAlta.setLayout(new BoxLayout(panelFormularioAlta, BoxLayout.Y_AXIS));
        panelFormularioAlta.setBorder(blackline);
        panelFormularioAlta.setBackground(new java.awt.Color(120, 250, 120));
        panelFormularioCobro = new JPanel();
        panelFormularioCobro.setLayout(new BoxLayout(panelFormularioCobro, BoxLayout.Y_AXIS));
        panelFormularioCobro.setBorder(blackline);
        panelFormularioCobro.setBackground(new java.awt.Color(120, 120, 250));
        panelFormularioCobrados = new JPanel();
        panelFormularioCobrados.setLayout(new BoxLayout(panelFormularioCobrados, BoxLayout.Y_AXIS));
        panelFormularioCobrados.setBorder(blackline);
        panelFormularioCobrados.setBackground(new java.awt.Color(250, 250, 120));
        panelProveedores = new JPanelProveedores();

        panelFormularioAltaScroll = new JScrollPane(panelFormularioAlta);
        panelFormularios.add(panelFormularioAltaScroll);

        panelFormularioCobroScroll = new JScrollPane(panelFormularioCobro);
        panelFormularios.add(panelFormularioCobroScroll);

        panelFormularioCobradosScroll = new JScrollPane(panelFormularioCobrados);
        panelFormularios.add(panelFormularioCobradosScroll);

        panelProveedoresScroll = new JScrollPane(panelProveedores);
        panelFormularios.add(panelProveedoresScroll);

        panelFormularioAltaScroll.setVisible(true);
        panelFormularioCobroScroll.setVisible(true);
        panelFormularioCobradosScroll.setVisible(true);
        panelProveedoresScroll.setVisible(true);

        apagarPaneles();
        activaPaneles(PANELES.PANEL_PROVEEDORES);


        //Creación del panel formulario alta
        matricula = new JTextField(7);
        DNI = new JTextField(9);
        modelo = new JTextField(50);
        propietario= new JTextField(50);
        diagnostico= new JTextArea(10,150);
        //diagnostico.setBounds(0,0,150,150);
        solucion= new JTextArea(10,150);
        horasPrevistas= new JTextField(2);
        botonAceptarAlta = new JButton("Aceptar");

        panelFormularioAlta.add(new JLabel("ALTA DE NUEVOS TRABAJOS"));
        panelFormularioAlta.add(Box.createRigidArea(new Dimension(1,20)));
        panelFormularioAlta.add(new JLabel("Matrícula: "));
        panelFormularioAlta.add(matricula);
        panelFormularioAlta.add(new JLabel("DNI Propietario: "));
        panelFormularioAlta.add(DNI);
        panelFormularioAlta.add(new JLabel("Propietario: "));
        panelFormularioAlta.add(propietario);
        panelFormularioAlta.add(new JLabel("Modelo: "));
        panelFormularioAlta.add(modelo);
        panelFormularioAlta.add(new JLabel("Diagnóstico: "));
        panelFormularioAlta.add(new JScrollPane(diagnostico));
        panelFormularioAlta.add(new JLabel("Solución: "));
        panelFormularioAlta.add(new JScrollPane(solucion));
        panelFormularioAlta.add(new JLabel("Horas previstas: "));
        panelFormularioAlta.add(horasPrevistas);
        panelFormularioAlta.add(Box.createRigidArea(new Dimension(5,10)));
        panelFormularioAlta.add(botonAceptarAlta);
        panelFormularioAlta.add(Box.createRigidArea(new Dimension(5,10)));

        //Formulario Cobro
        panelFormularioCobro.add(new JLabel("COBRO DE TRABAJOS"));
        panelFormularioCobro.add(Box.createRigidArea(new Dimension(5,20)));
        panelFormularioCobro.add(new JLabel("Seleccione trabajo: "));
        miListaCobros = new JList();
        panelFormularioCobro.add(new JScrollPane(miListaCobros));
        labelTrabajosPorCobrar = new JLabel("Trabajos por cobrar: 0");
        panelFormularioCobro.add(labelTrabajosPorCobrar);

        panelFormularioCobro.add(new JLabel("Horas trabajadas reales: "));
        horasReales = new JTextField(10);
        panelFormularioCobro.add(horasReales);
        botonCobrarTrabajo = new JButton("Cobrar");
        panelFormularioCobro.add(botonCobrarTrabajo);

        //Formulario Cobrados
        panelFormularioCobrados.add(new JLabel("TRABAJOS YA COBRADOS"));
        panelFormularioCobrados.add(Box.createRigidArea(new Dimension(5,20)));
        miListaCobrados = new JList();
        panelFormularioCobrados.add(new JScrollPane(miListaCobrados));
        labelTrabajosCobrados = new JLabel("Trabajos cobrados: 0");
        panelFormularioCobrados.add(labelTrabajosCobrados);




        botonAceptarAlta.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                comprobarAltaInfoYAceptar();
            }
        });

        botonCobrarTrabajo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                cobrarTrabajo();
            }
        });



	    this.pack();
    }

    private void comprobarAltaInfoYAceptar() {
        String sCSV, sFecha, sMatricula,sModelo,sPropietario,sDNIPropietario,sDiagnostico,sResolucion,sHorasTrabajoPrevistas;
        //Comprobamos que el usuario ha introducido la información adecuada.
        //Por programar...

        //Recogemos los datos del formulario
        sMatricula = matricula.getText();
        sModelo = modelo.getText();
        sPropietario = propietario.getText();
        sDNIPropietario = DNI.getText();
        sDiagnostico = diagnostico.getText();
        sResolucion = solucion.getText();
        sHorasTrabajoPrevistas = horasPrevistas.getText();

        //¿?¿?Todos los datos están ok¿?¿?¿?

        //Pedimos al controlador que realice el alta.

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sFecha = formatter.format(Calendar.getInstance().getTime());

        sCSV = sMatricula + ';' +
                sModelo + ';' +
                sPropietario + ';' +
                sDNIPropietario+ ";"+
                sFecha +";"+ //La fecha vacía para que el constructor instancie la actual
                sDiagnostico + ';' +
                sResolucion + ';' +
                sHorasTrabajoPrevistas + ";0";

        Controlador.getSingleton().altaTrabajo(sCSV);
        matricula.setText("");
        modelo.setText("");
        propietario.setText("");
        DNI.setText("");
        diagnostico.setText("");
        solucion.setText("");
        horasPrevistas.setText("");

    }

    private void cobrarTrabajo() {
        //Comprobamos que el usuario ha seleccionado un trabajo.
        int iSelected = this.miListaCobros.getSelectedIndex();

        String item = (String)miListaCobros.getModel().getElementAt(iSelected);

        //Falta coger horas reales con un dialogo flotante
        String sHorasReales = this.horasReales.getText();
        //Pedimos al controlador que realice el "cobro".
        miControlador.cobrarTrabajo(item, sHorasReales);

        cargarTrabajosPorCobrar();

    }

    protected void cargarTrabajosCobrados() {
        //Rellenamos el JList con los datos
        int i;
        //Rellenamos el JList con los datos
        String[] lista = Controlador.getSingleton().getListaTrabajosCobrados();
        DefaultListModel<String> myModel = new DefaultListModel<>();
        for (i =0;i<lista.length;i++) {
            myModel.addElement(lista[i]);
        }

        this.miListaCobrados.setModel(myModel);

        labelTrabajosCobrados.setText("Trabajos cobrados: " + i);
    }



    //Para activar los trabajos en el panel de cobros
    protected void cargarTrabajosPorCobrar() {
        int i;
        //Rellenamos el JList con los datos
        String[] lista = Controlador.getSingleton().getListaTrabajosPorCobrar();
        DefaultListModel<String> myModel = new DefaultListModel<>();
        for (i =0;i<lista.length;i++) {
            myModel.addElement(lista[i]);
        }

        this.miListaCobros.setModel(myModel);

        labelTrabajosPorCobrar.setText("Trabajos por cobrar: " + i);
    }
    public void terminar() {
       this.dispose();

    }

    public void apagarPaneles() {
        panelFormularioAltaScroll.setVisible(false);
        panelFormularioCobroScroll.setVisible(false);
        panelFormularioCobradosScroll.setVisible(false);
        panelProveedoresScroll.setVisible(false);
    }

    public void activaPaneles(PANELES panelActivar) {
        switch (panelActivar) {
            case PANEL_PROVEEDORES: panelProveedoresScroll.setVisible(true);
                                   // panelProveedores.setVisible(true);
                break;
            case PANEL_ALTATRABAJOS: panelFormularioAltaScroll.setVisible(true);
                                     //panelFormularioAlta.setVisible(true);
                break;
            case PANEL_TRABAJOSCOBRADOS: panelFormularioCobradosScroll.setVisible(true);
                break;
            case PANEL_TRABAJOSPORCOBRAR: panelFormularioCobroScroll.setVisible(true);
                break;
        }
        this.revalidate();
    }
}
