package client.controler;

import client.controler.event.EventBus;
import client.controler.event.PlantEvent;
import client.controler.event.PlantListener;
import client.view.*;
import com.google.gson.Gson;
import network.packets.FormatPacket;
import network.packets.PacketAttack;
import network.packets.PacketMovement;
import network.packets.PaquetPlant;
import server.model.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

/**
 * Controler of the party
 */
public class ControlerParty extends MouseAdapter implements MouseMotionListener, MouseWheelListener, PlantListener {

    private ControlerClient controlerClient;
    private ViewPartie viewPartie;
    // last coordinates registered of the mouse click
    private int lastMouseClickX, lastMouseClickY;
    // intialy scaling is relative to (0,0)
    private boolean isFirstClickCamp = true;
    private boolean isFirstClickEnemyCamp = true;
    private int selectedEntityID = -1;
    private Camp selectedCamp = null;// ID of the selected entity
    Gson gson = new Gson();

    public ControlerParty(ControlerClient controlerClient, ViewPartie viewPartie) {
        this.controlerClient = controlerClient;
        this.viewPartie = viewPartie;
        this.viewPartie.addMouseListener(this);
        this.viewPartie.addMouseMotionListener(this);
        this.viewPartie.addMouseWheelListener(this);
        EventBus.getInstance().subscribe("PlantEvent", this::handlePlantEvent);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // on convertit les coordonnées de la souris en coordonnées du repère actuel de la vue(avec total offset et scale)
        //System.out.println("Click original : " + e.getX() + " " + e.getY() );
        Point clickOriginal = new Point(e.getX(), e.getY());
        Point clickView = (Point) clickOriginal.clone();
        Point totalOffset = viewPartie.getTotalOffset();
        double scale = viewPartie.getScaleFactor();

        this.viewPartie.clickToView(clickView);

        Camp camp = this.determineSelectedCamp(clickView.x, clickView.y);

        if(camp != null) {
            // Check if the click is within the client's camp
            if (camp.getId() == this.viewPartie.getCamp_id()) {
                if (isFirstClickCamp) {
                    // First click: Select entity
                    Viking viking = determineSelectedViking(clickView.x, clickView.y);
                    // pas de viking il faut regarder si une ressource a été selectionné
                    if(viking == null){
                        System.out.println("Click sur camp : Pas de vikings slectionné");
                        selectedEntityID = -1;
                    }else {
                        System.out.println("Click sur camp : Viking slectionné");
                        selectedEntityID = viking.getId();
                        isFirstClickCamp = false;
                    }
                }else {
                    // Second click: Send movement command
                    String content = gson.toJson(new PacketMovement(this.selectedEntityID ,clickOriginal,totalOffset,scale ));
                    this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PacketMovement",content));
                    isFirstClickCamp = true;
                }
            }else {
                // on annule un clique si il existait un
                isFirstClickCamp = true;
                if(isFirstClickEnemyCamp){
                    // ouvrir un panneau pour demander de slect une ressource à attquer
                    System.out.println("Ouverture du panneau pour l'ataque sur le camp : " + camp.getId());
                    selectedCamp = camp;

                    isFirstClickEnemyCamp = false;
                }else {
                    if (camp.getId() == selectedCamp.getId()) {
                        // determiner la ressource slectionner
                        System.out.println("deuxieme click sur le camp ennemi");
                        Object selectedRessource = determineSelectedRessource(clickView.x, clickView.y);
                        if (selectedRessource != null) {
                            if(selectedRessource instanceof Field) {
                                System.out.println("Click sur le champ ennemi");
                                Scanner scanner = new Scanner(System.in);
                                System.out.print("Combien de viking envoyer sur le champ : ");
                                int nombreViking = Integer.parseInt(scanner.nextLine());
                                System.out.print("Simulation boutton valider : ");
                                String simulation = scanner.nextLine();
                                System.out.println("Simulation : " + simulation);
                                scanner.close();

                                // on envoie le paquet d'attaque
                                int [] ressourceID = {((Field) selectedRessource).getId()};
                                int[] nbs = {nombreViking};
                                String content = gson.toJson(new PacketAttack(selectedCamp.getId(), ressourceID, nbs));
                                this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PacketAttack", content));

                            }else{
                                System.out.println("Click sur une autre ressource à traiter");
                            }
                        }else{
                            System.out.println("Deuxieme Click dans le vide sur le camp ennemi");
                        }
                        ///  Si attaque soumise
                        isFirstClickCamp = true;
                    }else{
                        // on change de camp ennemi on affiche le panneau pour l'autre camp
                        selectedCamp = camp;

                    }
                }
            }
        }else{
            // clique dans le vide
            System.out.println("Click dans le vide");
        }


    }


