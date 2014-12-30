package br.ufrj.cos.expline.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import br.ufrj.cos.expline.derivation.ExpLineDerivationGraph;
import br.ufrj.cos.expline.derivation.ExpLineDerivationGraphComponent;
import br.ufrj.cos.expline.model.Activity;

import com.mxgraph.util.mxResources;

public class ActivityOptionsSelectionFrame extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378029138434324390L;
	
	protected final ExpLineDerivationGraphComponent derivationGraphComponent;
	
	protected ExpLineDerivationGraph expLineDerivationGraph;

	private ImplicationTableModel implicationTablemodel;

	private JComboBox<Activity> implicationActivitiesCombo;

	private JTable implicationTable;

	private JList<String> optionList;

	/**
	 * 
	 */
	public ActivityOptionsSelectionFrame(final JFrame frame, final ExpLineDerivationGraphComponent derivationGraphComponent, final List<Map<Activity, Boolean>> selectOptions)
	{
		super(frame);
		
		this.derivationGraphComponent = derivationGraphComponent;
		this.expLineDerivationGraph = (ExpLineDerivationGraph) derivationGraphComponent.getGraph();
		
		setTitle(mxResources.get("rule"));
		setLayout(new BorderLayout());
	    

	    implicationTablemodel = new ImplicationTableModel();
	    implicationTable = new JTable(implicationTablemodel);

	    getContentPane().add(new JScrollPane(
				implicationTable), BorderLayout.CENTER);
	    
	    
	    
	    String[] optionsLabel = new String[selectOptions.size()];
	    
	    for(int i=0; i<selectOptions.size(); i++){
	    	optionsLabel[i] = "Option "+i;
	    }
	    
	    optionList = new JList<String>(optionsLabel);
	    
	    optionList.addListSelectionListener(new ListSelectionListener()
	    {
	    	  public void valueChanged(ListSelectionEvent ev)
	    	  {
	    	       implicationTablemodel.setElements(selectOptions.get(ev.getFirstIndex()));
	    	       implicationTablemodel.fireTableDataChanged();
	    	  } 
	    	});
	    
	    
	    getContentPane().add(new JScrollPane(optionList), BorderLayout.WEST);
	    
	    
	    implicationTablemodel.setElements(selectOptions.get(0));
	   

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createMatteBorder(1, 0, 0, 0, Color.GRAY), BorderFactory
				.createEmptyBorder(16, 8, 8, 8)));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		// Adds Cancel button to close window without saving
		JButton cancelButton = new JButton(mxResources.get("cancel"));
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});

		buttonPanel.add(cancelButton);

		// Adds Ok button to close window saving the information
		JButton okButton = new JButton(mxResources.get("ok"));
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{	

				
			}
		});
		
		buttonPanel.add(okButton);
		
		// Sets default button for enter key
		getRootPane().setDefaultButton(okButton);

		setResizable(true);
		setSize(600, 500);
		setLocationRelativeTo(frame);
	}
	
	protected void insertImplicationElement() {
		implicationTablemodel.insertElement((Activity)implicationActivitiesCombo.getSelectedItem(), false);
		implicationTablemodel.fireTableDataChanged();
	}
	
	protected void removeImplicationElement() {
		implicationTablemodel.removeElement((Activity)implicationActivitiesCombo.getSelectedItem());
		implicationTablemodel.fireTableDataChanged();
	}

	public void executeOption(){
		
	}	


	class ImplicationTableModel extends AbstractTableModel {
		
		  Map<Activity, Boolean> elements;
		  
		  List<Activity> index;
		
		  String columnNames[] = { "Activity", "Action" };
		  
		  public ImplicationTableModel(){
			  super();
			  
			  elements = new HashMap<Activity, Boolean>();
			  index = new ArrayList<Activity>();
		  }
		
		  public void insertElement(Activity actv, boolean selected){
			  
			  if(!hasElement(actv)){
				  index.add(actv);
				  elements.put(actv, selected);
			  }
		  }
		  
		  public boolean hasElement(Activity actv){
			  for (Activity element : index) {
				   if(actv.equals(element))
					   return true;
			  }
			  
			  return false;
		  }
		  
		  public void removeElement(Activity actv){
			  index.remove(actv);
			  elements.remove(actv);
		  }
		  
		  public int getColumnCount() {
		    return columnNames.length;
		  }
		
		  public String getColumnName(int column) {
		    return columnNames[column];
		  }
		
		  public int getRowCount() {
		    return index.size();
		  }
		
		  public Object getValueAt(int row, int column) {
			  Activity actv = index.get(row);
			  if(column == 0)
				  return (String) actv.getValue();
			  else{
				  if(elements.get(actv))
					 return "SELECT";
				  else
					  return "NOT SELECT";
			  }
		  }
		
		  public Class getColumnClass(int column) {
		    return (getValueAt(0, column).getClass());
		  }
		
		  public void setValueAt(Object value, int row, int column) {
			  Activity actv = index.get(row);

			  if(column == 1){
				  elements.remove(actv);
				  elements.put(actv, (Boolean) value);
			  }
		  }
		
		  public boolean isCellEditable(int row, int column) {
			  return false;
		  }
		  
		  public Map<Activity, Boolean> getElements(){
			  return elements;
		  }
		  
		  public void setElements(Map<Activity, Boolean> elements){
			  index.clear();
			  this.elements = elements;
			  index.addAll(elements.keySet());
		  }
	}
	
}
