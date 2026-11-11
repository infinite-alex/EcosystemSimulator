package simulator.factories;
import org.json.JSONArray;
import org.json.JSONObject;

import simulator.strategies.SelectFirst;
import simulator.strategies.SelectionStrategy;
import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.Wolf;


public class WolfBuilder extends Builder<Animal> {

    private Factory<SelectionStrategy> strategyFactory;

    public WolfBuilder(Factory<SelectionStrategy> strategyFactory) {
        super("wolf", "Creates a wolf");
        this.strategyFactory = strategyFactory;
    }
    
    @Override
    public Animal createInstance(JSONObject data) {
    	SelectionStrategy mateStrat = new SelectFirst();
    	SelectionStrategy huntStrat = new SelectFirst();
    	Vector2D pos = null;
    	
    	if(data.has("mate_strategy")) {
    	 mateStrat =
                strategyFactory.createInstance(data.getJSONObject("mate_strategy"));
    	}
    	
    	if(data.has("hunger_strategy")) {
        huntStrat =
                strategyFactory.createInstance(data.getJSONObject("hunger_strategy"));}
    	
    	if(data.has("pos")) {
    		JSONObject posObj = data.getJSONObject("pos");
    		JSONArray x_info = posObj.getJSONArray("x_range");
    		JSONArray y_info = posObj.getJSONArray("y_range");
    		
    		double x = Utils.RAND.nextDouble(x_info.getDouble(0), x_info.getDouble(1));
    		double y = Utils.RAND.nextDouble(y_info.getDouble(0), y_info.getDouble(1));
    		
    		pos = new Vector2D(x,y);
    		
    	}
            return new Wolf(mateStrat, huntStrat, pos);
    }
}