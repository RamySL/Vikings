package client.controler;

import client.view.ViewPartie;
import com.google.gson.Gson;
import network.FormatPacket;
import network.PaquetClick;

import java.awt.event.*;

/**
 * Controler of the party
 */
public class ControlerParty extends MouseAdapter implements MouseMotionListener, MouseWheelListener {

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



}
