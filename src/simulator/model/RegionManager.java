package simulator.model;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.MapInfo.RegionData;

public class RegionManager implements MapInfo, AnimalMapView, Iterable<MapInfo.RegionData> {

    private int width, height;      
    private int cols, rows;         
    private int cellWidth, cellHeight; 
    private Region[][] regions;     
    private Map<Animal, Region> animalRegion; 
    private List<Animal> animals;  


    public RegionManager(int cols, int rows, int width, int height) {
        this.cols = cols;
        this.rows = rows;
        this.width = width;
        this.height = height;

        this.cellWidth = (int) width / cols;
        this.cellHeight = (int) height / rows;


        regions = new Region[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                regions[r][c] = new DefaultRegion();
            }
        }
        animalRegion = new HashMap<>();
        animals = new ArrayList<>();
    }
    public void setRegion(int row, int col, Region r) {
        Region old = regions[row][col];
        for (Animal a : old.getAnimals()) {
            r.addAnimal(a);
            animalRegion.put(a, r);
        }
        regions[row][col] = r;
    }
    public void registerAnimal(Animal a) {
    	 a.init(this);
        int col = (int) (a.getPosition().getX() / cellWidth);
        int row = (int) (a.getPosition().getY() / cellHeight);

        Region r = regions[row][col];
        r.addAnimal(a);
        animalRegion.put(a, r);
        animals.add(a);

       
    }
    public void unregisterAnimal(Animal a) {
        Region r = animalRegion.get(a);
        if (r != null) {
            r.removeAnimal(a);
            animalRegion.remove(a);
            animals.remove(a);
        }

    }

    public void updateAnimalRegion(Animal a) {
        int col = (int) (a.getPosition().getX() / cellWidth);
        int row = (int) (a.getPosition().getY() / cellHeight);

        Region newRegion = regions[row][col];
        Region oldRegion = animalRegion.get(a);

        if (oldRegion != newRegion) {
            oldRegion.removeAnimal(a);
            newRegion.addAnimal(a);
            animalRegion.put(a, newRegion);
        }
    }
    @Override
    public double getFood(Animal a, double dt) {
        Region r = animalRegion.get(a);
        if (r == null) return 0.0;
        return r.getFood(a, dt);
    }
    public void updateAllRegions(double dt) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                regions[r][c].update(dt);
            }
        }
    }

    @Override
    public List<Animal> getAnimalsinRange(Animal a, double range) {
        List<Animal> result = new ArrayList<>();
        int colCenter = (int) (a.getPosition().getX() / cellWidth);
        int rowCenter = (int) (a.getPosition().getY() / cellHeight);
        for (int r = Math.max(0, rowCenter - 1); r <= Math.min(rows - 1, rowCenter + 1); r++) {
            for (int c = Math.max(0, colCenter - 1); c <= Math.min(cols - 1, colCenter + 1); c++) {
                for (Animal other : regions[r][c].getAnimals()) {
                    if (other != a && a.getPosition().distanceTo(other.getPosition()) <= range) {
                        result.add(other);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public JSONObject asJSON() {
        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JSONObject regionObj = new JSONObject();
                regionObj.put("row", r);
                regionObj.put("col", c);
                regionObj.put("data", regions[r][c].asJSON());
                arr.put(regionObj);
            }
        }

        json.put("regions", arr);
        return json;
    }
	@Override
	public int getCols() {

		return this.cols;
	}
	@Override
	public int getRows() {

		return this.rows;
	}
	@Override
	public int getRegionWidth() {

		return this.cellWidth;
	}
	@Override
	public int getRegionHeight() {

		return this.cellHeight;
	}
	@Override
	public Iterator<RegionData> iterator() {
		return new Iterator<MapInfo.RegionData>() {
			
		int row=0;
		int col=0;
		//int row, int col, RegionInfo r
		// while(it.hasNext()) {
		//   System.out.println(it.next());
		//	}

		@Override
		public boolean hasNext() {
			return row< regions.length; // hasnext deja stie daca mai are, sau nu, 
										// asa ca noi dam raspunsul boolean 1 sau 0
		}
		@Override
		public MapInfo.RegionData next() {
			Region r = regions[row][col];
			MapInfo.RegionData data = new MapInfo.RegionData(row, col,r);
			col++;

			if(col == regions[row].length) { // regions.length =nr de randuri
				col=0;
				row++;
			}	
			return data;
		}
         
        
	};

	}
}