package client.view;

import server.model.*;
import sharedGUIcomponents.ComposantsPerso.FontPerso;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

/**
 * Vue de la partie, Dessine tous les éléments du modèle
 * !!! Elle est hardcode pour deux joueurs !!!
 */


public class ViewPartie extends JPanel {
    public static final int RATIO_X = 3, RATIO_Y = 3;
    public static final int WIDTH_VIEW = (2*Position.MARGIN+Position.WIDTH)*RATIO_X;
    public static final int HEIGHT_VIEW = (2*Position.MARGIN+Position.HEIGHT)*RATIO_Y;
    // the point where the camp shold be translated to
    public static final Point POINT_ANCRE = new Point(Position.MARGIN*RATIO_X,Position.MARGIN*RATIO_Y);
    // the amount that will be used to translate swing coordinates when draging
    private int offsetDraggingX = 0, offsetDraggingY = 0;
    // the amount that will be used to translate swing coordinates when zooming relativeley to the mouse
    // the offset to use when translating to show the client camp on the screen
    private int offsetCampX, offsetCampY;
    // scale factor for handeling the zoom
    private double scaleFactor=1.0;
    private int camp_id;
    private Camp camp;

    private Partie partieModel;
    private PanneauControle panneauControle;  // Ajout du champ PanneauControle
    private int windowWidth=WIDTH_VIEW, windowHeight=HEIGHT_VIEW;


