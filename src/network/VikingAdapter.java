package network;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import server.model.Farmer;
import server.model.Viking;
import server.model.Warrior;

import java.io.IOException;

/**
 * Pour traiter les classes lors de la déserialisation.
 * à généraliser par la suite pour toutes les classes abstraites
 */
public class VikingAdapter extends TypeAdapter<Viking> {

    private final Gson gson = new Gson();

    @Override
    public void write(JsonWriter out, Viking value) throws IOException {
        JsonObject jsonObj = gson.toJsonTree(value).getAsJsonObject();
        if (value instanceof Warrior) {
            jsonObj.addProperty("type", "warrior");
        } else if (value instanceof Farmer) {
            jsonObj.addProperty("type", "farmer");
        }
        gson.toJson(jsonObj, out);
    }

    @Override
    public Viking read(JsonReader in) throws IOException {
        JsonObject jsonObj = JsonParser.parseReader(in).getAsJsonObject();
        String type = jsonObj.get("type").getAsString();

        switch (type.toLowerCase()) {
            case "warrior":
                return gson.fromJson(jsonObj, Warrior.class);
            case "farmer":
                return gson.fromJson(jsonObj, Farmer.class);
            default:
                throw new JsonParseException("Unknown type: " + type);
        }
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Viking.class, new VikingAdapter())
                .create();
    }
}
