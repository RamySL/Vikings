package main;

import controler.ControlerServer;
import model.ModelServer;
import view.ViewServer;

import javax.swing.*;

public class MainServer {
    public static void main(String[] args) {
        ViewServer view = new ViewServer();
        ControlerServer controler = new ControlerServer(view);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.pack();
        frame.setVisible(true);
    }
}