    /**
     * Un méthode générique qui permet de déterminer si un click est sur Viking (la classe mere de Farmer et Warrior).
     * elle prend des coordonnées de vue
     *
     */
    public Viking determineSelectedViking(int x, int y) {
        for (Viking viking : this.viewPartie.getCamp().getVikings()) {
            // getPosition rend le top left point of the viking
            Point viewPos = ViewPartie.pointModelToView(viking.getPosition());
            viewPos = new Point(viewPos.x - (Position.WIDTH_VIKINGS / 2) * ViewPartie.RATIO_X, viewPos.y - (Position.HEIGHT_VIKINGS / 2) * ViewPartie.RATIO_Y);
            if (x >= viewPos.x && x <= viewPos.x + Position.WIDTH_VIKINGS * ViewPartie.RATIO_X
                    && y >= viewPos.y && y <= viewPos.y + Position.HEIGHT_VIKINGS * ViewPartie.RATIO_Y) {
                return viking;
            }
        }
        return null;

    }

    /**
     * Un méthode générique qui permet de déterminer si un click est sur un camp.
     * elle prend des coordonnées de vue du clique
     *
     */
    public Camp determineSelectedCamp(int x, int y) {
        for (Camp camp : this.viewPartie.getPartieModel().getCamps()) {
            // getPosition rend le top left point of the camp
            Point viewPos = ViewPartie.pointModelToView(camp.getPosition());
            if (x >= viewPos.x && x <= viewPos.x + (Position.WIDTH * ViewPartie.RATIO_X)
                    && y >= viewPos.y && y <= viewPos.y + (Position.HEIGHT * ViewPartie.RATIO_Y)) {
                return camp;
            }
        }
        return null;
    }

    /**
     * Determine la ressource selectionnée pour l'attaque
     * Invariant : retoune soit une Entity soit un Field
     * @param x
     * @param y
     * @return
     */
    public Object determineSelectedRessource(int x, int y) {
        // parcours que la liste des fields
        for (Field field : this.selectedCamp.getFields()) {
            // getPosition rend le top left point of the field
            Point viewPos = ViewPartie.pointModelToView(field.getPosition());
            viewPos = new Point(viewPos.x - (Position.WIDTH_FIELD / 2) * ViewPartie.RATIO_X, viewPos.y - (Position.HEIGHT_FIELD / 2) * ViewPartie.RATIO_Y);
            if (x >= viewPos.x && x <= viewPos.x + Position.WIDTH_FIELD * ViewPartie.RATIO_X
                    && y >= viewPos.y && y <= viewPos.y + Position.HEIGHT_FIELD * ViewPartie.RATIO_Y) {
                return field;
            }
        }
        return null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastMouseClickX = e.getX();
        lastMouseClickY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.viewPartie.addToOffset(e.getX() - lastMouseClickX, e.getY() - lastMouseClickY);
        lastMouseClickX = e.getX();
        lastMouseClickY = e.getY();
        this.viewPartie.revalidate();
        this.viewPartie.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double zoomFactor = 1.1;
        int viewX = e.getX();
        int viewY = e.getY();

        Point totalOffset = viewPartie.getTotalOffset();
        double currentScale = viewPartie.getScaleFactor();

        // Onn convertit les coordonnées de la souris en coordonnées du repère actuel (avec total offset et scale)
        double modelX = (viewX - totalOffset.x) / currentScale;
        double modelY = (viewY - totalOffset.y) / currentScale;


        int rotation = e.getWheelRotation();
        // rotation > 0 = zoom out, rotation < 0 = zoom in
        double scaleChange = (rotation > 0) ? 1 / zoomFactor : zoomFactor;
        viewPartie.multiplyScale(scaleChange);
        double newScale = viewPartie.getScaleFactor();

        // Calcule du montatant de la nouvelle translation pour garder le point qui était sous la souris
        // au même endroit après le zoom
        int newTotalOffsetX = (int) (viewX - modelX * newScale);
        int newTotalOffsetY = (int) (viewY - modelY * newScale);

        int offsetCampX = viewPartie.getOffsetCampX();
        int offsetCampY = viewPartie.getOffsetCampY();
        int newOffsetDraggingX = newTotalOffsetX - offsetCampX;
        int newOffsetDraggingY = newTotalOffsetY - offsetCampY;

        viewPartie.setOffset(newOffsetDraggingX, newOffsetDraggingY);

    }
    @Override
    public void onPlant(PlantEvent event) {
        sendPlantPacketToServer(event.getResource(), event.getFarmerX(), event.getFarmerY(), event.getFieldX(), event.getFieldY());
    }

    private void handlePlantEvent(Object event) {
        if (event instanceof PlantEvent) {
            PlantEvent plantEvent = (PlantEvent) event;
            sendPlantPacketToServer(plantEvent.getResource(), plantEvent.getFarmerX(), plantEvent.getFarmerY(), plantEvent.getFieldX(), plantEvent.getFieldY());
        }
    }

    public void sendPlantPacketToServer(String resource, int farmerX, int farmerY, int fieldX, int fieldY) {
        Gson gson = new Gson();
        String contentPaquet = gson.toJson(new PaquetPlant(resource, farmerX, farmerY, fieldX, fieldY));
        this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PaquetPlant", contentPaquet));
    }



}
