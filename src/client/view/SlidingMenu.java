package client.view;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;

import client.controler.event.*;
import server.model.*;

/**
 * SlidingMenu is a JPanel that slides in and out of view, providing a menu for planting crops.
 * It contains buttons and combo boxes for user interaction.
 * The menu can be toggled visible or hidden, and it updates its position based on the farmer's location.
 * It also handles the visibility of the plant button and combo box based on the farmer's state.
 * It can show information about the selected entity and its health.
 * It uses a timer to animate the sliding effect.
 * It also listens for plant events and updates the UI accordingly.
 * It is a subclass of JPanel and implements the ActionListener interface.
 */
public class SlidingMenu extends JPanel {
    private Timer timer;
    private int targetX;
    private boolean isFarmerOnField, isFarmerNearSheep;
    private boolean isFieldPlanted;
    private boolean isVisible;
    private JButton plantButton, harvestButton, eatButton, exitMenu;
    private JComboBox<String> plantComboBox;
    private JLabel entityLabel, ressourceLabel;
    private JProgressBar healthBar;
    // normalement plus générique
    private int idField, idFarmer, idAnimal;
    int posMenuY, widthMenu, windowWidth, windowHeight;
    private List<PlantListener> plantListeners = new ArrayList<>();

    /**
     * Constructor for SlidingMenu.
     * Initializes the menu with the specified position and size.
     * Sets up the plant button and combo box, and adds action listeners for user interaction.
     *
     * @param x      The x-coordinate of the window.
     * @param y      The y-coordinate of the menu.
     * @param width  The width of the menu.
     * @param height The height of the menu.
     */
    public SlidingMenu(int x, int y, int width, int height) {
        this.posMenuY=y;
        this.widthMenu=width;
        this.windowWidth=x;
        this.windowHeight=height;
        setLayout(null);
        setBackground(Color.LIGHT_GRAY);
        setBounds(windowWidth+widthMenu, posMenuY, widthMenu, windowHeight); // Initial position outside the view

        plantButton = new JButton("Plant");
        plantButton.setBounds(50, 200, 100, 40);
        plantButton.setVisible(false);
        add(plantButton);

        eatButton = new JButton("Eat");
        eatButton.setBounds(50, 200, 100, 40);
        eatButton.setVisible(false);
        add(eatButton);

        harvestButton = new JButton("Harvest");
        harvestButton.setBounds(50, 200, 100, 40);
        harvestButton.setVisible(false);
        add(harvestButton);

        exitMenu = new JButton("Exit");
        exitMenu.setBounds(50, 300, 100, 40);
        exitMenu.setVisible(false);
        add(exitMenu);

        String[] vegetals = {"Wheat", "Corn", "Mais"};
        plantComboBox = new JComboBox<>(vegetals);
        plantComboBox.setBounds(50, 200, 100, 40);
        plantComboBox.setVisible(false);
        add(plantComboBox);
        // Initialiser le JComboBox avec des végétaux à planter

        // Action du bouton "Planter"
        plantButton.addActionListener(e -> {
            plantButton.setVisible(false);  // Cacher le bouton lorsque "Planter" est cliqué
            plantComboBox.setVisible(true);  // Afficher le JComboBox pour choisir un végétal
        });
        // Action du bouton "Récolter"
        harvestButton.addActionListener(e -> {
            harvestButton.setVisible(false);  // Cacher le bouton lorsque "Récolter" est cliqué
            handleHarvestButtonClicked();
        });
        // Action du bouton "Manger"
        eatButton.addActionListener(e -> {
            eatButton.setVisible(false);  // Cacher le bouton lorsque "Manger" est cliqué
            handleEatButtonClicked();
        });

        // Action du bouton "Quitter"
        exitMenu.addActionListener(e -> {
            toggleHide();
            handleExitButtonClicked();
        });

        // Ajouter un ActionListener au JComboBox pour récupérer la sélection
        plantComboBox.addActionListener(e -> {
            if (e.getSource() == plantComboBox) {
                String selectedVegetal = (String) plantComboBox.getSelectedItem(); // Récupérer le végétal choisi

                // Appeler la méthode pour traiter la sélection
                handleComboBoxSelection(selectedVegetal);

                // Cacher le JComboBox après sélection
                plantComboBox.setVisible(false);
            }
        });


        timer = new Timer(1, e -> slide());
    }

