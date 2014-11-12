package br.ufrj.cos.expline.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import br.ufrj.cos.expline.io.ExpLineCodec;
import br.ufrj.cos.expline.io.WorkflowCodec;
import br.ufrj.cos.expline.swing.Actions.GenerateAbstractWorkflowAction;
import br.ufrj.cos.expline.swing.Actions.ScaleAction;

import com.mxgraph.io.mxCodecRegistry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;

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
				boolean result = editor.getDerivation().validatesDerivedWorklfow();
				
				if(result)
					JOptionPane.showMessageDialog(graphComponent,
							"Derivation is valid");	
				else
					JOptionPane.showMessageDialog(graphComponent,
							"Derivation is not valid");	
				
			}
		});

		
		temp.add(editor.bind("Generate abstract workflow", new GenerateAbstractWorkflowAction(true)));
		
//		temp.add(new JMenuItem("Generate abstract workflow"));
		temp.add(new JMenuItem("Run"));
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
		
		
		temp = new JMenu("View");
		this.add(temp);
		temp.add(new JMenuItem("Show Possibilities"));
		temp.add(new JMenuItem("Show Conflicts"));
		temp.add(new JMenuItem("Show Legends"));
		
		JMenu submenu = (JMenu) temp.add(new JMenu(mxResources.get("zoom")));

		submenu.add(editor.bind("400%", new ScaleAction(4)));
		submenu.add(editor.bind("200%", new ScaleAction(2)));
		submenu.add(editor.bind("150%", new ScaleAction(1.5)));
		submenu.add(editor.bind("100%", new ScaleAction(1)));
		submenu.add(editor.bind("75%", new ScaleAction(0.75)));
		submenu.add(editor.bind("50%", new ScaleAction(0.5)));
		
		
	}
}