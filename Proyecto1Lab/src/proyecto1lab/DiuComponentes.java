/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1lab;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import org.w3c.dom.events.MouseEvent;

/**
 *
 * @author alira
 */
public class DiuComponentes {
    public static class BotonPrimario extends JButton {
        public BotonPrimario(String texto) {
            super(texto);
            setFont(DiuColors.FONT_BTN);
            setForeground(DiuColors.TEXT_WHITE);
            setBackground(DiuColors.PRIMARY);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(getPreferredSize().width + 24, 38));
            setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { setBackground(DiuColors.PRIMARY_LIGHT); }
                public void mouseExited(MouseEvent e)  { setBackground(DiuColors.PRIMARY); }
                public void mousePressed(MouseEvent e) { setBackground(DiuColors.PRIMARY_DARK); }
                public void mouseReleased(MouseEvent e){ setBackground(DiuColors.PRIMARY_LIGHT); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ──────────────────────────────────────────────
    // BOTON ACENTO (dorado)
    // ──────────────────────────────────────────────
    public static class BotonAcento extends JButton {
        public BotonAcento(String texto) {
            super(texto);
            setFont(DiuColors.FONT_BTN);
            setForeground(new Color(0x7C, 0x2D, 0x12));
            setBackground(DiuColors.ACCENT);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { setBackground(new Color(0xFB, 0xBF, 0x24)); }
                public void mouseExited(MouseEvent e)  { setBackground(DiuColors.ACCENT); }
                public void mousePressed(MouseEvent e) { setBackground(DiuColors.ACCENT_DARK); }
                public void mouseReleased(MouseEvent e){ setBackground(new Color(0xFB, 0xBF, 0x24)); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ──────────────────────────────────────────────
    // BOTON PELIGRO (rojo)
    // ──────────────────────────────────────────────
    public static class BotonPeligro extends JButton {
        public BotonPeligro(String texto) {
            super(texto);
            setFont(DiuColors.FONT_BTN);
            setForeground(DiuColors.TEXT_WHITE);
            setBackground(DiuColors.DANGER);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { setBackground(new Color(0xEF, 0x44, 0x44)); }
                public void mouseExited(MouseEvent e)  { setBackground(DiuColors.DANGER); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ──────────────────────────────────────────────
    // CAMPO DE TEXTO ESTILIZADO
    // ──────────────────────────────────────────────
    public static class CampoTexto extends JTextField {
        public CampoTexto(int columnas) {
            super(columnas);
            setFont(DiuColors.FONT_BODY);
            setForeground(DiuColors.TEXT_DARK);
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(DiuColors.BORDER, 6),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            setPreferredSize(new Dimension(getPreferredSize().width, 38));
        }
    }

    // ──────────────────────────────────────────────
    // COMBO BOX ESTILIZADO
    // ──────────────────────────────────────────────
    public static class ComboEstilizado extends JComboBox<String> {
        public ComboEstilizado(String[] items) {
            super(items);
            setFont(DiuColors.FONT_BODY);
            setForeground(DiuColors.TEXT_DARK);
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(DiuColors.BORDER, 6),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
            ));
            setPreferredSize(new Dimension(getPreferredSize().width, 38));
        }
    }

    // ──────────────────────────────────────────────
    // TARJETA (card con sombra)
    // ──────────────────────────────────────────────
    public static class Tarjeta extends JPanel {
        public Tarjeta() {
            setBackground(DiuColors.BG_CARD);
            setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)
            ));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Insets ins = getInsets();
            int x = ins.left - 2, y = ins.top - 2;
            int w = getWidth() - ins.left - ins.right + 4;
            int h = getHeight() - ins.top - ins.bottom + 4;
            g2.setColor(DiuColors.BG_CARD);
            g2.fillRoundRect(x, y, w, h, 10, 10);
            g2.setColor(DiuColors.BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(x, y, w - 1, h - 1, 10, 10);
            g2.dispose();
        }
    }

    // ──────────────────────────────────────────────
    // BADGE de categoria
    // ──────────────────────────────────────────────
    public static class Badge extends JLabel {
        public Badge(String texto, Color bg, Color fg) {
            super(texto);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setForeground(fg);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
            putClientProperty("bg", bg);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor((Color) getClientProperty("bg"));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ──────────────────────────────────────────────
    // TABLA DIUNSA ESTILIZADA
    // ──────────────────────────────────────────────
    public static class TablaDiunsa extends JTable {
        public TablaDiunsa(Object[][] datos, String[] columnas) {
            super(datos, columnas);
            setFont(DiuColors.FONT_TABLE);
            setForeground(DiuColors.TEXT_DARK);
            setBackground(DiuColors.BG_CARD);
            setRowHeight(38);
            setShowVerticalLines(false);
            setShowHorizontalLines(true);
            setGridColor(DiuColors.BORDER);
            setSelectionBackground(new Color(0xDB, 0xEA, 0xFF));
            setSelectionForeground(DiuColors.TEXT_DARK);
            setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            setDefaultEditor(Object.class, null); // no editable
            setIntercellSpacing(new Dimension(0, 0));

            JTableHeader header = getTableHeader();
            header.setFont(DiuColors.FONT_TABLE_HDR);
            header.setBackground(new Color(0xF8, 0xFA, 0xFC));
            header.setForeground(new Color(0x47, 0x55, 0x69));
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, DiuColors.BORDER));
            header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
            header.setReorderingAllowed(false);

            // Alternar filas
            setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF8, 0xFA, 0xFC));
                    }
                    setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                    return c;
                }
            });
        }
    }

    // ──────────────────────────────────────────────
    // SEPARADOR CON TITULO
    // ──────────────────────────────────────────────
    public static JPanel separadorTitulo(String titulo) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(DiuColors.FONT_LABEL);
        lbl.setForeground(DiuColors.TEXT_MUTED);
        JSeparator sep = new JSeparator();
        sep.setForeground(DiuColors.BORDER);
        p.add(lbl, BorderLayout.WEST);
        p.add(sep, BorderLayout.CENTER);
        return p;
    }

    // ──────────────────────────────────────────────
    // ETIQUETA DE FORMULARIO
    // ──────────────────────────────────────────────
    public static JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(DiuColors.FONT_LABEL);
        lbl.setForeground(DiuColors.TEXT_DARK);
        return lbl;
    }

    // ──────────────────────────────────────────────
    // SPINNER NUMERICO
    // ──────────────────────────────────────────────
    public static JSpinner spinnerNumerico(double min, double max, double step, double valor) {
        JSpinner sp = new JSpinner(new SpinnerNumberModel(valor, min, max, step));
        sp.setFont(DiuColors.FONT_BODY);
        sp.setPreferredSize(new Dimension(140, 38));
        sp.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(DiuColors.BORDER, 6),
            BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));
        return sp;
    }

    // ──────────────────────────────────────────────
    // PANEL SCROLL LIMPIO
    // ──────────────────────────────────────────────
    public static JScrollPane scrollLimpio(Component comp) {
        JScrollPane sp = new JScrollPane(comp);
        sp.setBorder(BorderFactory.createLineBorder(DiuColors.BORDER));
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBackground(Color.WHITE);
        return sp;
    }

    // ──────────────────────────────────────────────
    // BORDE REDONDEADO PERSONALIZADO
    // ──────────────────────────────────────────────
    public static class RoundBorder extends AbstractBorder {
        private Color color;
        private int radius;
        public RoundBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }
        @Override
        public Insets getBorderInsets(Component c) { return new Insets(1,1,1,1); }
    }

    // ──────────────────────────────────────────────
    // BORDE CON SOMBRA
    // ──────────────────────────────────────────────
    public static class ShadowBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Sombra suave
            for (int i = 3; i >= 1; i--) {
                g2.setColor(new Color(0, 0, 0, 10 * i));
                g2.drawRoundRect(x + i, y + i, w - i * 2, h - i * 2, 10, 10);
            }
            g2.dispose();
        }
        @Override
        public Insets getBorderInsets(Component c) { return new Insets(4, 4, 6, 6); }
    }

    // ──────────────────────────────────────────────
    // HELPER: crear campo de formulario con etiqueta
    // ──────────────────────────────────────────────
    public static JPanel campoFormulario(String etiquetaTexto, JComponent campo) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel lbl = etiqueta(etiquetaTexto);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.add(lbl);
        p.add(Box.createVerticalStrut(4));
        p.add(campo);
        return p;
    }
}
