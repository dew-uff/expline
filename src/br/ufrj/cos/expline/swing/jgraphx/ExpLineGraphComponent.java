package br.ufrj.cos.expline.swing.jgraphx;

import java.awt.Color;
import java.awt.Point;

import org.w3c.dom.Document;

import br.ufrj.cos.expline.swing.ExpLineEditor;
import br.ufrj.cos.expline.swing.jgraphx.handler.ConnectionHandler;
import br.ufrj.cos.expline.swing.jgraphx.handler.EdgeHandler;
import br.ufrj.cos.expline.swing.jgraphx.handler.ElbowEdgeHandler;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.swing.handler.mxVertexHandler;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxEdgeStyle.mxEdgeStyleFunction;

/**
 * 
 */
public class ExpLineGraphComponent extends mxGraphComponent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6833603133512882012L;

	/**
	 * 
	 * @param graph
	 */
	public ExpLineGraphComponent(mxGraph graph)
	{
		super(graph);

		// Sets switches typically used in an editor
		setPageVisible(false);
		setGridVisible(false);
		setTripleBuffered(true);
		super.createHandlers();
		setAntiAlias(true);
		setToolTips(true);
		
		getConnectionHandler().setCreateTarget(false);
		// Loads the defalt stylesheet from an external file
		mxCodec codec = new mxCodec();
		Document doc = mxUtils.loadDocument(ExpLineEditor.class.getResource(
				"/resources/default-style.xml")
				.toString());
		codec.decode(doc.getDocumentElement(), graph.getStylesheet());

		// Sets the background to white
		getViewport().setOpaque(true);
		getViewport().setBackground(Color.WHITE);
	}

	/**
	 * Overrides drop behaviour to set the cell style if the target
	 * is not a valid drop target and the cells are of the same
	 * type (eg. both vertices or both edges). 
	 */
	public Object[] importCells(Object[] cells, double dx, double dy,
			Object target, Point location)
	{
		if (target == null && cells.length == 1 && location != null)
		{
			target = getCellAt(location.x, location.y);

			if (target instanceof mxICell && cells[0] instanceof mxICell)
			{
				mxICell targetCell = (mxICell) target;
				mxICell dropCell = (mxICell) cells[0];

				if (targetCell.isVertex() == dropCell.isVertex()
						|| targetCell.isEdge() == dropCell.isEdge())
				{
					mxIGraphModel model = graph.getModel();
					model.setStyle(target, model.getStyle(cells[0]));
					graph.setSelectionCell(target);

					return null;
				}
			}
		}

		return super.importCells(cells, dx, dy, target, location);
	}

	@Override
	protected ConnectionHandler createConnectionHandler() {
		// TODO Auto-generated method stub
		return new ConnectionHandler(this);
	}

	@Override
	public mxCellHandler createHandler(mxCellState state) {
		// TODO Auto-generated method stub
		if (graph.getModel().isVertex(state.getCell()))
		{
			return new mxVertexHandler(this, state);
		}
		else if (graph.getModel().isEdge(state.getCell()))
		{
			mxEdgeStyleFunction style = graph.getView().getEdgeStyle(state,
					null, null, null);

			if (graph.isLoop(state) || style == mxEdgeStyle.ElbowConnector
					|| style == mxEdgeStyle.SideToSide
					|| style == mxEdgeStyle.TopToBottom)
			{
				return new ElbowEdgeHandler(this, state);
			}

			return new EdgeHandler(this, state);
		}

		return new mxCellHandler(this, state);
	}
}