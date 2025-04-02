package network.packets;

public class PacketOpenPanelControl {

    public PacketOpenPanelControl(String entityType) {
        this.entityType=entityType;
    }

    private String entityType;
    private float health;
    private boolean isPlanted;
    private String ressource;

    public PacketOpenPanelControl(String entityType, float health) {
        this(entityType);
        this.health = health;
    }

    public PacketOpenPanelControl(String entityType, boolean isPlanted ){
        this(entityType);
        this.isPlanted = isPlanted;
    }

    public PacketOpenPanelControl(String entityType, boolean isPlanted, String ressource){
        this(entityType, isPlanted);
        this.ressource=ressource;
    }

    public String getEntityType() {
        return entityType;
    }

    public float getHealth() {
        return health;
    }
    public String getRessource(){
        return this.ressource;
    }
    public boolean isPlanted() {
        return this.isPlanted;
    }
}
