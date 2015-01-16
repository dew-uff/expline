package br.ufrj.cos.expline.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import br.ufrj.cos.expline.swing.Actions.GenerateAbstractWorkflowAction;
import br.ufrj.cos.expline.swing.Actions.GenerateConcreteWorkflowAction;

import com.mxgraph.swing.mxGraphComponent;

public class DerivationMenuBar extends JMenuBar
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4060203894740766714L;

	public DerivationMenuBar(final ExpLineEditor editor)
	{
		final mxGraphComponent graphComponent = editor.getGraphComponent();

		// Creates the file menu
		JMenu temp = new JMenu("Derivation");
		this.add(temp);
		temp.add(new JMenuItem("Validate")).addActionListener(new ActionListener()
		{
			/*
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e)
			{
				boolean result = editor.getDerivation().validatesDerivedWorkflow();
				
				if(result)
					JOptionPane.showMessageDialog(graphComponent,
							"Derivation Status: Selection is Valid");	
				else
					JOptionPane.showMessageDialog(graphComponent,
							"Derivation Status: Selection is not Valid");
				
			}
		});

		
		temp.add(editor.bind("Generate abstract workflow", new GenerateAbstractWorkflowAction(true)));
		
		JMenu concreteWorkflowSubmenu = (JMenu) temp.add(new JMenu("Generate concrete workflow"));
		
		concreteWorkflowSubmenu.add(new JMenuItem(editor.bind("Scicumulus", new GenerateConcreteWorkflowAction(true))));
		
		temp.add(new JMenuItem("Cancel")).addActionListener(new ActionListener()
		{
			/*
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e)
			{
				editor.changeToEditionView();
			}
		});
		
		
//		temp = new JMenu("View");
//		this.add(temp);
//		jmenuItemTemp = new JMenuItem("Show Possibilities");
//		jmenuItemTemp.setEnabled(false);
//		temp.add(jmenuItemTemp);
//		
//		jmenuItemTemp = new JMenuItem("Show Conflicts");
//		jmenuItemTemp.setEnabled(false);
//		temp.add(jmenuItemTemp);
//		
//		jmenuItemTemp = new JMenuItem("Show Legends");
//		jmenuItemTemp.setEnabled(false);
//		temp.add(jmenuItemTemp);
//		
//		JMenu submenu = (JMenu) temp.add(new JMenu(mxResources.get("zoom")));
//
//		submenu.add(editor.bind("400%", new ScaleAction(4)));
//		submenu.add(editor.bind("200%", new ScaleAction(2)));
//		submenu.add(editor.bind("150%", new ScaleAction(1.5)));
//		submenu.add(editor.bind("100%", new ScaleAction(1)));
//		submenu.add(editor.bind("75%", new ScaleAction(0.75)));
//		submenu.add(editor.bind("50%", new ScaleAction(0.5)));
		
		
	}
}