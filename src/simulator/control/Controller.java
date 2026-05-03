package simulator.control;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import org.json.JSONArray;

import simulator.model.AnimalInfo;
import simulator.model.AnimalMapView;
import simulator.model.EcoSysObserver;
import simulator.model.Simulator;
import simulator.view.SimpleObjectViewer;
import simulator.view.SimpleObjectViewer.ObjInfo;

public class Controller {
	private Simulator sim;

	public Controller(Simulator sim) {
		this.sim=sim;
	}
	public void loadData(JSONObject data) {

		if (data.has("regions")) {
			JSONArray regions = data.getJSONArray("regions");

			for (int i = 0; i < regions.length(); i++) {
				JSONObject r = regions.getJSONObject(i);

				JSONArray row = r.getJSONArray("row");
				JSONArray col = r.getJSONArray("col");
				JSONObject spec = r.getJSONObject("spec");

				for (int rf = row.getInt(0); rf <= row.getInt(1); rf++) {
					for (int cf = col.getInt(0); cf <= col.getInt(1); cf++) {
						sim.setRegion(rf, cf, spec);
					}
				}
			}
		}
		JSONArray animals = data.getJSONArray("animals");

		for (int i = 0; i < animals.length(); i++) {
			JSONObject a = animals.getJSONObject(i);
			int amount = a.getInt("amount");
			JSONObject spec = a.getJSONObject("spec");

			for (int j = 0; j < amount; j++) {
				sim.addAnimal(spec);
			}
		}
	}
	private List<ObjInfo> toAnimalsInfo(List<? extends AnimalInfo> animals) {
		List<ObjInfo> ol = new ArrayList<>(animals.size());
		for (AnimalInfo a : animals)
			ol.add(new ObjInfo(a.getGeneticCode(), (int) a.getPosition().getX(), (int) a.getPosition().getY(),(int)Math.round(a.getAge())+2));
		return ol;
	}

	public void run(double t, double dt, boolean sv, OutputStream out) {

		JSONObject initState = sim.asJSON();

		SimpleObjectViewer view = null;  
		if (sv) {  
			AnimalMapView m = sim.getMapInfo();  
			view = new SimpleObjectViewer("[ECOSYSTEM]", m.getWidth(), m.getHeight(), m.getCols(), m.getRows());  
			view.update(toAnimalsInfo(sim.getAnimals()), sim.getTime(), dt);  
		}

		while (sim.getTime() <= t) {
			sim.advance(dt);
			if (sv) view.update(toAnimalsInfo(sim.getAnimals()), sim.getTime(), dt);
		}

		JSONObject finalState = sim.asJSON();

		JSONObject result = new JSONObject();
		result.put("in", initState);
		result.put("out", finalState);

		PrintStream p = new PrintStream(out);
		p.print(result.toString(2));
		if (sv) view.close();
	}
	public void reset(int cols, int rows, int width, int height) {
		sim.reset(cols, rows, width, height);
	}
	public void advance(double dt) {
	    sim.advance(dt);
	    
	}
	public void addObserver(EcoSysObserver o) {
	    sim.addObserver(o);
	}
	public void removeObserver(EcoSysObserver o) {
	    sim.removeObserver(o);
	}
	public void setRegions(JSONObject rs) {
		if (rs.has("regions")) {
			JSONArray regions = rs.getJSONArray("regions");

			for (int i = 0; i < regions.length(); i++) {
				JSONObject r = regions.getJSONObject(i);

				JSONArray row = r.getJSONArray("row");
				JSONArray col = r.getJSONArray("col");
				JSONObject spec = r.getJSONObject("spec");

				for (int rf = row.getInt(0); rf <= row.getInt(1); rf++) {
					for (int cf = col.getInt(0); cf <= col.getInt(1); cf++) {
						sim.setRegion(rf, cf, spec);
					}
				}
			}
		}
	}
	
	
	
	
	
	
}