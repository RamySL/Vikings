package sharedGUIcomponents.ComposantsPerso;

import javax.swing.*;
import java.awt.*;

/**
 * Pour créer des panels avec des composants centrés.
 * Met le panel en borderLayout et ajoute des panels vides à l'est, à l'ouest, au nord et au sud.
 * pour centrer les composants ajoutés dans BorderLayout.CENTER
 */
public  class CenteredPanel {
    /**
     * Précondition: panel est en BorderLayout
     * @param panel
     */
    public static void centerArrangement(JPanel panel){

        JPanel eastPanel = new JPanel();
        JPanel westPanel = new JPanel();
        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();
        eastPanel.setPreferredSize(new Dimension(200,100));
        westPanel.setPreferredSize(new Dimension(200,100));
        northPanel.setPreferredSize(new Dimension(100,70));
        southPanel.setPreferredSize(new Dimension(100,70));
        eastPanel.setOpaque(false);
        westPanel.setOpaque(false);
        northPanel.setOpaque(false);
        southPanel.setOpaque(false);

        panel.add(eastPanel,BorderLayout.EAST);
        panel.add(westPanel,BorderLayout.WEST);
        panel.add(southPanel,BorderLayout.SOUTH);
        panel.add(northPanel,BorderLayout.NORTH);
    }

    /**
     * Arrangement centrale et ajout du boutton passé en paramètre tout en haut à gauche
     * Pour revenir en arrière dans les vu<p>
     * * Précondition: panel est en BorderLayout
     * @param panel
     * @param button généralement le boutton de retour
     */
    public static void centerArrangement(JPanel panel,JButton button){

        JPanel eastPanel = new JPanel();
        JPanel westPanel = new JPanel();
        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();
        eastPanel.setPreferredSize(new Dimension(200,100));
        westPanel.setPreferredSize(new Dimension(200,100));
        northPanel.setPreferredSize(new Dimension(100,70));
        southPanel.setPreferredSize(new Dimension(100,70));
        eastPanel.setOpaque(false);
        westPanel.setOpaque(false);
        northPanel.setOpaque(false);
        southPanel.setOpaque(false);

        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        northPanel.add(button);

        panel.add(eastPanel,BorderLayout.EAST);
        panel.add(westPanel,BorderLayout.WEST);
        panel.add(southPanel,BorderLayout.SOUTH);
        panel.add(northPanel,BorderLayout.NORTH);
    }

}
