package client.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import client.controler.event.*;
import server.model.*;
import sharedGUIcomponents.ComposantsPerso.BarreDeVie;
import sharedGUIcomponents.ComposantsPerso.Buttons;

public class SlidingMenu extends JPanel {
    private Timer timer;
    private int targetX;
    private boolean isFarmerOnField, isFarmerNearSheep;
    private boolean isFieldPlanted;
    private boolean isVisible;
    private Buttons.BouttonJeu plantButton, harvestButton, eatButton, exitMenu;
    private JComboBox<String> plantComboBox;
    private JLabel entityLabel, ressourceLabel;
    private BarreDeVie healthBar;
    private int idField, idFarmer, idAnimal;
    private int posMenuY, widthMenu, windowWidth, windowHeight;
    private List<PlantListener> plantListeners = new ArrayList<>();

    public SlidingMenu(int x, int y, int width, int height) {
        this.posMenuY = y;
        this.widthMenu = width;
        this.windowWidth = x;
        this.windowHeight = height;


        setLayout(new GridBagLayout());
        setBackground(Color.LIGHT_GRAY);
        setBorder(new EmptyBorder(30, 0, 30, 0));
        setBounds(windowWidth + widthMenu, posMenuY, widthMenu, windowHeight);


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10); // marges
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;

        entityLabel = new JLabel();
        entityLabel.setVisible(false);
        entityLabel.setFont(new Font("MV Boli", Font.PLAIN, 20));
        gbc.gridy = 0;
        add(entityLabel, gbc);

        gbc.fill = GridBagConstraints.BOTH;

        gbc.weighty = 1.0;
        healthBar = new BarreDeVie(10);
        healthBar.setVisible(false);
        gbc.gridy++;
        add(healthBar, gbc);

        gbc.weighty = 0.0;
        ressourceLabel = new JLabel();
        ressourceLabel.setVisible(false);
        ressourceLabel.setFont(new Font("MV Boli", Font.PLAIN, 20));
        gbc.gridy++;
        add(ressourceLabel, gbc);

        plantButton = new Buttons.BouttonJeu("Plant");
        plantButton.setVisible(false);
        gbc.gridy++;
        add(plantButton, gbc);

        plantComboBox = new JComboBox<>(new String[]{"Wheat", "Corn", "Mais"});
        plantComboBox.setVisible(false);
        gbc.gridy++;
        add(plantComboBox, gbc);

        harvestButton = new Buttons.BouttonJeu("Harvest");
        harvestButton.setVisible(false);
        gbc.gridy++;
        add(harvestButton, gbc);

        eatButton = new Buttons.BouttonJeu("Eat");
        eatButton.setVisible(false);
        gbc.gridy++;
        add(eatButton, gbc);

        exitMenu = new Buttons.BouttonJeu("Exit");
        exitMenu.setVisible(false);
        gbc.gridy++;
        add(exitMenu, gbc);

        plantButton.addActionListener(e -> {
            plantButton.setVisible(false);
            plantComboBox.setVisible(true);
        });

        harvestButton.addActionListener(e -> {
            harvestButton.setVisible(false);
            handleHarvestButtonClicked();
        });

        eatButton.addActionListener(e -> {
            eatButton.setVisible(false);
            handleEatButtonClicked();
        });

        plantComboBox.addActionListener(e -> {
            String selectedVegetal = (String) plantComboBox.getSelectedItem();
            handleComboBoxSelection(selectedVegetal);
            plantComboBox.setVisible(false);
        });

        timer = new Timer(1, e -> slide());
    }

    public void updatePosition(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.setBounds(windowWidth + this.widthMenu, this.posMenuY, this.widthMenu, windowHeight);
    }

    public void toggle(boolean visible) {
        if (isVisible && !visible) {
            targetX = windowWidth + widthMenu;
        } else if (!isVisible && visible) {
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
        } else {
            timer.stop();
        }
    }

    public void toggleVisible() {
        targetX = windowWidth;
        isVisible = true;
        exitMenu.setVisible(true);
        timer.start();
    }

    public void toggleHide() {
        targetX = windowWidth + widthMenu;
        isVisible = false;
        timer.start();
    }

    public void updateButtonVisibility(boolean isFarmerOnField, int idFarmer, int idField, boolean isFieldPlanted) {
        this.idFarmer = idFarmer;
        this.idField = idField;
        this.isFieldPlanted = isFieldPlanted;
        this.isFarmerOnField = isFarmerOnField;

        if (isFarmerOnField) {
            if (!isFieldPlanted) {
                harvestButton.setVisible(false);
                if (!plantComboBox.isVisible()) {
                    plantButton.setVisible(true);
                }
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
        eatButton.setVisible(isFarmerNearSheep);
    }

    public void elseWhereClicked() {
        plantComboBox.setVisible(false);
        plantButton.setVisible(false);
        eatButton.setVisible(false);
        harvestButton.setVisible(false);
        exitMenu.setVisible(false);
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
        PlantEvent event = new PlantEvent(selectedResource, this.idFarmer, this.idField);
        EventBus.getInstance().publish("PlantEvent", event);
    }

    private void handleEatButtonClicked() {
        EatEvent event = new EatEvent(this.idFarmer, this.idAnimal);
        EventBus.getInstance().publish("EatEvent", event);
    }

    private void handleHarvestButtonClicked() {
        HarvestEvent event = new HarvestEvent(this.idFarmer, this.idField);
        EventBus.getInstance().publish("HarvestEvent", event);
    }

    public void showInfos(String entity, float health) {
        showInfos(entity);
        healthBar.setVieMax(100);
        healthBar.setVie((int) health);
        healthBar.setVisible(true);
    }

    public void showInfos(String entity) {
        entityLabel.setText(entity);
        entityLabel.setVisible(true);
        exitMenu.setVisible(true);
    }

    public void showInfos(String entity, String ressource) {
        showInfos(entity);
        ressourceLabel.setText("Ressource: " + ressource);
        ressourceLabel.setVisible(true);
    }

    public void hideInfos() {
        if (healthBar != null) healthBar.setVisible(false);
        if (entityLabel != null) entityLabel.setVisible(false);
        if (ressourceLabel != null) ressourceLabel.setVisible(false);
    }

    public Buttons.BouttonJeu getExitMenu() {
        return exitMenu;
    }
}
