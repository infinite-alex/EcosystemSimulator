package simulator.factories;

import org.json.JSONObject;

import simulator.strategies.SelectClosest;
import simulator.strategies.SelectionStrategy;

public class SelectClosestBuilder extends Builder<SelectionStrategy> {

    public SelectClosestBuilder() {
		super("closest", "select closest");
	}

    public SelectionStrategy createInstance(JSONObject data) {
        return new SelectClosest();
    }
}