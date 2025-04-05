package client.controler;

import client.controler.event.EventBus;
import client.controler.event.PlantEvent;
import client.controler.event.PlantListener;
import client.view.*;
import com.google.gson.Gson;
import network.packets.FormatPacket;
import network.packets.PacketMovement;
import network.packets.PaquetPlant;
import server.model.*;

import java.awt.*;
import java.awt.event.*;

/**
 * Controler of the party
 */
public class ControlerParty extends MouseAdapter implements MouseMotionListener, MouseWheelListener, PlantListener {

    private ControlerClient controlerClient;
    private ViewPartie viewPartie;
    // last coordinates registered of the mouse click
    private int lastMouseClickX, lastMouseClickY;
    // intialy scaling is relative to (0,0)
    private boolean isFirstClick = true;
    private int selectedEntityID = -1; // ID of the selected entity
    private String selectedEntityType = null; // "Warrior" or "Farmer"
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
        System.out.println("Click original : " + e.getX() + " " + e.getY() );
        Point clickOriginal = new Point(e.getX(), e.getY());
        Point clickView = (Point) clickOriginal.clone();
        Point totalOffset = viewPartie.getTotalOffset();
        double scale = viewPartie.getScaleFactor();

        this.viewPartie.clickToView(clickView);

        // Check if the click is within the client's camp
        if (this.isInCamp(this.viewPartie.getCamp_id(), clickView.x, clickView.y)) {
            if (isFirstClick) {
                // First click: Select entity
                Viking viking = determineSelectedViking(clickView.x, clickView.y);
                // pas de viking il faut regarder si une ressource a été selectionné
                if(viking == null){
                    System.out.println("Click sur camp : Pas de vikings slectionné");
                }else {
                    System.out.println("Click sur camp : Viking slectionné");
                    selectedEntityID = viking.getId();
                    isFirstClick = false;
                }

            } else {
                // Second click: Send movement command
                String content = gson.toJson(new PacketMovement(this.selectedEntityID ,clickOriginal,totalOffset,scale ));
                this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PacketMovement",content));
                isFirstClick = true;
                selectedEntityID = -1;
                selectedEntityType = null;
            }
        } else {
            // Click outside camp: Reset
            System.out.println("Click outside camp");
            isFirstClick = true;
            selectedEntityID = -1;
            selectedEntityType = null;
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
            System.out.println("Click : " + x + " " + y);
            System.out.println("Viking : " + viking.getId() + " " + viewPos.x + " " + viewPos.y);
            if (x >= viewPos.x && x <= viewPos.x + Position.WIDTH_VIKINGS*ViewPartie.RATIO_X
                    && y >= viewPos.y && y <= viewPos.y + Position.HEIGHT_VIKINGS*ViewPartie.RATIO_Y) {
                return viking;
            }
        }
        return null;

    }

    public boolean isInCamp(int campId, int clickX, int clickY){
        // Si on clone pas on est entrain de modifier le modèle
        Point camp = ViewPartie.pointModelToView(Position.MAP_CAMPS_POSITION.get(campId));
        return clickX >= camp.x && clickX <= camp.x + (Position.WIDTH*ViewPartie.RATIO_X) &&
                clickY >= camp.y && clickY <= camp.y + (Position.HEIGHT*ViewPartie.RATIO_Y);
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
