package simulator.model;
import java.util.*;
import org.json.JSONObject;

import simulator.factories.Factory;

public class Simulator implements JSONable, Observable<EcoSysObserver>{

    private RegionManager regionManager;
    private List<Animal> animals;
    private Factory<Animal> animalsFactory;
    private Factory<Region> regionsFactory;
    private List<EcoSysObserver> observers;
    private double time;

    public Simulator(int cols, int rows, int width, int height, Factory<Animal> animalsFactory, Factory<Region> regionsFactory) {
        this.animalsFactory = animalsFactory;
        this.regionsFactory = regionsFactory;
        this.regionManager = new RegionManager(cols, rows, width, height);
        this.animals = new ArrayList<>();
        this.observers= new ArrayList<>();
        this.time = 0.0;
    }

    private void setRegion(int row, int col, Region r) {
        regionManager.setRegion(row, col, r);
    }

    public void setRegion(int row, int col, JSONObject rJson) {
        Region r = regionsFactory.createInstance(rJson);
        setRegion(row, col, r);
        
        notifyonRegionSet(row, col, r);
    }
    private void notifyonRegionSet(int row, int col, Region r) {  
  	  for(EcoSysObserver o : observers) { // pt fiecare observer trimitem mesajul
  		  o.onRegionSet(row, col, regionManager, r);  
  	  }
  	}
    
    private void notifyonAnimalAdded(Animal a) {  
    	  List<AnimalInfo> copie = new ArrayList<>(animals);
    	  for( EcoSysObserver o : observers) { // pt fiecare observer trimitem mesajul
    		  o.onAnimalAdded(time, regionManager, copie, a);  
    	  }
    }
    private void addAnimal(Animal a) {
        animals.add(a);
        regionManager.registerAnimal(a);

        
        notifyonAnimalAdded(a);
    }

    public void addAnimal(JSONObject aJson) {
        Animal a = animalsFactory.createInstance(aJson);
        addAnimal(a);
    }

    public AnimalMapView getMapInfo() {
        return regionManager;
    }
    
    public List<? extends AnimalInfo> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    public double getTime() {
        return time;
    }

    public void advance(double dt)
    {
        time += dt;
        animals.removeIf(a -> {
            if (a.getState() == State.DEAD) {
                regionManager.unregisterAnimal(a);
                return true;
            }
            return false;
        });

        for (Animal a : new ArrayList<>(animals)) {
            a.update(dt);
            regionManager.updateAnimalRegion(a);
        }

        regionManager.updateAllRegions(dt);

        for (Animal a : new ArrayList<>(animals)) {
            if (a.isPregnant()) {
                Animal baby = a.deliverBaby();
                addAnimal(baby);
            }
        }
        
        notifyonAdvance(dt);
    }
    private void notifyonAdvance(double dt) {  
  	  List<AnimalInfo> copie = new ArrayList<>(animals);
  	  for( EcoSysObserver o : observers) { // pt fiecare observer trimitem mesajul
  		  o.onAdvance(time, regionManager, copie, dt); 
  		  
  	  }
    }

    @Override
    public JSONObject asJSON() {
        JSONObject json = new JSONObject();
        json.put("time", time);
        json.put("state", regionManager.asJSON());
        return json;
    }
    private void notifyonReset() {
    	List<AnimalInfo> copie = new ArrayList<>(animals);
    	for (EcoSysObserver o : observers) {
            o.onReset(time, regionManager, copie);
        }
    }
    public void reset(int cols, int rows, int width, int height) 
    {   
    	for (Animal a : animals) {
    	    regionManager.unregisterAnimal(a);
    	}
    	this.animals = new ArrayList<>();
        this.regionManager = new RegionManager(cols, rows, width, height);
        this.time = 0.0;
        
        notifyonReset();
    	
    }
    

	@Override
	public void addObserver(EcoSysObserver o) {
		if(!observers.contains(o)) {
			observers.add(o);
			o.onRegister(time, regionManager, new ArrayList<>(animals));  
		}
		
	}

	@Override
	public void removeObserver(EcoSysObserver o) {
		observers.remove(o);
		
	}

}