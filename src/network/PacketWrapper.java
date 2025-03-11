package network;

import com.google.gson.JsonElement;

/**
 * Pour spécifier le type de paquet et son contenu. Et pourvoir faire le bon cast
 * après la désérialisation.<p>
 * Tous les échanges dans le reseau sont des objets de cette classe.<p>
 * Dans type on met le nom de la classe du paquet et dans content le contenu du paquet.
 */
public class PacketWrapper {
    protected String type;
    protected JsonElement content;

}
