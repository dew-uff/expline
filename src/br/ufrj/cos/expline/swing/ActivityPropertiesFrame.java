package br.ufrj.cos.expline.swing;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.AbstractTableModel;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Port;
import br.ufrj.cos.expline.model.RelationSchema;
import br.ufrj.cos.expline.model.RelationSchemaAttribute;
import br.ufrj.cos.expline.swing.Actions.AlgebraicOperatorAction;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

public class ActivityPropertiesFrame extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378029138434324390L;
	
	protected mxGraphComponent graphComponent;
	protected mxGraph graph;
	
	public JTabbedPane inputRelationtabbedPane = null;
	protected int inputRelationtNextIndex = 0;
	
	protected RelationalSchemaTableModel OutputRelationalSchemaTableModel = null;
	protected ArrayList<JTable> inputRelationalSchemaTables = new ArrayList<JTable>();
	protected JTable OutputRelationalSchemaTable = null;
	protected JComboBox<String> algebraicOperatorsJComboBox = null;

	protected final Frame owner;

	/**
	 * 
	 */
	public ActivityPropertiesFrame(Frame owner, mxGraphComponent graphComponent)
	{
		super(owner);
		
		this.owner = owner;
		
		this.graphComponent = graphComponent;
		this.graph = graphComponent.getGraph();
		
		setTitle(mxResources.get("properties"));
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
		JLabel titleLabel = new JLabel(mxResources.get("properties"));
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 18, 0, 0));
		titleLabel.setOpaque(false);
		panel.add(titleLabel, BorderLayout.CENTER);

		getContentPane().add(panel, BorderLayout.NORTH);
		
		//Creating the algebraic Panel
		JPanel algebraicPanel = new JPanel();
		algebraicPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		algebraicPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		algebraicPanel.add(new JLabel(mxResources.get("albegraicOperator")+":"));
		String[] algebraicOperators = new String[5];
		algebraicOperators[0] = mxResources.get("map");
		algebraicOperators[1] = mxResources.get("select");
		algebraicOperators[2] = mxResources.get("splitMap");
		algebraicOperators[3] = mxResources.get("reduce");
		algebraicOperators[4] = mxResources.get("join");
		algebraicOperatorsJComboBox = new JComboBox<String>(algebraicOperators);
		algebraicOperatorsJComboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Activity activity = (Activity) graph.getSelectionCell();
				activity.refreshPortsDefinition();
				
				
				
				
				
		       	int index = inputRelationtabbedPane.getTabCount() - 1;
	        	        	
	        	RelationSchema relationSchema = activity.getInputPorts().get(1).getRelationSchema();
	    		inputRelationtabbedPane.addTab(mxResources.get("attributeType")+" "+inputRelationtNextIndex, null, createInputRelationalSchemaPanel(relationSchema, index),
	                    "");
	    		
	    		inputRelationtNextIndex++;
				
			}
		});
		
		algebraicPanel.add(algebraicOperatorsJComboBox);

        
        //creating the tabbed Panel
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createTitledBorder(mxResources.get("relationSchema")));
        tabbedPane.addTab(mxResources.get("input"), null, createInputPanel(),
                          "");
        
        tabbedPane.addTab(mxResources.get("output"), null, createOutputRelationalSchemaPanel(),
                "");
        
        
        //Creating a Panel that is going to be located in the center of the screen that contains the algebraic and tabbed panel
        JPanel middle = new JPanel(new BorderLayout());
        middle.add(tabbedPane, BorderLayout.CENTER);
        middle.add(algebraicPanel, BorderLayout.NORTH);
        getContentPane().add(middle, BorderLayout.CENTER);


        loadAlgebraicOperator();
        

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
				
				saveAlgebraicOperator();
			    saveInputRelations();
				saveOutputRelationAttributes();
				graph.refresh();
				setVisible(false);
			}
		});
		
		buttonPanel.add(okButton);
		
		// Sets default button for enter key
		getRootPane().setDefaultButton(okButton);

		setResizable(true);
		setSize(400, 400);
	}
	
	public JPanel createCloseTabPanel(){
		
        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel(mxResources.get("relation")+" "+inputRelationtNextIndex);
        JButton btnClose = new TabCloseButton(inputRelationtNextIndex);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        pnlTab.add(lblTitle, gbc);
        //lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
       // pnlTab.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));

        gbc.gridx++;
        gbc.weightx = 0;
        pnlTab.add(btnClose, gbc);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));

        
        return pnlTab;
		
	}
	
	public JPanel createAddTabPanel(){
		
        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);
        JButton btnAdd = new TabAddButton();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        pnlTab.add(btnAdd, gbc);

        
        return pnlTab;
		
	}
	
	public JPanel createInputPanel(){
		
		inputRelationtabbedPane = new JTabbedPane();
		
		
		Activity activity = (Activity) graph.getSelectionCell();
		
		inputRelationtNextIndex = 1;
		
		List<Port> inputPorts = activity.getInputPorts();
		
		for (Port inputPort : inputPorts) {
			
			RelationSchema inputRelationSchema = inputPort.getRelationSchema();
			
			inputRelationtabbedPane.addTab(mxResources.get("relation")+" "+inputRelationtNextIndex, null, createInputRelationalSchemaPanel(inputRelationSchema, inputRelationtNextIndex-1),
                    "");
  
//			inputRelationtabbedPane.setTabComponentAt(inputRelationtNextIndex-1, createCloseTabPanel());
			
			inputRelationtNextIndex++;
		}
		
		
		if(inputRelationtNextIndex == 1){
			RelationSchema emptyRelationSchema = new RelationSchema();
			
			inputRelationtabbedPane.addTab(mxResources.get("relation")+" 1", null, createInputRelationalSchemaPanel(emptyRelationSchema, 0),
	                          "");
	        
//			inputRelationtabbedPane.setTabComponentAt(0, createCloseTabPanel());
			
			inputRelationtNextIndex++;
		}
		
        
        
//		inputRelationtabbedPane.addTab(mxResources.get("add"), null, new JPanel(),
//                "");
//
//		inputRelationtabbedPane.setTabComponentAt(inputRelationtNextIndex-1, createAddTabPanel());
//		inputRelationtabbedPane.setEnabledAt(inputRelationtNextIndex-1, false);
        
		
		
        
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(inputRelationtabbedPane);
		
		return panel;
	}
	
	public JPanel createInputRelationalSchemaPanel(RelationSchema relationSchema, int index){
		
		JPanel tablePanel = new JPanel(new BorderLayout());
			
		JTable inputTable = new JTable(new RelationalSchemaTableModel(relationSchema));
		
		inputRelationalSchemaTables.add(index, inputTable);
		
		
		// creates panel to contain the table
		tablePanel.add(new JScrollPane(inputTable), BorderLayout.CENTER);
        
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new GridLayout(2, 1));

		// Adds Add button to create new attribute in the table
		JButton addButton = new JButton(mxResources.get("add"));
		addButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int selected_index = inputRelationtabbedPane.getSelectedIndex();
				RelationalSchemaTableModel inputRelationalSchemaTableModel = (RelationalSchemaTableModel) inputRelationalSchemaTables.get(selected_index).getModel();
				inputRelationalSchemaTableModel.insertRow(new String[] {"", ""});
			}
		});
		
		commandPanel.add(addButton);
		
		
		// Adds Remove button to remove attribute of table
		JButton removeButton = new JButton(mxResources.get("remove"));
		removeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int selected_index = inputRelationtabbedPane.getSelectedIndex();
				RelationalSchemaTableModel inputRelationalSchemaTableModel = (RelationalSchemaTableModel) inputRelationalSchemaTables.get(selected_index).getModel();
				inputRelationalSchemaTableModel.removeRow(inputRelationalSchemaTables.get(selected_index).getSelectedRow());
			}
		});
		
		commandPanel.add(removeButton);
		
		
		JPanel east = new JPanel(new GridBagLayout());
		east.add(commandPanel);
		
		tablePanel.add(east, BorderLayout.EAST);
        
		return tablePanel;
	}
	
	public JPanel createOutputRelationalSchemaPanel(){
		
		JPanel tablePanel = new JPanel(new BorderLayout());		
			
		OutputRelationalSchemaTable = createOutputTable();
		
		// creates panel to contain the table
		tablePanel.add(new JScrollPane(
				OutputRelationalSchemaTable), BorderLayout.CENTER);
        
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new GridLayout(2, 1));

		// Adds Add button to create new attribute in the table
		JButton addButton = new JButton(mxResources.get("add"));
		addButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				OutputRelationalSchemaTableModel.insertRow(new String[] {"", ""});
			}
		});
		
		commandPanel.add(addButton);
		
		
		// Adds Remove button to remove attribute of table
		JButton removeButton = new JButton(mxResources.get("remove"));
		removeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				OutputRelationalSchemaTableModel.removeRow(OutputRelationalSchemaTable.getSelectedRow());
			}
		});
		
		commandPanel.add(removeButton);
		
		JPanel east = new JPanel(new GridBagLayout());
		east.add(commandPanel);
		
		tablePanel.add(east, BorderLayout.EAST);
    
        
		return tablePanel;
	}
	
	public JTable createOutputTable(){

		Activity activity = (Activity) graph.getSelectionCell();
	
        OutputRelationalSchemaTableModel = new RelationalSchemaTableModel(activity.getOutputPort().getRelationSchema());
        JTable table = new JTable(OutputRelationalSchemaTableModel);
        
		return table;
	}
	
	public void loadAlgebraicOperator(){

		Activity activity = (Activity) graph.getSelectionCell();
		
		String albebraicOperator = activity.getAlgebraicOperator();
		
		for (int i = 0; i < algebraicOperatorsJComboBox.getModel().getSize(); i++) {
			if (algebraicOperatorsJComboBox.getModel().getElementAt(i).equals(albebraicOperator)){
				algebraicOperatorsJComboBox.setSelectedIndex(i);
				break;
			}
		}	
		
	}
	
	public void saveAlgebraicOperator(){
		
		//This is a workaround to perform an Action
		//The correct is to create an class that contains the logic of saving the algebraic operator and this method and the Action itself should reference to it. 
		
		AlgebraicOperatorAction action = new AlgebraicOperatorAction((String)algebraicOperatorsJComboBox.getSelectedItem());
		
		action.actionPerformed(new ActionEvent(graphComponent, 1, ""));

	}
	
