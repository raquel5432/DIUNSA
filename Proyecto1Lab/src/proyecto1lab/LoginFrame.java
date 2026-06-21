/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1lab;

/**
 *
 * @author alira
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

public class LoginFrame extends JFrame{
    
    private static final Map<String, String> USUARIOS = new HashMap<>();
    static {
        USUARIOS.put("admin", "diunsa2024");
        USUARIOS.put("rrhh", "rrhh2024");
    }
 
    private final Runnable onLoginExitoso;
    private DiuComponentes.CampoTexto campoUsuario;
    private JPasswordField campoPassword;
    private JLabel lblError;
 
    public LoginFrame(Runnable onLoginExitoso) {
        this.onLoginExitoso = onLoginExitoso;
        setTitle("DIUNSA S.A. — Acceso al Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 480);
        setResizable(false);
        setLocationRelativeTo(null);
        construirUI();
    }
 
    private void construirUI() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, DiuColors.PRIMARY_DARK,
                    getWidth(), getHeight(), DiuColors.PRIMARY_LIGHT
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(36, 44, 36, 44));
 
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
        logoD.setFont(new Font("Segoe UI", Font.BOLD, 30));
        logoD.setForeground(Color.WHITE);
        logoD.setHorizontalAlignment(SwingConstants.CENTER);
        logoD.setPreferredSize(new Dimension(56, 56));
        logoD.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel lblDiunsa = new JLabel("DIUNSA S.A.");
        lblDiunsa.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblDiunsa.setForeground(Color.WHITE);
        lblDiunsa.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel lblSub = new JLabel("Acceso al Sistema de RRHH");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(0xBF, 0xDB, 0xFF));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setOpaque(false);
        encabezado.add(logoD);
        encabezado.add(Box.createVerticalStrut(12));
        encabezado.add(lblDiunsa);
        encabezado.add(Box.createVerticalStrut(2));
        encabezado.add(lblSub);
        encabezado.add(Box.createVerticalStrut(28));
 
        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setOpaque(false);
 
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(DiuColors.FONT_LABEL);
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        campoUsuario = new DiuComponentes.CampoTexto(18);
        campoUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        campoUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
 
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(DiuColors.FONT_LABEL);
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        campoPassword = new JPasswordField(18);
        campoPassword.setFont(DiuColors.FONT_BODY);
        campoPassword.setBorder(BorderFactory.createCompoundBorder(
            new DiuComponentes.RoundBorder(DiuColors.BORDER, 6),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        campoPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        campoPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        campoPassword.addActionListener(e -> intentarLogin());
        campoUsuario.addActionListener(e -> intentarLogin());
 
        lblError = new JLabel(" ");
        lblError.setFont(DiuColors.FONT_SMALL);
        lblError.setForeground(new Color(0xFE, 0xCA, 0xCA));
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        DiuComponentes.BotonAcento btnIngresar = new DiuComponentes.BotonAcento("  Ingresar  ");
        btnIngresar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnIngresar.addActionListener(e -> intentarLogin());
 
        formulario.add(lblUsuario);
        formulario.add(Box.createVerticalStrut(4));
        formulario.add(campoUsuario);
        formulario.add(Box.createVerticalStrut(14));
        formulario.add(lblPassword);
        formulario.add(Box.createVerticalStrut(4));
        formulario.add(campoPassword);
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(lblError);
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(btnIngresar);
 
        panel.add(encabezado, BorderLayout.NORTH);
        panel.add(formulario, BorderLayout.CENTER);
 
        setContentPane(panel);
    }
 
    private void intentarLogin() {
        String usuario = campoUsuario.getText().trim();
        String password = new String(campoPassword.getPassword());
 
        if (USUARIOS.containsKey(usuario) && USUARIOS.get(usuario).equals(password)) {
            dispose();
            if (onLoginExitoso != null) onLoginExitoso.run();
        } else {
            lblError.setText("Usuario o contraseña incorrectos.");
            campoPassword.setText("");
        }
    }
}
