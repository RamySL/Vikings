package sharedGUIcomponents.ComposantsPerso;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;

/**
 * Regroupe tous les bouttons utilisé dans le jeu
 */
public abstract class Buttons extends JButton {
    private Border border = new CompoundBorder(
            new LineBorder(new Color(0), 2),
            new EmptyBorder(5, 15, 5, 15));

    protected boolean enabled = true;
    public Buttons(String txt){
        super(txt);
        this.setBackground(new Color(0x35FFDA));
        this.setFont(FontPerso.mvBoli(15));
        this.setForeground(Color.BLACK);
        this.setFocusable(false);
        this.setBorder(border);
    }

    @Override
    public void setEnabled(boolean bool){
        this.enabled = bool;
        super.setEnabled(bool);
    }

    /**
     * bouttons utilisé hors deroulement de la partie
     */
    public static class BouttonHorsJeu extends Buttons {
        public BouttonHorsJeu(String txt) {
            super(txt);
            this.addHoverEffect();

        }

        private void addHoverEffect() {

            Color originalBackground = this.getBackground();
            Border originalBorder = this.getBorder();
            Color originalForeground = this.getForeground();

            Color hoverBackground = new Color(0x011C8A);

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (BouttonHorsJeu.this.enabled){
                        BouttonHorsJeu.this.setBackground(hoverBackground);
                        BouttonHorsJeu.this.setForeground(Color.WHITE);
                    }

                }
                @Override
                public void mouseExited(MouseEvent e) {
                    BouttonHorsJeu.this.setBackground(originalBackground);
                    BouttonHorsJeu.this.setBorder(originalBorder);
                    BouttonHorsJeu.this.setForeground(originalForeground);
                }
            });
        }
    }
/**
     * bouttons utilisé pendant le deroulement de la partie
     */
public static class BouttonJeu extends Buttons {
    private final Color bgNormal = new Color(0x1E1E2F);
    private final Color bgHover = new Color(0x2E3A8C);
    private final Color fgNormal = Color.WHITE;
    private final int arc = 20;

    private boolean hover = false;

    public BouttonJeu(String txt) {
        super(txt);
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(fgNormal);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Shape roundShape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc);
                if (roundShape.contains(e.getPoint())) {
                    if (!hover) {
                        hover = true;
                        repaint();
                    }
                } else {
                    if (hover) {
                        hover = false;
                        repaint();
                    }
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bg = hover ? bgHover : bgNormal;
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        FontMetrics fm = g2.getFontMetrics();
        String text = getText();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + textHeight) / 2 - 3;

        g2.setColor(getForeground());
        g2.setFont(getFont());
        g2.drawString(text, x, y);

        g2.dispose();
    }
}





}
