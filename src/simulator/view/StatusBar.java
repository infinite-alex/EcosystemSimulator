package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.*;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

public class StatusBar extends JPanel implements EcoSysObserver{
	  // TODO Añadir los atributos necesarios.  
	private JLabel timeLabel;
	private JLabel animalsLabel;
	private JLabel dimLabel;
	
	StatusBar(Controller ctrl){
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
	    // TODO Create several JLabels 
		//for the time, 
		//the number of animals, 
	    //and the dimension and add them to the panel. 
	    //      You can use the following code to add a vertical separator:
	    //
	    //     JSeparator s = new JSeparator(JSeparator.VERTICAL);
	    //     s.setPreferredSize(new Dimension(10, 20));
	    //     this.add(s);
		timeLabel = new JLabel("Time:0.0");
		animalsLabel = new JLabel("Animals:0");
		dimLabel = new JLabel("Map");
		
		this.add(timeLabel);
	    JSeparator s1 = new JSeparator(JSeparator.VERTICAL);
	    s1.setPreferredSize(new Dimension(10, 20));
	    this.add(s1);
		this.add(dimLabel);		
	    JSeparator s2 = new JSeparator(JSeparator.VERTICAL);
	    s2.setPreferredSize(new Dimension(10, 20));
	    this.add(s2);
		this.add(animalsLabel);

		
	}
	 // TODO The rest of methods.

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		timeLabel.setText("Time: " + time);
		animalsLabel.setText("Animals: " + animals.size()); 
		dimLabel.setText("Map: " + map.getWidth() + "x" + map.getHeight() + " (" + map.getRows() + "x" + map.getCols() + ")");
		
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		onRegister(time,map,animals);
		
	}
	
	@Override
	public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		timeLabel.setText("Time: " + time);
		animalsLabel.setText("Animals: " + animals.size());
		dimLabel.setText("Map: " + map.getWidth() + "x" + map.getHeight() + " (" + map.getRows() + "x" + map.getCols() + ")");
	}
	
	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		animalsLabel.setText("Animals: " + animals.size());
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		dimLabel.setText("Map: " + map.getWidth() + "x" + map.getHeight() + " (" + map.getRows() + "x" + map.getCols() + ")");
	}

}
