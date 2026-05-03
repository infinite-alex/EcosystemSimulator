package simulator.model;
import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.strategies.SelectionStrategy;

public class Sheep extends Animal {

	private SelectionStrategy dangerStrategy;
	private Animal dangerSource;
    public Sheep(SelectionStrategy mateStrategy, SelectionStrategy dangerStrategy,  Vector2D pos) {
        super("Sheep", Diet.HERBIVORE, 40.0, 35, mateStrategy, pos);
       this.setdangerStrategy(dangerStrategy);
    }
    
    protected Sheep(Sheep p1, Animal p2){
    	super(p1,p2);
    	this.dangerStrategy = p1.getDangerStrategy();
    	this.dangerSource = null;
    	
    }
    @Override
    public Diet getDiet() {
        return Diet.HERBIVORE;
    }

    @Override
    public double getSightRange() {
        return 40.0;
    }
    @Override
    public void update(double dt) {

        if (state == State.DEAD)
            return;

        switch (state) {
            case NORMAL:
                updateNormal(dt);
                break;
            case DANGER:
                updateDanger(dt);
                break;
            case MATE:
                updateMate(dt);
                break;
            default:
                break; 
        }

        if (energy <= 0.0 || age > 8.0)
            setState(State.DEAD);

        if (state != State.DEAD) {
            energy += regionMngr.getFood((Animal)this, dt);
            energy = Math.max(0.0, Math.min(100.0, energy));
        }
    }
    private void updateNormal(double dt) {
        if (pos.distanceTo(dest) < 8.0) {
            double dx = Utils.RAND.nextDouble() * regionMngr.getWidth();
            double dy = Utils.RAND.nextDouble() * regionMngr.getHeight();
            dest = new Vector2D(dx, dy);
        }

        move(speed*dt*Math.exp((energy-100.0)*0.007));
        age += dt;
        energy -= 20.0 * dt;
        desire += 40.0 * dt;
        energy = Math.max(0.0, Math.min(100.0, energy));
        desire = Math.max(0.0, Math.min(100.0, desire));
        if (dangerSource == null)
            searchDanger();
        if (dangerSource != null)
            setState(State.DANGER);
        else if (desire > 65.0)
            setState(State.MATE);
    }

    private void updateDanger(double dt) {
        if (dangerSource != null && dangerSource.getState() == State.DEAD)
            dangerSource = null;
        if (dangerSource == null) {
            updateNormal(dt);
            return;
        }
        dest = pos.plus(pos.minus(dangerSource.getPosition()).direction());
        move(2.0 * speed * dt * Math.exp((energy - 100.0) * 0.007));
        age += dt;
        energy -= 20.0 * 1.2 * dt;
        desire += 40.0 * dt;
        energy = Math.max(0.0, Math.min(100.0, energy));
        desire = Math.max(0.0, Math.min(100.0, desire));
        if (pos.distanceTo(dangerSource.getPosition()) > getSightRange())
            dangerSource = null;

        if (dangerSource == null) {
            searchDanger();
            if (dangerSource == null) {
                if (desire < 65.0)
                    setState(State.NORMAL);
                else
                    setState(State.MATE);
            }
        }
    }

    private void updateMate(double dt) {

        if (mateTarget != null &&
           (mateTarget.getState() == State.DEAD ||
            pos.distanceTo(mateTarget.getPosition()) > getSightRange()))
            mateTarget = null;

        if (mateTarget == null) {
            var animals = regionMngr.getAnimalsinRange(this, getSightRange());
            animals.removeIf(a -> !(a instanceof Sheep));
            mateTarget = mateStrategy.select(this, animals);

            if (mateTarget == null) {
                updateNormal(dt);
                return;
            }
        }

        dest = mateTarget.getPosition();
        move(2.0 * speed * dt * Math.exp((energy - 100.0) * 0.007));
        age += dt;
        energy -= 20.0 * 1.2 * dt;
        desire += 40.0 * dt;
        energy = Math.max(0.0, Math.min(100.0, energy));
        desire = Math.max(0.0, Math.min(100.0, desire));

        if (pos.distanceTo(mateTarget.getPosition()) < 8.0) {

            desire = 0.0;
            mateTarget.setDesire(0.0);

            if (!this.isPregnant() && Utils.RAND.nextDouble() < 0.9)
                this.setPregnant(new Sheep(this, (Sheep) mateTarget));

            mateTarget = null;
        }
        if (dangerSource == null)
            searchDanger();
        if (dangerSource != null)
            setState(State.DANGER);
        else if (desire < 65.0)
            setState(State.NORMAL);
    }
    
    private void searchDanger() {
        var animals = regionMngr.getAnimalsinRange(this, getSightRange());
        if (animals == null || animals.isEmpty()) {
            dangerSource = null;
            return;
        }
        animals.removeIf(a -> a.getDiet() != Diet.CARNIVORE);
        if (animals.isEmpty())
            dangerSource = null;
        else
            dangerSource = dangerStrategy.select(this, animals);
    }
	public SelectionStrategy getDangerStrategy() {
		return dangerStrategy;
	}

	public void setdangerStrategy(SelectionStrategy dangerStrategy) {
		this.dangerStrategy = dangerStrategy;
	}

	public Animal getDangerSource() {
		return dangerSource;
	}

	public void setDangerSource(Animal dangerSource) {
		this.dangerSource = dangerSource;
	}
	@Override
	protected void setNormalStateAction() {
	    mateTarget = null;
	    dangerSource = null;
	}

	@Override
	protected void setMateStateAction() {
	    dangerSource = null;
	}

	@Override
	protected void setHungerStateAction() {
	    // Sheep nu are comportament special pentru hunger
	}

	@Override
	protected void setDangerStateAction() {
	    mateTarget = null;
	}

	@Override
	protected void setDeadStateAction() {
	    mateTarget = null;
	    dangerSource = null;
	}
}