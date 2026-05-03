package simulator.strategies;
import java.util.List;
import simulator.model.Animal;


public class SelectClosest implements SelectionStrategy{

	@Override
	public Animal select(Animal a, List<Animal> as){
		if (as.isEmpty())
			return null;
		double nrclosest=a.getPosition().distanceTo(as.get(0).getPosition());
		Animal closest=as.get(0);
		for(int i=1; i<as.size(); i++) {
			if(nrclosest >a.getPosition().distanceTo(as.get(i).getPosition())){
				nrclosest=a.getPosition().distanceTo(as.get(i).getPosition());
				closest=as.get(i);
			}
		}
		
		return closest;

	}
}
