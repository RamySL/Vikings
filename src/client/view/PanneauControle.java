package client.view;

import javax.swing.*;
import java.awt.Dimension;

/**
 * Panel de contrôle qui gère l'affichage du bouton "Planter" et d'autres composants de contrôle.
 */

public class PanneauControle extends JPanel {
    private SlidingMenu slidingMenu;
    private int menuWidth=200;
    private int posMenuY=0;
    private int posMenuX=getWidth();
    private int menuHeight=getHeight();

    public PanneauControle(int x, int height) {
        setLayout(null);
        this.posMenuX=x;
        this.menuHeight=height;
        setPreferredSize(new Dimension(this.menuWidth, this.menuHeight));
        slidingMenu = new SlidingMenu(this.posMenuX,this.posMenuY, this.menuWidth,this.menuHeight);
        add(slidingMenu);

    }


    public void updatePosition(int width, int height) {
        this.menuHeight = height;
        this.posMenuX = width - this.menuWidth;  // Mise à jour de la position X pour être égale à la largeur de la fenêtre moins la largeur du menu
        slidingMenu.updatePosition(posMenuX, menuHeight);
    }

    public void updateSlidingMenuVisibility(boolean isFarmerOnField) {
        if (isFarmerOnField) {
            slidingMenu.toggleVisible();
        } else {
            slidingMenu.toggleHide();
        }
    }


    public void setFarmerOnField(boolean isFarmerOnField, int farmerX, int farmerY, int fieldX, int fieldY, boolean isFieldPlanted) {
        updateSlidingMenuVisibility(isFarmerOnField);
        slidingMenu.updatePlantButtonVisibility(isFarmerOnField, farmerX, farmerY, fieldX, fieldY, isFieldPlanted);
    }

    public void elseWhereClicked() {
        slidingMenu.toggleHide();
    }

    public SlidingMenu getSlidingMenu() {
        return slidingMenu;
    }
}
