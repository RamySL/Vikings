package main;

import server.controler.ControlerServer;
import server.view.Server;

import javax.swing.*;

public class MainServer {
    public static void main(String[] args) {
        Server view = new Server();
        ControlerServer controler = new ControlerServer(view);

        JFrame frame = new JFrame();
        frame.setIconImage(new ImageIcon("src/ressources/images/vikingServer_ico_128.png").getImage());
        frame.setTitle("Vikings Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.pack();
        frame.setVisible(true);
    }
}
