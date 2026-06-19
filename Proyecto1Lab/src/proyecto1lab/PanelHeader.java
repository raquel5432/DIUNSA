/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1lab;

import static java.awt.AWTEventMulticaster.add;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author alira
 */
public class PanelHeader extends JPanel{
    
    private JLabel lblTitulo;
    private JTextField campoBusqueda;

    public PanelHeader() {
        setLayout(new BorderLayout());
        setBackground(DiuColors.BG_HEADER);
        setPreferredSize(new Dimension(0, 60));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, DiuColors.BORDER));

        lblTitulo = new JLabel("Dashboard");
        lblTitulo.setFont(DiuColors.FONT_SUBTITLE);
        lblTitulo.setForeground(DiuColors.TEXT_MUTED);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));

        JPanel derecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        derecho.setOpaque(false);

        campoBusqueda = new JTextField(20);
        campoBusqueda.setFont(DiuColors.FONT_BODY);
        campoBusqueda.setForeground(DiuColors.TEXT_DARK);
        campoBusqueda.setBackground(new Color(0xF8, 0xFA, 0xFC));
        campoBusqueda.setBorder(BorderFactory.createCompoundBorder(
            new DiuComponentes.RoundBorder(DiuColors.BORDER, 20),
            BorderFactory.createEmptyBorder(6, 32, 6, 12)
        ));
        campoBusqueda.setPreferredSize(new Dimension(220, 34));

        // Icono busqueda simulado
        JLabel iconBuscar = new JLabel("[ buscar ]");
        iconBuscar.setFont(DiuColors.FONT_SMALL);
        iconBuscar.setForeground(DiuColors.TEXT_MUTED);

        derecho.add(campoBusqueda);

        add(lblTitulo, BorderLayout.WEST);
        add(derecho, BorderLayout.EAST);
    }

    public void setTitulo(String titulo) {
        lblTitulo.setText(titulo);
    }

    public JTextField getCampoBusqueda() {
        return campoBusqueda;
    }
}
