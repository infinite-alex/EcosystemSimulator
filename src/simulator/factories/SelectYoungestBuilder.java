package simulator.factories;

import org.json.JSONObject;

import simulator.strategies.SelectYoungest;
import simulator.strategies.SelectionStrategy;

public class SelectYoungestBuilder extends Builder<SelectionStrategy> {

    public SelectYoungestBuilder() {
		super("youngest", "select youngest");
	}
    @Override
    public SelectionStrategy createInstance(JSONObject data) {
        return new SelectYoungest();
    }
}