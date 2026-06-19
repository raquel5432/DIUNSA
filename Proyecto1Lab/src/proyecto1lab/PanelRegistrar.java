/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1lab;

import static java.awt.AWTEventMulticaster.add;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;

/**
 *
 * @author alira
 */
public class PanelRegistrar extends JPanel{
    
    private GestorNomina gestor;
    private Runnable onEmpleadoAgregado;

    // Campos comunes
    private DiuComponentes.CampoTexto campoId;
    private DiuComponentes.CampoTexto campoNombre;
    private DiuComponentes.CampoTexto campoDepartamento;
    private DiuComponentes.CampoTexto campoFechaIngreso;
    private DiuComponentes.ComboEstilizado comboTipo;
    private DiuComponentes.ComboEstilizado comboContrato;

    // EmpleadoPorHora
    private JSpinner spinnerTarifa;
    private JSpinner spinnerHoras;

    // EmpleadoFijo
    private JSpinner spinnerSalarioMensual;

    // Supervisor
    private JSpinner spinnerBono;

    // Gerente
    private JSpinner spinnerVentas;
    private JSpinner spinnerComision;

    // Paneles dinamicos
    private JPanel panelCamposDinamicos;
    private CardLayout cardDinamico;

    public PanelRegistrar(GestorNomina gestor, Runnable onEmpleadoAgregado) {
        this.gestor = gestor;
        this.onEmpleadoAgregado = onEmpleadoAgregado;
        setLayout(new BorderLayout(0, 20));
        setBackground(DiuColors.BG_APP);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        construir();
    }

