/**
 * $Id: GraphStructure.java,v 1.3 2012/11/21 14:16:01 mate Exp $
 * Copyright (c) 2012, JGraph Ltd
 */
package br.ufrj.cos.expline.analysis;

import java.util.ArrayList;
import java.util.Arrays;

import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.analysis.mxGraphStructure;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.view.mxGraph;

public class GraphStructure extends mxGraphStructure
{

	
	public static boolean makesCycle(mxAnalysisGraph aGraph, mxCell source, mxCell target)
	{

		mxGraph graph = aGraph.getGraph();
		mxIGraphModel model = graph.getModel();
		
		//Workaround to add the request edge in the clone graph to verify that makes cycle
		Object parent = graph.getDefaultParent();
		Object edge = graph.createEdge(parent, null, "", source, target, "");

		Object[] cells = model.cloneCells(aGraph.getChildCells(graph.getDefaultParent(), true, true), true);
		
		for (int i = 0; i < cells.length; i++) {
			mxCell temp = (mxCell)cells[i];
			if(temp.getId().equals(source.getId())){
				((mxCell)edge).setSource(temp);
				break;
			}
		}
		
		for (int i = 0; i < cells.length; i++) {
			mxCell temp = (mxCell)cells[i];
			if(temp.getId().equals(target.getId())){
				((mxCell)edge).setTarget(temp);
				break;
			}
		}
		
		ArrayList<Object> cellList = new ArrayList<Object>(Arrays.asList(cells));
		cellList.add(edge);
		
		//End of workaround
		
		mxGraphModel modelCopy = new mxGraphModel();
		mxGraph graphCopy = new mxGraph(modelCopy);
		Object parentCopy = graphCopy.getDefaultParent();
		graphCopy.addCells(cellList.toArray());
		mxAnalysisGraph aGraphCopy = new mxAnalysisGraph();
		aGraphCopy.setGraph(graphCopy);
		aGraphCopy.setGenerator(aGraph.getGenerator());
		aGraphCopy.setProperties(aGraph.getProperties());

		Object[] leaf = new Object[1];

		do
		{
			leaf[0] = getDirectedLeaf(aGraphCopy, parentCopy);

			if (leaf[0] != null)
			{
				graphCopy.removeCells(leaf);
			}
		}
		while (leaf[0] != null);

		int vertexNum = aGraphCopy.getChildVertices(parentCopy).length;

		if (vertexNum > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
}