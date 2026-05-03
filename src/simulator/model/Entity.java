package simulator.model;

import simulator.misc.Vector2D;

public interface Entity {
    Vector2D getPosition();
    void update(double dt);
}
