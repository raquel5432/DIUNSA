/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1lab;

import static java.awt.AWTEventMulticaster.add;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author alira
 */
public class PanelHistorial extends JPanel{
    
    private GestorNomina gestor;
    private DefaultTableModel modelo;

    private static final String[] COLUMNAS = {
        "#", "Periodo", "Total Bruto", "Total Deducciones", "Num. Empleados", "Total Neto"
    };

    public PanelHistorial(GestorNomina gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout(0, 16));
        setBackground(DiuColors.BG_APP);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        construir();
    }

    private void construir() {
        // ── Encabezado ──────────────────────────────
        JLabel titulo = new JLabel("Historial de Pagos");
        titulo.setFont(DiuColors.FONT_TITLE);
        titulo.setForeground(DiuColors.TEXT_DARK);

        JLabel subtitulo = new JLabel("Registro de periodos de nomina procesados y sus totales.");
        subtitulo.setFont(DiuColors.FONT_BODY);
        subtitulo.setForeground(DiuColors.TEXT_MUTED);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        panelTitulo.setOpaque(false);
        panelTitulo.add(titulo);
        panelTitulo.add(Box.createVerticalStrut(4));
        panelTitulo.add(subtitulo);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        toolbar.setOpaque(false);
        DiuComponentes.BotonPrimario btnCargar = new DiuComponentes.BotonPrimario("Cargar Historial");
        btnCargar.addActionListener(e -> cargarHistorial());
        DiuComponentes.BotonAcento btnGuardar = new DiuComponentes.BotonAcento("Guardar Periodo Actual");
        btnGuardar.addActionListener(e -> guardarPeriodo());
        toolbar.add(btnCargar);
        toolbar.add(btnGuardar);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.add(panelTitulo, BorderLayout.WEST);
        encabezado.add(toolbar, BorderLayout.EAST);

        // ── Tabla ────────────────────────────────────
        modelo = new DefaultTableModel(COLUMNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabla = new JTable(modelo) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF8, 0xFA, 0xFC));
                }
                return c;
            }
        };
        tabla.setFont(DiuColors.FONT_TABLE);
        tabla.setRowHeight(42);
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setGridColor(DiuColors.BORDER);
        tabla.setSelectionBackground(new Color(0xDB, 0xEA, 0xFF));
        tabla.setSelectionForeground(DiuColors.TEXT_DARK);
        tabla.setDefaultEditor(Object.class, null);
        tabla.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = tabla.getTableHeader();
        header.setFont(DiuColors.FONT_TABLE_HDR);
        header.setBackground(new Color(0xF1, 0xF5, 0xF9));
        header.setForeground(new Color(0x47, 0x55, 0x69));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, DiuColors.BORDER));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setReorderingAllowed(false);

        // Renderizadores montos
        DefaultTableCellRenderer rightAlignGreen = new DefaultTableCellRenderer() {
            { setHorizontalAlignment(SwingConstants.RIGHT); }
            @Override public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean s, boolean f, int row, int col) {
                Component c = super.getTableCellRendererComponent(t,v,s,f,row,col);
                if (!s) c.setBackground(row%2==0?Color.WHITE:new Color(0xF8,0xFA,0xFC));
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setForeground(col == 3 ? DiuColors.DANGER : DiuColors.SUCCESS);
                setBorder(BorderFactory.createEmptyBorder(0,8,0,12));
                return c;
            }
        };
        tabla.getColumnModel().getColumn(2).setCellRenderer(rightAlignGreen);
        tabla.getColumnModel().getColumn(3).setCellRenderer(rightAlignGreen);
        tabla.getColumnModel().getColumn(5).setCellRenderer(rightAlignGreen);

        // Columna periodo con icono
        tabla.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean s, boolean f, int row, int col) {
                Component c = super.getTableCellRendererComponent(t,v,s,f,row,col);
                if (!s) c.setBackground(row%2==0?Color.WHITE:new Color(0xF8,0xFA,0xFC));
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setForeground(DiuColors.PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(0,12,0,8));
                return c;
            }
        });

        JScrollPane scroll = DiuComponentes.scrollLimpio(tabla);

        DiuComponentes.Tarjeta tarjeta = new DiuComponentes.Tarjeta();
        tarjeta.setLayout(new BorderLayout(0, 8));

        // Panel informativo cuando no hay registros
        JPanel panelVacio = new JPanel(new BorderLayout());
        panelVacio.setOpaque(false);
        panelVacio.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        JLabel lblVacio = new JLabel("No hay registros de historial todavia.", SwingConstants.CENTER);
        lblVacio.setFont(DiuColors.FONT_BODY);
        lblVacio.setForeground(DiuColors.TEXT_MUTED);
        panelVacio.add(lblVacio, BorderLayout.CENTER);

        tarjeta.add(scroll, BorderLayout.CENTER);

        add(encabezado, BorderLayout.NORTH);
        add(tarjeta, BorderLayout.CENTER);

        cargarHistorial();
    }

    private void cargarHistorial() {
        modelo.setRowCount(0);
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "HN"));
        nf.setMinimumFractionDigits(2);

        List<GestorNomina.RegistroHistorial> historial = gestor.cargarTotalesBinario();
        int idx = 1;
        for (GestorNomina.RegistroHistorial reg : historial) {
            double neto = reg.totalBruto - reg.totalDeducciones;
            modelo.addRow(new Object[]{
                idx++,
                reg.periodo,
                "L. " + nf.format(reg.totalBruto),
                "L. " + nf.format(reg.totalDeducciones),
                reg.numEmpleados,
                "L. " + nf.format(neto)
            });
        }

        if (historial.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No se encontraron registros de historial.\n" +
                "Use 'Guardar Periodo Actual' para generar el primero.",
                "Sin historial", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void guardarPeriodo() {
        String periodo = JOptionPane.showInputDialog(this,
            "Ingrese el nombre del periodo (ej: 2024-Q1):",
            "Guardar Periodo", JOptionPane.QUESTION_MESSAGE);
        if (periodo != null && !periodo.trim().isEmpty()) {
            if (gestor.guardarTotalesBinario(periodo.trim())) {
                JOptionPane.showMessageDialog(this,
                    "Periodo '" + periodo + "' guardado en el historial.",
                    "Guardado", JOptionPane.INFORMATION_MESSAGE);
                cargarHistorial();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al guardar el periodo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void actualizar() {
        cargarHistorial();
    }
}
