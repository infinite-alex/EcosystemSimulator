package simulator.factories;

import java.util.List;

import org.json.JSONObject;

public interface Factory<T> {
    T createInstance(JSONObject info);
    List<JSONObject> getInfo();
}