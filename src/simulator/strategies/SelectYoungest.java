
package simulator.strategies;
import java.util.List;
import simulator.model.Animal;

public class SelectYoungest implements SelectionStrategy {

    @Override
    public Animal select(Animal a, List<Animal> as) {
        if( as.isEmpty() ) 
        {
        	return null;
        }
        Animal youngest= as.get(0);
        
        for( int i=1; i<as.size(); i++) {
        	if (as.get(i).getAge() < youngest.getAge()) {
        		youngest=as.get(i);
        	}
        }
        return youngest;
    }
}