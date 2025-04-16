package client.controler.event;

public class RepliEvent {
    private int idCampAttaque;

    public RepliEvent(int idCampAttaque) {
        this.idCampAttaque = idCampAttaque;
    }

    public int getIdCampAttaque() {
        return idCampAttaque;
    }
}
