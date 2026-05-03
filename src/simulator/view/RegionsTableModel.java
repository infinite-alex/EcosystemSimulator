package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.Diet;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;
import simulator.model.State;

public class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {

	  // TODO Declare necessary fields.
	  private List<MapInfo.RegionData> regions;
	  private Map<MapInfo.RegionData, Map<Diet,Integer>> data;
	  // dictionar in dictionar
//		  data
//		   regiune1 → {diet1 → 2, diet2 → 1}
//		   regiune2 → {diet1 → 0, diet2 → 3}
	  RegionsTableModel(Controller ctrl) {
		  
	    // TODO Initialize corresponding data structures.
		  
		  this.regions= new ArrayList<>();
		  this.data = new HashMap<>();
		  
	    // TODO Register the 'this' object as an observer.
		  
		  ctrl.addObserver(this);
	  }
	  // TODO The rest of methods.

	  @Override
	  public int getRowCount() {
		return regions.size(); // cate linii are tabelul
	  }

	  @Override
	  public int getColumnCount() {  // cate coloane are tabelul
		return 3+Diet.values().length;
	  }
	  
	  @Override
	  public String getColumnName(int col) {
	      if (col == 0) return "Species";
	      return State.values()[col - 1].toString();
	  }
	  
	  @Override
	  public Object getValueAt(int rowIndex, int columnIndex) {
			MapInfo.RegionData regionData = regions.get(rowIndex);

			if (columnIndex == 0) return regionData.row();
			if (columnIndex == 1) return regionData.col();
			if (columnIndex == 2) return regionData.r().toString();

			Diet dieta = Diet.values()[columnIndex - 3];
			return data.get(regionData).getOrDefault(dieta, 0);
	  }
	  
	  private void updateTable(MapInfo map) {
		  regions.clear(); // facem curat in tot ce avem, sa incepem nou
		  data.clear();
		  
		  for (MapInfo.RegionData regionData: map) {
			  
			  RegionInfo region = regionData.r();// ia efectiv regiunea( r=regiune)
			  Map<Diet,Integer> counter = new HashMap<>();
			  
			  for( AnimalInfo animal : region.getAnimalsInfo()) {
				  Diet dieta = animal.getDiet();
				  counter.put(dieta, counter.getOrDefault(dieta, 0) + 1);

			  }
			  regions.add(regionData);
			  data.put(regionData, counter);

		  }
		    fireTableDataChanged();
		
	  }
	  
	  
	  @Override
	  public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
	  	updateTable(map);
	  }

	  @Override
	  public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
	  	updateTable(map);
	  }

	  @Override
	  public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
	  	updateTable(map);
	  }

	  @Override
	  public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	  	updateTable(map);
	  }

	  @Override
	  public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	  	updateTable(map);
	  }
	  
	}