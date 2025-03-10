package src.Modele2;

import java.util.ArrayList;

public class Vikings {
    private ArrayList<Ble> bleList;
    private static int rendementTotal; 

    // Constructeur
    public Vikings(int size) {
        this.bleList = new ArrayList<>(size);
        this.rendementTotal = 0;  
    
        for (int i = 0; i < size; i++) {
            this.bleList.add(new Ble());
        }

     
        
    }


    public void addBle(Ble newBle) {
        this.bleList.add(newBle);
    }

    public void arroserEtFaireGrandir(Ble ble) {
        if (ble != null && !ble.estMort()) {
            int index = bleList.indexOf(ble);
            if (index != -1 ) {
                if( ble.getTaille()<100) {
                    ble.grandir();
                System.out.println("Le blé numero  " +  index  + "grandit avec une taille de "+  ble.getTaille() +" et une sante de "+ ble.getSante());


                }else {
                    System.out.println("Le blé à la position " + index + " est pret a etre recolte avec une taille de " + ble.getTaille() + " et une snate  de  " + ble.getSante());
                    
                }
                
            } else {
                System.out.println("Erreur : Impossible d’arroser, blé introuvable.");
            }
        } else {
            System.out.println("Impossible d’arroser : blé mort ou inexistant.");
        }
    }
    


    public int recolte(Ble ble) {
        int rendement =0 ; 
        if (ble != null) {
            rendement = ble.recolte();
            rendementTotal += rendement;  // Ajouter le rendement à la récolte totale la recolte est proportionnel a la sante au moment ou l'on recotle 
            System.out.println("Récolte effectuée. Rendement : " + rendement);
            System.out.println("Rendement total après récolte : " + rendementTotal);
            
        }
        return rendement ; 
    }
    public static int getRendementTotal() {
        return rendementTotal;
    }

    public ArrayList<Ble> getBleList() {
        return bleList;
    }
}
