package simulator.view;

import java.awt.BorderLayout;
import java.awt.LayoutManager;

import javax.swing.*;
import javax.swing.table.TableModel;

import simulator.control.Controller;



public class InfoTable extends JPanel {
	
	private String title;
	TableModel tableModel; // aici sunt datele , iar in JTable table este doar un tabel vizualam 
	
	InfoTable(String title, TableModel tableModel){
		this.title=title;
		this.tableModel = tableModel;
		initGUI();
	}
	private void initGUI() {
		
		// TODO change the panel's layout to BorderLayout()
		setLayout(new BorderLayout());

		// TODO add a titled border to the JPanel, with the text this.title
		
		setBorder(BorderFactory.createTitledBorder(this.title));
		//setBorder = margine la panel
	    //BorderFactory.createTitledBorder() = border cu titlu
		// TODO add a JTable (with a vertical scroll bar) that uses
		//      this.tableModel
		JTable table = new JTable(this.tableModel); // transformi informatia si o pui intr-un tabel
		JScrollPane scroll = new JScrollPane(table); // il faci un scroll
		add(scroll, BorderLayout.CENTER); // pui informatia in centru
		
	}
	
}
