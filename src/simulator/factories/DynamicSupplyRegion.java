package simulator.factories;

// deci in default region a avut ca animalele mancau si acum noi
//facem sa mai creasca sau ca se schimba


import simulator.model.Animal;
import static simulator.misc.Constants.*;
import simulator.model.Diet;
import simulator.model.Region;

public class DynamicSupplyRegion extends Region {
	private double food;
	private double factor;
	
    public DynamicSupplyRegion(double food, double factor) {
        super();     // ← OBLIGATORIU
        this.food = food;
        this.factor = factor;
    }	
    @Override
	public double getFood(Animal a, double dt) {
		if ( a.getDiet() == Diet.CARNIVORE)
			return 0.0;
		
		int n=0;
		for (int i = 0; i < animals.size(); i++) {
		    Animal current = animals.get(i);
		    if (current.getDiet() == Diet.HERBIVORE) {
		        n++;
		    }
		}
		
		double amount = FOOD_EAT_RATE_HERBS * Math.exp(-Math.max(0, n - FOOD_SHORTAGE_TH_HERBS) * FOOD_SHORTAGE_EXP_HERBS) * dt;
        double eaten = Math.min(food, amount);
        food -= eaten;

        return eaten;	
	}
    @Override
    public void update(double dt) {
    	food += dt * factor;

    }
    @Override
    public String toString() {
    	return "food depending on the animals in the region";
    }
}
