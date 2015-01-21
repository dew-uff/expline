package br.ufrj.cos.expline.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import br.ufrj.cos.expline.derivation.DerivationImp;
import br.ufrj.cos.expline.model.Activity;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

public class ActivityOptionsSelectionFrame extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378029138434324390L;
	
	protected final mxGraphComponent derivationGraphComponent;
	
	protected mxGraph expLineDerivationGraph;
	
	protected final DerivationImp derivationImp;

	private ImplicationTableModel implicationTablemodel;

	private JComboBox<Activity> implicationActivitiesCombo;

	private JTable implicationTable;

	private JList<String> optionList;
	
	private Activity selectedActivity;

	private boolean isActivitySelected;

	/**
	 * 
	 */
	public ActivityOptionsSelectionFrame(final JFrame frame, final DerivationImp derivationImp, final List<Map<Activity, Boolean>> selectOptions, final Activity selectedActivity, final boolean isActivitySelected)
	{
		super(frame);
		
		this.derivationImp = derivationImp;
		this.derivationGraphComponent = derivationImp.getDerivationGraphComponent();
		this.expLineDerivationGraph = this.derivationGraphComponent.getGraph();
		this.isActivitySelected = isActivitySelected;
		
		this.selectedActivity = selectedActivity;
		
		setTitle("Selection Implication Options");
		setLayout(new BorderLayout());
		
		// Creates the gradient panel
		JPanel panel = new JPanel(new BorderLayout())
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = -5062895855016210947L;

			/**
			 * 
			 */
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);

				// Paint gradient background
				Graphics2D g2d = (Graphics2D) g;
				g2d.setPaint(new GradientPaint(0, 0, Color.WHITE, getWidth(),
						0, getBackground()));
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}

		};

		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createMatteBorder(0, 0, 1, 0, Color.GRAY), BorderFactory
				.createEmptyBorder(8, 8, 12, 8)));

		// Adds title
		JLabel titleLabel = new JLabel("Choose one of the implication options for the selected activity");
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 18, 0, 0));
		titleLabel.setOpaque(false);
		panel.add(titleLabel, BorderLayout.CENTER);

		getContentPane().add(panel, BorderLayout.NORTH);
		
		
		
	    

	    implicationTablemodel = new ImplicationTableModel();
	    implicationTable = new JTable(implicationTablemodel);
	    
	    implicationTable.setFocusable(false);
	    implicationTable.setRowSelectionAllowed(false);	    
	    
	    
	    String[] optionsLabel = new String[selectOptions.size()];
	    
	    for(int i=0; i<selectOptions.size(); i++){
	    	optionsLabel[i] = "Option "+(i+1);
	    }
	    
	    optionList = new JList<String>(optionsLabel);
	    optionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    optionList.setSelectedIndex(0);
	    
	    optionList.addListSelectionListener(new ListSelectionListener()
	    {
	    	  public void valueChanged(ListSelectionEvent ev)
	    	  {
	    	       implicationTablemodel.setElements(selectOptions.get(optionList.getSelectedIndex()));
	    	       implicationTablemodel.fireTableDataChanged();
	    	  } 
	    	});
	    
	    
		JSplitPane inner = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(optionList), new JScrollPane(implicationTable));
		inner.setDividerLocation(60);
		inner.setResizeWeight(1);
		inner.setDividerSize(3);
		inner.setBorder(null);
		
	    getContentPane().add(inner, BorderLayout.CENTER);
	    
	    
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

				Map<Activity, Boolean> solution = selectOptions.get(optionList.getSelectedIndex());
				
				
				for (Activity actv : solution.keySet()) {
					
					derivationImp.setActivitySelection(actv, solution.get(actv));
					
					if(solution.get(actv))
						actv.setStyle(actv.getStyle().replace(";opacity=20", ""));
					else
						actv.setStyle(actv.getStyle() + ";opacity=20");
										 
				}
				
				derivationImp.setActivitySelection(selectedActivity, isActivitySelected);
				
				selectedActivity.setStyle(selectedActivity.getStyle().replace(";opacity=20", ""));
				
				setVisible(false);
				
			}
		});
		
		buttonPanel.add(okButton);
		
		// Sets default button for enter key
		getRootPane().setDefaultButton(okButton);

		setResizable(true);
		setSize(420, 300);
		setLocationRelativeTo(frame);
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
