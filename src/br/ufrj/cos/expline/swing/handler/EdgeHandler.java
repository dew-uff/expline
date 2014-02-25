/**
 * $Id: EdgeHandler.java,v 1.1 2012/11/15 13:26:44 gaudenz Exp $
 * Copyright (c) 2008-2012, JGraph Ltd
 */
package br.ufrj.cos.expline.swing.handler;

import br.ufrj.cos.expline.analysis.GraphStructure;

import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxEdgeHandler;
import com.mxgraph.view.mxCellState;

/**
 *
 */
public class EdgeHandler extends mxEdgeHandler
{

	/**
	 * 
	 * @param graphComponent
	 * @param state
	 */
	public EdgeHandler(mxGraphComponent graphComponent, mxCellState state)
	{
		super(graphComponent, state);
	}


	/**
	 * Returns the error message or an empty string if the connection for the
	 * given source target pair is not valid. Otherwise it returns null.
	 */
	public String validateConnection(Object source, Object target)
	{
		
		//ExpLine-begin
		mxCell src = (mxCell) source;
		mxCell trg = (mxCell) target;
		mxCell edge =  (mxCell) state.getCell();
		
		if(edge.getStyle().contains("WorkflowEdge")){
			if(src != null){
				if (src.getStyle().contains("Variant"))
					return "";
			}
			if(trg != null){
				if (trg.getStyle().contains("Variant"))
					return "";
			}
		}
		else{
			//it's variant relationship
			
			if(src != null){
				if (!src.getStyle().contains("OptionalVariationPoint") && !src.getStyle().contains("VariationPoint"))
					return "";
			}
			if(trg != null){
				if (!trg.getStyle().contains("Variant"))
					return "";
			}
			
		}
		
		if(src != null && trg != null){
			mxAnalysisGraph aGraph = new mxAnalysisGraph();
			aGraph.setGraph(graphComponent.getGraph());
			
			if (GraphStructure.makesCycle(aGraph, (mxCell)source, (mxCell)target))
				return "Can't create cycle!";
		}
		
		//ExpLine-End

		
		return graphComponent.getGraph().getEdgeValidationError(
				state.getCell(), source, target);
	}

}
