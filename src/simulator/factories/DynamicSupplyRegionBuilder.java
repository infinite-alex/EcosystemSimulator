package simulator.factories;

import org.json.JSONObject;
import static simulator.misc.Constants.*;

import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region> {

    public DynamicSupplyRegionBuilder() {
		super("dynamic", "food depending on how many sheep are");

	}

    public Region createInstance(JSONObject data) {
    	double food = data.has("food") ? data.getDouble("food") : INIT_FOOD;
    	double factor = data.has("factor") ? data.getDouble("factor") : FACTOR;

        return new DynamicSupplyRegion(food,factor);
    }
    protected void fillInData(JSONObject data) 
    {  
    	data.put("factor", "food increase factor");
    	data.put("food", "initial amount of food (optional, default 100.0)");
    }
    


}
