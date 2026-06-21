/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1lab;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 *
 * @author alira
 */
public class MainFrame extends JFrame{
    
    private GestorNomina gestor;
    private PanelSidebar sidebar;
    private PanelHeader header;
    private JPanel areaPrincipal;
    private CardLayout cardLayout;

    private PanelDashboard panelDashboard;
    private PanelEmpleados panelEmpleados;
    private PanelRegistrar panelRegistrar;
    private PanelNomina    panelNomina;
    private PanelHistorial panelHistorial;
    private PanelBuscar    panelBuscar;

    public MainFrame(GestorNomina gestor) {
        this.gestor = gestor;
        configurarVentana();
        construirUI();
        cargarDatosIniciales();
    }

    private void configurarVentana() {
        setTitle("DIUNSA S.A. — Sistema de Gestion de RRHH");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 680));
        setPreferredSize(new Dimension(1280, 800));
        setLocationRelativeTo(null);

        // Icono de aplicacion simulado con color
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Dialogo de confirmacion al cerrar
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int opc = JOptionPane.showConfirmDialog(MainFrame.this,
                    "Desea guardar el backup de empleados antes de salir?",
                    "Salir de DIUNSA RRHH",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                if (opc == JOptionPane.YES_OPTION) {
                    gestor.guardarBackupEmpleados();
                    System.exit(0);
                } else if (opc == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
                // CANCEL: no cierra
            }
        });
    }

    private void construirUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(DiuColors.BG_APP);

        // ── Sidebar ────────────────────────────────
        sidebar = new PanelSidebar();

        // ── Columna derecha: header + contenido ────
        header = new PanelHeader();

        cardLayout    = new CardLayout();
        areaPrincipal = new JPanel(cardLayout);
        areaPrincipal.setBackground(DiuColors.BG_APP);

        panelDashboard = new PanelDashboard(gestor);
        panelEmpleados = new PanelEmpleados(gestor, this::alAgregarEmpleado);
        panelRegistrar = new PanelRegistrar(gestor, this::alAgregarEmpleado);
        panelNomina    = new PanelNomina(gestor);
        panelHistorial = new PanelHistorial(gestor);
        panelBuscar    = new PanelBuscar(gestor);

        areaPrincipal.add(panelDashboard, "dashboard");
        areaPrincipal.add(panelEmpleados, "empleados");
        areaPrincipal.add(panelRegistrar, "registrar");
        areaPrincipal.add(panelNomina,    "nomina");
        areaPrincipal.add(panelHistorial, "historial");
        areaPrincipal.add(panelBuscar,    "buscar");

        JPanel derecha = new JPanel(new BorderLayout());
        derecha.setBackground(DiuColors.BG_APP);
        derecha.add(header, BorderLayout.NORTH);
        derecha.add(areaPrincipal, BorderLayout.CENTER);

        // Listener de navegacion
        sidebar.addNavListener(pantalla -> navegarA(pantalla));

        root.add(sidebar, BorderLayout.WEST);
        root.add(derecha,  BorderLayout.CENTER);

        setContentPane(root);
        pack();
    }

    private void navegarA(String pantalla) {
        cardLayout.show(areaPrincipal, pantalla);

        String[] titulos = {
            "Dashboard", "Directorio de Empleados",
            "Registrar Nuevo Empleado", "Calculo de Nomina Quincenal",
            "Historial de Pagos", "Buscar Personal"
        };
        String[] ids = {
            "dashboard", "empleados", "registrar", "nomina", "historial", "buscar"
        };
        for (int i = 0; i < ids.length; i++) {
            if (ids[i].equals(pantalla)) {
                header.setTitulo(titulos[i]);
                break;
            }
        }

        // Actualizar panel activo con datos frescos
        switch (pantalla) {
            case "dashboard":  panelDashboard.actualizar(); break;
            case "empleados":  panelEmpleados.actualizar(); break;
            case "nomina":     panelNomina.actualizar();    break;
            case "historial":  panelHistorial.actualizar(); break;
        }
    }

    private void alAgregarEmpleado() {
        // Notificacion en header
        panelDashboard.actualizar();
        panelEmpleados.actualizar();
    }

    private void cargarDatosIniciales() {
        // Intentar cargar backup de empleados si existe
        if (gestor.getCantidadEmpleados() == 0) {
            boolean cargado = gestor.cargarBackupEmpleados();
            if (cargado) {
                gestor.registrarLog("INICIO: Backup de empleados cargado exitosamente.");
            }
        }
        // Mostrar dashboard inicial
        navegarA("dashboard");
    }
}
