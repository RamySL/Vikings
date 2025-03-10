package view;

import Main.Main;
import model.Champ;
import model.Pillard;

import javax.swing.*;
import java.awt.*;

public class Affichage extends JPanel {
    private Champ terrain;
    private int height = 1920;
    private int width = 1080;


    public Affichage(Champ terrain) {
        this.terrain = terrain;
        setPreferredSize(new Dimension(width, height)); // Taille par défaut
        setBackground(new Color(144, 238, 144)); // Vert clair
        setLayout(null); // Permet de gérer manuellement la position des éléments
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int cellWidth = panelWidth / Main.taille_terrain;
        int cellHeight = panelHeight / Main.taille_terrain;

        for (int i = 0; i < Main.taille_terrain; i++) {
            for (int j = 0; j < Main.taille_terrain; j++) {
                if (terrain.getCase(i, j).contientRessource()) {
                    g.setColor(Color.BLACK);
                    g.drawRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                    g.setColor(new Color(165,42,42));
                    g.fillRect(j * cellWidth+4, i * cellHeight+4, cellWidth-4, cellHeight-4);
                    if(!(terrain.getCase(i,j).getRessource().getUseless())) {
                        g.setColor(Color.yellow);
                        g.fillOval(j * cellWidth + cellWidth / 2, (i * cellHeight) + cellHeight / 2,
                                cellWidth / 6, cellHeight / 6);
                    }
                }


                if (terrain.getCase(i, j).contientEntite()) {
                    g.setColor(Color.BLUE);
                    g.fillRect(j * cellWidth + cellWidth/2, i * cellHeight + cellHeight/2,
                                cellWidth/4, cellHeight/4);
                    }
                }
            }

        }


    }

