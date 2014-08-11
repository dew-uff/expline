package br.ufrj.cos.expline.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;

import br.ufrj.cos.expline.derivation.ExpLineDerivationGraph;
import br.ufrj.cos.expline.derivation.ExpLineDerivationGraphComponent;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

public class DerivationFrame extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378029138434324390L;
	
	protected mxGraphComponent graphComponent;
	protected mxGraph graph;
	
	public JTabbedPane inputRelationtabbedPane = null;
	protected int inputRelationtNextIndex = 0;
	


	protected final Frame owner;

	/**
	 * 
	 */
	public DerivationFrame(Frame owner, mxGraphComponent graphComponent)
	{
		super();
		
		this.owner = owner;
		
		this.graphComponent = graphComponent;
		this.graph = graphComponent.getGraph();
		
		//setTitle(mxResources.get("properties"));
		setLayout(new BorderLayout());

		//setResizable(true);
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
		
		
//		// Creates the file menu
//		JMenuBar menuBar = new JMenuBar();
//		JMenu temp = new JMenu("Derivate");
//		menuBar.add(temp);
//		temp.add(new JMenu("Execute"));
//		temp.add(new JMenu("Validate"));
//		temp.add(new JMenu("Cancel"));
//		
//		temp = new JMenu("View");
//		menuBar.add(temp);
//		temp.add(new JMenu("Show Possibilities"));
//		temp.add(new JMenu("Show Conflicts"));
//		temp.add(new JMenu("Show Legends"));
//		temp.add(new JMenu("Zoom"));
//		
//		menuBar.add(new JMenu("Exit"));
//		
//		
		
		
//		JFrame frame = new JFrame();
//		frame.getContentPane().add(this);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setJMenuBar(menuBar);
//		frame.setSize(870, 640);
//		
//		frame.setVisible(true);
		
		// Updates the frame title
		//updateTitle();
		
		
	}

}
