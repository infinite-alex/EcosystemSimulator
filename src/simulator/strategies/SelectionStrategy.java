package simulator.strategies;
import java.util.List;
import simulator.model.Animal;

public interface SelectionStrategy {
	Animal select(Animal a, List<Animal> as);
}

/*
The select method selects for animal a an animal from the list as according to the concrete strategy's criteria (it is assumed that the object corresponding to animal a will invoke select). If the list is empty, it always returns null. It is assumed that a does not appear in the list as.

Implement the following strategies:

SelectFirst: returns the first animal in the list as.
SelectClosest: returns the animal closest to animal a from the list as.
SelectYoungest: returns the youngest animal from the list as.
You can implement more strategies if you wish.

*/