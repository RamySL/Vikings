// ViewEndGame.java
package client.view;

import sharedGUIcomponents.ComposantsPerso.FontPerso;

import javax.swing.*;
import java.awt.*;

public class ViewEndGame extends JPanel {
    public ViewEndGame(String message) {
        setLayout(new BorderLayout());
        JLabel endLabel = new JLabel(message, JLabel.CENTER);
        endLabel.setFont(FontPerso.mvBoli(32));
        endLabel.setForeground(Color.WHITE);
        add(endLabel, BorderLayout.CENTER);
    }
}