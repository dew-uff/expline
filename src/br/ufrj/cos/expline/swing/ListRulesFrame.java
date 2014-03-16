package br.ufrj.cos.expline.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import br.ufrj.cos.expline.model.ExpLine;
import br.ufrj.cos.expline.model.Rule;
import br.ufrj.cos.expline.swing.jgraphx.ExpLineGraph;
import br.ufrj.cos.expline.swing.jgraphx.ExpLineGraphComponent;

import com.mxgraph.util.mxResources;

public class ListRulesFrame extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378029138434324390L;
	
	protected ExpLineGraphComponent expLineGraphComponent;
	protected ExpLineGraph expLineGraph;
	
	protected Frame owner;
	
	protected DefaultListModel<Rule> rulesListModel;
	
	protected JList<Rule> ruleJList;
	
	protected List<Rule> rules;

	/**
	 * 
	 */
	public ListRulesFrame(Frame owner, ExpLineGraphComponent expLineGraphComponent)
	{
		super(owner);
		
		this.owner = owner;
		
		this.expLineGraphComponent = expLineGraphComponent;
		this.expLineGraph = (ExpLineGraph) expLineGraphComponent.getGraph();
		
		ExpLine model = (ExpLine) expLineGraph.getModel();
		
		rules = model.getRules();
		
		setTitle(mxResources.get("rules"));
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
		JLabel titleLabel = new JLabel(mxResources.get("rules"));
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 18, 0, 0));
		titleLabel.setOpaque(false);
		panel.add(titleLabel, BorderLayout.CENTER);

		getContentPane().add(panel, BorderLayout.NORTH);
		
        
        //Creating a Panel that is going to be located in the center of the screen that contains the algebraic and tabbed panel
        JPanel middle = new JPanel(new BorderLayout());
        middle.add(createOutputRelationalSchemaPanel());
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

				setVisible(false);
			}
		});
		
		buttonPanel.add(okButton);
		
		// Sets default button for enter key
		getRootPane().setDefaultButton(okButton);


		setResizable(true);
		setSize(400, 400);
	}
	
	
	public JPanel createOutputRelationalSchemaPanel(){
		
		JPanel rulesListPanel = new JPanel(new BorderLayout());		
			
		rulesListModel = new DefaultListModel<Rule>();
		
		ruleJList = new JList<Rule>(rulesListModel);
		
		loadRulesList();
		
		// creates panel to contain the table
		rulesListPanel.add(new JScrollPane(
				ruleJList), BorderLayout.CENTER);
        
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new GridLayout(3, 1));

		// Adds Add button to create new attribute in the table
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addRule();
			}
		});
		
		commandPanel.add(addButton);
		
		JButton editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				editRule();
			}
		});
		
		commandPanel.add(editButton);
		
		
		// Adds Remove button to remove attribute of table
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				removeRule();
			}
		});
		
		commandPanel.add(removeButton);
		
		JPanel east = new JPanel(new GridBagLayout());
		east.add(commandPanel);
		
		rulesListPanel.add(east, BorderLayout.EAST);
    
        
		return rulesListPanel;
	}
	
	
	private void loadRulesList() {
		
		for (Rule rule : rules) {
			rulesListModel.addElement(rule);
		}
		
	}


	void editRule(){
		
		if(!ruleJList.isSelectionEmpty()){
			Rule rule = rulesListModel.getElementAt(ruleJList.getSelectedIndex());
			
			EditRuleFrame frame = new EditRuleFrame((Dialog)ListRulesFrame.this, expLineGraphComponent, rule);
			frame.setModal(true);
	
			frame.setVisible(true);
		}
	}
	
	void addRule(){
		Rule rule = new Rule();
		
		rules.add(rule);
		rulesListModel.addElement(rule);
		
		rule.setName("new rule");
		
		EditRuleFrame frame = new EditRuleFrame((Dialog)ListRulesFrame.this, expLineGraphComponent, rule);
		frame.setModal(true);

		frame.setVisible(true);
		
	}
	
	void removeRule(){
		
		Rule rule = rulesListModel.getElementAt(ruleJList.getSelectedIndex());
		
		rulesListModel.removeElement(rule);
		
		rules.remove(rule);
		
		
		
	}

}
