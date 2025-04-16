package client.view;

import java.awt.*;
import java.util.List;

public class vikingAnim extends Thread {
    private List<Image> img_anim;
    private List<Image> degat_anim;
    private List<Image> attack_anim;
    private Etat etat;
    private int campID;
    private int delay = 25;


    public vikingAnim(int campId) {
        etat = Etat.Chill;
        this.campID = campId;
        switch (campId){
            case 0:
                img_anim = Animation.images_anim_0;
                degat_anim = Animation.degat_anim_0;
                attack_anim = Animation.attack_anim_0;
                break;
            case 1:
                img_anim = Animation.images_anim_1;
                degat_anim = Animation.degat_anim_1;
                attack_anim = Animation.attack_anim_1;
                break;
            case 2:
                img_anim = Animation.images_anim_2;
                degat_anim = Animation.degat_anim_2;
                attack_anim = Animation.attack_anim_2;
                break;
            case 3:
                img_anim = Animation.images_anim_3;
                degat_anim = Animation.degat_anim_3;
                attack_anim = Animation.attack_anim_3;
                break;
        }
    }

    public Etat getEtat() {
        return etat;
    }



    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    @Override
        public void run() {
            try {
                int i = 1;
                while (true) { // à modifier pour definir la fin de jeu
                    if (etat == Etat.Chill) {
                        ViewPartie.images_anim.set(campID, img_anim.get(i));
                        Thread.sleep(delay);
                        i = (i + 1) % img_anim.size();
                        Thread.sleep(delay);
                    } else if (etat == Etat.Degat) {
                        ViewPartie.images_anim.set(campID, degat_anim.get(i));

                        Thread.sleep(delay);
                        i = (i + 1) % degat_anim.size();
                        Thread.sleep(delay);
                    } else if (etat == Etat.Attack) {
                        ViewPartie.images_anim.set(campID, attack_anim.get(i));

                        Thread.sleep(delay);
                        i = (i + 1) % attack_anim.size();
                        Thread.sleep(delay);
                    }
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }


