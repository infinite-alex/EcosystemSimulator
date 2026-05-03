 package simulator.model;

public class DefaultRegion extends Region{
    public DefaultRegion() {
        super();
    }
	@Override
	public void update(double dt) {}

	@Override
	public double getFood(Animal a, double dt) {
		if ( a.getDiet() == Diet.CARNIVORE)
			return 0.0;
		
		int n=0;
		for( int i = 0; i<animals.size(); i++) {
		    Animal an = animals.get(i);
			if ( an.getDiet() == Diet.HERBIVORE)
				n++;
		}
		return 60.0*Math.exp(-Math.max(0,n-5.0)*2.0)*dt;
		
	}
    public String toString() {
    	return "infinite food";
    }
	
 // parintele are deja aceasta functie, dar o rescriem aici

}
