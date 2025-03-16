package client.controler;

import client.view.ViewPartie;
import com.google.gson.Gson;
import network.FormatPacket;
import network.PaquetCoordClick;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Controler of the party
 */
public class ControlerParty extends MouseAdapter implements MouseMotionListener {

    private ControlerClient controlerClient;
    private ViewPartie viewPartie;
    // last coordinates registered of the mouse
    private int lastMouseX, lastMouseY;

    public ControlerParty(ControlerClient controlerClient, ViewPartie viewPartie) {
        this.controlerClient = controlerClient;
        this.viewPartie = viewPartie;
        this.viewPartie.addMouseListener(this);
        this.viewPartie.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Gson gson = new Gson();
        String contentPaquet = gson.toJson(new PaquetCoordClick(e.getX(),e.getY()));

        // ! ! je pense sans le get c'est mieux
        this.controlerClient.getThreadCommunicationClient().getClient().sendMessage(FormatPacket.format("PaquetCoordClick",contentPaquet));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.viewPartie.addToOffset(e.getX() - lastMouseX, e.getY() - lastMouseY);
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }

}
