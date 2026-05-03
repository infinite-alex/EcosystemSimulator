package simulator.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Region implements FoodSupplier, RegionInfo {

    protected List<Animal> animals;
  
    protected Region() {
      
        animals = new ArrayList<>();
    }


    protected final void addAnimal(Animal a) {
        animals.add(a);
    }

    protected final void removeAnimal(Animal a) {
        animals.remove(a);
    }
    public abstract void update(double dt);

    public final List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    @Override
    public JSONObject asJSON() {
        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();

        for (Animal a : animals) {
            arr.put(a.asJSON());
        }

        json.put("animals", arr);
        return json;
    }
    
    public List<AnimalInfo> getAnimalsInfo() {  
    	  return new ArrayList<>(animals); // se puede usar Collections.unmodifiableList(animals);  
    	}
 
}
    
    


