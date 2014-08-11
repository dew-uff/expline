package br.ufrj.cos.expline.swing;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
		JMenu temp = new JMenu("Derivate");
		this.add(temp);
		temp.add(new JMenuItem("Execute"));
		temp.add(new JMenuItem("Validate"));
		temp.add(new JMenuItem("Cancel"));
		
		temp = new JMenu("View");
		this.add(temp);
		temp.add(new JMenuItem("Show Possibilities"));
		temp.add(new JMenuItem("Show Conflicts"));
		temp.add(new JMenuItem("Show Legends"));
		temp.add(new JMenuItem("Zoom"));
		
		
	}
}