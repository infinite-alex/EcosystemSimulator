package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.*;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

public class MapWindow extends JFrame implements EcoSysObserver {

	  private Controller ctrl;  
	  private AbstractMapViewer viewer;  
	  private Frame parent;

	  MapWindow(Frame parent, Controller ctrl) {  
	    super("[MAP VIEWER]");  
	    this.ctrl = ctrl;  
	    this.parent = parent;  
	    initGUI();  
	    // TODO registrar this como observador  
	    this.ctrl.addObserver(this);
	  }

	  private void initGUI() {  
	    JPanel mainPanel = new JPanel(new BorderLayout());  
	    // TODO poner contentPane como mainPanel
	    setContentPane(mainPanel);
	    // TODO crear el viewer y añadirlo a mainPanel (en el centro)
	    this.viewer = new MapViewer();
	    mainPanel.add(viewer, BorderLayout.CENTER);
	    // TODO en el método windowClosing, eliminar ‘MapWindow.this’ de los
	    //      observadores
	    
	    addWindowListener(new WindowListener() {    
	    	@Override public void windowClosing(WindowEvent e) {
	        ctrl.removeObserver(MapWindow.this);
	    }

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {	
			}

			@Override
			public void windowIconified(WindowEvent e) {	
			}

			@Override
			public void windowDeiconified(WindowEvent e) {	
			}

			@Override
			public void windowActivated(WindowEvent e) {	
			}

			@Override
			public void windowDeactivated(WindowEvent e) {	
			}
	    	});
	    setPreferredSize(new Dimension(800, 600));
	    pack();
	    if (this.parent != null)
	      setLocation(
	        this.parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2,
	        this.parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
	      setResizable(false);  
	      setVisible(true);  
	  }

	  @Override
	  public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		  viewer.reset(time, map,animals);
		
	  }

	  @Override
	  public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		  viewer.reset(time, map,animals);
		
	  }

	  @Override
	  public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		  viewer.update(animals, time);
		
	  }

	  @Override
	  public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		  viewer.repaint();
		
	  }

	  public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		  SwingUtilities.invokeLater(() -> {
		    viewer.update(animals, time);
		  });

		}

	}