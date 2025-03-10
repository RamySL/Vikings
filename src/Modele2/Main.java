package src.Modele2;

import javax.swing.*;
import src.Vue.PanelBle;
import src.Controller.PanelBleController;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Village Viking");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // On crée un vikings avec 7 blé au début 
        Vikings viking = new Vikings(8);

        // Le panel qui affiche les blé
        PanelBle panel = new PanelBle(viking.getBleList().get(0));
        frame.add(panel);

       
        PanelBleController controller = new PanelBleController(panel, viking, null); 
    
        Decroissance decroissance = new Decroissance(viking.getBleList(), controller);

      
        controller = new PanelBleController(panel, viking, decroissance);

        // On demarre le thread de decroissance
        new Thread(decroissance).start();

        frame.setVisible(true);
    }
}
