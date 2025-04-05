package network.packets;

/**
 * PacketOpenPanelControl is a class that represents a packet used to open a control panel in the game.
 * It contains information about the entity type, health, whether it is planted, and the resource associated with it.
 * This class is used to send information about the control panel to the client.
 */
public class PacketOpenPanelControl {

    private String entityType;
    private float health;
    private boolean isPlanted;
    private String ressource;

    /**
     * Constructor for the PacketOpenPanelControl class.
     * @param entityType The type of entity associated with the control panel.
     */
    public PacketOpenPanelControl(String entityType) {
        this.entityType=entityType;
    }


    /**
     * Constructor for the PacketOpenPanelControl class.
     * @param entityType The type of entity associated with the control panel.
     * @param health The health of the entity.
     */
    public PacketOpenPanelControl(String entityType, float health) {
        this(entityType);
        this.health = health;
    }

    /**
     * Constructor for the PacketOpenPanelControl class.
     * @param entityType The type of entity associated with the control panel.
     * @param isPlanted A boolean indicating if the entity is planted.
     */
    public PacketOpenPanelControl(String entityType, boolean isPlanted ){
        this(entityType);
        this.isPlanted = isPlanted;
    }

    /**
     * Constructor for the PacketOpenPanelControl class.
     * @param entityType The type of entity associated with the control panel.
     * @param isPlanted A boolean indicating if the entity is planted.
     * @param ressource The resource associated with the entity.
     *
     */
    public PacketOpenPanelControl(String entityType, boolean isPlanted, String ressource){
        this(entityType, isPlanted);
        this.ressource=ressource;
    }


    /**
     * Getters for the entity type and health.
     */
    /**
     * @return The type of entity associated with the control panel.
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * @return The health of the entity.
     */
    public float getHealth() {
        return health;
    }

    /**
     * @return The resource associated with the entity.
     */
    public String getRessource(){
        return this.ressource;
    }

    /**
     * @return A boolean indicating if the entity is planted.
     */
    public boolean isPlanted() {
        return this.isPlanted;
    }
}
