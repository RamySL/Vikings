package client.controler;

import client.controler.event.*;
import client.view.*;
import com.google.gson.Gson;
import network.packets.FormatPacket;
import network.packets.PaquetClick;
import network.packets.*;

import java.awt.event.*;

/**
 * Controler of the party
 */
public class ControlerParty extends MouseAdapter implements MouseMotionListener, MouseWheelListener, PlantListener, EatListenner, HarvestListenner, ExitListenner {

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
        EventBus.getInstance().subscribe("EatEvent", this::handleEatEvent);
        EventBus.getInstance().subscribe("HarvestEvent", this::handleHarvestEvent);
        EventBus.getInstance().subscribe("ExitEvent", this::handleExitEvent);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Gson gson = new Gson();
        String contentPaquet = gson.toJson(new PaquetClick(e.getX(),e.getY(),
                this.viewPartie.getTotalOffset().x, this.viewPartie.getTotalOffset().y, this.viewPartie.getScaleFactor()));

        // ! ! je pense sans le get c'est mieux
        this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PaquetClick",contentPaquet));
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
        // 1 if the mouse wheel was toward the user, -1 if the  wheel was rotated away from the user
        int rotation_direction = e.getWheelRotation();
        int mouseX = e.getX();
        int mouseY = e.getY();
        double scaleFactor = 1.1;
        // zoom out (rotation towards the user)
        //this.viewPartie.addToOffset(mouseX-lastMousePosX, mouseY-lastMousePosY);
        lastMousePosX = mouseX;
        lastMousePosY = mouseY;
        if(rotation_direction>0){
            this.viewPartie.multiplyScale(1/scaleFactor);
        }else{
            // zoom in (rotation away from the user)
            this.viewPartie.multiplyScale(scaleFactor);
        }

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

    @Override
    public void onEat(EatEvent event) {
        sendEatPacketToServer(event.getVikingX(), event.getVikingY(), event.getAnimalX(), event.getAnimalY());
    }

    private void handleEatEvent(Object event) {
        if (event instanceof EatEvent) {
            EatEvent eatEvent = (EatEvent) event;
            sendEatPacketToServer(eatEvent.getVikingX(), eatEvent.getVikingY(), eatEvent.getAnimalX(), eatEvent.getAnimalY());
        }
    }

    public void sendEatPacketToServer(int vikingX, int vikingY, int animalX, int animalY) {
        Gson gson = new Gson();
        String contentPaquet = gson.toJson(new PaquetEat(vikingX, vikingY, animalX, animalY));
        this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PaquetEat", contentPaquet));
    }

    @Override
    public void onHarvest(HarvestEvent event) {
        sendHarvestPacketToServer(event.getFarmerX(), event.getFarmerY(), event.getFieldX(), event.getFieldY());
    }
    private void handleHarvestEvent(Object event) {
        if (event instanceof HarvestEvent) {
            HarvestEvent harvestEvent = (HarvestEvent) event;
            sendHarvestPacketToServer(harvestEvent.getFarmerX(), harvestEvent.getFarmerY(), harvestEvent.getFieldX(), harvestEvent.getFieldY());
        }
    }
    public void sendHarvestPacketToServer(int farmerX, int farmerY, int fieldX, int fieldY) {
        Gson gson = new Gson();
        String contentPaquet = gson.toJson(new PaquetHarvest(farmerX, farmerY, fieldX, fieldY));
        this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PaquetHarvest", contentPaquet));


    }

    @Override
    public void onExit(ExitEvent event) {
        sendExitPacketToServer();

    }
    private void handleExitEvent(Object event) {
        if (event instanceof ExitEvent) {
            ExitEvent exitEvent = (ExitEvent) event;
            sendExitPacketToServer();
        }
    }
    public void sendExitPacketToServer() {
        Gson gson = new Gson();
        String contentPaquet = gson.toJson(new PaquetExit("exit"));
        this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PaquetExit", contentPaquet));
    }



}
