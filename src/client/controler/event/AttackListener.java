package client.controler.event;

public interface AttackListener {
    /**
     * This method is called when an attack event occurs.
     *
     * @param event The attack event that occurred.
     */
    void onAttack(AttackEvent event);
}
