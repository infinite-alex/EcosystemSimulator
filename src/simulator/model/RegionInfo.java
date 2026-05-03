package simulator.model;

import java.util.List;

public interface RegionInfo extends JSONable {  
	public List<AnimalInfo> getAnimalsInfo(); // aici am scris existenta metoda, iar in region o implementezi
	// AnimalInfo = lista read only
	
/*	 De ce NU merge:
		return animals;
		Animal implements AnimalInfo

		Java NU permite:
		List<Animal> → List<AnimalInfo>

		Pentru că generics în Java NU sunt la fel, 
		sunt doua obiecte complet diferite
		
		AnimalInfo este doar sa vezi lista animalele
		dar sa nu le modifice
		
  
	// Iterable<AnimalInfo>  
	/*It is an interface that defines what information
	 *  can be seen about a region and includes methods 
	 *  that query the state of the region but never modify it. 
	 *  For now, the interface only extends JSONable, later
	 *   we will add more methods.
	 * 
	 */
}
