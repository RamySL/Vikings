package client.view;
import javax.swing.*;
import java.awt.*;


/**
 * Panel de contrôle qui gère l'affichage du bouton "Planter" et d'autres composants de contrôle.
 */
public class PanneauControle extends JPanel {
    private JButton plantButton;
    private boolean isFarmerOnField;
    private JComboBox<String> plantComboBox;

    public PanneauControle() {
        // Définir la disposition du panneau (GridLayout pour une disposition simple)
        setLayout(new GridLayout(20, 15));

        // Initialiser le bouton "Planter"
        plantButton = new JButton("Plant");

        // Définir une taille préférée pour le bouton
        plantButton.setPreferredSize(new Dimension(5, 5));


        // Initialiser le JComboBox avec des végétaux à planter
        String[] vegetals = {"Wheat", "Corn", "Mais"};
        plantComboBox = new JComboBox<>(vegetals);
        plantComboBox.setVisible(false); // Le JComboBox est caché au début

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

        // Ajouter les composants au panneau
        add(plantButton);
        add(plantComboBox);

        // Le bouton est initialement invisible
        plantButton.setVisible(false); // Le bouton est visible au début
    }

    // Getter pour le bouton "Planter"
    public JButton getPlantButton() {
        return plantButton;
    }

    /**
     * Mise à jour de la visibilité du bouton "Planter" en fonction de la position du fermier.
     */
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

        // Rafraîchir la vue pour que la mise à jour de la visibilité soit prise en compte immédiatement
        this.revalidate();
        this.repaint();
    }

    /**
     * Actualise la visibilité du bouton en fonction de l'état du fermier.
     */
    public void refreshVisibility() {
        plantButton.setVisible(isFarmerOnField);
    }

    /**
     * Méthode à appeler chaque fois que l'état du fermier change (si le fermier est sur un champ ou non)
     */
    public void setFarmerOnField(boolean isFarmerOnField) {
        this.isFarmerOnField = isFarmerOnField;
        refreshVisibility();  // Rafraîchir la visibilité du bouton en fonction de l'état du fermier
    }
}