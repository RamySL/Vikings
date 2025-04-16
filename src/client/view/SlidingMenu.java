package client.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import client.controler.event.*;
import server.model.*;
import sharedGUIcomponents.ComposantsPerso.BarreDeVie;
import sharedGUIcomponents.ComposantsPerso.Buttons;
import sharedGUIcomponents.ComposantsPerso.FontPerso;

public class SlidingMenu extends JPanel {
    private Timer timer;
    private int targetX;
    private boolean isFarmerOnField, isVikingNearSheep;
    private boolean isFieldPlanted;
    private boolean isVisible;
    private Buttons.BouttonJeu plantButton, harvestButton, eatButton, exitMenu, attackButton, repliButton;
    private JComboBox<String> plantComboBox;
    private JLabel entityLabel, ressourceLabel;

    private JTextField textFieldNbVikings;

    private int idField, idViking;
    private int idAnimal;
    int posMenuY, widthMenu, windowWidth, windowHeight;
    private BarreDeVie healthBar;
    private List<PlantListener> plantListeners = new ArrayList<>();


    private ArrayList<Integer> idRessources = new ArrayList<>();
    // pour stocker le nombre de viking à envoyer pour l'attaque de chaque ressources
    private ArrayList<Integer> nbVikings = new ArrayList<>();
    private int camp_to_attack = -1;


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
        entityLabel.setFont(FontPerso.mvBoli(20));
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
        ressourceLabel.setPreferredSize(new Dimension(widthMenu, windowHeight));

        ressourceLabel.setFont(FontPerso.mvBoli(20));
        gbc.gridy++;
        add(ressourceLabel, gbc);

        textFieldNbVikings = new JTextField("1");
        textFieldNbVikings.setVisible(false);
        textFieldNbVikings.setFont(FontPerso.mvBoli(20));
        gbc.gridy++;
        add(textFieldNbVikings, gbc);

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

        attackButton = new Buttons.BouttonJeu("Attack");
        attackButton.setVisible(false);
        gbc.gridy++;
        add(attackButton, gbc);

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

        attackButton.addActionListener( e -> {
            attackButton.setVisible(false);
            handleAttackButtonClicked();
        });

        textFieldNbVikings.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // get the text from the text field and convert it to Integer
                    attackButton.setVisible(true);
                    String text = textFieldNbVikings.getText();
                    SlidingMenu.this.nbVikings.add(Integer.parseInt(text));
                    SlidingMenu.this.initAttack();
                }
            }
        });
        repliButton = new Buttons.BouttonJeu("Repli");
        repliButton.setVisible(false);
        gbc.gridy++;
        add(repliButton, gbc);
        repliButton.addActionListener(e -> {
            handleRepliButtonClicked();
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


    /**
     * Initializes the attack menu for a camp.
     * @param camp
     */
    public void initAttack(Camp camp) {
        this.camp_to_attack = camp.getId();
        attackButton.setVisible(false);
        textFieldNbVikings.setVisible(false);
        exitMenu.setVisible(true);
        entityLabel.setText("Camp " + camp.getId() + ": " + camp.getUsername());
        entityLabel.setVisible(true);
        ressourceLabel.setText("<html>Choisit une<br>ressource</html>");
        ressourceLabel.setVisible(true);
        plantButton.setVisible(false);
        eatButton.setVisible(false);
        harvestButton.setVisible(false);
    }

    /**
     * Surcharge pour le cas ou on est entrain de choisir plusieur ressources à attaquer
     */
    public void initAttack(){
        textFieldNbVikings.setVisible(false);
        exitMenu.setVisible(true);
        ressourceLabel.setText("<html>Choisit une<br>ressource</html>");
        ressourceLabel.setVisible(true);
        plantButton.setVisible(false);
        eatButton.setVisible(false);
        harvestButton.setVisible(false);
        repliButton.setVisible(true);
    }


    /**
     * Présondition: état du menu est déja avec les infos du camp à attaquer
     */
    public void setAttack(int idRessource) {
        this.idRessources.add(idRessource);
        ressourceLabel.setVisible(false); // pour enlever le "Choisit une ressource"
        textFieldNbVikings.setVisible(true);
        attackButton.setVisible(false);
    }


    public void updateButtonVisibility(boolean isFarmerOnField, int idFarmer, int idField, boolean isFieldPlanted) {
        eatButton.setVisible(false);
        textFieldNbVikings.setVisible(false);
        attackButton.setVisible(false);

        this.idViking = idFarmer;
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
    public void updateButtonVisibility(boolean isVikingNearSheep, int idViking, int idSheep) {
        this.isVikingNearSheep = isVikingNearSheep;
        this.idViking = idViking;
        this.idAnimal = idSheep;

        eatButton.setVisible(isVikingNearSheep);
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

    /**
     * Handles the selection of a resource from the combo box.
     * It creates a PlantEvent with the selected resource and publishes it to the EventBus.
     *
     * @param selectedResource The selected resource from the combo box.
     */
   private void handleComboBoxSelection(String selectedResource) {

       // Créer et publier l'événement
       PlantEvent event = new PlantEvent(selectedResource, this.idViking, this.idField);
       EventBus.getInstance().publish("PlantEvent", event);
   }

   private void handleEatButtonClicked() {
       // Créer et publier l'événement
       EatEvent event = new EatEvent(this.idViking, this.idAnimal);
       EventBus.getInstance().publish("EatEvent", event);
    }

    private void handleHarvestButtonClicked() {
        HarvestEvent event = new HarvestEvent(this.idViking, this.idField);
        EventBus.getInstance().publish("HarvestEvent", event);

    }

    private void handleAttackButtonClicked() {
        // transform this.nbVikings and this.idRessources to int[]
        int[] idRessources = this.idRessources.stream().mapToInt(Integer::intValue).toArray();
        int[] nbVikings = this.nbVikings.stream().mapToInt(Integer::intValue).toArray();
        AttackEvent event = new AttackEvent(this.camp_to_attack, idRessources, nbVikings);
        EventBus.getInstance().publish("AttackEvent", event);
        // reinit idressources and nbVikings and camp_to_attack
        this.idRessources.clear();
        this.nbVikings.clear();
        this.camp_to_attack = -1;

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
    private void handleRepliButtonClicked() {
        if (camp_to_attack != -1) {
            RepliEvent event = new RepliEvent(camp_to_attack);
            EventBus.getInstance().publish("RepliEvent", event);
        }

        // Réinitialise les états
        this.idRessources.clear();
        this.nbVikings.clear();
        this.camp_to_attack = -1;

        // Masquer boutons/inputs liés à l’attaque
        attackButton.setVisible(false);
        textFieldNbVikings.setVisible(false);
        repliButton.setVisible(false);
        ressourceLabel.setVisible(false);
    }
}
