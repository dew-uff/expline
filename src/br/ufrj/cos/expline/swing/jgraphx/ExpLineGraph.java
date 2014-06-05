package br.ufrj.cos.expline.swing.jgraphx;

import br.ufrj.cos.expline.model.Edge;
import br.ufrj.cos.expline.model.ExpLine;
import br.ufrj.cos.expline.model.Port;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.view.mxGraph;

/**
 * A graph that creates new edges from a given template edge.
 */
public class ExpLineGraph extends mxGraph
{
	/**
	 * Holds the edge to be used as a template for inserting new edges.
	 */
	protected Object edgeTemplate;

	/**
	 * 
	 * 
	 */
	public ExpLineGraph()
	{
		
		super(new ExpLine());
		setMultigraph(false);
		
		Edge edge = new Edge("", new mxGeometry(), Edge.WORKFLOW_TYPE);
		edge.getGeometry().setRelative(true);
		
		setEdgeTemplate(edge);
		
	}

	/**
	 * Sets the edge template to be used to inserting edges.
	 */
	public void setEdgeTemplate(Object template)
	{
		edgeTemplate = template;
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
		if (edgeTemplate != null)
		{
			mxCell edge = (mxCell) cloneCells(new Object[] { edgeTemplate })[0];
			edge.setId(id);

			return edge;
		}

		return super.createEdge(parent, id, value, source, target, style);
	}

	@Override
	public boolean isCellEditable(Object cell) {
		
		mxCell cell_ = (mxCell) cell;
		if(cell != null && cell_.isVertex())
			return super.isCellEditable(cell);
		else
			return false;
	}

	@Override
	public boolean isCellDeletable(Object cell) {
		// TODO Auto-generated method stub
		
		if (cell instanceof Port)
			return false;
		else
			return super.isCellDeletable(cell);
	}

	@Override
	public boolean isCellSelectable(Object cell) {
		// TODO Auto-generated method stub
		
		if (cell instanceof Port)
			return false;
		else
		return super.isCellSelectable(cell);
	}

	@Override
	public boolean isCellResizable(Object cell) {
		return false;
	}

}