//	public void saveInputRelations(){
//
//		Activity activity = (Activity) graph.getSelectionCell();
//		
//		activity.clearInputRelationsSchemas();
//		
//		for (JTable inputRelationalSchemaTable : inputRelationalSchemaTables) {
//			RelationalSchemaTableModel inputRelationalSchemaTableModel = (RelationalSchemaTableModel) inputRelationalSchemaTable.getModel();
//			
//			//activity.addInputRelationSchema(inputRelationalSchemaTableModel.getRelationSchema());
//			
//			Port inputPort = new Port(Port.INPUT_TYPE, activity);
//			
//			inputPort.setRelationSchema(inputRelationalSchemaTableModel.getRelationSchema());
//			
//			activity.addInputPort(inputPort);
//		}
//	}
	
	public void saveInputRelations(){

		Activity activity = (Activity) graph.getSelectionCell();
		
		activity.clearInputRelationsSchemas();
		
		
		for (int i = 0; i < inputRelationalSchemaTables.size(); i++) {
			
			RelationalSchemaTableModel inputRelationalSchemaTableModel = (RelationalSchemaTableModel) inputRelationalSchemaTables.get(i).getModel();
			
			Port inputPort = activity.getInputPorts().get(i);
			
			inputPort.setRelationSchema(inputRelationalSchemaTableModel.getRelationSchema());
			
			activity.addInputPort(inputPort);
		}
		
	}
	
	public void saveOutputRelationAttributes(){

		Activity activity = (Activity) graph.getSelectionCell();
		
		RelationSchema outputRelationSchema = OutputRelationalSchemaTableModel.getRelationSchema();
		
		//activity.setOutputRelationSchema(outputRelationSchema);
		
		activity.getOutputPort().setRelationSchema(outputRelationSchema);;
		
	}

	/**
	 * Overrides {@link JDialog#createRootPane()} to return a root pane that
	 * hides the window when the user presses the ESCAPE key.O
	 */
	protected JRootPane createRootPane()
	{
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(new ActionListener()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{
				setVisible(false);
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}
	
	
	
	public class RelationalSchemaAttribute {

        private String name;
        private String type;

        public RelationalSchemaAttribute(String type, String name) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
        
        public void setName(String name) {
            this.name = name;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    public class RelationalSchemaTableModel extends AbstractTableModel {

        private RelationSchema relation;

        public RelationalSchemaTableModel(RelationSchema relation) {
            this.relation = relation;
        }


        
        public RelationSchema getRelationSchema() {
			return relation;	
		}



		public void insertRow(String[] values){
        	
        	RelationSchemaAttribute attr = new RelationSchemaAttribute(values[0], values[1]);
        	
        	relation.addAttribute(attr);

            fireTableDataChanged();
        }

        public void removeRow(int row){
        	relation.remove(row);
            fireTableDataChanged();
        }
        
        
        @Override
        public int getRowCount() {
            return relation.getAttributes().size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }



		@Override
        public String getColumnName(int column) {
            String name = "??";
            switch (column) {
                case 0:
                    name = mxResources.get("attrType");
                    break;
                case 1:
                    name = mxResources.get("attrName");
                    break;
            }
            return name;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class type = String.class;
            switch (columnIndex) {
                case 0:
                case 1:
                    type = String.class;
                    break;
            }
            return type;
        }
        
        @Override
        public boolean isCellEditable(int row, int col)
        { return true; }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            RelationSchemaAttribute relationAttr = relation.getAttributes().get(rowIndex);
            Object value = null;
            switch (columnIndex) {
                case 0:
                    value = relationAttr.getType();
                    break;
                case 1:
                    value = relationAttr.getName();
                    break;
            }
            return value;
        }            
        
        @Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        	RelationSchemaAttribute relationAttr = relation.getAttributes().get(rowIndex);
            switch (columnIndex) {
                case 0:
                    relationAttr.setType((String)aValue);
                    break;
                case 1:
                	relationAttr.setName((String)aValue);
                    break;
            }
			super.setValueAt(aValue, rowIndex, columnIndex);
		}
            
    }
    
    private class TabCloseButton extends JButton implements ActionListener {
        private int index;
    	public TabCloseButton(int index) {
    		this.index = index;
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(null);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }
 
        public void actionPerformed(ActionEvent e) {
        	if(inputRelationtabbedPane.getTabCount()>2){
	        	int tab_index = inputRelationtabbedPane.indexOfTab(mxResources.get("relation")+" "+index);
	        	inputRelationtabbedPane.removeTabAt(tab_index);
	        	inputRelationalSchemaTables.remove(tab_index);
        	}
        }
 
        //we don't want to update UI for this button
        public void updateUI() {
        }
 
        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }
    
    
    private class TabAddButton extends JButton implements ActionListener {
        public TabAddButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("create a new tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(null);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }
 
        public void actionPerformed(ActionEvent e) {
        	int index = inputRelationtabbedPane.getTabCount() - 1;
        	inputRelationtabbedPane.removeTabAt(index);
        	        	
        	RelationSchema relationSchema = new RelationSchema();
    		inputRelationtabbedPane.addTab(mxResources.get("attributeType")+" "+inputRelationtNextIndex, null, createInputRelationalSchemaPanel(relationSchema, index),
                    "");
  
    		inputRelationtabbedPane.setTabComponentAt(index, createCloseTabPanel());
    		
    		
    		inputRelationtabbedPane.addTab(mxResources.get("add"), null, new JPanel(),
                    "");

    		inputRelationtabbedPane.setTabComponentAt(index+1, createAddTabPanel());
    		inputRelationtabbedPane.setEnabledAt(index+1, false);
    		
    		inputRelationtNextIndex++;
        }
 
        //we don't want to update UI for this button
        public void updateUI() {
        }
 
        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, getHeight()/2, getWidth() - delta - 1, getHeight()/2);
            g2.drawLine(getWidth()/2, delta, getWidth()/2, getHeight() - delta - 1);
            g2.dispose();
        }
    }


}
