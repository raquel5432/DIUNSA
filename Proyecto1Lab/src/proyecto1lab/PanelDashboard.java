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
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author alira
 */
public class PanelDashboard extends JPanel{
    
    private GestorNomina gestor;
    private JPanel panelStats;
    private JScrollPane scrollTabla;

    public PanelDashboard(GestorNomina gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout(0, 0));
        setBackground(DiuColors.BG_APP);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        construir();
    }

    private void construir() {
        removeAll();

        // ── Encabezado ──────────────────────────────
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel tituloPanel = new JPanel();
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));
        tituloPanel.setOpaque(false);

        JLabel titulo = new JLabel("Dashboard");
        titulo.setFont(DiuColors.FONT_TITLE);
        titulo.setForeground(DiuColors.TEXT_DARK);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Resumen general de empleados y nomina activa.");
        subtitulo.setFont(DiuColors.FONT_BODY);
        subtitulo.setForeground(DiuColors.TEXT_MUTED);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        tituloPanel.add(titulo);
        tituloPanel.add(Box.createVerticalStrut(4));
        tituloPanel.add(subtitulo);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.setOpaque(false);
        acciones.add(new DiuComponentes.BotonAcento("  Ver Nomina Completa"));
        acciones.add(new DiuComponentes.BotonPrimario("  + Nuevo Empleado"));

        encabezado.add(tituloPanel, BorderLayout.WEST);
        encabezado.add(acciones, BorderLayout.EAST);

        // ── Tarjetas de estadisticas ─────────────────
        panelStats = new JPanel(new GridLayout(1, 4, 16, 0));
        panelStats.setOpaque(false);
        panelStats.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        actualizarStats();

        // ── Tabla empleados recientes ─────────────────
        JPanel panelTabla = new DiuComponentes.Tarjeta();
        panelTabla.setLayout(new BorderLayout(0, 10));

        JLabel lblTabla = new JLabel("Empleados Recientes");
        lblTabla.setFont(DiuColors.FONT_SUBTITLE);
        lblTabla.setForeground(DiuColors.TEXT_DARK);
        JLabel lblSub2 = new JLabel("Ultimas incorporaciones a la planilla");
        lblSub2.setFont(DiuColors.FONT_SMALL);
        lblSub2.setForeground(DiuColors.TEXT_MUTED);

        JPanel headerTabla = new JPanel();
        headerTabla.setLayout(new BoxLayout(headerTabla, BoxLayout.Y_AXIS));
        headerTabla.setOpaque(false);
        headerTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        headerTabla.add(lblTabla);
        headerTabla.add(lblSub2);

        String[] cols = {"ID", "Nombre", "Departamento", "Tipo", "Pago Quincenal", "Acciones"};
        int n = gestor.getCantidadEmpleados();
        Object[][] datos = new Object[n][6];
        for (int i = 0; i < n; i++) {
            Empleado e = gestor.getEmpleados()[i];
            datos[i][0] = e.getId();
            datos[i][1] = e.getNombre();
            datos[i][2] = e.getDepartamento();
            datos[i][3] = e.getCategoria();
            datos[i][4] = formatearLempiras(e.calcularPagoQuincenal());
            datos[i][5] = "Ver Perfil";
        }

        DiuComponentes.TablaDiunsa tabla = new DiuComponentes.TablaDiunsa(datos, cols);
        JScrollPane scroll = DiuComponentes.scrollLimpio(tabla);
        scroll.setPreferredSize(new Dimension(0, 280));

        panelTabla.add(headerTabla, BorderLayout.NORTH);
        panelTabla.add(scroll, BorderLayout.CENTER);

        // ── Ensamblado ───────────────────────────────
        JPanel contenido = new JPanel(new BorderLayout(0, 0));
        contenido.setOpaque(false);
        contenido.add(encabezado, BorderLayout.NORTH);
        contenido.add(panelStats, BorderLayout.CENTER);
        contenido.add(panelTabla, BorderLayout.SOUTH);

        add(contenido, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void actualizarStats() {
        panelStats.removeAll();

        int total = gestor.getCantidadEmpleados();
        int fijos = 0, porHora = 0, supervisores = 0, gerentes = 0;
        double totalBruto = 0, totalDeducciones = 0;

        for (int i = 0; i < total; i++) {
            Empleado e = gestor.getEmpleados()[i];
            String cat = e.getCategoria();
            if ("Fijo".equals(cat))       fijos++;
            else if ("Por Hora".equals(cat)) porHora++;
            else if ("Supervisor".equals(cat)) supervisores++;
            else if ("Gerente".equals(cat))    gerentes++;
            double neto = e.calcularPagoQuincenal();
            totalBruto += neto;
        }
        totalDeducciones = totalBruto * 0.05;

        panelStats.add(tarjetaStat("Total Empleados",
            String.valueOf(total),
            "Fijos: " + fijos + " | Por Hora: " + porHora,
            DiuColors.PRIMARY));

        panelStats.add(tarjetaStat("Nomina Neta (Quincenal)",
            formatearLempiras(totalBruto),
            "Bruto: " + formatearLempiras(totalBruto * 1.05),
            new Color(0x05, 0x96, 0x69)));

        panelStats.add(tarjetaStat("Deducciones (IHSS/RAP)",
            formatearLempiras(totalDeducciones),
            "Deducciones aplicadas",
            DiuColors.DANGER));

        panelStats.add(tarjetaStat("Estructura",
            "Sup.: " + supervisores + "  Ger.: " + gerentes,
            "Supervisores y Gerentes",
            DiuColors.ACCENT_DARK));
    }

    private JPanel tarjetaStat(String titulo, String valor, String subtexto, Color accentColor) {
        DiuComponentes.Tarjeta card = new DiuComponentes.Tarjeta();
        card.setLayout(new BorderLayout(0, 6));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(DiuColors.FONT_SMALL);
        lblTit.setForeground(DiuColors.TEXT_MUTED);

        JLabel lblVal = new JLabel(valor);
        lblVal.setFont(DiuColors.FONT_STAT_NUM);
        lblVal.setForeground(DiuColors.TEXT_DARK);

        JLabel lblSub = new JLabel(subtexto);
        lblSub.setFont(DiuColors.FONT_SMALL);
        lblSub.setForeground(DiuColors.TEXT_MUTED);

        // Indicador de color izquierdo
        JPanel indicador = new JPanel();
        indicador.setBackground(accentColor);
        indicador.setPreferredSize(new Dimension(4, 0));
        indicador.setOpaque(true);

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        textos.add(lblTit);
        textos.add(Box.createVerticalStrut(6));
        textos.add(lblVal);
        textos.add(Box.createVerticalStrut(4));
        textos.add(lblSub);

        card.add(indicador, BorderLayout.WEST);
        card.add(Box.createHorizontalStrut(12), BorderLayout.CENTER);
        card.add(textos, BorderLayout.EAST);

        return card;
    }

    private String formatearLempiras(double monto) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "HN"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return "L. " + nf.format(monto);
    }

    public void actualizar() {
        construir();
    }
}
