package main;

import client.controler.ControlerClient;
import client.controler.ControlerParty;
import client.view.ViewClient;

import javax.swing.*;

public class MainClient {
    public static void main(String[] args) {
        ViewClient view = new ViewClient();
        ControlerClient controler = new ControlerClient(view);
        ControlerParty controlerParty = new ControlerParty(controler, view.getViewPartie());

        JFrame frame = new JFrame();
        frame.setIconImage(new ImageIcon("src/ressources/images/viking_ico_64.png").getImage());
        frame.setTitle("Vikings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.pack();
        frame.setVisible(true);

    }
}
