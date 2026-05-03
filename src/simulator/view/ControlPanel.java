package simulator.view;
import java.awt.*;
import java.io.File;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.view.ViewUtils;


public class ControlPanel extends JPanel{
	private Controller ctrl;
	private ChangeRegionsDialog changeRegionsDialog;
	
	private JToolBar toolaBar;
	private JFileChooser fc;
	private boolean stopped = true; //utilizando de los botones de 
	private JButton exitButton;
	private JButton openButton;
	private JButton viewerButton;
	private JButton runButton;
	private JButton regionsButton;
	private JButton stopButton;
	private JTextField dtField;
	private JSpinner stepsSpinner;
	
	  // TODO add fields here
	
	ControlPanel(Controller ctrl){
		this.ctrl = ctrl;
		this.dtField = new JTextField("0.03");
		this.stepsSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		initGUI();
	}

	public JButton createButton(String caption, int x,int y,int w,int h){
		  JButton button = new JButton(caption);
		  button.setBounds(x,y, w,h);
		  return button;
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		toolaBar=new JToolBar();
		add(toolaBar, BorderLayout.PAGE_START);
		ChangeRegionsDialog dialog = new ChangeRegionsDialog(ctrl);
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));
		
	    // TODO create the different widgets (buttons, etc.) and add them to toolaBar.
	    //      All of them must have their corresponding tooltip. You can use
	    //      this.toolaBar.addSeparator() to add the vertical separator line
	    //      between the components that need it.
		
		// OPEN BOTON
		this.toolaBar.addSeparator();
		this.openButton = new JButton();
		this.openButton.setBounds(20,20,150,100);
		this.openButton.setToolTipText("DESCHIDE");
		this.openButton.setIcon(new ImageIcon("resources/icons/open.png"));
		
		this.openButton.addActionListener(e->{
			int result = this.fc.showOpenDialog(ViewUtils.getWindow(this));
			if(result == JFileChooser.APPROVE_OPTION) {
				try {
					java.io.File f = fc.getSelectedFile();
					org.json.JSONObject input = new org.json.JSONObject(new org.json.JSONTokener(new java.io.FileInputStream(f)));
					int cols=input.getInt("cols");
					int rows=input.getInt("rows");
					int width=input.getInt("width");
					int height=input.getInt("height");
					ctrl.reset(cols, rows, width, height);
					ctrl.loadData(input);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
				
		});
		
		this.toolaBar.add(openButton);
		
		// VIEWER BOTON
		this.toolaBar.addSeparator();
		this.viewerButton = new JButton();
		this.viewerButton.setBounds(20,20,150,100);
		this.viewerButton.setToolTipText("DESCHIDE");
		this.viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		this.viewerButton.addActionListener((e) ->{new MapWindow(ViewUtils.getWindow(this), ctrl);});
		this.toolaBar.add(viewerButton);
		

		// REGIONS BOTON
		this.regionsButton = new JButton();
		this.regionsButton.setBounds(20,20,150,100);
		this.regionsButton.setToolTipText("DESCHIDE");
		this.regionsButton.setIcon(new ImageIcon("resources/icons/regions.png"));
		this.regionsButton.addActionListener(e -> {
		    dialog.open(ViewUtils.getWindow(this));});
		this.toolaBar.add(regionsButton);
		
		this.toolaBar.addSeparator();
		
		//RUN BOTON
		this.runButton = new JButton();
		this.runButton.setBounds(20,20,150,100);
		this.runButton.setToolTipText("DESCHIDE");
		this.runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		this.runButton.addActionListener(e -> {
			  try {
			    this.stopped = false;

			    enableAllButtons(false);

			    double dt = Double.parseDouble(this.dtField.getText());
			    int steps = (Integer) this.stepsSpinner.getValue();

			    runSim(steps, dt);

			  } catch (Exception ex) {
				  ViewUtils.showErrorMsg(this, ex.getMessage());
				  enableAllButtons(true);  
				}
			});
		

		this.toolaBar.add(runButton);
		
		
		// STOP BOTON
		this.stopButton = new JButton();
		this.stopButton.setBounds(20,20,150,100);
		this.stopButton.setToolTipText("DESCHIDE");
		this.stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		this.stopButton.addActionListener(e -> {this.stopped = true; enableAllButtons(true);});
		this.toolaBar.add(stopButton);
		
		toolaBar.add(Box.createGlue());	
		this.toolaBar.addSeparator(); 
		
		this.toolaBar.add(new JLabel("dt:"));
		this.toolaBar.add(dtField);

		this.toolaBar.add(new JLabel("steps:"));
		this.toolaBar.add(stepsSpinner);
		
		// EXIT BUTTON
		this.toolaBar.add(Box.createGlue()); //this aligns the button to the right
		this.toolaBar.addSeparator();
		this.exitButton = new JButton();
		this.exitButton.setBounds(20,20,150,100);
		this.exitButton.setToolTipText("IESI");
		this.exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		this.exitButton.addActionListener((e) -> ViewUtils.quit(this));
		this.toolaBar.add(exitButton);
		
	    // TODO Inicializar this.fc con una instancia de JFileChooser. Para que siempre  
	    // abre en la carpeta de ejemplos puedes usar:  
	    //  
	    //   this.fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));
	   
		
		
		
		
	    // TODO Inicializar this.changeRegionsDialog con instancias del diálogo de cambio   
	    // de regiones  
	  }
	
		private void enableAllButtons(boolean v) {
		  this.openButton.setEnabled(v);
		  this.viewerButton.setEnabled(v);
		  this.regionsButton.setEnabled(v);
		  this.runButton.setEnabled(v);
		  this.exitButton.setEnabled(v);
		  this.stopButton.setEnabled(!v);
		  dtField.setEnabled(v);
		  stepsSpinner.setEnabled(v);
		}
		private void runSim(int n, double dt) {
		  if (n > 0 && !this.stopped) {
		    try {
		      this.ctrl.advance(dt);
		      SwingUtilities.invokeLater(() -> runSim(n - 1, dt));

		    } catch (Exception e) {
		      ViewUtils.showErrorMsg(this, e.getMessage());
		      this.stopped = true;
		      enableAllButtons(true);
		    }
		  } 
		  else {
			  this.stopped = true;
			  enableAllButtons(true);
		  }
		  
		}
	
	  }

	
	
	
	
	