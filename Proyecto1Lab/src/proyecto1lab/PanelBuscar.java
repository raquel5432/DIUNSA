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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author alira
 */
public class PanelBuscar extends JPanel{
    
    private GestorNomina gestor;
    private DiuComponentes.CampoTexto campoBusqueda;
    private JPanel panelResultado;

    public PanelBuscar(GestorNomina gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout(0, 20));
        setBackground(DiuColors.BG_APP);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        construir();
    }

    private void construir() {
        // ── Encabezado ──────────────────────────────
        JLabel titulo = new JLabel("Buscar Personal");
        titulo.setFont(DiuColors.FONT_TITLE);
        titulo.setForeground(DiuColors.TEXT_DARK);

        JLabel subtitulo = new JLabel("Ingrese el ID o nombre del empleado para ver su ficha.");
        subtitulo.setFont(DiuColors.FONT_BODY);
        subtitulo.setForeground(DiuColors.TEXT_MUTED);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        panelTitulo.setOpaque(false);
        panelTitulo.add(titulo);
        panelTitulo.add(Box.createVerticalStrut(4));
        panelTitulo.add(subtitulo);

        // ── Barra de busqueda grande ──────────────────
        DiuComponentes.Tarjeta tarjetaBusqueda = new DiuComponentes.Tarjeta();
        tarjetaBusqueda.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        campoBusqueda = new DiuComponentes.CampoTexto(30);
        campoBusqueda.setPreferredSize(new Dimension(320, 42));
        campoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        DiuComponentes.BotonPrimario btnBuscar = new DiuComponentes.BotonPrimario("  Buscar  ");
        btnBuscar.setPreferredSize(new Dimension(100, 42));
        btnBuscar.addActionListener(e -> buscar(campoBusqueda.getText().trim()));
        campoBusqueda.addActionListener(e -> buscar(campoBusqueda.getText().trim()));

        JLabel lblHint = new JLabel("Busca por ID (ej: EMP-001) o nombre completo");
        lblHint.setFont(DiuColors.FONT_SMALL);
        lblHint.setForeground(DiuColors.TEXT_MUTED);

        tarjetaBusqueda.add(campoBusqueda);
        tarjetaBusqueda.add(btnBuscar);
        tarjetaBusqueda.add(lblHint);

        // ── Area de resultado ─────────────────────────
        panelResultado = new JPanel(new BorderLayout());
        panelResultado.setOpaque(false);
        mostrarEstadoVacio();

        add(panelTitulo, BorderLayout.NORTH);
        add(tarjetaBusqueda, BorderLayout.CENTER);
        add(panelResultado, BorderLayout.SOUTH);
    }

    private void buscar(String termino) {
        panelResultado.removeAll();
        if (termino.isEmpty()) {
            mostrarEstadoVacio();
            return;
        }

        // Busqueda por ID exacto primero, luego por nombre parcial
        Empleado encontrado = gestor.buscarPorId(termino);
        if (encontrado == null) {
            for (int i = 0; i < gestor.getCantidadEmpleados(); i++) {
                Empleado e = gestor.getEmpleados()[i];
                if (e.getNombre().toLowerCase().contains(termino.toLowerCase())) {
                    encontrado = e;
                    break;
                }
            }
        }

        if (encontrado == null) {
            JPanel noEncontrado = new JPanel(new BorderLayout());
            noEncontrado.setOpaque(false);
            noEncontrado.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
            JLabel lbl = new JLabel("No se encontro ningun empleado con: \"" + termino + "\"",
                SwingConstants.CENTER);
            lbl.setFont(DiuColors.FONT_BODY);
            lbl.setForeground(DiuColors.TEXT_MUTED);
            noEncontrado.add(lbl, BorderLayout.CENTER);
            panelResultado.add(noEncontrado, BorderLayout.CENTER);
        } else {
            panelResultado.add(construirFichaEmpleado(encontrado), BorderLayout.CENTER);
        }

        panelResultado.revalidate();
        panelResultado.repaint();
    }

    private JPanel construirFichaEmpleado(Empleado e) {
        DiuComponentes.Tarjeta ficha = new DiuComponentes.Tarjeta();
        ficha.setLayout(new BorderLayout(16, 0));
        ficha.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "HN"));
        nf.setMinimumFractionDigits(2);

        // Avatar con inicial
        JLabel avatar = new JLabel(String.valueOf(e.getNombre().charAt(0))) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DiuColors.PRIMARY);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 28));
        avatar.setForeground(Color.WHITE);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(70, 70));

        // Nombre y tipo
        JPanel encabezadoFicha = new JPanel();
        encabezadoFicha.setLayout(new BoxLayout(encabezadoFicha, BoxLayout.Y_AXIS));
        encabezadoFicha.setOpaque(false);

        JLabel lblNombre = new JLabel(e.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblNombre.setForeground(DiuColors.TEXT_DARK);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        String tipo = e.getCategoria();
        Color badgeBg = DiuColors.BADGE_FIJO_BG;
        Color badgeFg = DiuColors.BADGE_FIJO_FG;
        switch (tipo) {
            case "Por Hora": badgeBg = DiuColors.BADGE_HORA_BG; badgeFg = DiuColors.BADGE_HORA_FG; break;
            case "Supervisor": badgeBg = DiuColors.BADGE_SUP_BG; badgeFg = DiuColors.BADGE_SUP_FG; break;
            case "Gerente": badgeBg = DiuColors.BADGE_GER_BG; badgeFg = DiuColors.BADGE_GER_FG; break;
        }
        DiuComponentes.Badge badge = new DiuComponentes.Badge(tipo, badgeBg, badgeFg);
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblId = new JLabel("ID: " + e.getId() + "  |  " + e.getDepartamento());
        lblId.setFont(DiuColors.FONT_BODY);
        lblId.setForeground(DiuColors.TEXT_MUTED);
        lblId.setAlignmentX(Component.LEFT_ALIGNMENT);

        encabezadoFicha.add(lblNombre);
        encabezadoFicha.add(Box.createVerticalStrut(4));
        encabezadoFicha.add(badge);
        encabezadoFicha.add(Box.createVerticalStrut(4));
        encabezadoFicha.add(lblId);

        // Grid de detalle
        JPanel gridDetalle = new JPanel(new GridLayout(2, 3, 16, 10));
        gridDetalle.setOpaque(false);
        gridDetalle.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        double neto  = e.calcularPagoQuincenal();
        double bruto = e.getSalarioBase() == 0 ? neto : e.getSalarioBase();
        double deduc = Math.max(0, bruto - neto);

        gridDetalle.add(campoDetalle("Fecha de Ingreso",  e.getFechaIngreso()));
        gridDetalle.add(campoDetalle("Tipo de Contrato",  e.getContrato().getDescripcion()));
        gridDetalle.add(campoDetalle("Salario Base",      "L. " + nf.format(e.getSalarioBase())));
        gridDetalle.add(campoDetalle("Pago Bruto",        "L. " + nf.format(bruto)));
        gridDetalle.add(campoDetalle("Deducciones",       "L. " + nf.format(deduc)));
        gridDetalle.add(campoDetalle("Pago Neto Quincenal","L. " + nf.format(neto)));

        JPanel top = new JPanel(new BorderLayout(12, 0));
        top.setOpaque(false);
        top.add(avatar, BorderLayout.WEST);
        top.add(encabezadoFicha, BorderLayout.CENTER);

        ficha.add(top, BorderLayout.NORTH);
        ficha.add(gridDetalle, BorderLayout.CENTER);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        wrap.add(ficha, BorderLayout.NORTH);
        return wrap;
    }

    private JPanel campoDetalle(String etiqueta, String valor) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel lblEt = new JLabel(etiqueta);
        lblEt.setFont(DiuColors.FONT_SMALL);
        lblEt.setForeground(DiuColors.TEXT_MUTED);

        JLabel lblVal = new JLabel(valor);
        lblVal.setFont(DiuColors.FONT_SUBTITLE);
        lblVal.setForeground(DiuColors.TEXT_DARK);

        p.add(lblEt);
        p.add(Box.createVerticalStrut(2));
        p.add(lblVal);
        return p;
    }

    private void mostrarEstadoVacio() {
        panelResultado.removeAll();
        JLabel lbl = new JLabel("Ingrese un ID o nombre para comenzar la busqueda.",
            SwingConstants.CENTER);
        lbl.setFont(DiuColors.FONT_BODY);
        lbl.setForeground(DiuColors.TEXT_MUTED);
        lbl.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        panelResultado.add(lbl, BorderLayout.CENTER);
        panelResultado.revalidate();
        panelResultado.repaint();
    }

    public void actualizar() { /* sin estado persistente */ }
}
