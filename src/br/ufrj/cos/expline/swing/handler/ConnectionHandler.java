/**
 * $Id: ConnectionHandler.java,v 1.1 2012/11/15 13:26:44 gaudenz Exp $
 * Copyright (c) 2008, Gaudenz Alder
 */
package br.ufrj.cos.expline.swing.handler;

import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import br.ufrj.cos.expline.analysis.GraphStructure;

import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellMarker;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

/**
 * Connection handler creates new connections between cells. This control is used to display the connector
 * icon, while the preview is used to draw the line.
 * 
 * mxEvent.CONNECT fires between begin- and endUpdate in mouseReleased. The <code>cell</code>
 * property contains the inserted edge, the <code>event</code> and <code>target</code> 
 * properties contain the respective arguments that were passed to mouseReleased.
 */
public class ConnectionHandler extends mxConnectionHandler
{

	public ConnectionHandler(mxGraphComponent graphComponent) {
		super(graphComponent);
		
		marker = new mxCellMarker(graphComponent)
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 103433247310526381L;

			// Overrides to return cell at location only if valid (so that
			// there is no highlight for invalid cells that have no error
			// message when the mouse is released)
			protected Object getCell(MouseEvent e)
			{
				Object cell = super.getCell(e);

				if (isConnecting())
				{
					if (source != null)
					{
						error = validateConnection(source.getCell(), cell);

						
						//ExpLine-Begin: This code was commented to allow the components highlight						
//						if (error != null && error.length() == 0)
//						{
//							cell = null;
//
//							// Enables create target inside groups
//							if (createTarget)
//							{
//								error = null;
//							}
//						}
						//ExpLine-End
					}
				}
				else if (!isValidSource(cell))
				{
					cell = null;
				}

				return cell;
			}

			// Sets the highlight color according to isValidConnection
			protected boolean isValidState(mxCellState state)
			{
				if (isConnecting())
				{
					return error == null;
				}
				else
				{
					return super.isValidState(state);
				}
			}

			// Overrides to use marker color only in highlight mode or for
			// target selection
			protected Color getMarkerColor(MouseEvent e, mxCellState state,
					boolean isValid)
			{
				return (isHighlighting() || isConnecting()) ? super
						.getMarkerColor(e, state, isValid) : null;
			}

			// Overrides to use hotspot only for source selection otherwise
			// intersects always returns true when over a cell
			protected boolean intersects(mxCellState state, MouseEvent e)
			{
				if (!isHighlighting() || isConnecting())
				{
					return true;
				}

				return super.intersects(state, e);
			}
		};

		marker.setHotspotEnabled(true);

	}
	
	//ExpLine-Begin	
	
	public boolean isValidExpLineConnection(mxCell source, mxCell target)
	{

		if(target != null && source != target) {	
			if (source.getStyle().contains("Variant") && !(target.getStyle().contains("VariationPoint") || target.getStyle().contains("OptionalVariationPoint"))){
				return false;
			}
			
			if(target.getStyle().contains("Variant") && !(source.getStyle().contains("VariationPoint") || source.getStyle().contains("OptionalVariationPoint"))){
				return false;
			}
			
			
			mxAnalysisGraph aGraph = new mxAnalysisGraph();
			aGraph.setGraph(graphComponent.getGraph());
			
			if (GraphStructure.makesCycle(aGraph, source, target))
				return false;
			else
				return true;
		}
		
		return false;
	}
	
	public void defineEdgeType(){
		
		mxCell previewState = (mxCell) connectPreview.getPreviewState().getCell();
		
		
		mxCell src = (mxCell) previewState.getTerminal(true);
		mxCell trg = (mxCell) previewState.getTerminal(false);
		
		previewState.setStyle("strokeWidth=8;strokeColor=#000066");
		
		if(trg != null){
			if (src.getStyle().contains("Variant")){
				previewState.setStyle("arrow;strokeWidth=8");
				previewState.setSource(trg);
				previewState.setTarget(src);
			}
			else
				if(trg.getStyle().contains("Variant")){
					previewState.setStyle("arrow;strokeWidth=8");
				}
		}
	}

	
	//ExpLine-End

	@Override
	public String validateConnection(Object source, Object target) {
		if (target == null && createTarget)
		{
			return null;
		}

		if (!isValidTarget(target))
		{
			return "";
		}
		
		//ExpLine-Begin
		if(!isValidExpLineConnection((mxCell)source, (mxCell)target))
		{	
			return "";
		}
		//ExpLine-End

		return graphComponent.getGraph().getEdgeValidationError(
				connectPreview.getPreviewState().getCell(), source, target);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (isActive())
		{
			if (error != null)
			{
				if (error.length() > 0)
				{
					JOptionPane.showMessageDialog(graphComponent, error);
				}
			}
			else if (first != null)
			{
				mxGraph graph = graphComponent.getGraph();
				double dx = first.getX() - e.getX();
				double dy = first.getY() - e.getY();
	
				if (connectPreview.isActive()
						&& (marker.hasValidState() || isCreateTarget() || graph
								.isAllowDanglingEdges()))
				{
					graph.getModel().beginUpdate();
	
					try
					{
						Object dropTarget = null;
	
						if (!marker.hasValidState() && isCreateTarget())
						{
							Object vertex = createTargetVertex(e, source.getCell());
							dropTarget = graph.getDropTarget(
									new Object[] { vertex }, e.getPoint(),
									graphComponent.getCellAt(e.getX(), e.getY()));
	
							if (vertex != null)
							{
								// Disables edges as drop targets if the target cell was created
								if (dropTarget == null
										|| !graph.getModel().isEdge(dropTarget))
								{
									mxCellState pstate = graph.getView().getState(
											dropTarget);
	
									if (pstate != null)
									{
										mxGeometry geo = graph.getModel()
												.getGeometry(vertex);
	
										mxPoint origin = pstate.getOrigin();
										geo.setX(geo.getX() - origin.getX());
										geo.setY(geo.getY() - origin.getY());
									}
								}
								else
								{
									dropTarget = graph.getDefaultParent();
								}
	
								graph.addCells(new Object[] { vertex }, dropTarget);
							}
	
							// FIXME: Here we pre-create the state for the vertex to be
							// inserted in order to invoke update in the connectPreview.
							// This means we have a cell state which should be created
							// after the model.update, so this should be fixed.
							mxCellState targetState = graph.getView().getState(
									vertex, true);
							connectPreview.update(e, targetState, e.getX(),
									e.getY());
						}
	
						//Expline-Begin: Definir o tipo de aresta a ser criado no diagrama
						
						defineEdgeType();
						
						//Expline-End
						
						Object cell = connectPreview.stop(
								graphComponent.isSignificant(dx, dy), e);
						
	
						if (cell != null)
						{
							graphComponent.getGraph().setSelectionCell(cell);
							eventSource.fireEvent(new mxEventObject(
									mxEvent.CONNECT, "cell", cell, "event", e,
									"target", dropTarget));
						}
	
						e.consume();
					}
					finally
					{
						graph.getModel().endUpdate();
					}
				}
			}
		}

		reset();

	}
	
	
	

}
