package client.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class sheepAnim extends Thread {
    public static Image anim = new ImageIcon("src/ressources/images/animation/sheep.png").getImage();
    private List<Image> images;
    public static boolean iSrunning = false; // pour savoir si l'on est en mouvement ou pas
    public static boolean left = false; // pour la direction
    private int delay = 300;
    private List<Image> degat_anim;

    public sheepAnim() {
        images = new ArrayList<>();
        images.add(anim);
        images.add(new ImageIcon("src/ressources/images/animation/sheep_2.png").getImage());
    }

    @Override
    public void run() {
        try {
            int i = 1;
            while (true) { // à modifier pour definir la fin de jeu
                if (!iSrunning) {
                    anim = images.get(i);
                    Thread.sleep(delay);
                    i = (i + 1) % images.size();
                    Thread.sleep(delay);
                }

                // modifier ici pour rajouter l'anim si il y a mouvment, modifie aussi les variables au dessus
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
