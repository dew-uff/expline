package br.ufrj.cos.expline.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Expression;
import br.ufrj.cos.expline.model.Rule;
import br.ufrj.cos.expline.swing.jgraphx.ExpLineGraph;
import br.ufrj.cos.expline.swing.jgraphx.ExpLineGraphComponent;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxResources;

public class EditRuleFrame extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378029138434324390L;
	
	protected ExpLineGraphComponent expLineGraphComponent;
	protected ExpLineGraph expLineGraph;
	
	protected Rule rule;
	
	protected List<FilterPanel> conditionExpressions;
	
	protected List<FilterPanel> implicationExpressions;
	
	JPanel conditionPanelHolder;
	
	JPanel implicationPanelHolder;
	
	JTextField ruleNameField;

	private ImplicationTableModel implicationTablemodel;

	private JComboBox<Activity> implicationActivitiesCombo;
	

	/**
	 * 
	 */
	public EditRuleFrame(final Dialog owner, ExpLineGraphComponent expLinegraphComponent, Rule rule)
	{
		super(owner);
		
		this.expLineGraphComponent = expLinegraphComponent;
		this.expLineGraph = (ExpLineGraph) expLinegraphComponent.getGraph();
		
		this.rule = rule;
		
		setTitle(mxResources.get("rule"));
		setLayout(new BorderLayout());


		List<Activity> allVariants = getAllVariants();
		
		JComboBox<Activity> activitiesConditionComboBox = new JComboBox<Activity>(allVariants.toArray(new Activity[]{}));
		
		activitiesConditionComboBox.setSelectedItem(rule.getConditionElement());


	    JPanel ruleNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    
	    ruleNamePanel.add(new JLabel(mxResources.get("name")));
	    ruleNameField = new JTextField();
	    ruleNameField.setColumns(13);
	    ruleNameField.setText(rule.getName());
	    ruleNamePanel.add(ruleNameField);	    

	    
	    JPanel conditionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    
	    conditionPanel.add(new JLabel("Activity"));
	    conditionPanel.add(activitiesConditionComboBox);
	    conditionPanel.add(new JLabel("Selected"));
	    conditionPanel.add(new JCheckBox());
	    
	    conditionPanel.setBorder(BorderFactory.createTitledBorder("Condition"));
	    
	    JPanel topPanel = new JPanel(new BorderLayout());
	    
	    topPanel.add(ruleNamePanel, BorderLayout.NORTH);
	    topPanel.add(conditionPanel, BorderLayout.CENTER);
	    
	    
	    JPanel implicationPanel = new JPanel(new BorderLayout());
	    implicationPanel.setBorder(BorderFactory.createTitledBorder("Implication"));
	    

	    
	    getContentPane().add(topPanel, BorderLayout.NORTH);
	    getContentPane().add(implicationPanel, BorderLayout.CENTER);
	   
	    
	    implicationTablemodel = new ImplicationTableModel();
	    JTable table = new JTable(implicationTablemodel);
	    implicationPanel.add(new JScrollPane(
				table), BorderLayout.CENTER);
	    
//	    Activity actv = new Activity();
//	    actv.setValue("actv2");
//	    model.insertElement(actv, true);
//	    
//	    actv = new Activity();
//	    actv.setValue("actv3");
//	    model.insertElement(actv, true);
	    
	    JPanel implicationTopPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    
	    implicationTopPanel1.add(new JLabel("Activity"));
	    
	    implicationActivitiesCombo = new JComboBox<Activity>(allVariants.toArray(new Activity[]{}));
	    
	    implicationTopPanel1.add(implicationActivitiesCombo);
	    
	    JButton addButton = new JButton("Add");
	    
	    implicationTopPanel1.add(addButton);
	    
	    addButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{	 
				 insertImplicationElement();
			}
		});
	    
	    JButton removeButton = new JButton("Remove");
	    
	    implicationTopPanel1.add(removeButton);
	    
	    removeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{	 
				 removeImplicationElement();
			}
		});
	    
	    JPanel implicationTopPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    
	    JRadioButton ORButton = new JRadioButton("OR Operation");
	    JRadioButton ANDButton = new JRadioButton("AND Operation");
	    ORButton.setSelected(true);
	    
	    ButtonGroup operationGroup = new ButtonGroup();
	    operationGroup.add(ORButton);
	    operationGroup.add(ANDButton);
	    
	    
	    implicationTopPanel2.add(ORButton);
	    implicationTopPanel2.add(ANDButton);
	    
	    JPanel gridPanel = new JPanel(new GridLayout(2, 0));

	    gridPanel.add(implicationTopPanel2);
	    gridPanel.add(implicationTopPanel1);
	    
	    implicationPanel.add(gridPanel, BorderLayout.NORTH);


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
				saveData();
				setVisible(false);
			}
		});
		
		buttonPanel.add(okButton);
		
		// Sets default button for enter key
		getRootPane().setDefaultButton(okButton);

		setResizable(true);
		setSize(600, 500);
		setLocationRelativeTo(owner);
	}
	
	protected void insertImplicationElement() {
		implicationTablemodel.insertElement((Activity)implicationActivitiesCombo.getSelectedItem(), false);
		implicationTablemodel.fireTableDataChanged();
	}
	
	protected void removeImplicationElement() {
		implicationTablemodel.removeElement((Activity)implicationActivitiesCombo.getSelectedItem());
		implicationTablemodel.fireTableDataChanged();
	}

	private List<Activity> getAllVariants() {
		
		mxCell root =  (mxCell) expLineGraphComponent.getGraph().getModel().getRoot();
		root = (mxCell) root.getChildAt(0);
		
		List<Activity> activities = new ArrayList<Activity>();
		
		
		for (int i = 0; i < root.getChildCount(); i++) {
			
			mxICell cell = root.getChildAt(i);
			if(cell.isVertex()){	
				if (cell instanceof Activity) {
					Activity actv = (Activity) cell;
					
					if(actv.getType() != Activity.INVARIANT_TYPE && actv.getType() != Activity.VARIATION_POINT_TYPE){
						
						activities.add(actv);
					}
				}
			}
		}
		
		return activities;
	}

	public void saveData(){
		
		rule.setName(ruleNameField.getText());
		
		//rule.setImplicationOperation(implicationOperation);
		
		rule.setImplicationElements(implicationTablemodel.getElements());
		
	}


	class ImplicationTableModel extends AbstractTableModel {
		
		  Map<Activity, Boolean> elements;
		  
		  List<Activity> index;
		
		  String columnNames[] = { "Activities", "Select" };
		  
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
			  else
				  return elements.get(actv);
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
			  if(column == 1)
				  return true;
			  else
				  return false;
		  }
		  
		  public Map<Activity, Boolean> getElements(){
			  return elements;
		  }
	}
	
}