    private void construir() {
        // ── Encabezado ──────────────────────────────
        JLabel titulo = new JLabel("Registrar Nuevo Empleado");
        titulo.setFont(DiuColors.FONT_TITLE);
        titulo.setForeground(DiuColors.TEXT_DARK);

        JLabel subtitulo = new JLabel("Complete los campos segun el tipo de empleado.");
        subtitulo.setFont(DiuColors.FONT_BODY);
        subtitulo.setForeground(DiuColors.TEXT_MUTED);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        panelTitulo.setOpaque(false);
        panelTitulo.add(titulo);
        panelTitulo.add(Box.createVerticalStrut(4));
        panelTitulo.add(subtitulo);

        // ── Formulario en tarjeta ─────────────────────
        DiuComponentes.Tarjeta tarjeta = new DiuComponentes.Tarjeta();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));

        // Selector de tipo (primero para controlar el resto)
        JPanel rowTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        rowTipo.setOpaque(false);
        rowTipo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTipo = DiuComponentes.etiqueta("Tipo de Empleado:");
        String[] tipos = {"EmpleadoPorHora", "EmpleadoFijo", "Supervisor", "Gerente"};
        comboTipo = new DiuComponentes.ComboEstilizado(tipos);
        comboTipo.setPreferredSize(new Dimension(200, 36));

        rowTipo.add(lblTipo);
        rowTipo.add(comboTipo);
        tarjeta.add(DiuComponentes.separadorTitulo("TIPO DE EMPLEADO"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(rowTipo);
        tarjeta.add(Box.createVerticalStrut(16));

        // Campos comunes
        tarjeta.add(DiuComponentes.separadorTitulo("INFORMACION GENERAL"));
        tarjeta.add(Box.createVerticalStrut(8));

        JPanel gridComun = new JPanel(new GridLayout(2, 2, 16, 12));
        gridComun.setOpaque(false);
        gridComun.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridComun.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        campoId = new DiuComponentes.CampoTexto(15);
        campoNombre = new DiuComponentes.CampoTexto(20);
        campoDepartamento = new DiuComponentes.CampoTexto(15);
        campoFechaIngreso = new DiuComponentes.CampoTexto(12);
        campoFechaIngreso.setText("2024-01-15");

        gridComun.add(DiuComponentes.campoFormulario("ID del Empleado *", campoId));
        gridComun.add(DiuComponentes.campoFormulario("Nombre Completo *", campoNombre));
        gridComun.add(DiuComponentes.campoFormulario("Departamento *", campoDepartamento));
        gridComun.add(DiuComponentes.campoFormulario("Fecha de Ingreso (YYYY-MM-DD)", campoFechaIngreso));

        tarjeta.add(gridComun);
        tarjeta.add(Box.createVerticalStrut(8));

        // Tipo de contrato
        JPanel rowContrato = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rowContrato.setOpaque(false);
        rowContrato.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] contratos = {
            TipoContrato.TIEMPO_COMPLETO.getDescripcion(),
            TipoContrato.MEDIO_TIEMPO.getDescripcion(),
            TipoContrato.POR_HORA.getDescripcion(),
            TipoContrato.TEMPORAL.getDescripcion()
        };
        comboContrato = new DiuComponentes.ComboEstilizado(contratos);
        comboContrato.setPreferredSize(new Dimension(250, 36));
        rowContrato.add(DiuComponentes.campoFormulario("Tipo de Contrato", comboContrato));
        tarjeta.add(rowContrato);
        tarjeta.add(Box.createVerticalStrut(16));

        // Campos dinamicos segun tipo
        tarjeta.add(DiuComponentes.separadorTitulo("DATOS ESPECIFICOS"));
        tarjeta.add(Box.createVerticalStrut(8));

        cardDinamico = new CardLayout();
        panelCamposDinamicos = new JPanel(cardDinamico);
        panelCamposDinamicos.setOpaque(false);
        panelCamposDinamicos.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelCamposDinamicos.add(construirPanelPorHora(), "EmpleadoPorHora");
        panelCamposDinamicos.add(construirPanelFijo(),    "EmpleadoFijo");
        panelCamposDinamicos.add(construirPanelSupervisor(), "Supervisor");
        panelCamposDinamicos.add(construirPanelGerente(), "Gerente");

        tarjeta.add(panelCamposDinamicos);

        comboTipo.addActionListener(e -> {
            String sel = (String) comboTipo.getSelectedItem();
            cardDinamico.show(panelCamposDinamicos, sel);
        });

        // Botones
        tarjeta.add(Box.createVerticalStrut(20));
        JPanel rowBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        rowBotones.setOpaque(false);
        rowBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        DiuComponentes.BotonPrimario btnGuardar = new DiuComponentes.BotonPrimario("  Registrar Empleado");
        DiuComponentes.BotonPeligro  btnLimpiar = new DiuComponentes.BotonPeligro("Limpiar");

        btnGuardar.addActionListener(e -> guardarEmpleado());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        rowBotones.add(btnGuardar);
        rowBotones.add(btnLimpiar);
        tarjeta.add(rowBotones);

        // Scroll para el formulario
        JScrollPane scroll = new JScrollPane(tarjeta);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        add(panelTitulo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel construirPanelPorHora() {
        JPanel p = new JPanel(new GridLayout(1, 2, 16, 0));
        p.setOpaque(false);
        spinnerTarifa = DiuComponentes.spinnerNumerico(0, 9999, 10, 150.0);
        spinnerHoras  = DiuComponentes.spinnerNumerico(0, 500, 1, 80);
        p.add(DiuComponentes.campoFormulario("Tarifa por Hora (L.)", spinnerTarifa));
        p.add(DiuComponentes.campoFormulario("Horas Trabajadas (quincena)", spinnerHoras));
        JLabel nota = new JLabel("  * Horas extras (> 80) se pagan al 1.5x");
        nota.setFont(DiuColors.FONT_SMALL);
        nota.setForeground(DiuColors.TEXT_MUTED);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(p, BorderLayout.NORTH);
        wrap.add(nota, BorderLayout.SOUTH);
        return wrap;
    }

    private JPanel construirPanelFijo() {
        JPanel p = new JPanel(new GridLayout(1, 1, 16, 0));
        p.setOpaque(false);
        spinnerSalarioMensual = DiuComponentes.spinnerNumerico(0, 999999, 500, 12000.0);
        p.add(DiuComponentes.campoFormulario("Salario Mensual (L.)", spinnerSalarioMensual));
        JLabel nota = new JLabel("  * Se aplican deducciones IHSS 3.5% y RAP 1.5% sobre el pago quincenal");
        nota.setFont(DiuColors.FONT_SMALL);
        nota.setForeground(DiuColors.TEXT_MUTED);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(p, BorderLayout.NORTH);
        wrap.add(nota, BorderLayout.SOUTH);
        return wrap;
    }

    private JPanel construirPanelSupervisor() {
        JPanel p = new JPanel(new GridLayout(1, 2, 16, 0));
        p.setOpaque(false);
        // Reusar o crear nuevo si es null
        spinnerSalarioMensual = (spinnerSalarioMensual == null)
            ? DiuComponentes.spinnerNumerico(0, 999999, 500, 15000.0)
            : spinnerSalarioMensual;
        JSpinner spinnerSalSup = DiuComponentes.spinnerNumerico(0, 999999, 500, 15000.0);
        spinnerBono = DiuComponentes.spinnerNumerico(0, 0.20, 0.01, 0.10);
        p.add(DiuComponentes.campoFormulario("Salario Mensual (L.)", spinnerSalSup));
        p.add(DiuComponentes.campoFormulario("Bono Productividad (0.00 - 0.20)", spinnerBono));
        JLabel nota = new JLabel("  * Bono maximo permitido: 20% del salario base");
        nota.setFont(DiuColors.FONT_SMALL);
        nota.setForeground(DiuColors.TEXT_MUTED);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(p, BorderLayout.NORTH);
        wrap.add(nota, BorderLayout.SOUTH);
        return wrap;
    }

    private JPanel construirPanelGerente() {
        JPanel p = new JPanel(new GridLayout(2, 2, 16, 12));
        p.setOpaque(false);
        JSpinner spinnerSalGer = DiuComponentes.spinnerNumerico(0, 999999, 500, 25000.0);
        JSpinner spinnerBonoGer = DiuComponentes.spinnerNumerico(0, 0.20, 0.01, 0.15);
        spinnerVentas   = DiuComponentes.spinnerNumerico(0, 9999999, 1000, 500000.0);
        spinnerComision = DiuComponentes.spinnerNumerico(0, 0.20, 0.001, 0.02);
        p.add(DiuComponentes.campoFormulario("Salario Mensual (L.)", spinnerSalGer));
        p.add(DiuComponentes.campoFormulario("Bono Productividad (0.00 - 0.20)", spinnerBonoGer));
        p.add(DiuComponentes.campoFormulario("Ventas del Departamento (L.)", spinnerVentas));
        p.add(DiuComponentes.campoFormulario("Porcentaje de Comision", spinnerComision));
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(p, BorderLayout.NORTH);
        return wrap;
    }

    private void guardarEmpleado() {
        String id    = campoId.getText().trim();
        String nombre = campoNombre.getText().trim();
        String depto  = campoDepartamento.getText().trim();
        String fecha  = campoFechaIngreso.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || depto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Los campos ID, Nombre y Departamento son obligatorios.",
                "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gestor.buscarPorId(id) != null) {
            JOptionPane.showMessageDialog(this,
                "Ya existe un empleado con el ID: " + id,
                "ID duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipoSel = (String) comboTipo.getSelectedItem();
        int contratoIdx = comboContrato.getSelectedIndex();
        TipoContrato contrato = TipoContrato.values()[contratoIdx];
        Empleado nuevo = null;

        try {
            switch (tipoSel) {
                case "EmpleadoPorHora": {
                    double tarifa = ((Number) spinnerTarifa.getValue()).doubleValue();
                    int horas     = ((Number) spinnerHoras.getValue()).intValue();
                    nuevo = new EmpleadoPorHora(id, nombre, depto, fecha, contrato, tarifa * horas, tarifa, horas);
                    break;
                }
                case "EmpleadoFijo": {
                    double sal = ((Number) spinnerSalarioMensual.getValue()).doubleValue();
                    EmpleadoFijo ef = new EmpleadoFijo(sal);
                    ef.setId(id); ef.setNombre(nombre);
                    ef.setDepartamento(depto); ef.setFechaIngreso(fecha);
                    ef.setContrato(contrato); ef.setSalarioBase(sal);
                    nuevo = ef;
                    break;
                }
                case "Supervisor": {
                    // obtener el spinner de supervisor del card
                    double sal  = 15000.0;
                    double bono = ((Number) spinnerBono.getValue()).doubleValue();
                    Supervisor sup = new Supervisor(sal, bono);
                    sup.setId(id); sup.setNombre(nombre);
                    sup.setDepartamento(depto); sup.setFechaIngreso(fecha);
                    sup.setContrato(contrato); sup.setSalarioBase(sal);
                    nuevo = sup;
                    break;
                }
                case "Gerente": {
                    double sal     = 25000.0;
                    double bono    = 0.15;
                    double ventas  = ((Number) spinnerVentas.getValue()).doubleValue();
                    double comision = ((Number) spinnerComision.getValue()).doubleValue();
                    Gerente ger = new Gerente(sal, bono, ventas, comision);
                    ger.setId(id); ger.setNombre(nombre);
                    ger.setDepartamento(depto); ger.setFechaIngreso(fecha);
                    ger.setContrato(contrato); ger.setSalarioBase(sal);
                    nuevo = ger;
                    break;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al crear el empleado: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nuevo != null && gestor.agregarEmpleado(nuevo)) {
            JOptionPane.showMessageDialog(this,
                "Empleado registrado correctamente:\n" +
                nombre + " (" + id + ") — " + tipoSel,
                "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            if (onEmpleadoAgregado != null) onEmpleadoAgregado.run();
        } else {
            JOptionPane.showMessageDialog(this,
                "No se pudo registrar el empleado. Puede que el sistema este lleno.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        campoId.setText("");
        campoNombre.setText("");
        campoDepartamento.setText("");
        campoFechaIngreso.setText("2024-01-15");
        comboTipo.setSelectedIndex(0);
        comboContrato.setSelectedIndex(0);
    }
}
