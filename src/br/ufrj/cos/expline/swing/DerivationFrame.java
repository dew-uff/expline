package br.ufrj.cos.expline.swing;

import java.awt.BorderLayout;
import java.awt.Panel;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import br.ufrj.cos.expline.derivation.ExpLineDerivationGraph;
import br.ufrj.cos.expline.derivation.ExpLineDerivationGraphComponent;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class DerivationFrame extends Panel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378029138434324390L;
	
	protected mxGraphComponent graphComponent;
	protected mxGraph graph;
	
	public JTabbedPane inputRelationtabbedPane = null;
	protected int inputRelationtNextIndex = 0;
	

	/**
	 * 
	 */
	public DerivationFrame(mxGraphComponent graphComponent)
	{
		super();
		
		
		this.graphComponent = graphComponent;
		this.graph = graphComponent.getGraph();
		
		setLayout(new BorderLayout());

		setSize(400, 400);
		
		
		ExpLineDerivationGraph expLineDer = new ExpLineDerivationGraph();
		
		expLineDer.getModel().setRoot(graph.getModel().getRoot());
		
		
		ExpLineDerivationGraphComponent derivationComponent = new ExpLineDerivationGraphComponent(expLineDer);
		
		
		mxCell root =  (mxCell) expLineDer.getModel().getRoot();
		root = (mxCell) root.getChildAt(0);
		
		for (int i = 0; i < root.getChildCount(); i++) {
			
//			root.getChildAt(i).setStyle("strokeWidth=6;strokeColor=#66CC00");
//			root.getChildAt(i).setStyle("strokeWidth=6;strokeColor=#FF0000");
			root.getChildAt(i).setStyle(root.getChildAt(i).getStyle() + ";opacity=20");
			
		}
		
		derivationComponent.refresh();
		
		
		this.add(derivationComponent, BorderLayout.CENTER);
		
		
		// Creates the file menu
		JMenuBar menuBar = new JMenuBar();
		JMenu temp = new JMenu("Derivate");
		menuBar.add(temp);
		temp.add(new JMenuItem("Execute"));
		temp.add(new JMenuItem("Validate"));
		temp.add(new JMenuItem("Cancel"));
		
		temp = new JMenu("View");
		menuBar.add(temp);
		temp.add(new JMenuItem("Show Possibilities"));
		temp.add(new JMenuItem("Show Conflicts"));
		temp.add(new JMenuItem("Show Legends"));
		temp.add(new JMenuItem("Zoom"));
		
		menuBar.add(new JMenuItem("Exit"));
		
		
	}

}
