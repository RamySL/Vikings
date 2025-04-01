package client.controler;

import client.controler.event.EventBus;
import client.controler.event.PlantEvent;
import client.controler.event.PlantListener;
import client.view.*;
import com.google.gson.Gson;
import network.packets.FormatPacket;
import network.packets.PaquetClick;
import network.packets.PaquetPlant;
import server.model.Position;

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
    private int lastMousePosX=0, lastMousePosY=0;

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
        Gson gson = new Gson();
        String contentPaquet = gson.toJson(new PaquetClick(e.getX(),e.getY(),
                this.viewPartie.getTotalOffset().x, this.viewPartie.getTotalOffset().y, this.viewPartie.getScaleFactor()));

        // !!! je pense sans le get c'est mieux
        //this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PaquetClick",contentPaquet));
        Point model = Position.viewToModel(new Point(e.getX(),e.getY()), this.viewPartie.getTotalOffset(), this.viewPartie.getScaleFactor());
        if (Position.isInCamp(this.viewPartie.getCamp_id(), model.x, model.y)) {
            // On envoie le paquet au serveur
            System.out.println("Click in the camp");
        } else {
            System.out.println("Click outside the camp");
        }


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
