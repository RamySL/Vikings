package client.view;

import server.model.*;
import sharedGUIcomponents.ComposantsPerso.BarreDeTemps;
import sharedGUIcomponents.ComposantsPerso.BarreDeVie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static client.view.Animation.images_anim_0;

/**
 * Vue de la partie, Dessine tous les éléments du modèle
 * !!! Elle est hardcode pour deux joueurs !!!
 */


public class ViewPartie extends JPanel {
    private Animation animation = new Animation();

    public static ArrayList<Image> images_anim;

    private Image bareer = new ImageIcon("src/ressources/images/animation/enclos.png").getImage();

    private ArrayList<vikingAnim> vikings_thread;
    vikingAnim c1 = new vikingAnim(0);
    vikingAnim c2 = new vikingAnim(1);
    vikingAnim c3 = new vikingAnim(2);
    vikingAnim c4 = new vikingAnim(3);

    bleAnim bavp = new bleAnim();
    cowAnim cavp = new cowAnim();
    fermierAnim favp = new fermierAnim();
    sheepAnim savp = new sheepAnim();
    Image img_camp = new ImageIcon("src/ressources/images/grass_image.png").getImage();
    Image img_field = new ImageIcon("src/ressources/images/animation/terre.png").getImage();

    public static final int RATIO_X = 3,  RATIO_Y = 3;
    public static final int WIDTH_VIEW =  (2*Position.MARGIN_X+Position.WIDTH)*RATIO_X;
    public static final int HEIGHT_VIEW =  (2*Position.MARGIN_Y+Position.HEIGHT)*RATIO_Y;
    // the point where the camp shold be translated to
    public static final Point POINT_ANCRE =  new Point(Position.MARGIN_X*RATIO_X,Position.MARGIN_Y*RATIO_Y);
    // the amount that will be used to translate swing coordinates when draging
    private int offsetDraggingX = 0,  offsetDraggingY = 0;
    // the amount that will be used to translate swing coordinates when zooming relativeley to the mouse
    // the offset to use when translating to show the client camp on the screen
    private int  offsetCampX, offsetCampY;
    // scale factor for handeling the zoom
    private double scaleFactor = 1.0;
    private int camp_id ;
    private Camp camp ;
    private BarreDeTemps timeBar;

    private Partie partieModel ;
    private PanneauControle panneauControle ;  // Ajout du champ PanneauControle
    private int windowWidth = WIDTH_VIEW, windowHeight = HEIGHT_VIEW;

    private boolean launched = false;


