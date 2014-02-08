package br.ufrj.cos.expline.swing.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import javax.swing.table.AbstractTableModel;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

public class EditorPropertiesFrame extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378029138434324390L;
	
	protected mxGraphComponent graphComponent;
	protected mxGraph graph;
	
	protected RelationalSchemaTableModel InputRelationalSchemaTableModel = null;
	protected RelationalSchemaTableModel OutputRelationalSchemaTableModel = null;
	protected JTable InputRelationalSchemaTable = null;
	protected JTable OutputRelationalSchemaTable = null;
	protected JComboBox<String> algebraicOperatorsJComboBox = null;

	/**
	 * 
	 */
	public EditorPropertiesFrame(Frame owner, mxGraphComponent graphComponent)
	{
		super(owner);
		
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
		String[] algebraicOperators = {"Map", "Split Map", "Reduce", "Filter", "Join"};
		algebraicOperatorsJComboBox = new JComboBox<String>(algebraicOperators);
		loadAlgebraicOperator();
		algebraicPanel.add(algebraicOperatorsJComboBox);

        
        //creating the tabbed Panel
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Input", null, createInputRelationalSchemaPanel(),
                          "");
        tabbedPane.addTab("Output", null, createOutputRelationalSchemaPanel(),
                "");
        
        
        //Creating a Panel that is going to be located in the center of the screen that contains the algebraic and tabbed panel
        JPanel middle = new JPanel(new BorderLayout());
        middle.add(tabbedPane, BorderLayout.CENTER);
        middle.add(algebraicPanel, BorderLayout.NORTH);
        getContentPane().add(middle, BorderLayout.CENTER);



		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createMatteBorder(1, 0, 0, 0, Color.GRAY), BorderFactory
				.createEmptyBorder(16, 8, 8, 8)));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		// Adds Cancel button to close window without saving
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});

		buttonPanel.add(cancelButton);

		// Adds Ok button to close window saving the information
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				saveAlgebraicOperator();
			    saveInputRelationAttributes();
				saveOutputRelationAttributes();
				setVisible(false);
			}
		});
		
		buttonPanel.add(okButton);
		
		// Sets default button for enter key
		getRootPane().setDefaultButton(okButton);

		setResizable(false);
		setSize(400, 400);
	}
	
	public JPanel createInputRelationalSchemaPanel(){
		
		JPanel tablePanel = new JPanel(new BorderLayout());	
			
		InputRelationalSchemaTable = createInputTable();
		
		// creates panel to contain the table
		tablePanel.add(new JScrollPane(InputRelationalSchemaTable), BorderLayout.CENTER);
        
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.Y_AXIS));

		// Adds Add button to create new attribute in the table
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				InputRelationalSchemaTableModel.insertRow(new String[] {"", ""});
			}
		});
		
		commandPanel.add(addButton);
		
		
		// Adds Remove button to remove attribute of table
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				InputRelationalSchemaTableModel.removeRow(InputRelationalSchemaTable.getSelectedRow());
			}
		});
		
		commandPanel.add(removeButton);
		
		
		tablePanel.add(commandPanel, BorderLayout.EAST);
        
		return tablePanel;
	}
	
	public JPanel createOutputRelationalSchemaPanel(){
		
		JPanel tablePanel = new JPanel(new BorderLayout());		
			
		OutputRelationalSchemaTable = createOutputTable();
		
		// creates panel to contain the table
		tablePanel.add(new JScrollPane(
				OutputRelationalSchemaTable), BorderLayout.CENTER);
        
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.Y_AXIS));

		// Adds Add button to create new attribute in the table
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				OutputRelationalSchemaTableModel.insertRow(new String[] {"", ""});
			}
		});
		
		commandPanel.add(addButton);
		
		
		// Adds Remove button to remove attribute of table
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				OutputRelationalSchemaTableModel.removeRow(OutputRelationalSchemaTable.getSelectedRow());
			}
		});
		
		commandPanel.add(removeButton);
		
		
		tablePanel.add(commandPanel, BorderLayout.EAST);
    
        
		return tablePanel;
	}
	
	public JTable createInputTable(){

		List<RelationalSchemaAttribute> relations = getInputRelationAttributes();
        
        InputRelationalSchemaTableModel = new RelationalSchemaTableModel(relations);
        JTable table = new JTable(InputRelationalSchemaTableModel);

		return table;
	}
	
	public JTable createOutputTable(){

		List<RelationalSchemaAttribute> relations = getOutputRelationAttributes();
        
        OutputRelationalSchemaTableModel = new RelationalSchemaTableModel(relations);
        JTable table = new JTable(OutputRelationalSchemaTableModel);
        
		return table;
	}
	
	public void loadAlgebraicOperator(){

		mxCell cell = (mxCell) graph.getSelectionCell();
		
		String style = cell.getStyle();
		
		String[] key_values = style.trim().split(";");
		
		for (String key_value : key_values) {
			if(key_value.contains("algebraicOperator")){
				String albebraicOperator = key_value.split("=")[1];
				
				for (int i = 0; i < algebraicOperatorsJComboBox.getModel().getSize(); i++) {
					if (algebraicOperatorsJComboBox.getModel().getElementAt(i).equals(albebraicOperator)){
						algebraicOperatorsJComboBox.setSelectedIndex(i);
						break;
					}
				}
				break;
			}
		}
		
	}
	
	public void saveAlgebraicOperator(){

		mxCell cell = (mxCell) graph.getSelectionCell();
		
		String style = cell.getStyle();
		
		String newStyle = "";
		
		String[] key_values = style.trim().split(";");
		
		for (String key_value : key_values) {
			if(!key_value.contains("algebraicOperator"))
				newStyle = newStyle + ";" + key_value;
		}
		
		newStyle = newStyle.substring(1, newStyle.length()) +";algebraicOperator="+algebraicOperatorsJComboBox.getSelectedItem();
		
		cell.setStyle(newStyle);
	}
	
	public void saveInputRelationAttributes(){

		mxCell cell = (mxCell) graph.getSelectionCell();
		
		String newStyle = clearInputRelationFromActivity();
		
		List<RelationalSchemaAttribute> relAttrs = InputRelationalSchemaTableModel.getRelationAttributes();
		
		for (RelationalSchemaAttribute relAttr : relAttrs) {
			newStyle = newStyle + ";inRelSchAttr=" + relAttr.getType() + "@%&"+ relAttr.getName();
		}
		
		cell.setStyle(newStyle);
	}
	
	public void saveOutputRelationAttributes(){

		mxCell cell = (mxCell) graph.getSelectionCell();
		
		String newStyle = clearOutputRelationFromActivity();
		
		List<RelationalSchemaAttribute> relAttrs = OutputRelationalSchemaTableModel.getRelationAttributes();
		
		for (RelationalSchemaAttribute relAttr : relAttrs) {
			newStyle = newStyle + ";outRelSchAttr=" + relAttr.getType() + "@%&"+ relAttr.getName();
		}
		
		cell.setStyle(newStyle);
	}
	
	public String clearInputRelationFromActivity(){
		
		mxCell cell = (mxCell) graph.getSelectionCell();
		
		String style = cell.getStyle();
		
		String newStyle = "";
		
		String[] key_values = style.trim().split(";");
		
		for (String key_value : key_values) {
			if(!key_value.contains("inRelSchAttr"))
				newStyle = newStyle + ";" + key_value;
		}
		
		return newStyle.substring(1, newStyle.length());
	}
	
	public String clearOutputRelationFromActivity(){
		
		mxCell cell = (mxCell) graph.getSelectionCell();
		
		String style = cell.getStyle();
		
		String newStyle = "";
		
		String[] key_values = style.trim().split(";");
		
		for (String key_value : key_values) {
			if(!key_value.contains("outRelSchAttr"))
				newStyle = newStyle + ";" + key_value;
		}
		
		return newStyle.substring(1, newStyle.length());
	}
	
	public List<RelationalSchemaAttribute> getInputRelationAttributes(){

		List<RelationalSchemaAttribute> relationSchemaAttributes = new ArrayList<>();
		
		mxCell cell = (mxCell) graph.getSelectionCell();
		
		String style = cell.getStyle();
		
		String[] key_values = style.trim().split(";");
		
		for (String key_value : key_values) {
			if(key_value.contains("inRelSchAttr")){
				String value = key_value.split("=")[1];
				String attributeType = value.split("@%&")[0];
				String attributeName = value.split("@%&")[1];
				
				relationSchemaAttributes.add(new RelationalSchemaAttribute(attributeType, attributeName));
			}
			key_value.split("=");
		}
		
		return relationSchemaAttributes;
	}
	
	public List<RelationalSchemaAttribute> getOutputRelationAttributes(){

		List<RelationalSchemaAttribute> relationSchemaAttributes = new ArrayList<>();
		
		mxCell cell = (mxCell) graph.getSelectionCell();
		
		String style = cell.getStyle();
		
		String[] key_values = style.trim().split(";");
		
		for (String key_value : key_values) {
			if(key_value.contains("outRelSchAttr")){
				String value = key_value.split("=")[1];
				String attributeType = value.split("@%&")[0];
				String attributeName = value.split("@%&")[1];
				
				relationSchemaAttributes.add(new RelationalSchemaAttribute(attributeType, attributeName));
			}
			key_value.split("=");
		}
		
		return relationSchemaAttributes;
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

        private List<RelationalSchemaAttribute> relations;

        public RelationalSchemaTableModel(List<RelationalSchemaAttribute> relations) {
            this.relations = new ArrayList<>(relations);
        }


        
        public void insertRow(String[] values){
        	RelationalSchemaAttribute relation = new RelationalSchemaAttribute(values[0], values[1]);
        	relations.add(0, relation);

            fireTableDataChanged();
        }

        public void removeRow(int row){
        	relations.remove(row);
            fireTableDataChanged();
        }
        
        public final List<RelationalSchemaAttribute> getRelationAttributes(){
        	return relations;
        }
        
        
        
        @Override
        public int getRowCount() {
            return relations.size();
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
                    name = "Attribute Type";
                    break;
                case 1:
                    name = "Attribute Name";
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
            RelationalSchemaAttribute relationAttr = relations.get(rowIndex);
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
        	RelationalSchemaAttribute relationAttr = relations.get(rowIndex);
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

}
