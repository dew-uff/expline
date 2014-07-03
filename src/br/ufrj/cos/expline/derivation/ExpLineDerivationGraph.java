package br.ufrj.cos.expline.derivation;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Edge;
import br.ufrj.cos.expline.model.ExpLine;
import br.ufrj.cos.expline.model.Port;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

/**
 * A graph that creates new edges from a given template edge.
 */
public class ExpLineDerivationGraph extends mxGraph
{

	/**
	 * 
	 * 
	 */
	public ExpLineDerivationGraph()
	{
		
		super(new ExpLine());
		setMultigraph(false);
		
		Edge edge = new Edge("", new mxGeometry(), Edge.WORKFLOW_TYPE);
		edge.getGeometry().setRelative(true);
		
	}
	
	public Edge createWorkflowEdgeTemplate(){
		Edge edge = new Edge("", new mxGeometry(), Edge.WORKFLOW_TYPE);
		edge.getGeometry().setRelative(true);
		
		return edge;
	}
	
	public Edge createVariantRelationshipEdgeTemplate(){
		Edge edge = new Edge("", new mxGeometry(), Edge.VARIANT_RELATIONSHIP_TYPE);
		edge.getGeometry().setRelative(true);
		
		return edge;
	}
	
	// Removes the folding icon and disables any folding
	public boolean isCellFoldable(Object cell, boolean collapse)
	{
		return false;
	}

	/**
	 * Prints out some useful information about the cell in the tooltip.
	 */
	public String getToolTipForCell(Object cell)
	{
		String tip = "";

		if (getModel().isVertex(cell))
		{

			mxCell cell_ = (mxCell) cell;
			String style = cell_.getStyle();
			
			String[] key_values = style.trim().split(";");
			
			for (String key_value : key_values) {
				if(key_value.contains("algebraicOperator")){
					String albebraicOperator = key_value.split("=")[1];
					
					tip += "Algebraic Operator = " + albebraicOperator;
					
					break;
				}
			}
			
		}


		return tip;
	}

	/**
	 * Overrides the method to use the currently selected edge template for
	 * new edges.
	 * 
	 * @param expLineGraph
	 * @param parent
	 * @param id
	 * @param value
	 * @param source
	 * @param target
	 * @param style
	 * @return
	 */

	public Object createEdge(Object parent, String id, Object value,
			Object source, Object target, String style)
	{
	
		if (source instanceof Port) {
			
			mxCell edge = (mxCell) cloneCells(new Object[] { createWorkflowEdgeTemplate() })[0];
			edge.setId(id);
			
			return edge;
			
		}
		else{
			mxCell edge = (mxCell) cloneCells(new Object[] { createVariantRelationshipEdgeTemplate() })[0];
			edge.setId(id);
			
			return edge;
		}
	
	}
	
	
	@Override
	public boolean isCellEditable(Object cell) {
		
		return false;
	}
	
	

	@Override
	public boolean isCellConnectable(Object cell) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean isCellDeletable(Object cell) {
		// TODO Auto-generated method stub
		
		return false;
	}
	
	@Override
	public boolean isCellSelectable(Object cell) {
		// TODO Auto-generated method stub
		
		
		return false;

	}
	
	/**
	 * Draws the cell state with the given label onto the canvas. No
	 * children or descendants are painted here. This method invokes
	 * cellDrawn after the cell, but not its descendants have been
	 * painted.
	 * 
	 * @param canvas Canvas onto which the cell should be drawn.
	 * @param state State of the cell to be drawn.
	 * @param drawLabel Indicates if the label should be drawn.
	 */
	public void drawState(mxICanvas canvas, mxCellState state, boolean drawLabel)
	{
		Object cell = (state != null) ? state.getCell() : null;

		if (cell != null && cell != view.getCurrentRoot()
				&& cell != model.getRoot()
				&& (model.isVertex(cell) || model.isEdge(cell)))
		{
			Object obj = canvas.drawCell(state);
			Object lab = null;

			// Holds the current clipping region in case the label will
			// be clipped
			Shape clip = null;
			Rectangle newClip = state.getRectangle();

			// Indirection for image canvas that contains a graphics canvas
			mxICanvas clippedCanvas = (isLabelClipped(state.getCell())) ? canvas
					: null;

			if (clippedCanvas instanceof mxImageCanvas)
			{
				clippedCanvas = ((mxImageCanvas) clippedCanvas)
						.getGraphicsCanvas();
				// TODO: Shift newClip to match the image offset
				//Point pt = ((mxImageCanvas) canvas).getTranslate();
				//newClip.translate(-pt.x, -pt.y);
			}

			if (clippedCanvas instanceof mxGraphics2DCanvas)
			{
				Graphics g = ((mxGraphics2DCanvas) clippedCanvas).getGraphics();
				clip = g.getClip();
				
				// Ensure that our new clip resides within our old clip
				if (clip instanceof Rectangle)
				{
					g.setClip(newClip.intersection((Rectangle) clip));
				}
				// Otherwise, default to original implementation
				else
				{
					g.setClip(newClip);
				}
			}

			if (drawLabel)
			{
				Object cell_tmp = state.getCell();
				
				String label = state.getLabel();
				
				if (cell_tmp instanceof Activity) {
					Activity actv = (Activity) cell_tmp;
					
					if (actv.getType() != Activity.VARIANT_TYPE) {
						String algebraicOperator = actv.getAlgebraicOperator();
						label = label.trim() + "\n(" +algebraicOperator +")";
					}
				}
				
				

				if (label != null && state.getLabelBounds() != null)
				{
					lab = canvas.drawLabel(label, state, isHtmlLabel(cell));
				}
			}

			// Restores the previous clipping region
			if (clippedCanvas instanceof mxGraphics2DCanvas)
			{
				((mxGraphics2DCanvas) clippedCanvas).getGraphics()
						.setClip(clip);
			}

			// Invokes the cellDrawn callback with the object which was created
			// by the canvas to represent the cell graphically
			if (obj != null)
			{
				cellDrawn(canvas, state, obj, lab);
			}
		}
	}

	@Override
	public boolean isCellResizable(Object cell) {
		return false;
	}
	
	@Override
	public boolean isCellMovable(Object cell) {
		// TODO Auto-generated method stub
		
		return false;
	}

}