package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.strategies.SelectionStrategy;

public class Wolf extends Animal {
    private Animal huntTarget;
    private SelectionStrategy huntingStrategy;

    public Wolf(SelectionStrategy mateStrategy, SelectionStrategy huntingStrategy, Vector2D pos) {
        super("Wolf", Diet.CARNIVORE, 50.0, 60.0, mateStrategy, pos);
        this.huntingStrategy = huntingStrategy;
        this.huntTarget = null;
    }

    protected Wolf(Wolf p1, Animal p2) {
        super(p1, p2);
        this.huntingStrategy = p1.huntingStrategy;
        this.huntTarget = null;
    }

    @Override
    public Diet getDiet() {
        return Diet.CARNIVORE;
    }

    @Override
    public double getSightRange() {
        return 50.0;
    }

    @Override
    public void update(double dt) {

        if (state == State.DEAD)
            return;

        switch (state) {
            case NORMAL:
                updateNormal(dt);
                break;
            case HUNGER:
                updateHunger(dt);
                break;
            case MATE:
                updateMate(dt);
                break;
            default:
                break;
        }
        if (pos.getX() < 0 || pos.getX() > regionMngr.getWidth() || pos.getY() < 0 || pos.getY() > regionMngr.getHeight()) {
        	double x = Math.max(0, Math.min(regionMngr.getWidth(), pos.getX()));
        	double y = Math.max(0, Math.min(regionMngr.getHeight(), pos.getY()));
        	pos = new Vector2D(x, y);
        	setState(State.NORMAL);
        }
        if (energy <= 0.0 || age > 14.0)
            setState(State.DEAD);

        if (state != State.DEAD) {
            energy += regionMngr.getFood(this, dt);
            energy = Math.max(0.0, Math.min(100.0, energy));
        }
    }

    private void updateNormal(double dt) {
        if (pos.distanceTo(dest) < 8.0) {
            double dx = Utils.RAND.nextDouble() * regionMngr.getWidth();
            double dy = Utils.RAND.nextDouble() * regionMngr.getHeight();
            dest = new Vector2D(dx, dy);
        }

        move(speed * dt * Math.exp((energy - 100.0) * 0.007));
        age += dt;
        energy -= 18.0 * dt;
        desire += 30.0 * dt;
        clampValues();

        if (energy < 50.0)
            setState(State.HUNGER);
        else if (desire > 65.0)
            setState(State.MATE);
    }

    private void updateHunger(double dt) {
        if (huntTarget == null ||
            huntTarget.getState() == State.DEAD ||
            pos.distanceTo(huntTarget.getPosition()) > getSightRange()) {

            searchHuntTarget();
        }
        if (huntTarget == null) {
            updateNormal(dt);
            return;
        }
        dest = huntTarget.getPosition();
        move(3.0 * speed * dt * Math.exp((energy - 100.0) * 0.007));
        age += dt;
        energy -= 18.0 * 1.2 * dt;
        desire += 30.0 * dt;
        clampValues();
        if (pos.distanceTo(huntTarget.getPosition()) < 8.0) {
            huntTarget.setState(State.DEAD);
            huntTarget = null;
            energy += 50.0;
            clampValues();
        }
        if (energy > 50.0) {
            if (desire < 65.0)
                setState(State.NORMAL);
            else
                setState(State.MATE);
        }
    }

    private void updateMate(double dt) {
        if (mateTarget != null &&
            (mateTarget.getState() == State.DEAD ||
             pos.distanceTo(mateTarget.getPosition()) > getSightRange()))
            mateTarget = null;

        if (mateTarget == null) {
            var animals = regionMngr.getAnimalsinRange(this, getSightRange());
            animals.removeIf(a -> !(a instanceof Wolf));
            mateTarget = mateStrategy.select(this, animals);

            if (mateTarget == null) {
                updateNormal(dt);
                return;
            }
        }

        dest = mateTarget.getPosition();
        move(3.0 * speed * dt * Math.exp((energy - 100.0) * 0.007));
        age += dt;
        energy -= 18.0 * 1.2 * dt;
        desire += 30.0 * dt;
        clampValues();
        if (pos.distanceTo(mateTarget.getPosition()) < 8.0) {
            desire = 0.0;
            mateTarget.setDesire(0.0);
            if (!this.isPregnant() && Utils.RAND.nextDouble() < 0.75)
                this.setPregnant(new Wolf(this, mateTarget));
            energy -= 10.0;
            clampValues();

            mateTarget = null;
        }
        if (energy < 50.0)
            setState(State.HUNGER);
        else if (desire < 65.0)
            setState(State.NORMAL);
    }

    private void searchHuntTarget() {
        var animals = regionMngr.getAnimalsinRange(this, getSightRange());
        if (animals == null || animals.isEmpty()) {
            huntTarget = null;
            return;
        }
        animals.removeIf(a -> a.getDiet() != Diet.HERBIVORE);
        if (animals.isEmpty())
            huntTarget = null;
        else
            huntTarget = huntingStrategy.select(this, animals);
    }

    private void clampValues() {
        energy = Math.max(0.0, Math.min(100.0, energy));
        desire = Math.max(0.0, Math.min(100.0, desire));
    }

    @Override
    protected void setNormalStateAction() {
        huntTarget = null;
        mateTarget = null;
    }

    @Override
    protected void setMateStateAction() {
        huntTarget = null;
    }

    @Override
    protected void setHungerStateAction() {
        mateTarget = null;
    }

    @Override
    protected void setDangerStateAction() {
        // Wolf nu folosește starea DANGER
    }

    @Override
    protected void setDeadStateAction() {
        huntTarget = null;
        mateTarget = null;
    }
	
}