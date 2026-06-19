/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1lab;

import static java.awt.AWTEventMulticaster.add;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import org.w3c.dom.events.MouseEvent;

/**
 *
 * @author alira
 */
public class PanelSidebar extends JPanel{
    
    public interface NavListener {
        void onNavChanged(String pantalla);
    }

    private List<NavListener> listeners = new ArrayList<>();
    private String pantallaActual = "dashboard";

    private static final String[][] MENU = {
        {"dashboard",  "  Dashboard"},
        {"empleados",  "  Directorio"},
        {"registrar",  "  Registrar Empleado"},
        {"nomina",     "  Calculo de Nomina"},
        {"historial",  "  Historial de Pagos"},
        {"buscar",     "  Buscar Personal"},
    };

    private JButton[] botones = new JButton[MENU.length];

    public PanelSidebar() {
        setLayout(new BorderLayout());
        setBackground(DiuColors.BG_SIDEBAR);
        setPreferredSize(new Dimension(220, 0));

        // Logo superior
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(24, 18, 20, 18));

        JLabel icono = new JLabel("D");
        icono.setFont(new Font("Segoe UI", Font.BOLD, 22));
        icono.setForeground(DiuColors.TEXT_WHITE);
        icono.setOpaque(true);
        icono.setBackground(DiuColors.ACCENT);
        icono.setHorizontalAlignment(SwingConstants.CENTER);
        icono.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        icono.setMaximumSize(new Dimension(44, 36));
        icono.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDiunsa = new JLabel("DIUNSA");
        lblDiunsa.setFont(DiuColors.FONT_LOGO);
        lblDiunsa.setForeground(DiuColors.TEXT_WHITE);
        lblDiunsa.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Gestion HR");
        lblSub.setFont(DiuColors.FONT_LOGO_SUB);
        lblSub.setForeground(new Color(0x93, 0xC5, 0xFD));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoRow.setOpaque(false);
        logoRow.add(icono);
        JPanel lblStack = new JPanel();
        lblStack.setLayout(new BoxLayout(lblStack, BoxLayout.Y_AXIS));
        lblStack.setOpaque(false);
        lblStack.add(lblDiunsa);
        lblStack.add(lblSub);
        logoRow.add(lblStack);
        logoRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoPanel.add(logoRow);

        // Separador
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(0x1A, 0x4F, 0xA8));
        sep.setBackground(new Color(0x1A, 0x4F, 0xA8));

        // Panel de menu
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel lblMenu = new JLabel("  MENU PRINCIPAL");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblMenu.setForeground(new Color(0x93, 0xC5, 0xFD));
        lblMenu.setBorder(BorderFactory.createEmptyBorder(8, 6, 10, 0));
        lblMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuPanel.add(lblMenu);

        for (int i = 0; i < MENU.length; i++) {
            final String pantalla = MENU[i][0];
            final String label    = MENU[i][1];
            JButton btn = crearBotonNav(label);
            botones[i] = btn;
            btn.addActionListener(e -> seleccionar(pantalla));
            menuPanel.add(btn);
            menuPanel.add(Box.createVerticalStrut(2));
        }

        // Panel inferior con info de usuario
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userPanel.setOpaque(false);
        userPanel.setBorder(BorderFactory.createEmptyBorder(12, 10, 16, 10));

        JLabel avatar = new JLabel("HR") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DiuColors.ACCENT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        avatar.setForeground(DiuColors.TEXT_WHITE);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(34, 34));

        JPanel infoUser = new JPanel();
        infoUser.setLayout(new BoxLayout(infoUser, BoxLayout.Y_AXIS));
        infoUser.setOpaque(false);
        JLabel nombre = new JLabel("Admin HR");
        nombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nombre.setForeground(DiuColors.TEXT_WHITE);
        JLabel sede = new JLabel("Sede San Pedro Sula");
        sede.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        sede.setForeground(new Color(0x93, 0xC5, 0xFD));
        infoUser.add(nombre);
        infoUser.add(sede);
        userPanel.add(avatar);
        userPanel.add(infoUser);

        JSeparator sepBottom = new JSeparator();
        sepBottom.setForeground(new Color(0x1A, 0x4F, 0xA8));

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.setOpaque(false);
        south.add(sepBottom);
        south.add(userPanel);

        add(logoPanel, BorderLayout.NORTH);
        add(sep, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(menuPanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        seleccionar("dashboard");
    }

    private JButton crearBotonNav(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getBackground() != DiuColors.BG_SIDEBAR) {
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(DiuColors.FONT_SIDEBAR);
        btn.setForeground(new Color(0xBF, 0xDB, 0xFF));
        btn.setBackground(DiuColors.BG_SIDEBAR);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setPreferredSize(new Dimension(200, 38));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn.getFont() != DiuColors.FONT_SIDEBAR_ACTIVE) {
                    btn.setBackground(new Color(0x1A, 0x4F, 0xA8));
                }
            }
            public void mouseExited(MouseEvent e) {
                if (btn.getFont() != DiuColors.FONT_SIDEBAR_ACTIVE) {
                    btn.setBackground(DiuColors.BG_SIDEBAR);
                }
            }
        });
        return btn;
    }

    private void seleccionar(String pantalla) {
        this.pantallaActual = pantalla;
        for (int i = 0; i < MENU.length; i++) {
            if (MENU[i][0].equals(pantalla)) {
                botones[i].setFont(DiuColors.FONT_SIDEBAR_ACTIVE);
                botones[i].setForeground(DiuColors.TEXT_WHITE);
                botones[i].setBackground(DiuColors.PRIMARY_LIGHT);
            } else {
                botones[i].setFont(DiuColors.FONT_SIDEBAR);
                botones[i].setForeground(new Color(0xBF, 0xDB, 0xFF));
                botones[i].setBackground(DiuColors.BG_SIDEBAR);
            }
        }
        for (NavListener l : listeners) l.onNavChanged(pantalla);
    }

    public void addNavListener(NavListener l) { listeners.add(l); }
    public String getPantallaActual() { return pantallaActual; }

}
