/**
 * $Id: KeyboardHandler.java,v 1.1 2012/11/15 13:26:46 gaudenz Exp $
 * Copyright (c) 2008, Gaudenz Alder
 */
package br.ufrj.cos.expline.swing;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.util.mxGraphActions;

/**
 * @author Administrator
 * 
 */
public class KeyboardHandler extends mxKeyboardHandler
{

	/**
	 * 
	 * @param graphComponent
	 */
	public KeyboardHandler(mxGraphComponent graphComponent)
	{
		super(graphComponent);
	}

	/**
	 * Return JTree's input map.
	 */
	protected InputMap getInputMap(int condition)
	{
		InputMap map = super.getInputMap(condition);

		if (condition == JComponent.WHEN_FOCUSED && map != null)
		{
			map.put(KeyStroke.getKeyStroke("control S"), "save");
			map.put(KeyStroke.getKeyStroke("control shift S"), "saveAs");
			map.put(KeyStroke.getKeyStroke("control N"), "new");
			map.put(KeyStroke.getKeyStroke("control O"), "open");

			map.put(KeyStroke.getKeyStroke("control Z"), "undo");
			map.put(KeyStroke.getKeyStroke("control Y"), "redo");
			map
					.put(KeyStroke.getKeyStroke("control shift V"),
							"selectVertices");
			map.put(KeyStroke.getKeyStroke("control shift E"), "selectEdges");
		}

		return map;
	}

	/**
	 * Return the mapping between JTree's input map and JGraph's actions.
	 */
	protected ActionMap createActionMap()
	{
		ActionMap map = super.createActionMap();

		map.put("save", new Actions.SaveAction(false));
		map.put("saveAs", new Actions.SaveAction(true));
		map.put("new", new Actions.NewAction());
		map.put("open", new Actions.OpenAction());
		map.put("undo", new Actions.HistoryAction(true));
		map.put("redo", new Actions.HistoryAction(false));
		map.put("selectVertices", mxGraphActions.getSelectVerticesAction());
		map.put("selectEdges", mxGraphActions.getSelectEdgesAction());

		return map;
	}

}
