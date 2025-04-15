package network;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import server.model.*;

import java.io.IOException;

/**
 * Pour traiter les classes lors de la déserialisation.
 * à généraliser par la suite pour toutes les classes abstraites
 */
public class ModelAdapter {

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = new Gson();
        gsonBuilder.registerTypeAdapter(Entity.class, new EntityAdapter(gson));
        gsonBuilder.registerTypeAdapter(Viking.class, new VikingAdapter(gson));
        gsonBuilder.registerTypeAdapter(Livestock.class, new LivestockAdapter(gson));
        gsonBuilder.registerTypeAdapter(Vegetable.class, new VegetableAdapter(gson));

        return gsonBuilder.create();
    }
}

class EntityAdapter extends TypeAdapter<Entity>{
    private Gson gson;
    public EntityAdapter(Gson gson) {
        this.gson = gson;
    }
    @Override
    public void write(JsonWriter out, Entity value) throws IOException {
        JsonObject jsonObj = gson.toJsonTree(value).getAsJsonObject();
        if (value instanceof Warrior) {
            jsonObj.addProperty("type", "warrior");
        } else if (value instanceof Farmer) {
            jsonObj.addProperty("type", "farmer");
        }else if (value instanceof Sheep) {
            jsonObj.addProperty("type", "sheep");
        }else if (value instanceof Wheat) {
            jsonObj.addProperty("type", "wheat");
        } else if (value instanceof Cow) {
            jsonObj.addProperty("type", "cow");
        } else if (value instanceof AbsenceVegetable) {
            jsonObj.addProperty("type", "absence");
        }
        gson.toJson(jsonObj, out);
    }

    @Override
    public Entity read(JsonReader in) throws IOException {
        JsonObject jsonObj = JsonParser.parseReader(in).getAsJsonObject();
        String type = jsonObj.get("type").getAsString();

        switch (type.toLowerCase()) {
            case "warrior":
                return gson.fromJson(jsonObj, Warrior.class);
            case "farmer":
                return gson.fromJson(jsonObj, Farmer.class);
            case "sheep":
                return gson.fromJson(jsonObj, Sheep.class);
            case "cow":
                return gson.fromJson(jsonObj, Cow.class);
            case "wheat":
                return gson.fromJson(jsonObj, Wheat.class);
            case "absence":
                return gson.fromJson(jsonObj, AbsenceVegetable.class);
            default:
                throw new JsonParseException("Unknown type: " + type);
        }
    }
}

// fait une class équivalente à EntityAdapter mais qui VikingAdapter
// sachant que les sous classes concrete sont Warrior et Farmer
class VikingAdapter extends TypeAdapter<Viking> {
    private Gson gson;

    public VikingAdapter(Gson gson) {
        this.gson = gson;
    }

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
}


class LivestockAdapter extends TypeAdapter<Livestock> {
    private Gson gson;

    public LivestockAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, Livestock value) throws IOException {
        JsonObject jsonObj = gson.toJsonTree(value).getAsJsonObject();
        if (value instanceof Sheep) {
            jsonObj.addProperty("type", "sheep");
        } else if (value instanceof Cow) {
            jsonObj.addProperty("type", "cow");
        }
        gson.toJson(jsonObj, out);
    }

    @Override
    public Livestock read(JsonReader in) throws IOException {
        JsonObject jsonObj = JsonParser.parseReader(in).getAsJsonObject();
        String type = jsonObj.get("type").getAsString();

        switch (type.toLowerCase()) {
            case "sheep":
                return gson.fromJson(jsonObj, Sheep.class);
            case "cow":
                return gson.fromJson(jsonObj, Cow.class);
            default:
                throw new JsonParseException("Unknown type: " + type);
        }
    }
}

// fait une class équivalente à EntityAdapter mais qui VegetableAdapter
// sachant que les sous classes concrete sont Wheat, AbsenceVegetable
class VegetableAdapter extends TypeAdapter<Vegetable> {
    private Gson gson;

    public VegetableAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, Vegetable value) throws IOException {
        JsonObject jsonObj = gson.toJsonTree(value).getAsJsonObject();
        if (value instanceof Wheat) {
            jsonObj.addProperty("type", "wheat");
        } else if (value instanceof AbsenceVegetable) {
            jsonObj.addProperty("type", "absence");
        }
        gson.toJson(jsonObj, out);
    }

    @Override
    public Vegetable read(JsonReader in) throws IOException {
        JsonObject jsonObj = JsonParser.parseReader(in).getAsJsonObject();
        String type = jsonObj.get("type").getAsString();

        switch (type.toLowerCase()) {
            case "wheat":
                return gson.fromJson(jsonObj, Wheat.class);
            case "absence":
                return gson.fromJson(jsonObj, AbsenceVegetable.class);
            default:
                throw new JsonParseException("Unknown type: " + type);
        }
    }
}