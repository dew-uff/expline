package br.ufrj.cos.expline.swing.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Expression;
import br.ufrj.cos.expline.model.Rule;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

public class EditRule extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378029138434324390L;
	
	protected mxGraphComponent graphComponent;
	protected mxGraph graph;
	protected JPanel monitoringSrvrPanel;
	
	protected Rule rule;
	
	protected List<FilterPanel> conditionExpressions;
	
	protected List<FilterPanel> implicationExpressions;
	
	JPanel conditionPanelHolder;
	
	JPanel implicationPanelHolder;
	
	JTextField ruleNameField;

	private final ListRulesFrame owner;
	

	/**
	 * 
	 */
	public EditRule(final Dialog owner, mxGraphComponent graphComponent, Rule rule)
	{
		super(owner);
		
		this.owner = (ListRulesFrame) owner;
		
		this.graphComponent = graphComponent;
		this.graph = graphComponent.getGraph();
		
		this.rule = rule;
		
		conditionExpressions = new ArrayList<FilterPanel>();
		implicationExpressions = new ArrayList<FilterPanel>();
		
		setTitle(mxResources.get("rule"));
		setLayout(new BorderLayout());


	    JPanel centerPanel = new JPanel(new GridLayout(2,0));
	    
		JPanel conditionPanel1 = new JPanel();
		conditionPanel1.setPreferredSize(new Dimension(600, 450));
		conditionPanelHolder = new JPanel(new GridLayout(0, 1));
		   
		   
	    JPanel borderLayoutPanel = new JPanel(new BorderLayout());
	    borderLayoutPanel.add(conditionPanelHolder, BorderLayout.NORTH);
	    JScrollPane scrollPane = new JScrollPane(borderLayoutPanel);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	      
	    //conditionPanelHolder.add(new FilterPanel(this, FilterPanel.CONDITION_TYPE));
	    
	    if(rule.getCondition().isEmpty())
	    	conditionPanelHolder.add(new FilterPanel(this, FilterPanel.CONDITION_TYPE, new Expression()));
	    
	    for (Expression exp : rule.getCondition()) {
	    	conditionPanelHolder.add(new FilterPanel(this, FilterPanel.CONDITION_TYPE, exp));
		}

	    conditionPanel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	    conditionPanel1.setLayout(new BorderLayout());
	    conditionPanel1.add(scrollPane, BorderLayout.CENTER);

	    conditionPanel1.setBorder(BorderFactory.createTitledBorder("Condition"));
	    
	    
	    JPanel conditionPanel2 = new JPanel();
		conditionPanel1.setPreferredSize(new Dimension(600, 450));
		implicationPanelHolder  = new JPanel(new GridLayout(0, 1));
		   
		   
	    borderLayoutPanel = new JPanel(new BorderLayout());
	    borderLayoutPanel.add(implicationPanelHolder, BorderLayout.NORTH);
	    scrollPane = new JScrollPane(borderLayoutPanel);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	      
	    if(rule.getImplication().isEmpty())
	    	implicationPanelHolder.add(new FilterPanel(this, FilterPanel.IMPLICATION_TYPE, new Expression()));
	    
	    for (Expression exp : rule.getImplication()) {
	    	implicationPanelHolder.add(new FilterPanel(this, FilterPanel.IMPLICATION_TYPE, exp));
		}

	    conditionPanel2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	    conditionPanel2.setLayout(new BorderLayout());
	    conditionPanel2.add(scrollPane, BorderLayout.CENTER);

	    conditionPanel2.setBorder(BorderFactory.createTitledBorder("Implication"));
	    
	    JPanel cp1 = new JPanel(new BorderLayout());
	    //cp1.add(panel, BorderLayout.NORTH);
	    cp1.add(conditionPanel1, BorderLayout.CENTER);
	    
	    JPanel cp2 = new JPanel(new BorderLayout());
	    //cp2.add(panel2, BorderLayout.NORTH);
	    cp2.add(conditionPanel2, BorderLayout.CENTER);
	    
	    centerPanel.add(cp1);
	    centerPanel.add(cp2);
	    
	    
	    JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    
	    namePanel.add(new JLabel("Name"));
	    ruleNameField = new JTextField();
	    ruleNameField.setColumns(13);
	    ruleNameField.setText(rule.getName());
	    namePanel.add(ruleNameField);	    

	    getContentPane().add(namePanel, BorderLayout.NORTH);
	    getContentPane().add(centerPanel, BorderLayout.CENTER);
	    


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
	
	public void saveData(){
		
		//salva do zero todas as express�es pra n�o dar problema de duplicata ou sujeira 
		
		List<Expression> conditions = new ArrayList<Expression>();
		List<Expression> implications = new ArrayList<Expression>();
		
		rule.setCondition(conditions);
		rule.setImplication(implications);
		
		rule.setName(ruleNameField.getText());
		
		for (FilterPanel conditionPanel : conditionExpressions) {
			
			Expression exp = new Expression();
			
			conditions.add(exp);
			
			String operator = (String) conditionPanel.selectOperatorJComboBox.getSelectedItem();
			
			if(operator.equals("Selected")){
				exp.setOperation(Expression.OPERATION_SELECTED);
			}
			else{
				exp.setOperation(Expression.OPERATION_NOT_SELECTED);
			}
			
			
			
			String modifier = (String) conditionPanel.modifierJComboBox.getSelectedItem();
			
			if(modifier.contains("All")){
				exp.setModifier(Expression.MODIFIER_ALL);
			}
			else
			if(modifier.contains("Some")){
				exp.setModifier(Expression.MODIFIER_SOME);
			}
			else
			if(modifier.contains("Any")){
				exp.setModifier(Expression.MODIFIER_ANY);
			}
			
			
			if(modifier.contains("optional")){
				exp.setFilter(Expression.FILTER_OPTIONAL);
			}
			else
			if(modifier.contains("variant")){
				exp.setFilter(Expression.FILTER_VARIANT);
			}
			else
				exp.setFilter(Expression.FILTER_NONE);
			
			
			
			for (Component component : conditionPanel.menu.getComponents()){
				JCheckBox jchBox = (JCheckBox) component;
				
				if(jchBox.isSelected()){
					String id = jchBox.getActionCommand();
					
					Activity activity = (Activity) ((mxGraphModel)graph.getModel()).getCell(id);
					exp.addActivity(activity);
					
				}
				
			}
			
		}
		
		
		
		for (FilterPanel implicationPanel : implicationExpressions) {
			
			Expression exp = new Expression();
			
			implications.add(exp);
			
			String operator = (String) implicationPanel.selectOperatorJComboBox.getSelectedItem();
			
			if(operator.equals("Select")){
				exp.setOperation(Expression.OPERATION_SELECTED);
			}
			else{
				exp.setOperation(Expression.OPERATION_NOT_SELECTED);
			}
			
			
			
			String modifier = (String) implicationPanel.modifierJComboBox.getSelectedItem();
			
			if(modifier.contains("All")){
				exp.setModifier(Expression.MODIFIER_ALL);
			}
			else
			if(modifier.contains("Some")){
				exp.setModifier(Expression.MODIFIER_SOME);
			}
			else
			if(modifier.contains("Any")){
				exp.setModifier(Expression.MODIFIER_ANY);
			}
			
			
			if(modifier.contains("optional")){
				exp.setFilter(Expression.FILTER_OPTIONAL);
			}
			else
			if(modifier.contains("variant")){
				exp.setFilter(Expression.FILTER_VARIANT);
			}
			else
				exp.setFilter(Expression.FILTER_NONE);
			
			
			
			for (Component component : implicationPanel.menu.getComponents()){
				JCheckBox jchBox = (JCheckBox) component;
				
				if(jchBox.isSelected()){
					String id = jchBox.getActionCommand();
					
					Activity activity = (Activity) ((mxGraphModel)graph.getModel()).getCell(id);
					exp.addActivity(activity);
				}
				
			}
			
		}
		
		
	}


}
