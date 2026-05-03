package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.List;

import javax.swing.*;
import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

public class MainWindow extends JFrame implements EcoSysObserver{
	
	private Controller ctrl;
	
	public MainWindow( Controller ctrl) {
		super("[ECOSYSTEM SIMULATOR]");
		this.ctrl = ctrl;
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		JPanel mainPanel= new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		
		// TODO create a ControlPanel and add it to PAGE_START of mainPanel
		ControlPanel controlPanel= new ControlPanel(ctrl);
		mainPanel.add(controlPanel, BorderLayout.PAGE_START);

	    // TODO create a StatusBar and add it to PAGE_END of mainPanel
		StatusBar statusPanel= new StatusBar(ctrl);
		mainPanel.add(statusPanel, BorderLayout.PAGE_END);
		
//	    // A panel for the tables (it uses vertical BoxLayout)
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		
		JPanel speciesTable = new InfoTable("Species", new SpeciesTableModel(this.ctrl));
		speciesTable.setPreferredSize(new Dimension(500, 250));
		contentPanel.add(speciesTable);

		JPanel regionsTable = new InfoTable("Regions", new RegionsTableModel(this.ctrl));
		regionsTable.setPreferredSize(new Dimension(500, 250));
		contentPanel.add(regionsTable);
		// TODO create the species table and add it to contentPanel.
	    //      Use setPreferredSize(new Dimension(500, 250)) to set the size.

	    // TODO Create the regions table and add it to contentPanel.
	    //      Usa setPreferredSize(new Dimension(500, 250)) to set the size.

	    // TODO call ViewUtils.quit(MainWindow.this) in the windowClosing method.
		
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				ViewUtils.quit(MainWindow.this);
			}
		});

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
		

	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		repaint();
		
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		repaint();
		
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		repaint();
		
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		repaint();
		
	}

	@Override
	public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		repaint();

		
	}

}
