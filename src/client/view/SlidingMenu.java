package client.view;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;

import client.controler.event.PlantEvent;
import client.controler.event.PlantListener;
import server.model.*;
import client.controler.event.EventBus;

public class SlidingMenu extends JPanel {
    private Timer timer;
    private int targetX;
    private boolean isFarmerOnField;
    private boolean isFieldPlanted;
    private boolean isVisible;
    private JButton plantButton;
    private JComboBox<String> plantComboBox;
    private int farmerX, farmerY, fieldX, fieldY;
    int posMenuY, widthMenu, windowWidth, windowHeight;
    private List<PlantListener> plantListeners = new ArrayList<>();

    public SlidingMenu(int x, int y, int width, int height) {
        this.posMenuY=y;
        this.widthMenu=width;
        this.windowWidth=x;
        this.windowHeight=height;
        setLayout(null);
        setBackground(Color.LIGHT_GRAY);
        setBounds(windowWidth+widthMenu, posMenuY, widthMenu, windowHeight); // Initial position outside the view

        plantButton = new JButton("Plant");
        plantButton.setBounds(50, 50, 100, 40);
        plantButton.setVisible(false);
        add(plantButton);

        String[] vegetals = {"Wheat", "Corn", "Mais"};
        plantComboBox = new JComboBox<>(vegetals);
        plantComboBox.setBounds(50, 100, 100, 40);
        plantComboBox.setVisible(false);
        add(plantComboBox);
        // Initialiser le JComboBox avec des végétaux à planter

        // Action du bouton "Planter"
        plantButton.addActionListener(e -> {
            plantButton.setVisible(false);  // Cacher le bouton lorsque "Planter" est cliqué
            plantComboBox.setVisible(true);  // Afficher le JComboBox pour choisir un végétal
            System.out.println("Le choix du végétal est maintenant visible");
        });
        // Ajouter un ActionListener au JComboBox pour récupérer la sélection
        plantComboBox.addActionListener(e -> {
            if (e.getSource() == plantComboBox) {
                String selectedVegetal = (String) plantComboBox.getSelectedItem(); // Récupérer le végétal choisi
                System.out.println("Végétal choisi: " + selectedVegetal);

                // Appeler la méthode pour traiter la sélection
                handleComboBoxSelection(selectedVegetal);

                // Cacher le JComboBox après sélection
                plantComboBox.setVisible(false);
                System.out.println("Le JComboBox a été caché après la sélection");
            }
        });


        timer = new Timer(1, e -> slide());
    }

   public void updatePosition(int windowWidth, int windowHeight) {
       this.windowWidth = windowWidth;
       this.windowHeight = windowHeight;
       // On met à jour les positions x et y
       this.setBounds(windowWidth  , this.posMenuY, this.widthMenu, windowHeight);  // Position X = largeur de la fenêtre - largeur du menu
   }


    public void toggle() {
        if (isVisible) {
            targetX = windowWidth+widthMenu;
        } else {
            targetX = windowWidth;
        }
        isVisible = !isVisible;
        timer.start();
    }

    private void slide() {
        int currentX = getX();
        if (currentX != targetX) {
            int step = (targetX - currentX) / 10;
            setLocation(currentX + step, getY());
            revalidate();
            repaint();
        } else {
            timer.stop();
        }
    }
    public void toggleVisible(){
        targetX = windowWidth;
        isVisible =true;
        timer.start();
    }
    public void toggleHide(){
        targetX = windowWidth+widthMenu;
        isVisible =false;
        timer.start();
    }

    public void updatePlantButtonVisibility(boolean isFarmerOnField, int farmerX, int farmerY, int fieldX, int fieldY, boolean isFieldPlanted) {
        this.isFarmerOnField = isFarmerOnField;
        this.farmerX = farmerX;
        this.farmerY = farmerY;
        this.fieldX = fieldX;
        this.fieldY = fieldY;

        // If the farmer is on a field, the button becomes visible
        if (isFarmerOnField) {
            if (!isFieldPlanted) {
                plantButton.setVisible(true);
                //System.out.println("Plant button visible at farmer position (" + farmerX + ", " + farmerY + ") and field position (" + fieldX + ", " + fieldY + ")");
            }} else {
            plantButton.setVisible(false);
            //System.out.println("Plant button hidden");
        }
    }
    public void elseWhereClicked() {
        plantComboBox.setVisible(false);
        plantButton.setVisible(false);
    }
    public void setFarmerOnField(boolean isFarmerOnField, Farmer farmer, Field field) {
        this.isFarmerOnField = isFarmerOnField;
        refreshVisibility();
    }
    public void refreshVisibility() {
        plantButton.setVisible(isFarmerOnField);
    }


    public void addPlantListener(PlantListener listener) {
        plantListeners.add(listener);
    }

   private void handleComboBoxSelection(String selectedResource) {
       //System.out.println("Selected resource: " + selectedResource);

       // Créer et publier l'événement
       PlantEvent event = new PlantEvent(selectedResource, this.farmerX, this.farmerY, this.fieldX, this.fieldY);
       EventBus.getInstance().publish("PlantEvent", event);
   }
}