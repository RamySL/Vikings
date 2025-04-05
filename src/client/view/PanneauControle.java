package client.view;

import javax.swing.*;
import java.awt.Dimension;


/** * PanneauControle is a JPanel that contains a SlidingMenu.
 * It manages the visibility and position of the SlidingMenu based on user interactions.
 * It also provides methods to update the SlidingMenu's visibility and show information about entities.
 */
public class PanneauControle extends JPanel {
    private SlidingMenu slidingMenu;
    private int menuWidth=200;
    private int posMenuY=0;
    private int posMenuX=getWidth();
    private int menuHeight=getHeight();

    /**
     * Constructor for PanneauControle.
     * @param x The x-coordinate of the SlidingMenu.
     * @param height The height of the SlidingMenu.
     */
    public PanneauControle(int x, int height) {
        setLayout(null);
        this.posMenuX=x;
        this.menuHeight=height;
        setPreferredSize(new Dimension(this.menuWidth, this.menuHeight));
        slidingMenu = new SlidingMenu(this.posMenuX,this.posMenuY, this.menuWidth,this.menuHeight);
        add(slidingMenu);
    }


    /**
     * Updates the position of the SlidingMenu based on the given width and height.
     * @param width
     * @param height
     * @see SlidingMenu#updatePosition(int, int)
     */
    public void updatePosition(int width, int height) {
        this.menuHeight = height;
        this.posMenuX = width - this.menuWidth;  // Mise à jour de la position X pour être égale à la largeur de la fenêtre moins la largeur du menu
        slidingMenu.updatePosition(posMenuX, menuHeight);
    }

    /**
     * Updates the visibility of the SlidingMenu based on whether the farmer is on the field.
     * @param isFarmerOnField True if the farmer is on the field, false otherwise.
     * @see SlidingMenu#toggleVisible()
     * @see SlidingMenu#toggleHide()
     */
    public void updateSlidingMenuVisibility(boolean isFarmerOnField) {
        if (isFarmerOnField) {
            slidingMenu.toggleVisible();
        } else {
            slidingMenu.toggleHide();
        }
    }


    /**
     * Sets the visibility of the SlidingMenu based on whether the farmer is on the field.
     * This method also updates the visibility of the plant button in the SlidingMenu.
     * @param isFarmerOnField True if the farmer is on the field, false otherwise.
     * @param farmerX The x-coordinate of the farmer.
     * @param farmerY The y-coordinate of the farmer.
     * @param fieldX The x-coordinate of the field.
     * @param fieldY The y-coordinate of the field.
     * @param isFieldPlanted True if the field is planted, false otherwise.
     * @see SlidingMenu#updatePlantButtonVisibility(boolean, int, int, int, int, boolean)
     */
    public void setFarmerOnField(boolean isFarmerOnField, int farmerX, int farmerY, int fieldX, int fieldY, boolean isFieldPlanted) {
        updateSlidingMenuVisibility(isFarmerOnField);
        slidingMenu.updatePlantButtonVisibility(isFarmerOnField, farmerX, farmerY, fieldX, fieldY, isFieldPlanted);
    }

    /**
     * Handles the event when the user clicks on the "elseWhere" button in the SlidingMenu.
     * This method toggles the visibility of the SlidingMenu and hides the information panel.
     * It also calls the elseWhereClicked method in the SlidingMenu.
     * @see SlidingMenu#elseWhereClicked()
     * @see SlidingMenu#toggleHide()
     * @see SlidingMenu#hideInfos()
     */
    public void elseWhereClicked() {
        slidingMenu.toggleHide();
        slidingMenu.hideInfos();
        slidingMenu.elseWhereClicked();
    }

    /**
     * Returns the SlidingMenu object.
     * @return The SlidingMenu object.
     */
    public SlidingMenu getSlidingMenu() {
        return slidingMenu;
    }

    /**
     * Sets the visibility of the SlidingMenu based on the given boolean value.
     * @param visible True to show the SlidingMenu, false to hide it.
     * @see SlidingMenu#toggleVisible()
     * @see SlidingMenu#toggleHide()
     */
    public void setVisibility(boolean visible) {
        if (visible) {
            System.out.println("est visibme");
            slidingMenu.toggleVisible();
        } else {
            slidingMenu.toggleHide();
        }
    }

    /**
     * Shows information about the given entity in the SlidingMenu.
     * @param entity The name of the entity.
     * @param health The health of the entity.
     * @see SlidingMenu#showInfos(String, float)
     * @see SlidingMenu#showInfos(String)
     * @see SlidingMenu#showInfos(String, String)
     */
    public void showInfos(String entity, float health) {
        slidingMenu.showInfos(entity, health);
    }
    public void showInfos(String entity) {
        slidingMenu.showInfos(entity);
    }
    public void showInfos(String entity, String ressource) {
        slidingMenu.showInfos(entity, ressource);
    }

}
