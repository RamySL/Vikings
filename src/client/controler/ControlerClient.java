package controler;

import com.google.gson.Gson;
import model.ModelClient;
import view.ViewClient;

import java.awt.event.*;

/**
 * Gère les évenements déclenchés par l'utilisateur en interagissant avec l'interface graphique du jeu
 */
public class ControlerClient implements ActionListener, KeyListener, MouseListener {
    private ThreadCommunicationClient threadCommunicationClient;
    private ViewClient view;

    public ControlerClient(ViewClient view) {
        this.view = view;

        this.view.getViewPartie().addMouseListener(this);
        this.view.getConnectButton().addActionListener(this);
        this.view.getBroadcastButton().addActionListener(this);
        this.view.getUnicastButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.view.getConnectButton()){
            this.view.changeCard("2");
            // On écoute le serveur.// norlement je recupere les infos saisie dans les field
            this.threadCommunicationClient = new ThreadCommunicationClient(new ModelClient("localhost", 1234), this.view);
            this.threadCommunicationClient.start();
        }

        if(e.getSource() == this.view.getBroadcastButton()){
            //this.client.sendMessage(this.view.getMessageField().getText());
            this.threadCommunicationClient.getClient().sendMessage("broadcast");
        }

        if(e.getSource() == this.view.getUnicastButton()){
            this.threadCommunicationClient.getClient().sendMessage("unicast");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Gson gson = new Gson();
        String json = gson.toJson(new PaquetMouvement(e.getX(),e.getY()));
        this.threadCommunicationClient.getClient().sendMessage(json);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
