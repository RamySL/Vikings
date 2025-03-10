package Controller;
import Modele2.*;
import Vue.PanelBle;
import java.awt.event.*;

public class PanelBleController {
    private PanelBle view;
    private Vikings viking;
    private Decroissance decroissance;
    private int currentIndex = 0; // Indice du blé actuel

    public PanelBleController(PanelBle view, Vikings viking, Decroissance decroissance) {
        this.view = view;
        this.viking = viking;
        this.decroissance = decroissance;

       
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleWheatClick(e);
            }
        });

        
        view.getBoutonArroser().addActionListener(e -> {
            if (!viking.getBleList().isEmpty() && currentIndex < viking.getBleList().size()) {
                Ble currentBle = viking.getBleList().get(currentIndex);
                if (!currentBle.estMort()) {
                    viking.arroserEtFaireGrandir(currentBle);
                    view.repaint();
                }
            }
        });
    }

    private void handleWheatClick(MouseEvent e) {
        if (viking.getBleList().isEmpty()) return;
        if (currentIndex >= viking.getBleList().size()) return;
        
        Ble currentBle = viking.getBleList().get(currentIndex);

        // Si le blé est déjà mort ou récolté, on passe au suivant
        if (currentBle.estMort() || currentBle.getTaille() == 0) {
            passerAuBleSuivant();
            if (currentIndex < viking.getBleList().size()) {
                currentBle = viking.getBleList().get(currentIndex);
            } else {
                return;
            }
        }


        int diameter = currentBle.getTaille();
        if (diameter < 10) diameter = 10;

        int centerX = view.getWidth() / 2;
        int centerY = view.getHeight() / 2;
        double distance = Math.sqrt(Math.pow(e.getX() - centerX, 2) + Math.pow(e.getY() - centerY, 2));

        if (distance <= diameter / 2) {
            int rendement = viking.recolte(currentBle);
            if (rendement > 0) {
                view.updateRendement(Vikings.getRendementTotal());
                passerAuBleSuivant();
                view.repaint();
            }
        }
    }


    private void passerAuBleSuivant() {
        if (viking.getBleList().isEmpty()) return;

        if (currentIndex < viking.getBleList().size() - 1) {
            currentIndex++;
            view.updateBle(viking.getBleList().get(currentIndex));
        } else {
            System.out.println("Il n'a plus de blé.");
            // On desactive puisque le dernier blé est atteint
            view.getBoutonArroser().setEnabled(false);
            
        }
    }

    public synchronized void setCurrentIndex(int index) {
        this.currentIndex = index;
    }

    public synchronized int getCurrentIndex() {
        return currentIndex;
    }
}
