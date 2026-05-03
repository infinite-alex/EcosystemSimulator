package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import simulator.model.State;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

public class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {

	  // TODO Declare necessary fields.
	  private List<String> species;
	  Map<String, Map<State, Integer>> data;
	  
	  SpeciesTableModel(Controller ctrl) {
		  
	    // TODO Initialize corresponding data structures.
		  this.species = new ArrayList<>();
		  this.data = new HashMap<>();
		 
	    // TODO Register the 'this' object as an observer.
		  ctrl.addObserver(this);
	  }
	  
	  @Override
	  public String getColumnName(int col) {
	      if (col == 0) return "Species";
	      return State.values()[col - 1].toString();
	  }
	  
	  @Override
	  public int getRowCount() {
		return species.size();
	  }

	  @Override
	  public int getColumnCount() {
		return 1 + State.values().length;
	  }
	  
	  @Override
	  public Object getValueAt(int rowIndex, int columnIndex) {
		  	
		  String specii= species.get(rowIndex); // ia specia de pe linia respectivă
		  
		  if(columnIndex == 0) { // daca nu sunt animale, trimite numele primei speciei
			  return specii;
		  }
		  else 
		  {
			State[] stadii = State.values();  // ia toate starile posibile
			State stadiu = stadii[columnIndex -1]; // inafara de prima
			
			return data.get(specii).getOrDefault(stadiu, 0); 
		  }
		 
	  }

	  private void updateTable(List<AnimalInfo> animals) {
		  species.clear(); // facem curat in tot ce avem, sa incepem nou
		  data.clear();
		  
		  for (AnimalInfo a: animals) {
			  String specii = a.getGeneticCode(); // luam fiecare cod 
			  State stadiu = a.getState();// si stadiul al animalului
			  
			  if(!species.contains(specii)) { // daca nu il contine in tabel
				  species.add(specii);// il adaugam
				  data.put(specii, new HashMap<>());
			  }
			  Map<State, Integer> ani = data.get(specii);
			  ani.put(stadiu,  ani.getOrDefault(stadiu, 0) +1);
			  
		  }
		    fireTableDataChanged();
		
	  }

	  @Override
	  public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		  updateTable(animals);
		
	  }

	  @Override
	  public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		  updateTable(animals);
		
	  }

	  @Override
	  public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		  updateTable(animals);
		
	  }


	  @Override
	  public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		  updateTable(animals);
		
	  }


	  @Override
	  public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub
		
	  }
	}