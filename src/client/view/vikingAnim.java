package client.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class vikingAnim extends Thread {
    public static Image anim =  new ImageIcon("src/ressources/images/animation/vicking/idle_0.png").getImage();
    private List<Image> images;
    private List<Image> images_r;
    public static boolean iSrunning = false; // pour savoir si l'on est en mouvement ou pas
    public static boolean left = false; // pour la direction
    private int delay = 125;

    public vikingAnim() {
            images = new ArrayList<>();
            images.add(anim);
            images.add(new ImageIcon("src/ressources/images/animation/vicking/idle_1.png").getImage());
            images.add(new ImageIcon("src/ressources/images/animation/vicking/idle_2.png").getImage());
            images.add(new ImageIcon("src/ressources/images/animation/vicking/idle_3.png").getImage());
            images_r = new ArrayList<>();
            images_r.add(new ImageIcon("src/ressources/images/animation/vicking/idle_0_r.png").getImage());
            images_r.add(new ImageIcon("src/ressources/images/animation/vicking/idle_1_r.png").getImage());
            images_r.add(new ImageIcon("src/ressources/images/animation/vicking/idle_2_r.png").getImage());
            images_r.add(new ImageIcon("src/ressources/images/animation/vicking/idle_3_r.png").getImage());
    }

        @Override
        public void run() {
            try {
                int i = 1;
                while (true) { // à modifier pour definir la fin de jeu
                    if (!iSrunning) {
                        if (!left){
                            anim = images.get(i);
                            Thread.sleep(delay);
                            i = (i + 1) % images.size();
                            Thread.sleep(delay);
                        }else {
                            anim = images_r.get(i);
                            Thread.sleep(delay);
                            i = (i + 1) % images_r.size();
                            Thread.sleep(delay);
                        }
                    }

                    // modifier ici pour rajouter l'anim si il y a mouvment, modifie aussi les variables au dessus
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }


