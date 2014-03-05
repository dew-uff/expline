package br.ufrj.cos.expline.swing.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

public class EditorRulesFrame extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378029138434324390L;
	
	protected mxGraphComponent graphComponent;
	protected mxGraph graph;
	protected JPanel monitoringSrvrPanel;

	/**
	 * 
	 */
	public EditorRulesFrame(Frame owner, mxGraphComponent graphComponent)
	{
		super(owner);
		
		this.graphComponent = graphComponent;
		this.graph = graphComponent.getGraph();
		
		setTitle(mxResources.get("rule"));
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
		JLabel titleLabel = new JLabel("Condition");
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 18, 0, 0));
		titleLabel.setOpaque(false);
		panel.add(titleLabel, BorderLayout.CENTER);
		
		
		// Creates the gradient panel
		JPanel panel2 = new JPanel(new BorderLayout())
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

		panel2.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createMatteBorder(0, 0, 1, 0, Color.GRAY), BorderFactory
				.createEmptyBorder(8, 8, 12, 8)));

		// Adds title
		titleLabel = new JLabel("Implication");
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 18, 0, 0));
		titleLabel.setOpaque(false);
		panel2.add(titleLabel, BorderLayout.CENTER);

		//getContentPane().add(panel, BorderLayout.NORTH);
		
		
		
		//ConditionPanel conditionPanel = new ConditionPanel();
	    JPanel centerPanel = new JPanel(new GridLayout(2,0));
	    JPanel conditionPanel1 = new ConditionPanel();
	    //conditionPanel1.setBorder(BorderFactory.createTitledBorder("Condition"));
	    JPanel conditionPanel2 = new ConditionPanel();
	    //conditionPanel2.setBorder(BorderFactory.createTitledBorder("Implication"));
	    
	    JPanel cp1 = new JPanel(new BorderLayout());
	    cp1.add(panel, BorderLayout.NORTH);
	    cp1.add(conditionPanel1, BorderLayout.CENTER);
	    
	    JPanel cp2 = new JPanel(new BorderLayout());
	    cp2.add(panel2, BorderLayout.NORTH);
	    cp2.add(conditionPanel2, BorderLayout.CENTER);
	    
//	    centerPanel.add( conditionPanel1);
//	    centerPanel.add( conditionPanel2);
	    
	    centerPanel.add(cp1);
	    centerPanel.add(cp2);

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
				setVisible(false);
			}
		});
		
		buttonPanel.add(okButton);
		
		// Sets default button for enter key
		getRootPane().setDefaultButton(okButton);

		setResizable(true);
		setSize(600, 500);
	}
	
	


}
