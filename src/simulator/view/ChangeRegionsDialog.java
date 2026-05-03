package simulator.view;

import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;


import simulator.launcher.Main;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;

import javax.swing.*;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class ChangeRegionsDialog extends JDialog implements EcoSysObserver {

	  private DefaultComboBoxModel<String> regionsModel;  
	  private DefaultComboBoxModel<String> fromRowModel;  
	  private DefaultComboBoxModel<String> toRowModel;  
	  private DefaultComboBoxModel<String> fromColModel;  
	  private DefaultComboBoxModel<String> toColModel;

	  private DefaultTableModel dataTableModel;  
	  private Controller ctrl;  
	  private List<JSONObject> regionsInfo;

	  private String[] headers = { "Key", "Value", "Description" };

	  // TODO en caso de ser necesario, añadir los atributos aquí…  
	  ChangeRegionsDialog(Controller ctrl) {  
	    super((Frame)null, true);  
	    this.ctrl = ctrl;  
	    initGUI();  
	    // TODO registrar this como observer;  
	    this.ctrl.addObserver(this);
	  }

	  private void initGUI() {  
	    setTitle("Change Regions");  
	    JPanel mainPanel = new JPanel();  
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));  
	    setContentPane(mainPanel);

	    // TODO crea varios paneles para organizar los componentes visuales en el  
	    //      dialogo, y añadelos al mainpanel. P.ej., uno para el texto de ayuda,
	    //      uno para la tabla, uno para los combobox, y uno para los botones.  

	    // PANEL TEXT
	    
	    //this.regionsInfo = Main.regionsFactory.getInfo();
	    
	   
	    // TODO crear el texto de ayuda que aparece en la parte superior del diálogo y  
	    //      añadirlo al panel correspondiente diálogo (Ver el apartado Figuras)

	    JLabel help = new JLabel("<html><p>Select a region type, the rows/cols interval, and provide the values for the parameters in the VALUE column (default values are used for parameter with no value.</p></html>");
	    help.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(help);
		
	    // TABEL + CONTROLS

		JPanel controlsPanel= new JPanel();
	    JPanel tablePanel= new JPanel();



	    mainPanel.add(tablePanel);
	    mainPanel.add(controlsPanel);
	    // OK / CANCEL 
	    JPanel execute= new JPanel();
	    mainPanel.add(execute);
	   
	    // this.regionsInfo se usará para establecer la información en la tabla  
	    this.regionsInfo = Main.regionsFactory.getInfo();

	    if (this.regionsInfo == null) {
	        throw new IllegalStateException("regionsInfo is null!");
	    }

	    // this.dataTableModel es un modelo de tabla que incluye todos los parámetros de  
	    // la region
	    this.dataTableModel = new DefaultTableModel() {  
	      @Override  
	      public boolean isCellEditable(int row, int column) {  
	        // TODO hacer editable solo la columna 1  
	    	  return column ==1;
	      }  
	    };  
	    this.dataTableModel.setColumnIdentifiers(this.headers);

	    // TODO crear un JTable que use dataTableModel, y añadirlo al diálogo
	    JTable table = new JTable(this.dataTableModel);
	    JScrollPane scroll=new JScrollPane(table);
	    tablePanel.setLayout(new BorderLayout());
	    tablePanel.add(scroll, BorderLayout.CENTER);

	    // this.regionsModel es un modelo de combobox que incluye los tipos de regiones
	    this.regionsModel = new DefaultComboBoxModel<>();

	    // TODO añadir la descripción de todas las regiones a regionsModel. Para eso
	    //      usa la clave “desc” o “type” de los JSONObject en regionsInfo,
	    //      ya que estos nos dan información sobre lo que puede crear la factoría.
	    for (JSONObject o : regionsInfo) {
	        regionsModel.addElement(o.getString("desc")); 
	    }
	    
	    // TODO crear un combobox que use regionsModel y añadirlo al diálogo.

	    JComboBox<String> regionsCombo = new JComboBox<>(regionsModel);
	    regionsCombo.addActionListener(e -> {
	        int i = regionsCombo.getSelectedIndex();

	        JSONObject info = regionsInfo.get(i);
	        JSONObject data = info.getJSONObject("data");

	        dataTableModel.setRowCount(0); // curața tabelul

	        for (String key : data.keySet()) {
	            String desc = data.getString(key);
	            dataTableModel.addRow(new Object[]{key, "", desc});
	        }
	    });
	    controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.X_AXIS));
	    controlsPanel.add(new JLabel("Regions: "));
	    controlsPanel.add(regionsCombo);
	   
	    
	    // TODO crear 4 modelos de combobox para this.fromRowModel, this.toRowModel,
	    //      this.fromColModel y this.toColModel.
	    this.fromRowModel = new DefaultComboBoxModel<>();
	    this.toRowModel = new DefaultComboBoxModel<>();
	    this.fromColModel = new DefaultComboBoxModel<>();
	    this.toColModel = new DefaultComboBoxModel<>();
	    
	    // TODO crear 4 combobox que usen estos modelos y añadirlos al diálogo.
	    JComboBox<String> fromRow = new JComboBox<>(fromRowModel);
	    JComboBox<String> toRow = new JComboBox<>(toRowModel);
	    JComboBox<String> fromCol = new JComboBox<>(fromColModel);
	    JComboBox<String> toCol= new JComboBox<>(toColModel);
	    
	    // TODO crear los botones OK y Cancel y añadirlos al diálogo.
	    controlsPanel.add(new JLabel("From Row "));
	    controlsPanel.add(fromRow);
	    
	    controlsPanel.add(new JLabel("To Row "));
	    controlsPanel.add(toRow);
	    
	    controlsPanel.add(new JLabel("From Col "));
	    controlsPanel.add(fromCol);
	    
	    controlsPanel.add(new JLabel("To Col "));
	    controlsPanel.add(toCol);

	    JButton ok= new JButton("OK");
	    ok.addActionListener(e -> {
	        try {
	        	JSONObject regionData = new JSONObject();

	        	for (int i = 0; i < dataTableModel.getRowCount(); i++) { // aici punem datele
	        	    String key = (String) dataTableModel.getValueAt(i, 0);
	        	    String value = (String) dataTableModel.getValueAt(i, 1);

	        	    if (value != null && !value.isEmpty()) {
	        	        regionData.put(key, Double.parseDouble(value));
	        	    }
	        	}
	        	int i = regionsCombo.getSelectedIndex(); // punem un index pe regiune 
	        	
	        	String type = regionsInfo.get(i).getString("type"); // ia tipul regiunii care e string 
	        	
	        	int rowFrom =Integer.parseInt((String) fromRow.getSelectedItem());
	        	int rowTo =Integer.parseInt((String) toRow.getSelectedItem());
	        	int colFrom= Integer.parseInt((String) fromCol.getSelectedItem());
	        	int colTo =Integer.parseInt((String) toCol.getSelectedItem());
	        
	        	JSONObject spec =new JSONObject();
	        	spec.put("type", type);
	        	spec.put("data", regionData);

	        	JSONObject region= new JSONObject();
	        	
	        	JSONArray rows = new JSONArray();
	        	rows.put(rowFrom);
	        	rows.put(rowTo);
	        	region.put("row", rows);
	        	
	        	JSONArray colss = new JSONArray();
	        	colss.put(colFrom);
	        	colss.put(colTo);
	        	
	        	region.put("col", colss);
	        	region.put("spec", spec);

	        	JSONArray arr = new JSONArray();
	        	arr.put(region);
	        	
	        	JSONObject result = new JSONObject();
	        	result.put("regions", arr);
	        	ctrl.setRegions(result);
	        	setVisible(false);
	        
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	        	ViewUtils.showErrorMsg(this, ex.getMessage());
	        }
	    	});
	    JButton cancel= new JButton("CANCEL");
	    cancel.addActionListener(e -> {
	        setVisible(false);
	    });
	    
	    execute.add(ok);
	    execute.add(cancel);
	    
	    setPreferredSize(new Dimension(700, 400)); // puedes usar otro tamaño  
	    pack();  
	    setResizable(false);  
	    setVisible(false);  
	  }

	  public void open(Frame parent) {
	    setLocation(
	      parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2,
	      parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
	    pack();  
	    setVisible(true);  
	  }

	  @Override
	  public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		  fromRowModel.removeAllElements();
		  toRowModel.removeAllElements();
		  fromColModel.removeAllElements();
		  toColModel.removeAllElements();

		  for (int i = 0; i < map.getRows(); i++) {
		      String val = String.valueOf(i);
		      fromRowModel.addElement(val);
		      toRowModel.addElement(val);
		  }

		  for (int j = 0; j < map.getCols(); j++) {
		      String val = String.valueOf(j);
		      fromColModel.addElement(val);
		      toColModel.addElement(val);
		  }
		
	  }

	  @Override
	  public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		  fromRowModel.removeAllElements();
		  toRowModel.removeAllElements();
		  fromColModel.removeAllElements();
		  toColModel.removeAllElements();

		  for (int i = 0; i < map.getRows(); i++) {
		      String val = String.valueOf(i);
		      fromRowModel.addElement(val);
		      toRowModel.addElement(val);
		  }

		  for (int j = 0; j < map.getCols(); j++) {
		      String val = String.valueOf(j);
		      fromColModel.addElement(val);
		      toColModel.addElement(val);
		  }
	  }

	  @Override
	  public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	  }

	  @Override
	  public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	  }

	  @Override
	  public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
	  }

	}