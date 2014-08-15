package br.ufrj.cos.expline.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;

import br.ufrj.cos.expline.swing.Actions.AlignCellsAction;
import br.ufrj.cos.expline.swing.Actions.ExitAction;
import br.ufrj.cos.expline.swing.Actions.HistoryAction;
import br.ufrj.cos.expline.swing.Actions.NewAction;
import br.ufrj.cos.expline.swing.Actions.OpenAction;
import br.ufrj.cos.expline.swing.Actions.PageSetupAction;
import br.ufrj.cos.expline.swing.Actions.PrintAction;
import br.ufrj.cos.expline.swing.Actions.SaveAction;
import br.ufrj.cos.expline.swing.Actions.ScaleAction;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxResources;

public class MenuBar extends JMenuBar
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4060203894740766714L;

	public MenuBar(final ExpLineEditor editor)
	{
		final mxGraphComponent graphComponent = editor.getGraphComponent();

		JMenu menu = null;
		JMenu submenu = null;

		// Creates the file menu
		menu = add(new JMenu(mxResources.get("file")));

		menu.add(editor.bind(mxResources.get("new"), new NewAction(), "/images/new.gif"));
		menu.add(editor.bind(mxResources.get("openFile"), new OpenAction(), "/images/open.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("save"), new SaveAction(false), "/images/save.gif"));
		menu.add(editor.bind(mxResources.get("saveAs"), new SaveAction(true), "/images/saveas.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("pageSetup"), new PageSetupAction(), "/images/pagesetup.gif"));
		menu.add(editor.bind(mxResources.get("print"), new PrintAction(), "/images/print.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("exit"), new ExitAction()));

		// Creates the edit menu
		menu = add(new JMenu(mxResources.get("edit")));

		menu.add(editor.bind(mxResources.get("undo"), new HistoryAction(true), "/images/undo.gif"));
		menu.add(editor.bind(mxResources.get("redo"), new HistoryAction(false), "/images/redo.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("cut"), TransferHandler.getCutAction(), "/images/cut.gif"));
		menu.add(editor.bind(mxResources.get("copy"), TransferHandler.getCopyAction(), "/images/copy.gif"));
		menu.add(editor.bind(mxResources.get("paste"), TransferHandler.getPasteAction(), "/images/paste.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("delete"), mxGraphActions.getDeleteAction(), "/images/delete.gif"));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("selectAll"), mxGraphActions.getSelectAllAction()));
		menu.add(editor.bind(mxResources.get("selectNone"), mxGraphActions.getSelectNoneAction()));
		
		// Creates the view menu
		menu = add(new JMenu(mxResources.get("view")));

		submenu = (JMenu) menu.add(new JMenu(mxResources.get("zoom")));

		submenu.add(editor.bind("400%", new ScaleAction(4)));
		submenu.add(editor.bind("200%", new ScaleAction(2)));
		submenu.add(editor.bind("150%", new ScaleAction(1.5)));
		submenu.add(editor.bind("100%", new ScaleAction(1)));
		submenu.add(editor.bind("75%", new ScaleAction(0.75)));
		submenu.add(editor.bind("50%", new ScaleAction(0.5)));

		submenu.addSeparator();

		submenu.add(editor.bind(mxResources.get("custom"), new ScaleAction(0)));

		menu.addSeparator();

		menu.add(editor.bind(mxResources.get("zoomIn"), mxGraphActions.getZoomInAction()));
		menu.add(editor.bind(mxResources.get("zoomOut"), mxGraphActions.getZoomOutAction()));

		menu.addSeparator();

		submenu = (JMenu) menu.add(new JMenu(mxResources.get("align")));

		submenu.add(editor.bind(mxResources.get("left"), new AlignCellsAction(mxConstants.ALIGN_LEFT),
				"/images/alignleft.gif"));
		submenu.add(editor.bind(mxResources.get("center"), new AlignCellsAction(mxConstants.ALIGN_CENTER),
				"/images/aligncenter.gif"));
		submenu.add(editor.bind(mxResources.get("right"), new AlignCellsAction(mxConstants.ALIGN_RIGHT),
				"/images/alignright.gif"));

		submenu.addSeparator();

		submenu.add(editor.bind(mxResources.get("top"), new AlignCellsAction(mxConstants.ALIGN_TOP),
				"/images/aligntop.gif"));
		submenu.add(editor.bind(mxResources.get("middle"), new AlignCellsAction(mxConstants.ALIGN_MIDDLE),
				"/images/alignmiddle.gif"));
		submenu.add(editor.bind(mxResources.get("bottom"), new AlignCellsAction(mxConstants.ALIGN_BOTTOM),
				"/images/alignbottom.gif"));

		menu.addSeparator();

		submenu = (JMenu) menu.add(new JMenu(mxResources.get("layout")));

		submenu.add(editor.graphLayout("verticalHierarchical", true));
		submenu.add(editor.graphLayout("horizontalHierarchical", true));

		submenu.addSeparator();

		submenu.add(editor.graphLayout("verticalPartition", false));
		submenu.add(editor.graphLayout("horizontalPartition", false));

		submenu.addSeparator();

		submenu.add(editor.graphLayout("verticalStack", false));
		submenu.add(editor.graphLayout("horizontalStack", false));

		submenu.addSeparator();

		submenu.add(editor.graphLayout("verticalTree", true));
		submenu.add(editor.graphLayout("horizontalTree", true));

		submenu.addSeparator();

		submenu.add(editor.graphLayout("placeEdgeLabels", false));
		submenu.add(editor.graphLayout("parallelEdges", false));

		submenu.addSeparator();

		submenu.add(editor.graphLayout("organicLayout", true));
		submenu.add(editor.graphLayout("circleLayout", true));


		// Creates the generate menu
		menu = add(new JMenu("Experiment Line"));
		
		JMenuItem item = menu.add(new JMenuItem(mxResources.get("activityProperties")));
		item.addActionListener(new ActionListener()
		{
			/*
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e)
			{
				
				if (!editor.getGraphComponent().getGraph()
						.isSelectionEmpty()) {
					if (editor.getGraphComponent().getGraph().getSelectionCount() == 1){
						
						if(((mxCell)editor.getGraphComponent().getGraph().getSelectionCell()).isVertex()){
							
							editor.properties();
							
						}
						else{				
							JOptionPane.showMessageDialog(graphComponent,
									mxResources.get("noActivitySelected"));	
						}
						
					}
					else{
						JOptionPane.showMessageDialog(graphComponent,
								mxResources.get("moreThanOneSected"));	
					}
					
				}
				else{
					JOptionPane.showMessageDialog(graphComponent,
							mxResources.get("noActivitySelected"));	
				}
				
			}
		});
		
		 item = menu.add(new JMenuItem(mxResources.get("rules")));
		item.addActionListener(new ActionListener()
		{
			/*
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e)
			{
				
				editor.rules();

			}
		});
		
		
		menu.addSeparator();
		
//		menu.add(new JMenuItem("Derive abstract workflow"));
		
		item = menu.add(new JMenuItem("Derive abstract workflow"));
		item.addActionListener(new ActionListener()
		{
			/*
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e)
			{
				editor.changeToDerivationView();			
			}
			

		});
		
		menu.add(new JMenuItem("Derive and run workflow"));

		
		// Creates the help menu
		menu = add(new JMenu(mxResources.get("help")));

		item = menu.add(new JMenuItem(mxResources.get("aboutExpLine")));
		item.addActionListener(new ActionListener()
		{
			/*
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e)
			{
				editor.about();
			}
		});
	};
};