package client.controler;

import client.controler.event.*;
import client.view.*;
import com.google.gson.Gson;
import network.ModelAdapter;
import network.packets.FormatPacket;
import network.packets.*;
import network.packets.PacketAttack;
import network.packets.PacketMovement;
import network.packets.PaquetPlant;
import server.model.*;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Controler of the party
 */
public class ControlerParty extends MouseAdapter implements ActionListener, MouseMotionListener, MouseWheelListener, PlantListener, EatListenner, HarvestListenner {

    private ControlerClient controlerClient;
    private ViewPartie viewPartie;
    private SlidingMenu slidingMenu;
    // last coordinates registered of the mouse click
    private int lastMouseClickX, lastMouseClickY;
    // intialy scaling is relative to (0,0)
    private boolean isFirstClickCamp = true;
    private boolean isFirstClickEnemyCamp = true;
    private int selectedEntityID = -1;// ID of the selected entity
    private Camp selectedCamp = null;
    Gson gson = ModelAdapter.getGson();
    // pour créer qu'une seule fois les thread checker position
    public boolean firstPacketGame = true;
    private HashMap<Integer, VikingPositionChecker> mapIdChecker = new HashMap<>();
    private VikingPositionChecker lastThreadChecker = null;

    public ControlerParty(ControlerClient controlerClient, ViewPartie viewPartie) {
        this.controlerClient = controlerClient;
        this.controlerClient.setControlerParty(this);
        this.viewPartie = viewPartie;
        this.viewPartie.addMouseListener(this);
        this.viewPartie.addMouseMotionListener(this);
        this.viewPartie.addMouseWheelListener(this);
        this.slidingMenu = this.viewPartie.getSlidingMenu();
        this.slidingMenu.getExitMenu().addActionListener(this);
        EventBus.getInstance().subscribe("PlantEvent", this::handlePlantEvent);
        EventBus.getInstance().subscribe("EatEvent", this::handleEatEvent);
        EventBus.getInstance().subscribe("HarvestEvent", this::handleHarvestEvent);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e instanceof EventInitCamp){


        }else {
            // on convertit les coordonnées de la souris en coordonnées du repère actuel de la vue(avec total offset et scale)
            //System.out.println("Click original : " + e.getX() + " " + e.getY() );
            Point clickOriginal = new Point(e.getX(), e.getY());
            Point clickView = (Point) clickOriginal.clone();
            Point totalOffset = viewPartie.getTotalOffset();
            double scale = viewPartie.getScaleFactor();

            this.viewPartie.clickToView(clickView);

            Camp camp = this.determineSelectedCamp(clickView.x, clickView.y);
            this.viewPartie.panelHide();

            if (camp != null) {
                selectedCamp = camp;

                // Check if the click is within the client's camp
                if (camp.getId() == this.viewPartie.getCamp_id()) {
                    if (isFirstClickCamp) {
                        // First click: Select entity
                        Object o = determineSelectedEntity(clickView.x, clickView.y);
                        if (o == null) {
                            //this.viewPartie.panelHide();
                        } else {
                            // Ouverture panel
                            this.viewPartie.panelSetVisibility(true);
                            if (o instanceof Entity) {
                                this.viewPartie.panelShowInfos(o.toString(), ((Entity) o).getHealth());
                            } else if (o instanceof Field) {
                                if (((Field) o).isPlanted()) {
                                    this.viewPartie.panelShowInfos(o.toString(), ((Field) o).getResource());
                                } else {
                                    this.viewPartie.panelShowInfos(o.toString());
                                }
                            }

                            if (o instanceof Viking) {
                                // à chaque fois ya que le viking selectionner qui a son thread actif
                                VikingPositionChecker th = this.mapIdChecker.get(((Viking) o).getId());
                                if (lastThreadChecker != null && lastThreadChecker != th) {
                                    lastThreadChecker.locked = true;
                                }
                                lastThreadChecker = th;
                                synchronized (th.lock){
                                    th.locked = false;
                                    th.lock.notify();

                                }

                                System.out.println("Click sur camp : Viking slectionné");
                                selectedEntityID = ((Viking) o).getId();
                                isFirstClickCamp = false;
                            }
                        }

                    } else {
                        // Second click: Send movement command

                        String content = gson.toJson(new PacketMovement(this.selectedEntityID, clickOriginal, totalOffset, scale));
                        this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PacketMovement", content));

                        isFirstClickCamp = true;
                    }
                } else {
                    // on annule un clique si il existait un
                    isFirstClickCamp = true;
                    if (isFirstClickEnemyCamp) {
                        // ouvrir un panneau pour demander de slect une ressource à attquer
                        System.out.println("Ouverture du panneau pour l'ataque sur le camp : " + camp.getId());
                        isFirstClickEnemyCamp = false;
                    } else {
                        if (camp.getId() == selectedCamp.getId()) {
                            // determiner la ressource slectionner
                            System.out.println("deuxieme click sur le camp ennemi");
                            Object selectedRessource = determineSelectedField(clickView.x, clickView.y);
                            if (selectedRessource != null) {
                                if (selectedRessource instanceof Field) {
                                    System.out.println("Click sur le champ ennemi");
                                    Scanner scanner = new Scanner(System.in);
                                    System.out.print("Combien de viking envoyer sur le champ : ");
                                    int nombreViking = Integer.parseInt(scanner.nextLine());
                                    System.out.print("Simulation boutton valider : ");
                                    String simulation = scanner.nextLine();
                                    System.out.println("Simulation : " + simulation);
                                    scanner.close();

                                    // on envoie le paquet d'attaque
                                    int[] ressourceID = {((Field) selectedRessource).getId()};
                                    int[] nbs = {nombreViking};
                                    String content = gson.toJson(new PacketAttack(selectedCamp.getId(), ressourceID, nbs));
                                    this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PacketAttack", content));

                                } else {
                                    System.out.println("Click sur une autre ressource à traiter");
                                }
                            } else {
                                System.out.println("Deuxieme Click dans le vide sur le camp ennemi");
                            }
                            ///  Si attaque soumise
                            isFirstClickCamp = true;
                        } else {
                            // on change de camp ennemi on affiche le panneau pour l'autre camp
                            selectedCamp = camp;

                        }
                    }
                }
            } else {
                // clique dans le vide
                System.out.println("Click dans le vide");
            }
        }

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
    public Object determineSelectedField(int x, int y) {
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

    /**
     * detecte même field
     * @param x
     * @param y
     * @return
     */
    public Object determineSelectedEntity(int x, int y) {
        for (Entity entity : this.viewPartie.getCamp().getEntities()) {
            // getPosition rend le top left point of the entity
            Point viewPos = ViewPartie.pointModelToView(entity.getPosition());
            viewPos = new Point(viewPos.x - (Position.WIDTH_VIKINGS / 2) * ViewPartie.RATIO_X, viewPos.y - (Position.HEIGHT_VIKINGS / 2) * ViewPartie.RATIO_Y);
            if (x >= viewPos.x && x <= viewPos.x + Position.WIDTH_VIKINGS * ViewPartie.RATIO_X
                    && y >= viewPos.y && y <= viewPos.y + Position.HEIGHT_VIKINGS * ViewPartie.RATIO_Y) {
                return entity;
            }
        }
        return determineSelectedField(x,y);
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
        if (rotation > 0 && viewPartie.getScaleFactor() > 0.5
                || rotation < 0 && viewPartie.getScaleFactor() < 2.0){

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


    }
    @Override
    public void onPlant(PlantEvent event) {
        sendPlantPacketToServer(event.getResource(),event.getIdFarmer(), event.getIdField());
    }

    private void handlePlantEvent(Object event) {
        if (event instanceof PlantEvent) {
            PlantEvent plantEvent = (PlantEvent) event;
            sendPlantPacketToServer(plantEvent.getResource(), plantEvent.getIdFarmer(), plantEvent.getIdField());
        }
    }

    public void sendPlantPacketToServer(String resource, int idFarmer, int idField) {
        String contentPaquet = gson.toJson(new PaquetPlant(resource, idFarmer, idField));
        this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PaquetPlant", contentPaquet));
    }

    @Override
    public void onEat(EatEvent event) {
        sendEatPacketToServer(event.getIdViking(), event.getIdAnimal());
    }

    private void handleEatEvent(Object event) {
        if (event instanceof EatEvent) {
            EatEvent eatEvent = (EatEvent) event;
            sendEatPacketToServer(eatEvent.getIdViking(), eatEvent.getIdAnimal());
        }
    }

    public void sendEatPacketToServer(int idViking, int idAnimal) {
        Gson gson = new Gson();
        String contentPaquet = gson.toJson(new PaquetEat(idViking, idAnimal));
        this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PaquetEat", contentPaquet));
    }

    @Override
    public void onHarvest(HarvestEvent event) {
        sendHarvestPacketToServer(event.getIdFarmer(), event.getIdField());
    }
    private void handleHarvestEvent(Object event) {
        if (event instanceof HarvestEvent) {
            HarvestEvent harvestEvent = (HarvestEvent) event;
            sendHarvestPacketToServer(harvestEvent.getIdFarmer(), harvestEvent.getIdField());
        }
    }
    public void sendHarvestPacketToServer(int idFarmer, int idField) {
        Gson gson = new Gson();
        String contentPaquet = gson.toJson(new PaquetHarvest(idFarmer, idField));
        this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PaquetHarvest", contentPaquet));


    }

    public void setVikingNearSheep(boolean nearSheep, int idViking, int idSheep ) {
        this.viewPartie.panelSetVikingNearSheep(nearSheep, idViking, idSheep);
    }

    public void setFarmerNearField (boolean onField, int idFarmer, int idField, boolean isPlanted) {
        this.viewPartie.panelSetFarmerOnField(onField, idFarmer, idField, isPlanted);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.slidingMenu.getExitMenu()) {
            this.viewPartie.panelHide();
            this.isFirstClickCamp = true;
            this.isFirstClickEnemyCamp = true;
            this.selectedEntityID = -1;
            this.selectedCamp = null;
        }
    }

    public void setPartie(){
        if(firstPacketGame){
            for (Warrior warrior : this.viewPartie.getCamp().getWarriors()){
                VikingPositionChecker t = new WarriorPositionChecker(this, this.viewPartie.getCamp(), this.viewPartie.getCamp(),warrior);
                this.mapIdChecker.put(warrior.getId(),t);
                t.start();
            }
            for (Farmer farmer : this.viewPartie.getCamp().getFarmers()){
                VikingPositionChecker t = new FarmerPositionChecker(this, this.viewPartie.getCamp(), this.viewPartie.getCamp(), farmer);
                this.mapIdChecker.put(farmer.getId(), t);
                t.start();
            }
            firstPacketGame = false;
        }else{
            for (VikingPositionChecker t : this.mapIdChecker.values()){
                t.setNextCamp(this.viewPartie.getCamp());
            }

        }
    }
}
