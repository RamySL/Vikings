package server.model;

import java.util.HashMap;
import java.util.Map;

public class CampManager {
    private static Map<Integer, Camp> camps = new HashMap<>();

    public static void addCamp(Camp camp) {
        camps.put(camp.getId(), camp);
    }

    public static Camp getCampById(int id) {
        return camps.get(id);
    }
}