    /**
     * Constructeur de la vue de la partie
     */
    public ViewPartie(ViewClient viewClient, Partie partieModel) {

        this.setPreferredSize(new Dimension(windowWidth, windowHeight));

        this.partieModel = partieModel;
        this.panneauControle = new PanneauControle(windowWidth,  windowHeight);
        this.setLayout(new BorderLayout());
        this.panneauControle.setOpaque(false);
        this.add(panneauControle, BorderLayout.CENTER);  // Ajouter PanneauControle au panneau principal
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updatePanneauControlePosition();
            }
        });

    }

    /**
     * !! a lot of redundant calculations for the drawing !!
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(offsetDraggingX, offsetDraggingY);
        g2.translate(this.offsetCampX, this.offsetCampY);
        g2.scale(scaleFactor,scaleFactor);

        // L'anti-aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw the x and y axes in black and thick
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(0, 0, 1000, 0);
        g2.drawLine(0, 0, 0, 1000);

        for (Camp camp : partieModel.getCamps()) {
            drawCamp(camp, g2);
        }

        g2.dispose();
    }

    private void updatePanneauControlePosition() {
        // Redimensionner et déplacer le panneau de contrôle à droite de la fenêtre
        int width = getWidth();  // Largeur de la fenêtre
        int height = getHeight();  // Hauteur de la fenêtre
        panneauControle.updatePosition(width, height);

    }

    /**
     * Convertit un point du modèle en un point de la vue, sans ajouter les transaltions et mise à l'echelle<p>
     * Attend que en plus des transformation aportée par le RATIO_X et RATIO_Y, il faut rajouter les autres transformations
     * @param pointModele
     * @return
     */
    public static Point pointModelToView(Point pointModele) {
        return new Point(pointModele.x * RATIO_X, (Position.HEIGHT + 2 * Position.MARGIN - pointModele.y) * RATIO_Y);
    }

    public void clickToView(Point click) {
        //Point res = new Point(pointModele.x * RATIO_X, (Position.HEIGHT + 2 * Position.MARGIN - pointModele.y) * RATIO_Y);
        // translae de la quantité totale de translation
        Point totalOffset = this.getTotalOffset();
        click.translate(-totalOffset.x, -totalOffset.y);
        // scale
        click.x = (int) (click.x / this.getScaleFactor());
        click.y = (int) (click.y / this.getScaleFactor());
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
     * multiply the current scaling by the one passed in parms
     * @param scaleFactor
     */
    public void multiplyScale(double scaleFactor){
        this.scaleFactor *= scaleFactor;
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

    /**
     * @param camp
     * @param g2
     */
    private void drawCamp(Camp camp, Graphics2D g2) {
        Point topLeftModel = Position.MAP_CAMPS_POSITION.get(camp.getId());
        Point topLeftView = pointModelToView(topLeftModel);
        // redundant calculation (can be stock in a map for each camp)

        // si l'id du camp correspend à l'id du camp du client alors drawRect avec un trait noir très épais sinon
        // drawRect avec un trait bleu normal

        if (camp.getId() == camp_id) {
            g2.setStroke(new BasicStroke(5));
            g2.setColor(Color.BLACK);
        } else {
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.BLUE);
        }
        g2.drawRect(topLeftView.x, topLeftView.y, Position.WIDTH * RATIO_X, Position.HEIGHT * RATIO_Y);

        drawWarriors(camp.getWarriors(), g2);
        drawSheep(camp.getSheep(), g2);
        drawFarmers(camp.getFarmers(), g2);
        drawFields(camp.getFields(), g2);
    }

    private void drawSheep(ArrayList<Sheep> sheep, Graphics2D g2) {
        for (Sheep l : sheep) {
                g2.setColor(Color.YELLOW);
                Point pointView = pointModelToView(l.getPosition());
                int width = Position.WIDTH_SHEEP * RATIO_X;
                int height = Position.HEIGHT_SHEEP * RATIO_Y;
                g2.fillRect(pointView.x - width / 2, pointView.y - height / 2, width, height);

        }
    }

    public void drawWarrior(Warrior warrior, Graphics2D g2) {
        g2.setColor(Color.RED);
        Point pointView = pointModelToView(warrior.getPosition());
        int width = Position.WIDTH_VIKINGS * RATIO_X;
        int height = Position.HEIGHT_VIKINGS * RATIO_Y;
        g2.fillRect(pointView.x - width / 2, pointView.y - height / 2, width, height);


    }

    public void drawWarriors(ArrayList<Warrior> warriors, Graphics2D g2) {
        for (Warrior warrior : warriors) {
            drawWarrior(warrior, g2);
        }
    }

    private void drawFarmers(ArrayList<Farmer> farmers, Graphics2D g2) {
        g2.setColor(Color.ORANGE);
        for (Farmer farmer : farmers) {
            Point pointView = pointModelToView(farmer.getPosition());
            int width = Position.WIDTH_VIKINGS * RATIO_X;
            int height = Position.HEIGHT_VIKINGS * RATIO_Y;
            g2.fillRect(pointView.x - width / 2, pointView.y - height / 2, width, height);

        }
    }

    private void drawFields(ArrayList<Field> fields, Graphics2D g2) {
        g2.setColor(Color.BLACK);
        for (Field field : fields) {
            Point pointView = pointModelToView(field.getPosition());
            int width = Position.WIDTH_FIELD * RATIO_X;
            int height = Position.HEIGHT_FIELD * RATIO_Y;
            g2.drawRect(pointView.x - width / 2, pointView.y - height / 2, width, height);
            if (field.isPlanted()) {
                g2.drawString(field.getResource(), pointView.x - width / 2, pointView.y - height / 2);

            }
        }
    }

    public void setPartie(Partie partieModel) {
        this.partieModel = partieModel;
        this.camp = partieModel.getCamp(this.camp_id);
        this.revalidate();
        this.repaint();

    }

    /**
     * set the offset to the one passed in parms
     * @param x
     * @param y
     */
    public void setOffset(int x, int y) {
        offsetDraggingX = x;
        offsetDraggingY = y;
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

    public void panelHide(){
        this.panneauControle.elseWhereClicked();
    }

    public void panelSetFarmerOnField(boolean isFarmerOnField, int idFarmer, int idField, boolean isFieldPlanted) {
        this.panneauControle.setFarmerOnField(isFarmerOnField, idFarmer, idField, isFieldPlanted);
    }

    // setFarmerNearSheep(true, idFarmer, idSheep);
    public void panelSetVikingNearSheep(boolean isVikingNearSheep, int idViking, int idSheep) {
        this.panneauControle.setVikingNearSheep(isVikingNearSheep, idViking, idSheep);
    }

    public void panelSetVisibility(boolean isVisible) {
        this.panneauControle.setVisibility(isVisible);
    }


    public void panelShowInfos(String entityType, float health) {
        this.panneauControle.showInfos(entityType, health);
    }

    public void panelShowInfos(String entityType, String ressource) {
        this.panneauControle.showInfos(entityType, ressource);
    }

    public void panelShowInfos(String entityType) {
        this.panneauControle.showInfos(entityType);
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public int getOffsetCampX() {
        return offsetCampX;
    }

    public int getOffsetCampY() {
        return offsetCampY;
    }

    public int getCamp_id() {
        return camp_id;
    }

    public Partie getPartieModel() {
        return partieModel;
    }

    public Camp getCamp() {
        return camp;
    }

    public SlidingMenu getSlidingMenu() {
        return this.panneauControle.getSlidingMenu();
    }
}