package client.view;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Animation {

    public static List<Image> images_anim_0 = new ArrayList<>();
    public static List<Image> degat_anim_0 = new ArrayList<>();
    public static List<Image> attack_anim_0 = new ArrayList<>();

    public static List<Image> images_anim_1 = new ArrayList<>();
    public static List<Image> degat_anim_1 = new ArrayList<>();
    public static List<Image> attack_anim_1 = new ArrayList<>();

    public static List<Image> images_anim_2 = new ArrayList<>();
    public static List<Image> degat_anim_2  = new ArrayList<>();
    public static List<Image> attack_anim_2 = new ArrayList<>();

    public static List<Image> images_anim_3 = new ArrayList<>();
    public static List<Image> degat_anim_3 = new ArrayList<>();
    public static List<Image> attack_anim_3 = new ArrayList<>();

    public Animation(){
        images_anim_0.add(new ImageIcon("src/ressources/images/animation/vicking/idle_0.png").getImage());
        images_anim_0.add(new ImageIcon("src/ressources/images/animation/vicking/idle_1.png").getImage());
        //images_anim_0.add(new ImageIcon("src/ressources/images/animation/vicking/idle_2.png").getImage());
        images_anim_0.add(new ImageIcon("src/ressources/images/animation/vicking/idle_3.png").getImage());

        images_anim_1.add(new ImageIcon("src/ressources/images/animation/vicking/idle_0_c1.png").getImage());
        images_anim_1.add(new ImageIcon("src/ressources/images/animation/vicking/idle_1_c1.png").getImage());
        //images_anim_1.add(new ImageIcon("src/ressources/images/animation/vicking/idle_2_c1.png").getImage());
        images_anim_1.add(new ImageIcon("src/ressources/images/animation/vicking/idle_3_c1.png").getImage());

        images_anim_2.add(new ImageIcon("src/ressources/images/animation/vicking/idle_0_c2.png").getImage());
        images_anim_2.add(new ImageIcon("src/ressources/images/animation/vicking/idle_1_c2.png").getImage());
        //images_anim_2.add(new ImageIcon("src/ressources/images/animation/vicking/idle_2_c2.png").getImage());
        images_anim_2.add(new ImageIcon("src/ressources/images/animation/vicking/idle_3_c2.png").getImage());

        images_anim_3.add(new ImageIcon("src/ressources/images/animation/vicking/idle_0_c3.png").getImage());
        images_anim_3.add(new ImageIcon("src/ressources/images/animation/vicking/idle_1_c3.png").getImage());
        //images_anim_3.add(new ImageIcon("src/ressources/images/animation/vicking/idle_2_c3.png").getImage());
        images_anim_3.add(new ImageIcon("src/ressources/images/animation/vicking/idle_3_c3.png").getImage());


    }
}
