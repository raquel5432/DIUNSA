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
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author alira
 */
public class PanelEmpleados extends JPanel{
    
    private GestorNomina gestor;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField campoBusqueda;
    private JComboBox<String> filtroTipo;

    private static final String[] COLUMNAS = {
        "ID", "Nombre", "Departamento", "Tipo", "Contrato", "Pago Quincenal"
    };

    public PanelEmpleados(GestorNomina gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout(0, 16));
        setBackground(DiuColors.BG_APP);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        construir();
    }

    private void construir() {
        // ── Encabezado ──────────────────────────────
        JLabel titulo = new JLabel("Directorio de Empleados");
        titulo.setFont(DiuColors.FONT_TITLE);
        titulo.setForeground(DiuColors.TEXT_DARK);

        JLabel subtitulo = new JLabel("Todos los empleados registrados en el sistema.");
        subtitulo.setFont(DiuColors.FONT_BODY);
        subtitulo.setForeground(DiuColors.TEXT_MUTED);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        panelTitulo.setOpaque(false);
        panelTitulo.add(titulo);
        panelTitulo.add(Box.createVerticalStrut(4));
        panelTitulo.add(subtitulo);

        // ── Barra de herramientas ─────────────────────
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);
        toolbar.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        campoBusqueda = new DiuComponentes.CampoTexto(20);
        campoBusqueda.putClientProperty("placeholder", "Buscar por nombre o ID...");
        campoBusqueda.setPreferredSize(new Dimension(220, 36));

        String[] tipos = {"Todos los tipos", "Por Hora", "Fijo", "Supervisor", "Gerente"};
        filtroTipo = new DiuComponentes.ComboEstilizado(tipos);
        filtroTipo.setPreferredSize(new Dimension(170, 36));

        DiuComponentes.BotonPrimario btnFiltrar = new DiuComponentes.BotonPrimario("Filtrar");
        btnFiltrar.addActionListener(e -> cargarDatos(campoBusqueda.getText(), (String) filtroTipo.getSelectedItem()));

        campoBusqueda.addActionListener(e ->
            cargarDatos(campoBusqueda.getText(), (String) filtroTipo.getSelectedItem()));

        toolbar.add(new JLabel("Buscar:"));
        toolbar.add(campoBusqueda);
        toolbar.add(new JLabel("Tipo:"));
        toolbar.add(filtroTipo);
        toolbar.add(btnFiltrar);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.add(panelTitulo, BorderLayout.NORTH);
        encabezado.add(toolbar, BorderLayout.SOUTH);

        // ── Tabla ────────────────────────────────────
        modelo = new DefaultTableModel(COLUMNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo) {
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
        tabla.setForeground(DiuColors.TEXT_DARK);
        tabla.setRowHeight(38);
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setGridColor(DiuColors.BORDER);
        tabla.setSelectionBackground(new Color(0xDB, 0xEA, 0xFF));
        tabla.setSelectionForeground(DiuColors.TEXT_DARK);
        tabla.setDefaultEditor(Object.class, null);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(DiuColors.FONT_TABLE_HDR);
        header.setBackground(new Color(0xF1, 0xF5, 0xF9));
        header.setForeground(new Color(0x47, 0x55, 0x69));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, DiuColors.BORDER));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setReorderingAllowed(false);

        // Renderizador para columna Tipo con badge de color
        tabla.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean focus, int row, int col) {
                JLabel lbl = new JLabel(value == null ? "" : value.toString());
                lbl.setOpaque(true);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                if (sel) {
                    lbl.setBackground(new Color(0xDB, 0xEA, 0xFF));
                } else {
                    lbl.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF8, 0xFA, 0xFC));
                }
                String tipo = value == null ? "" : value.toString();
                switch (tipo) {
                    case "Por Hora":   lbl.setForeground(DiuColors.BADGE_HORA_FG); break;
                    case "Fijo":       lbl.setForeground(DiuColors.BADGE_FIJO_FG); break;
                    case "Supervisor": lbl.setForeground(DiuColors.BADGE_SUP_FG);  break;
                    case "Gerente":    lbl.setForeground(DiuColors.BADGE_GER_FG);  break;
                    default:           lbl.setForeground(DiuColors.TEXT_DARK);
                }
                return lbl;
            }
        });

        // Renderizador alineacion de montos
        tabla.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            { setHorizontalAlignment(SwingConstants.RIGHT); }
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, sel, focus, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF8, 0xFA, 0xFC));
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setForeground(DiuColors.SUCCESS);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 12));
                return c;
            }
        });

        JScrollPane scroll = DiuComponentes.scrollLimpio(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(DiuColors.BORDER));

        // Tarjeta contenedora
        DiuComponentes.Tarjeta tarjeta = new DiuComponentes.Tarjeta();
        tarjeta.setLayout(new BorderLayout(0, 8));
        JLabel lblTotal = new JLabel();
        lblTotal.setFont(DiuColors.FONT_SMALL);
        lblTotal.setForeground(DiuColors.TEXT_MUTED);
        tarjeta.add(scroll, BorderLayout.CENTER);

        add(encabezado, BorderLayout.NORTH);
        add(tarjeta, BorderLayout.CENTER);

        cargarDatos("", "Todos los tipos");
    }

    public void cargarDatos(String busqueda, String tipoFiltro) {
        modelo.setRowCount(0);
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "HN"));
        nf.setMinimumFractionDigits(2);

        for (int i = 0; i < gestor.getCantidadEmpleados(); i++) {
            Empleado e = gestor.getEmpleados()[i];
            String tipo = e.getCategoria();

            boolean coincideBusqueda = busqueda.isEmpty() ||
                e.getNombre().toLowerCase().contains(busqueda.toLowerCase()) ||
                e.getId().toLowerCase().contains(busqueda.toLowerCase());

            boolean coincideTipo = "Todos los tipos".equals(tipoFiltro) ||
                tipo.equals(tipoFiltro);

            if (coincideBusqueda && coincideTipo) {
                modelo.addRow(new Object[]{
                    e.getId(),
                    e.getNombre(),
                    e.getDepartamento(),
                    tipo,
                    e.getContrato().getDescripcion(),
                    "L. " + nf.format(e.calcularPagoQuincenal())
                });
            }
        }
    }

    public void actualizar() {
        cargarDatos(
            campoBusqueda != null ? campoBusqueda.getText() : "",
            filtroTipo != null ? (String) filtroTipo.getSelectedItem() : "Todos los tipos"
        );
    }
}
