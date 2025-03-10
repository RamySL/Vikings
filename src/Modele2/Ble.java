package src.Modele2;

public class Ble {
    public static final int LIMITE = 100; // Limite maximale de la taille d'un blé
    private int taille;
    private boolean besoin;
    private int sante;
    private boolean estMort; 

    public Ble() {
        this.taille = 5;
        this.besoin = true;
        this.sante = 5;
        this.estMort = false;
    }

    public int getTaille() {
        return this.taille;
    }

    public int getSante() {
        return this.sante;
    }
    
  
    public boolean estMort() {
        return this.estMort;
    }

    
    public synchronized void grandir() {
        if (estMort) {
            System.out.println("Le blé est mort, il ne peut plus grandir.");
            return;
        }
        if (taille < LIMITE) {
            this.taille += 10;
            if (taille > LIMITE) {
                taille = LIMITE; 
            }
            this.sante += 5;
        }
    }

    public boolean estPretARecolter() {
        if (estMort) return false;
        return this.taille >= 90;
    }

    public int recolte() {
        if (estMort) {
            System.out.println("Le blé est mort, impossible de récolter.");
            return 0;
        }
        if (estPretARecolter()) {
            int rendement = this.sante;
            this.taille = 0; 
            return rendement;
        }
        return 0;
    }

    public void affecte() {
        if (!estMort) {
            if (this.sante > 0) {
                this.sante--;
                System.out.println("La santé du blé diminue. Santé actuelle : " + this.sante);
            } else { 
                estMort = true;
                System.out.println("Le blé est mort, mon pote.");
            }
        }
        // Si estMort est déjà true, on ne fait rien
    }
    
}
