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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

/**
 *
 * @author alira
 */
public class SistemaRRHHGUI {
    
    public static void main(String[] args) {
        // Configurar apariencia del sistema antes del EDT
        configurarApariencia();

        // Lanzar en el Event Dispatch Thread de Swing
        SwingUtilities.invokeLater(() -> {
            mostrarLogin();
        });
    }

    private static void mostrarLogin() {
        LoginFrame login = new LoginFrame(() -> mostrarSplash());
        login.setVisible(true);
    }

    private static void configurarApariencia() {
        try {
            // Usar Look and Feel del sistema para mejor integracion
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Suavizado de fuentes
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    private static void mostrarSplash() {
        // Pantalla de carga (splash screen)
        JWindow splash = new JWindow();
        splash.setSize(480, 280);
        splash.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fondo degradado azul DIUNSA
                GradientPaint gp = new GradientPaint(
                    0, 0,            DiuColors.PRIMARY_DARK,
                    getWidth(), getHeight(), DiuColors.PRIMARY_LIGHT
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Logo D
        JLabel logoD = new JLabel("D") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DiuColors.ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        logoD.setFont(new Font("Segoe UI", Font.BOLD, 36));
        logoD.setForeground(Color.WHITE);
        logoD.setHorizontalAlignment(SwingConstants.CENTER);
        logoD.setPreferredSize(new Dimension(60, 60));

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);

        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        logoRow.setOpaque(false);
        logoRow.add(logoD);
        JPanel lblStack = new JPanel();
        lblStack.setLayout(new BoxLayout(lblStack, BoxLayout.Y_AXIS));
        lblStack.setOpaque(false);
        JLabel lblDiunsa = new JLabel("DIUNSA S.A.");
        lblDiunsa.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblDiunsa.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("Sistema de Gestion de RRHH");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(0xBF, 0xDB, 0xFF));
        lblStack.add(lblDiunsa);
        lblStack.add(lblSub);
        logoRow.add(lblStack);

        JLabel lblVersion = new JLabel("Version 1.0  |  Laboratorio POO — UNITEC");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVersion.setForeground(new Color(0x93, 0xC5, 0xFD));
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JProgressBar barra = new JProgressBar();
        barra.setIndeterminate(true);
        barra.setPreferredSize(new Dimension(380, 4));
        barra.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
        barra.setBackground(new Color(0x1A, 0x4F, 0xA8));
        barra.setForeground(DiuColors.ACCENT);
        barra.setBorderPainted(false);
        barra.setAlignmentX(Component.CENTER_ALIGNMENT);

        centro.add(logoRow);
        centro.add(Box.createVerticalStrut(24));
        centro.add(lblVersion);
        centro.add(Box.createVerticalStrut(20));
        centro.add(barra);

        panel.add(centro, BorderLayout.CENTER);
        splash.setContentPane(panel);
        splash.setVisible(true);

        // Timer para cerrar splash y abrir main window
        Timer timer = new Timer(1800, e -> {
            splash.dispose();
            abrirVentanaPrincipal();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private static void abrirVentanaPrincipal() {
        // Crear el gestor de nomina con datos de prueba precargados
        GestorNomina gestor = new GestorNomina();

        MainFrame frame = new MainFrame(gestor);
        frame.setVisible(true);
    }
}