    /**
     * Updates the position of the menu based on the window size.
     * Sets the bounds of the menu to be at the right edge of the window.
     *
     * @param windowWidth  The width of the window.
     * @param windowHeight The height of the window.
     */
   public void updatePosition(int windowWidth, int windowHeight) {
       this.windowWidth = windowWidth;
       this.windowHeight = windowHeight;
       // On met à jour les positions x et y
       this.setBounds(windowWidth  , this.posMenuY, this.widthMenu, windowHeight);  // Position X = largeur de la fenêtre - largeur du menu
   }

    /**
     * Toggles the visibility of the menu.
     * If the menu is currently visible, it will slide out of view.
     * If it is hidden, it will slide into view.
     *
     * @param visible true to show the menu, false to hide it.
     */
    public void toggle(boolean visible) {
        if (isVisible && !visible) {
            targetX = windowWidth+widthMenu;
        } else if (!isVisible && visible) {
            targetX = windowWidth;
        }
        isVisible = !isVisible;
        timer.start();
    }

    /**
     * Slides the menu in or out of view based on the targetX position.
     * It updates the position of the menu gradually until it reaches the targetX.
     */
    private void slide() {
        int currentX = getX();
        if (currentX != targetX) {
            int step = (targetX - currentX) / 10;
            setLocation(currentX + step, getY());
        } else {
            timer.stop();
        }
    }
    /**
     * Toggles the visibility of the menu to show or hide it.
     * It sets the targetX position and starts the timer for sliding animation.
     */
    public void toggleVisible(){
        targetX = windowWidth;
        isVisible =true;
        exitMenu.setVisible(true);
        timer.start();
    }
    /**
     * Hides the menu by setting the targetX position to the right edge of the window.
     * It starts the timer for sliding animation.
     */
    public void toggleHide(){
        targetX = windowWidth+widthMenu;
        isVisible =false;
        timer.start();
    }
    /**
     * Updates the visibility of the plant button based on the farmer's position and field status.
     * If the farmer is on a field and the field is not planted, the button becomes visible.
     * If the farmer is not on a field, the button is hidden.
     *
     * @param isFarmerOnField true if the farmer is on a field, false otherwise.
     * @param isFieldPlanted true if the field is already planted, false otherwise.
     */
    public void updateButtonVisibility(boolean isFarmerOnField, int idFarmer, int idField, boolean isFieldPlanted) {
        this.idFarmer = idFarmer;
        this.idField = idField;
        this.isFieldPlanted = isFieldPlanted;
        this.isFarmerOnField = isFarmerOnField;

        // If the farmer is on a field, the button becomes visible
        if (isFarmerOnField) {
            if (!isFieldPlanted) {
                plantButton.setVisible(true);
                harvestButton.setVisible(false);
            } else {
                plantButton.setVisible(false);
                harvestButton.setVisible(true);
            }
        } else {
            plantButton.setVisible(false);
            harvestButton.setVisible(false);
        }
    }
    public void updateButtonVisibility(boolean isFarmerNearSheep, int idFramer, int idSheep) {
        this.isFarmerNearSheep = isFarmerNearSheep;
        this.idFarmer = idFramer;
        this.idAnimal = idSheep;

        // If the farmer is near a sheep, the button becomes visible
        if (isFarmerNearSheep) {
            eatButton.setVisible(true);
        } else {
            eatButton.setVisible(false);
        }
    }
    /**
     * Hides the plant button and combo box when clicking elsewhere on the screen.
     * It sets their visibility to false.
     */
    public void elseWhereClicked() {
        plantComboBox.setVisible(false);
        plantButton.setVisible(false);
        eatButton.setVisible(false);
        harvestButton.setVisible(false);
        exitMenu.setVisible(false);
    }
    /**
     * Sets the visibility of the farmer on the field.
     * If the farmer is on a field, the plant button becomes visible.
     * If the farmer is not on a field, the button is hidden.
     *
     * @param isFarmerOnField true if the farmer is on a field, false otherwise.
     * @param farmer          The farmer object.
     * @param field           The field object.
     */
    public void setFarmerOnField(boolean isFarmerOnField, Farmer farmer, Field field) {
        this.isFarmerOnField = isFarmerOnField;
        refreshVisibility();
    }
    /**
     * Refreshes the visibility of the plant button based on the farmer's state.
     * If the farmer is on a field, the button becomes visible.
     * If the farmer is not on a field, the button is hidden.
     */
    public void refreshVisibility() {
        plantButton.setVisible(isFarmerOnField);
    }

