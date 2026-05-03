
package simulator.model;

import java.util.List;

public interface AnimalMapView extends JSONable{
    int getWidth();
    int getHeight();
    public int getCols();  
	public int getRows();  
    double getFood(Animal a, double dt);
    List<Animal> getAnimals();
    List<Animal> getAnimalsinRange(Animal a, double range);
	public int getRegionWidth();  
	public int getRegionHeight();  
}