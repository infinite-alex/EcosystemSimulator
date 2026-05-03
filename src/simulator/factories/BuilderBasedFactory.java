package simulator.factories;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {

	private Map<String, Builder<T>> builders;  
    private List<JSONObject> buildersInfo;
    
    public BuilderBasedFactory() {   // fac aici un builder gol
        this.builders = new HashMap<>();
    	this.buildersInfo = new ArrayList<>(); // ambele merg ArrayList<JSONObject>()

  	}
    public void addBuilder(Builder<T> b) {    // aici il adaug
	      // add an entry "b.getTypeTag() |−> b" to builders.   
	      builders.put(b.getTypeTag(), b);
	      // add b.getInfo() to buildersInfo  
	      buildersInfo.add(b.getInfo()); 
	}
    public BuilderBasedFactory(List<Builder<T>> builders) {
        this();  // trimite spre parametrii fiecaruia
        
        for(Builder<T> b: builders) {
        	addBuilder(b);
        }
	       // call addBuilder(b) for each builder b in builder  
	       // … 
    }

		@Override  
		public T createInstance(JSONObject info) {  
			if (info == null) {  
				throw new IllegalArgumentException("’info’ cannot be null");  
			}

		    String type = info.getString("type");
		    Builder<T> b = builders.get(type);
		    if (b != null) {
		        JSONObject data = info.has("data") ? info.getJSONObject("data"): new JSONObject();
		        T obj = b.createInstance(data);
		        if (obj != null) {
		            return obj;
		        }
		    }

			// Look for a builder with a tag equals to info.getString("type"), in the
	    //  map _builder, and call its createInstance method and return the result
	    // if it is not null. The value you pass to createInstance is the following
	    // because 'data' is optional:
	    //

	    // If no builder is found or the result is null ...  
			throw new IllegalArgumentException("Unrecognized ‘info’:" + info.toString());  
		}

		@Override  
		public List<JSONObject> getInfo() {  
			return Collections.unmodifiableList(buildersInfo);  
		}
}