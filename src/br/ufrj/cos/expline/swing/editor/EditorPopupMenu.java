package br.ufrj.cos.expline.swing.editor;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import br.ufrj.cos.expline.swing.editor.EditorActions.HistoryAction;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxResources;

public class EditorPopupMenu extends JPopupMenu
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3132749140550242191L;

	public EditorPopupMenu(BasicGraphEditor editor)
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
				EditorMenuBar.populateAlgebraicOperatorMenu(menu, editor);
			}
				
			addSeparator();
		}
		
		
		
		//ExpLine-End


		add(editor.bind(mxResources.get("selectAll"), mxGraphActions
				.getSelectAllAction()));
	}

}
