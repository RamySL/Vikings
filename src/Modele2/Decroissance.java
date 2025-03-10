package Modele2;
import Controller.PanelBleController;
import java.util.List;

public class Decroissance implements Runnable {
    private List<Ble> bleList;
    private volatile int currentIndex;
    private boolean running;
    private PanelBleController controller;

    public Decroissance(List<Ble> bleList, PanelBleController controller) {
        this.bleList = bleList;
        this.controller = controller;
        this.currentIndex = 0;
        this.running = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            while (running) {
                Thread.sleep(2000);  // On decroit  toutes les 2 secondes

                if (bleList.isEmpty()) continue;

                synchronized (this) {
                    Ble currentBle = bleList.get(currentIndex);

                    // Si le blé n'est pas mort, on affecte la santé
                    if (!currentBle.estMort()) {
                        currentBle.affecte();
                        System.out.println("Blé " + currentIndex + " : Santé actuelle : " + currentBle.getSante());
                    } else {
                        // Le blé est mort, on passe au suivant
                        System.out.println("Blé " + currentIndex + " est mort, passage au suivant.");
                        passerAuBleSuivant();
                    }

                    //  si le blé est récolté (taille == 0) on passe au suivant 
                    if (currentBle.getTaille() == 0) {
                        System.out.println("Blé " + currentIndex + " récolté, passage au suivant.");
                        passerAuBleSuivant();
                    }
                }

                
                controller.setCurrentIndex(currentIndex);

                Thread.sleep(2500);  
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
    private void passerAuBleSuivant() {
        if (bleList.isEmpty()) return;

        if (currentIndex < bleList.size() - 1) {
            currentIndex++;
        } else {
            System.out.println("Il n'a plus de blé.");
            running = false; // Arrête la décroissance une fois le dernier blé atteint
        }
    }

    public void stopDecroissance() {
        this.running = false;
    }

    public synchronized int getCurrentIndex() {
        return currentIndex;
    }

    public synchronized void setCurrentIndex(int index) {
        this.currentIndex = index;
    }
}
