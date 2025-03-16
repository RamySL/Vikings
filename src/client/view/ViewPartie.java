package client.view;

import server.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Vue de la partie, Dessine tous les éléments du modèle
 * !!! Elle est hardcode pour deux joueurs !!!
 */


public class ViewPartie extends JPanel {
    public static final int RATIO_X = 3, RATIO_Y = 2;
    public static final int WIDTH_VIEW = (2*Position.MARGIN+Position.WIDTH)*RATIO_X;
    public static final int HEIGHT_VIEW = (2*Position.MARGIN+Position.HEIGHT)*RATIO_Y;
    // the point where the camp shold be translated to
    public static final Point POINT_ANCRE = new Point(Position.MARGIN*RATIO_X,Position.MARGIN*RATIO_Y);
    // the amount that will be used to translate swing coordinates when draging
    private int offsetDraggingX = 0, offsetDraggingY = 0;
    // the offset to use when translating to show the client camp on the screen
    private int offsetCampX, offsetCampY;
    private int camp_id;

    private Partie partieModel;
    private PanneauControle panneauControle;  // Ajout du champ PanneauControle

    /**
     * Constructeur de la vue de la partie
     */
    public ViewPartie(ViewClient viewClient, Partie partieModel) {
        this.setPreferredSize(new Dimension(WIDTH_VIEW, HEIGHT_VIEW));
        this.partieModel = partieModel;
        // Initialisation de PanneauControle
        this.panneauControle = new PanneauControle();

        this.setLayout(new BorderLayout());
        this.panneauControle.setOpaque(false);

        this.add(panneauControle, BorderLayout.CENTER);  // Ajouter PanneauControle au panneau principal

    }

    // Méthode pour obtenir PanneauControle
    public PanneauControle getPanneauControle() {
        return panneauControle;
    }

    /**
     * Convertit un point du modèle en un point de la vue
     * @param pointModele
     * @return
     */
    public Point pointModelToView(Point pointModele) {
        return new Point(pointModele.x * RATIO_X, (Position.HEIGHT + 2 * Position.MARGIN - pointModele.y) * RATIO_Y);
    }

    /**
     * add the offset passed to the current draging offsets
     * @param x amount to add to offsetX
     * @param y amount to add to offsetY
     */
    public void addToOffset(int x, int y) {
        offsetDraggingX += x;
        offsetDraggingY += y;
    }

    /**
     * set the offset to the camp with the given id<p>
     * !! this method should be executed before the view is painted !!
     * @param camp_id
     */
    public void setOffsetCampID(int camp_id) {
        this.camp_id = camp_id;
        // top left point of the camp in view coordinates
        Point pointTopLeft = pointModelToView(Position.MAP_CAMPS_POSITION.get(camp_id));
        //Point translation = new Point(ancre.x - pointCible.x, ancre.y - pointCible.y );
        Point offset = new Point(POINT_ANCRE.x - pointTopLeft.x, POINT_ANCRE.y - pointTopLeft.y);
        this.offsetCampX = offset.x;
        this.offsetCampY = offset.y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(offsetDraggingX, offsetDraggingY);
        // L'anti-aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(this.offsetCampX, this.offsetCampY);
        for (Camp camp : partieModel.getCamps()) {
            drawCamp(camp, g2);
        }
    }

    private void drawCamp(Camp camp, Graphics2D g2) {
        g2.setColor(Color.BLUE);
        if(camp.getId() == 0)
            g2.drawRect(0, 0, 390, 600);
        else{
            g2.drawRect(400, 0, 400, 600);
        }

        drawWarriors(camp.getWarriors(), g2);
        drawSheap(camp.getSheap(), g2);
        drawFarmers(camp.getFarmers(), g2);
        drawFields(camp.getFields(), g2);
    }

    private void drawSheap(ArrayList<Sheap> sheap, Graphics2D g2) {
        for (Sheap l : sheap) {

                g2.setColor(Color.YELLOW);
                g2.fillOval(l.getPosition().x + l.getCampId() * 400, l.getPosition().y, 16, 16);

        }
    }

    public void drawWarrior(Warrior warrior, Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fillRect(warrior.getPosition().x + warrior.getCampId() * 400 - 8, warrior.getPosition().y - 8, 16, 16);
    }

    public void drawWarriors(ArrayList<Warrior> warriors, Graphics2D g2) {
        for (Warrior warrior : warriors) {
            drawWarrior(warrior, g2);
        }
    }

    private void drawFarmers(ArrayList<Farmer> farmers, Graphics2D g2) {
        g2.setColor(Color.ORANGE);
        for (Farmer farmer : farmers) {
            g2.fillRect(farmer.getPosition().x + farmer.getCampId() * 400 - 8, farmer.getPosition().y - 8, 16, 16);
        }
    }

    private void drawFields(ArrayList<Field> fields, Graphics2D g2) {
        g2.setColor(Color.BLACK);
        for (Field field : fields) {
            g2.drawRect(field.getPosition().x + field.getCampId() * 400 - 8, field.getPosition().y - 8, 16, 16);
            if (field.isPlanted()) {
                g2.drawString(field.getResource(), field.getPosition().x + field.getCampId() * 400, field.getPosition().y);
            }
        }
    }

    public void setPartie(Partie partieModel) {
        this.partieModel = partieModel;
        this.revalidate();
        this.repaint();

    }

    /**
     *
     * @return the total offset used to translate view coordinates (dragging + camp position offset)
     */
    public Point getTotalOffset() {
        int totalOffsetX = offsetDraggingX + offsetCampX;
        int totalOffsetY = offsetDraggingY + offsetCampY;
        return new Point(totalOffsetX, totalOffsetY);
    }
}