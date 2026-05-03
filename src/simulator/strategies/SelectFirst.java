package simulator.strategies;
import java.util.List;
import simulator.model.Animal;

public class SelectFirst implements SelectionStrategy {

    @Override
    public Animal select(Animal a, List<Animal> as) {
        if( as.isEmpty() ) 
        {
        	return null;
        }
        return as.get(0);

        
    }
}
// cum selecteaza primul? intr-o lista se afla primul, daca e goala ii da null,
// daca nu e, ii da primul animal