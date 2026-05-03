package simulator.factories;

import org.json.JSONObject;

import simulator.strategies.SelectFirst;
import simulator.strategies.SelectionStrategy;

public class SelectFirstBuilder extends Builder<SelectionStrategy> {

    public SelectFirstBuilder() {
		super("first", "select first");
	}
    @Override
    public SelectionStrategy createInstance(JSONObject data) {
        return new SelectFirst();
    }
}