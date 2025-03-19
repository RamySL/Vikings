package client.view;

import javax.swing.*;
import java.awt.*;
import com.google.gson.Gson;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import network.PaquetPlant;
import network.PacketWrapper;
import network.ThreadCommunicationServer;

public class SlidingMenu extends JPanel {
    private Timer timer;
    private int targetX;
    private boolean isFarmerOnField;
    private boolean isVisible;
    private JButton plantButton;
    private JComboBox<String> plantComboBox;
    int posMenuY;
    int widthMenu;
    int windowWidth;
    int windowHeight;

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
        plantComboBox.addActionListener(e -> {
            String selectedVegetal = (String) plantComboBox.getSelectedItem(); // Récupérer le végétal choisi
            System.out.println("Végétal choisi: " + selectedVegetal);

            //ne pas oublier envoi d'un message au serveur pour planter le vegetal choisi

            // Cacher le JComboBox après sélection
            plantComboBox.setVisible(false);
            System.out.println("Le JComboBox a été caché après la sélection");
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
    public void updatePlantButtonVisibility(boolean isFarmerOnField) {
        this.isFarmerOnField = isFarmerOnField;

        // Si le fermier est sur un champ, le bouton devient visible
        if (isFarmerOnField) {
            plantButton.setVisible(true);
            System.out.println("Planter visible");
        } else {
            plantButton.setVisible(false);
            System.out.println("Planter caché");
        }
    }
    public void elseWhereClicked() {
        plantComboBox.setVisible(false);
        plantButton.setVisible(false);
    }
    public void setFarmerOnField(boolean isFarmerOnField) {
        this.isFarmerOnField = isFarmerOnField;
        refreshVisibility();
    }
    public void refreshVisibility() {
        plantButton.setVisible(isFarmerOnField);
    }

    /*private void handleComboBoxSelection(String selectedResource) {
        System.out.println("Selected resource: " + selectedResource);
        sendPlantPacketToServer(selectedResource, 1);
    }

    private void sendPlantPacketToServer(String resource, int fieldId) {
        PaquetPlant packet = new PaquetPlant(resource, fieldId, this.th);
        packet.sendPlantPacketToServer();

    }*/
}