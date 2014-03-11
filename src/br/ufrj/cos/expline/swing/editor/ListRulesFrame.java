package br.ufrj.cos.expline.swing.editor;

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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import br.ufrj.cos.expline.model.Rule;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

public class ListRulesFrame extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378029138434324390L;
	
	protected mxGraphComponent graphComponent;
	protected mxGraph graph;
	protected JPanel monitoringSrvrPanel;
	
	protected Frame owner;

	/**
	 * 
	 */
	public ListRulesFrame(Frame owner, mxGraphComponent graphComponent)
	{
		super(owner);
		
		
		this.owner = owner;
		
		this.graphComponent = graphComponent;
		this.graph = graphComponent.getGraph();
		
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
		
		JPanel tablePanel = new JPanel(new BorderLayout());		
			
		final DefaultListModel<Rule> model = new DefaultListModel<Rule>();
		
		
		JList<Rule> OutputRelationalSchemaTable = new JList<Rule>(model);
		
		// creates panel to contain the table
		tablePanel.add(new JScrollPane(
				OutputRelationalSchemaTable), BorderLayout.CENTER);
        
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new GridLayout(2, 1));

		// Adds Add button to create new attribute in the table
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
//				OutputRelationalSchemaTableModel.insertRow(new String[] {"", ""});
				EditRule frame = new EditRule((Dialog)ListRulesFrame.this, graphComponent, new Rule());
				frame.setModal(true);

				frame.setVisible(true);
			}
		});
		
		commandPanel.add(addButton);
		
		
		// Adds Remove button to remove attribute of table
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				model.addElement(new Rule());
//				OutputRelationalSchemaTableModel.removeRow(OutputRelationalSchemaTable.getSelectedRow());
			}
		});
		
		commandPanel.add(removeButton);
		
		JPanel east = new JPanel(new GridBagLayout());
		east.add(commandPanel);
		
		tablePanel.add(east, BorderLayout.EAST);
    
        
		return tablePanel;
	}

}
