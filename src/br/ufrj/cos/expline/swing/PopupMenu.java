package br.ufrj.cos.expline.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import br.ufrj.cos.expline.swing.Actions.AlgebraicOperatorOptionItem;
import br.ufrj.cos.expline.swing.Actions.HistoryAction;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

public class PopupMenu extends JPopupMenu
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3132749140550242191L;

	public PopupMenu(final ExpLineEditor editor)
	{
		boolean selected = !editor.getGraphComponent().getGraph()
				.isSelectionEmpty();
		
		boolean isVertex = false;
		
		if (selected && editor.getGraphComponent().getGraph().getSelectionCount() == 1)
			isVertex = ((mxCell)editor.getGraphComponent().getGraph().getSelectionCell()).isVertex();
		
		add(editor.bind(mxResources.get("undo"), new HistoryAction(true),
				"/images/undo.gif"));

		addSeparator();

		add(
				editor.bind(mxResources.get("cut"), TransferHandler
						.getCutAction(),
						"/images/cut.gif"))
				.setEnabled(selected);
		add(
				editor.bind(mxResources.get("copy"), TransferHandler
						.getCopyAction(),
						"/images/copy.gif"))
				.setEnabled(selected);
		add(editor.bind(mxResources.get("paste"), TransferHandler
				.getPasteAction(),
				"/images/paste.gif"));

		addSeparator();

		add(
				editor.bind(mxResources.get("delete"), mxGraphActions
						.getDeleteAction(),
						"/images/delete.gif"))
				.setEnabled(selected);

		addSeparator();
		
		
		//ExpLine-Begin
		
		if(selected){
			add(
					editor.bind(mxResources.get("editName"), mxGraphActions
							.getEditAction()));
			
			if (isVertex){
				JMenu menu = (JMenu) add(new JMenu(mxResources.get("albegraicOperator")));
				PopupMenu.populateAlgebraicOperatorMenu(menu, editor);
				
				
				
				JMenuItem item = add(new JMenuItem(mxResources.get("properties")));
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
									JOptionPane.showMessageDialog(editor.editionGraphComponent,
											mxResources.get("noActivitySelected"));	
								}
								
							}
							else{
								JOptionPane.showMessageDialog(editor.editionGraphComponent,
										mxResources.get("moreThanOneSected"));	
							}
							
						}
						else{
							JOptionPane.showMessageDialog(editor.editionGraphComponent,
									mxResources.get("noActivitySelected"));	
						}
						
					}
				});
				
			}
				
			addSeparator();
		}
		
		
		
		//ExpLine-End


		add(editor.bind(mxResources.get("selectAll"), mxGraphActions
				.getSelectAllAction()));
	}
	
	//ExpLine-Begin
	
	public static void populateAlgebraicOperatorMenu(JMenu menu, ExpLineEditor editor)
	{
		
		ButtonGroup algebraicOperatorGroup = new ButtonGroup();
		
		mxGraphComponent graphComponent = editor
				.getGraphComponent();
		mxGraph graph = graphComponent.getGraph();
		
		mxCell cell = (mxCell) graph.getSelectionCell();
		
		String style = cell.getStyle();
		
		String newStyle = "";
		
		String[] key_values = style.trim().split(";");
		
		String algebraicOperator = "";
		
		for (String key_value : key_values) {
			if(key_value.contains("algebraicOperator")) {
				newStyle = newStyle + ";" + key_value;
				algebraicOperator = key_value.split("=")[1];
				break;
			}
		}
		
		if(algebraicOperator.equals(mxResources.get("map")))
			menu.add(new AlgebraicOperatorOptionItem(algebraicOperatorGroup, editor, mxResources.get("map"), true));
		else
			menu.add(new AlgebraicOperatorOptionItem(algebraicOperatorGroup, editor, mxResources.get("map"), false));
		
		if(algebraicOperator.equals(mxResources.get("select")))
			menu.add(new AlgebraicOperatorOptionItem(algebraicOperatorGroup, editor, mxResources.get("select"), true));
		else
			menu.add(new AlgebraicOperatorOptionItem(algebraicOperatorGroup, editor, mxResources.get("select"), false));
		
		if(algebraicOperator.equals(mxResources.get("splitMap")))
			menu.add(new AlgebraicOperatorOptionItem(algebraicOperatorGroup, editor, mxResources.get("splitMap"), true));
		else
			menu.add(new AlgebraicOperatorOptionItem(algebraicOperatorGroup, editor, mxResources.get("splitMap"), false));
		
		if(algebraicOperator.equals(mxResources.get("reduce")))
			menu.add(new AlgebraicOperatorOptionItem(algebraicOperatorGroup, editor, mxResources.get("reduce"), true));
		else
			menu.add(new AlgebraicOperatorOptionItem(algebraicOperatorGroup, editor, mxResources.get("reduce"), false));
		
		if(algebraicOperator.equals(mxResources.get("join")))
			menu.add(new AlgebraicOperatorOptionItem(algebraicOperatorGroup, editor, mxResources.get("join"), true));
		else
			menu.add(new AlgebraicOperatorOptionItem(algebraicOperatorGroup, editor, mxResources.get("join"), false));
		
	}
	
	//ExpLine-End

}