    /**
     * Adds a PlantListener to the list of listeners.
     * This allows other classes to listen for plant events.
     *
     * @param listener The PlantListener to be added.
     */
    public void addPlantListener(PlantListener listener) {
        plantListeners.add(listener);
    }

    /**
     * Handles the selection of a resource from the combo box.
     * It creates a PlantEvent with the selected resource and publishes it to the EventBus.
     *
     * @param selectedResource The selected resource from the combo box.
     */
   private void handleComboBoxSelection(String selectedResource) {

       // Créer et publier l'événement
       PlantEvent event = new PlantEvent(selectedResource, this.idFarmer, this.idField);
       EventBus.getInstance().publish("PlantEvent", event);
   }

   private void handleEatButtonClicked() {
       // Créer et publier l'événement
       EatEvent event = new EatEvent(this.idFarmer, this.idAnimal);
       EventBus.getInstance().publish("EatEvent", event);
    }

    private void handleHarvestButtonClicked(){
        HarvestEvent event = new HarvestEvent(this.idFarmer, this.idField);
        EventBus.getInstance().publish("HarvestEvent", event);
    }

    private void handleExitButtonClicked(){
        ExitEvent event = new ExitEvent("Exit");
        EventBus.getInstance().publish("ExitEvent", event);
    }
    /**
     * Shows information about the selected entity and its health.
     * It updates the labels and progress bar to display the entity's name and health.
     *
     * @param entity The name of the entity.
     * @param health The health of the entity.
     */
    public void showInfos(String entity, float health) {
        showInfos(entity);
        if (healthBar == null){
            healthBar = new JProgressBar();
            healthBar.setBounds(50,100,100,40);
            add(healthBar);
        }
        healthBar.setStringPainted(true);
        healthBar.setValue((int) health);
        healthBar.setVisible(true);

    }
    /**
     * Shows information about the selected entity.
     * It updates the label to display the entity's name.
     *
     * @param entity The name of the entity.
     */
    public void showInfos(String entity){
        if (entityLabel == null) {
            entityLabel=new JLabel();
            entityLabel.setBounds(50, 50, 100, 40);
            add(entityLabel);
        }
        entityLabel.setText(entity);
        entityLabel.setVisible(true);
        exitMenu.setVisible(true);
    }
    /**
     * Shows information about the selected entity and its resource.
     * It updates the labels to display the entity's name and resource.
     *
     * @param entity   The name of the entity.
     * @param ressource The resource associated with the entity.
     */
    public void showInfos(String entity, String ressource) {
        showInfos(entity);
        if (ressourceLabel == null ) {
            ressourceLabel = new JLabel();
            ressourceLabel.setBounds(30, 100, 100, 40);
            add(ressourceLabel);
        }
        ressourceLabel.setText("Ressource: " + ressource);
        ressourceLabel.setVisible(true);
    }
    /**
     * Hides the health bar and labels when clicking elsewhere on the screen.
     * It sets their visibility to false.
     */
    public void hideInfos(){
        if (healthBar != null) {
            healthBar.setVisible(false);
        }
        if (entityLabel != null) {
            entityLabel.setVisible(false);
        }
        if (ressourceLabel != null) {
            ressourceLabel.setVisible(false);
        }
    }

}