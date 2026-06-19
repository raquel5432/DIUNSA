/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1lab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;
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
public class PanelNomina extends JPanel{
    
    private GestorNomina gestor;
    private DefaultTableModel modelo;

    private static final String[] COLUMNAS = {
        "ID", "Nombre", "Departamento", "Tipo", "Salario Bruto", "Deducciones", "Pago Neto"
    };

    public PanelNomina(GestorNomina gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout(0, 16));
        setBackground(DiuColors.BG_APP);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        construir();
    }

    private void construir() {
        // ── Encabezado ──────────────────────────────
        JLabel titulo = new JLabel("Calculo de Nomina Quincenal");
        titulo.setFont(DiuColors.FONT_TITLE);
        titulo.setForeground(DiuColors.TEXT_DARK);

        JLabel subtitulo = new JLabel("Pago quincenal por empleado con deducciones IHSS y RAP.");
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

        DiuComponentes.BotonAcento btnExportar = new DiuComponentes.BotonAcento("  Exportar CSV");
        btnExportar.addActionListener(e -> exportarCSV());
        DiuComponentes.BotonPrimario btnActualizar = new DiuComponentes.BotonPrimario("Actualizar");
        btnActualizar.addActionListener(e -> actualizar());

        toolbar.add(btnActualizar);
        toolbar.add(btnExportar);

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
        tabla.setRowHeight(38);
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

        // Renderizador para columnas de montos (derecha, verde)
        DefaultTableCellRenderer moneyRenderer = new DefaultTableCellRenderer() {
            { setHorizontalAlignment(SwingConstants.RIGHT); }
            @Override public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, sel, focus, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF8, 0xFA, 0xFC));
                setFont(DiuColors.FONT_TABLE);
                setForeground(col == 5 ? DiuColors.DANGER : DiuColors.SUCCESS);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 12));
                return c;
            }
        };
        tabla.getColumnModel().getColumn(4).setCellRenderer(moneyRenderer);
        tabla.getColumnModel().getColumn(5).setCellRenderer(moneyRenderer);
        tabla.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            { setHorizontalAlignment(SwingConstants.RIGHT); }
            @Override public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, sel, focus, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF8, 0xFA, 0xFC));
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setForeground(new Color(0x05, 0x96, 0x69));
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 12));
                return c;
            }
        });

        // Renderizador tipo con color
        tabla.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, sel, focus, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF8, 0xFA, 0xFC));
                setFont(new Font("Segoe UI", Font.BOLD, 11));
                setHorizontalAlignment(SwingConstants.CENTER);
                String tipo = value == null ? "" : value.toString();
                switch (tipo) {
                    case "Por Hora":   setForeground(DiuColors.BADGE_HORA_FG); break;
                    case "Fijo":       setForeground(DiuColors.BADGE_FIJO_FG); break;
                    case "Supervisor": setForeground(DiuColors.BADGE_SUP_FG);  break;
                    case "Gerente":    setForeground(DiuColors.BADGE_GER_FG);  break;
                    default:           setForeground(DiuColors.TEXT_DARK);
                }
                return c;
            }
        });

        JScrollPane scroll = DiuComponentes.scrollLimpio(tabla);

        // ── Fila de totales ───────────────────────────
        JPanel panelTotales = new JPanel(new GridLayout(1, 3, 16, 0));
        panelTotales.setOpaque(false);
        panelTotales.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        // Los totales se calculan al cargar datos
        cargarDatos(modelo, panelTotales);

        DiuComponentes.Tarjeta tarjeta = new DiuComponentes.Tarjeta();
        tarjeta.setLayout(new BorderLayout(0, 8));
        tarjeta.add(scroll, BorderLayout.CENTER);

        add(encabezado, BorderLayout.NORTH);
        add(tarjeta, BorderLayout.CENTER);
        add(panelTotales, BorderLayout.SOUTH);
    }

    private void cargarDatos(DefaultTableModel mdl, JPanel panelTotales) {
        mdl.setRowCount(0);
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "HN"));
        nf.setMinimumFractionDigits(2);

        double totalBruto = 0, totalDeducciones = 0, totalNeto = 0;

        for (int i = 0; i < gestor.getCantidadEmpleados(); i++) {
            Empleado e = gestor.getEmpleados()[i];
            double neto     = e.calcularPagoQuincenal();
            double bruto    = e.getSalarioBase() == 0 ? neto : e.getSalarioBase();
            double deduc    = Math.max(0, bruto - neto);

            mdl.addRow(new Object[]{
                e.getId(),
                e.getNombre(),
                e.getDepartamento(),
                e.getCategoria(),
                "L. " + nf.format(bruto),
                "L. " + nf.format(deduc),
                "L. " + nf.format(neto)
            });
            totalBruto      += bruto;
            totalDeducciones += deduc;
            totalNeto        += neto;
        }

        if (panelTotales != null) {
            panelTotales.removeAll();
            panelTotales.add(resumenCard("Total Bruto",      "L. " + nf.format(totalBruto),      DiuColors.PRIMARY));
            panelTotales.add(resumenCard("Total Deducciones","L. " + nf.format(totalDeducciones), DiuColors.DANGER));
            panelTotales.add(resumenCard("Total Neto",       "L. " + nf.format(totalNeto),        DiuColors.SUCCESS));
        }
    }

    private JPanel resumenCard(String titulo, String valor, Color color) {
        DiuComponentes.Tarjeta card = new DiuComponentes.Tarjeta();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(DiuColors.FONT_LABEL);
        lblTit.setForeground(DiuColors.TEXT_MUTED);
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblVal = new JLabel(valor);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblVal.setForeground(color);
        lblVal.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblTit);
        card.add(Box.createVerticalStrut(4));
        card.add(lblVal);
        return card;
    }

    private void exportarCSV() {
        String archivo = gestor.generarReporteCSV();
        if (archivo != null) {
            JOptionPane.showMessageDialog(this,
                "Reporte exportado exitosamente:\n" + archivo,
                "Exportar CSV", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al exportar el reporte CSV.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizar() {
        // Reconstruir con datos frescos
        removeAll();
        construir();
        revalidate();
        repaint();
    }
}