     /**
      * Constructeur de la vue de la partie
      */
     public ViewPartie(ViewClient viewClient, Partie partieModel) {
         // recupere la taille de l'ecran du pc
         this.setPreferredSize(new Dimension(windowWidth, windowHeight));
         images_anim = new ArrayList<>();
         images_anim.add(new ImageIcon("src/ressources/animation/vicking/idle_0.png").getImage());
         images_anim.add(new ImageIcon("src/ressources/animation/vicking/idle_0_c1.png").getImage());
         images_anim.add(new ImageIcon("src/ressources/animation/vicking/idle_0_c2.png").getImage());
         images_anim.add(new ImageIcon("src/ressources/animation/vicking/idle_0_c3.png").getImage());

         vikings_thread = new ArrayList<>();
         vikings_thread.add(c1);
         vikings_thread.add(c2);
         vikings_thread.add(c3);
         vikings_thread.add(c4);

         bavp.start();
         cavp.start();
         favp.start();
         savp.start();

         this.partieModel = partieModel;
         this.panneauControle = new PanneauControle(windowWidth,  windowHeight);
         this.panneauControle.setOpaque(false);
         this.setLayout(new BorderLayout());
        this.add(panneauControle, BorderLayout.CENTER);  // Ajouter PanneauControle au panneau principal
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                 updatePanneauControlePosition();
             }
         });

         // Initialize the progress bar
         timeBar = new BarreDeTemps(20);
         timeBar.setTemps(20);

         // Add the progress bar to the bottom of the panel
         this.add(timeBar, BorderLayout.NORTH);

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
        //g2.drawLine(0, 0, 1000, 0);
        //g2.drawLine(0, 0, 0, 1000);
         drawCamps(g2);
        for (Camp camp : partieModel.getCamps()) {
            drawCamp(camp,g2);
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
        return new Point(pointModele.x * RATIO_X, (Position.HEIGHT + 2 * Position.MARGIN_Y - pointModele.y) * RATIO_Y);
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
     * Dessine tout ce qui doit être dessiner en premier comme ça les vikings etc apparaissent au dessus
     */
    public void drawCamps (Graphics2D g2){

        for (Camp camp : partieModel.getCamps()) {
            Point topLeftModel = Position.MAP_CAMPS_POSITION.get(camp.getId());
            Point topLeftView = pointModelToView(topLeftModel);
            if (camp.getId() == camp_id) {
                g2.setStroke(new BasicStroke(20));
                g2.setColor(Color.RED);
            } else {
                g2.setStroke(new BasicStroke(1));
                g2.setColor(Color.BLUE);
            }

            g2.drawRect(topLeftView.x, topLeftView.y, Position.WIDTH * RATIO_X, Position.HEIGHT * RATIO_Y);
            g2.drawImage(img_camp, topLeftView.x ,  topLeftView.y, Position.WIDTH * RATIO_X, Position.HEIGHT * RATIO_Y, null);

            drawFields(camp.getFields(), g2);
            //drawWheats(camp.getWheats(), g2);


        }

    }
    /**
     * @param camp
     * @param g2
     */
    private void drawCamp(Camp camp, Graphics2D g2) {
        drawBareer(camp.getEnclosses(), g2);
        drawWarriors(camp.getWarriors(), g2, camp);
        drawSheep(camp.getSheeps(), g2);
        drawFarmers(camp.getFarmers(), g2);
        drawCow(camp.getCows(), g2);


    }


    private void drawBareer(ArrayList<Enclos> enclos, Graphics2D g2) {
        for (Enclos e : enclos) {
            Point pointView = e.getPosition();
            Point topLeftView = pointModelToView(pointView);
            int width = Position.WIDTH_ENCLOS * RATIO_X;
            int height = Position.HEIGHT_ENCLOS * RATIO_Y;
            g2.drawImage(bareer, topLeftView.x - width/2, topLeftView.y - height/2, width, height, null);
        }
    }


    private void drawSheep(ArrayList<Sheep> sheep, Graphics2D g2) {
        for (Sheep l : sheep) {
                g2.setColor(Color.YELLOW);
                Point pointView = pointModelToView(l.getPosition());
                int width = Position.WIDTH_SHEEP * RATIO_X;
                int height = Position.HEIGHT_SHEEP * RATIO_Y;

                g2.drawImage(sheepAnim.anim, pointView.x - width + 10 , pointView.y - height  ,width+10, height+10, null);


        }
    }

    private void drawCow(ArrayList<Cow> cow, Graphics2D g2) {
        for (Cow l : cow) {
            g2.setColor(Color.green);
            Point pointView = pointModelToView(l.getPosition());
            int width = Position.WIDTH_COW * RATIO_X;
            int height = Position.HEIGHT_COW * RATIO_Y;
            //g2.fillRect(pointView.x - width / 2, pointView.y - height / 2, width, height);
            g2.drawImage(cowAnim.anim, pointView.x - width + 10 , pointView.y - height - 20  ,width+20, height+20, null);


        }
    }
    public void drawWarrior(Warrior warrior, Graphics2D g2, Camp camp) {
        g2.setColor(Color.RED);
        Point pointView = pointModelToView(warrior.getPosition());
        int width = Position.WIDTH_VIKINGS * RATIO_X;
        int height = Position.HEIGHT_VIKINGS * RATIO_Y;
        switch (camp.getId()){
            case 0:
                g2.drawImage(images_anim.get(0), pointView.x - width + 5, pointView.y - height , width*2, height*2, null);
                break;
            case 1:
                g2.drawImage(images_anim.get(1), pointView.x - width + 5, pointView.y - height , width*2, height*2, null);
                break;
            case 2:
                g2.drawImage(images_anim.get(2), pointView.x - width + 5, pointView.y - height , width*2, height*2, null);
                break;
            case 3:
                g2.drawImage(images_anim.get(3), pointView.x - width + 5, pointView.y - height , width*2, height*2, null);
                break;
        }


    }

    public void drawWarriors(ArrayList<Warrior> warriors, Graphics2D g2, Camp camp) {
        for (Warrior warrior : warriors) {
            drawWarrior(warrior, g2, camp);
        }
    }

    private void drawFarmers(ArrayList<Farmer> farmers, Graphics2D g2) {
        g2.setColor(Color.ORANGE);
        for (Farmer farmer : farmers) {
            Point pointView = pointModelToView(farmer.getPosition());
            int width = Position.WIDTH_VIKINGS * RATIO_X;
            int height = Position.HEIGHT_VIKINGS * RATIO_Y;
            g2.drawImage(fermierAnim.anim, pointView.x - width + 5, pointView.y - height , width*2, height*2, null);
        }
    }

    private void drawFields(ArrayList<Field> fields, Graphics2D g2) {
        g2.setColor(Color.BLACK);
        for (Field field : fields) {
            Point pointView = pointModelToView(field.getPosition());
            int width = Position.WIDTH_FIELD * RATIO_X;
            int height = Position.HEIGHT_FIELD * RATIO_Y;
//            System.out.println("field position: " +width);
//            System.out.println("field view position: " + height);

            g2.drawImage(img_field, pointView.x - width / 2, pointView.y - height / 2, width, height, null);
            if (field.isPlanted()) {
                if (field.getVegetable() instanceof Wheat){
                    drawWheats(g2, pointView.x - width / 2, pointView.y - height / 2 );
                }

            }
        }
    }


    private void drawWheats(Graphics2D g2, int point_of_view_x, int point_of_view_y ) {
        g2.setColor(Color.BLACK);
        int width = Position.WIDTH_WHEAT * RATIO_X;
        int height = Position.HEIGHT_WHEAT * RATIO_Y;
        for (int x = point_of_view_x+16; x < point_of_view_x +135; x += 15 ) {
            for (int y = point_of_view_y+13; y < point_of_view_y +93; y += 8 ) {
                g2.drawImage(bleAnim.anim, x - width / 2, y - height / 2, width, height, null);
            }
        }
    }



    public void setPartie(Partie partieModel) {
        this.partieModel = partieModel;
        this.camp = partieModel.getCamp(this.camp_id);
        this.revalidate();
        this.repaint();

        if(!launched){
            this.launched = true;
            int n = Array.getLength(this.partieModel.getCamps());
            for(int i = 0; i < n; i++){
                vikings_thread.get(i).start();
            }

        }

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

    /**
     * affiche le camp slectionné pour l'attaque
     */
    public void initAttack(Camp camp){
        this.panneauControle.initAttack(camp);
    }

    public void repli(Camp camp){
        this.panneauControle.repli(camp);
    }



    public void setAttack(int idRessource){
        this.panneauControle.setAttack(idRessource);
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
    public void setTime(int time) {
        timeBar.setTemps(time);
    }
    public void setEndGame(List<Integer> winningCampIds) {
        System.out.println("End game for camps: " + winningCampIds);
    }




}

