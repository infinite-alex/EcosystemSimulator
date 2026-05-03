package simulator.model;
import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.strategies.SelectionStrategy;
// danger source??

public abstract class Animal implements Entity, AnimalInfo {

    protected String geneticCode;
    protected Diet diet;
    protected Vector2D pos;
    protected Vector2D dest;
    protected double speed;
    protected double energy;
    protected double age;
    protected State state;
    protected double desire;
    protected double sightRange; 
    protected Animal mateTarget;
    protected SelectionStrategy selectionStrategy;
    protected SelectionStrategy mateStrategy; 
    protected Animal baby;
    protected AnimalMapView regionMngr; 

    protected Animal(String geneticCode, Diet diet, double sightRange, double initSpeed, SelectionStrategy mateStrategy, Vector2D pos) {
        this.geneticCode = geneticCode;
        this.pos = pos;        
        this.age = 0.0;
        this.desire=0.0;
        this.energy = 100.0;      
        this.state = State.NORMAL;
        this.sightRange=sightRange;
        this.mateStrategy=mateStrategy;
        this.speed=Utils.getRandomizedParameter(initSpeed, 0.1);
        this.diet= diet;
       
        if (geneticCode == null || geneticCode.isEmpty())
            throw new IllegalArgumentException("Genetic code must be a non-empty string");

        if (sightRange <= 0)
            throw new IllegalArgumentException("Sight range must be a positive number");

        if (initSpeed <= 0)
            throw new IllegalArgumentException("Initial speed must be a positive number");

        if (mateStrategy == null)
            throw new IllegalArgumentException("Mate strategy cannot be null");

    }
    
    protected Animal(Animal p1, Animal p2) {

        if (p1 == null || p2 == null)
            throw new IllegalArgumentException("Parents cannot be null");

        this.geneticCode = p1.getGeneticCode();
        this.diet = p1.getDiet();
        this.mateStrategy = p2.mateStrategy;
        this.state = State.NORMAL;
        this.desire = 0.0;
        this.dest = null;
        this.baby = null;
        this.mateTarget = null;
        this.regionMngr = null;
        this.energy = (p1.getEnergy() + p2.getEnergy()) / 2.0;
        this.age = 0.0;
        this.pos = p1.getPosition().plus(Vector2D.get_random_vector(-1,1).scale(60.0*(Utils.RAND.nextGaussian()+1)));
        this.sightRange = Utils.getRandomizedParameter((p1.getSightRange()+p2.getSightRange())/2, 0.2);
        this.speed = Utils.getRandomizedParameter((p1.getSpeed()+p2.getSpeed())/2, 0.2);
    }
    
    @Override
    public void update(double dt) {
        age += dt;
    }
    public void init(AnimalMapView regionMngr) {
        this.regionMngr = regionMngr;

        if (this.pos == null) {
            double x = Utils.RAND.nextDouble() * (regionMngr.getWidth() -1 );
            double y = Utils.RAND.nextDouble() * (regionMngr.getHeight() -1);
            this.pos = new Vector2D(x, y);
        } else {
                double x = pos.getX();
                double y = pos.getY();

                int width = regionMngr.getWidth();
                int height = regionMngr.getHeight();

                while (x >= width) x = x - width;
                while (x < 0) x = x + width;
                while (y >= height) y = y - height;
                while (y < 0) y = y + height;

                this.pos = new Vector2D(x, y);
            }
        
        double dx = Utils.RAND.nextDouble() * (regionMngr.getWidth() - 1 );
        double dy = Utils.RAND.nextDouble() * (regionMngr.getHeight() - 1 );

        this.dest = new Vector2D(dx, dy);
    }


	public Animal deliverBaby() {
		Animal b= this.baby;
		this.baby=null;
		return b;
	}
	
    protected void move(double speed) 
    {
        pos = pos.plus(dest.minus(pos).direction().scale(speed));
        double x = pos.getX();
        double y = pos.getY();

        int width = regionMngr.getWidth();
        int height = regionMngr.getHeight();

        while (x >= width) x -= width;
        while (x < 0) x += width;
        while (y >= height) y -= height;
        while (y < 0) y += height;

        pos = new Vector2D(x, y);
    }
    protected void setState(State state) {
     	this.state = state;
         switch (state) {
         case NORMAL:
             setNormalStateAction();
             break;
         case HUNGER:
             setHungerStateAction();
             break;
         case MATE:
             setMateStateAction();
             break;
         case DANGER:
             setDangerStateAction();
             break;
         case DEAD:
             setDeadStateAction();
             break;
         }
     }
    protected abstract void setDeadStateAction();

	protected abstract void setDangerStateAction();

	protected abstract void setMateStateAction();

	protected abstract void setHungerStateAction();

	protected abstract void setNormalStateAction();


	
	@Override       // invata mai mult despre json, functia asta spre exemplu
	public JSONObject asJSON() {
	    JSONObject json = new JSONObject();
	    JSONArray posArray = new JSONArray();
	    posArray.put(pos.getX());
	    posArray.put(pos.getY());
	    json.put("pos", posArray);
	    json.put("gcode", geneticCode);
	    json.put("diet", diet.toString());
	    json.put("state", state.toString());
	    return json;
	}
	
    public void setSelectionStrategy(SelectionStrategy s) 
    {
    	selectionStrategy=s;
    }
    @Override
    public State getState() 
    {
        return state;
    }

    @Override
    public Vector2D getPosition() 
    {
        return pos;
    }

    @Override
    public String getGeneticCode() 
    {
        return geneticCode;
    }

    @Override
    public double getSpeed() 
    {
        return speed;
    }
    
    public double getDesire() {
        return desire;
    }

    public void setDesire(double desire) {
        this.desire = Math.max(0.0, Math.min(100.0, desire));
    }

    @Override
    public double getEnergy() 
    {
        return energy;
    }

    @Override
    public double getAge() 
    {
        return age;
    }

    @Override
    public Vector2D getDestination() 
    {
        return dest;
    }

    @Override
    public boolean isPregnant() 
    {
        return baby != null;
    }
    public void setPregnant(Animal baby) {
        this.baby = baby;
    }

    @Override
    public abstract Diet getDiet();

    @Override
    public abstract double getSightRange();


}
