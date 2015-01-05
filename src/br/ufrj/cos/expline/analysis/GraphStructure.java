/**
 * $Id: GraphStructure.java,v 1.3 2012/11/21 14:16:01 mate Exp $
 * Copyright (c) 2012, JGraph Ltd
 */
package br.ufrj.cos.expline.analysis;

import java.util.ArrayList;
import java.util.Arrays;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Edge;
import br.ufrj.cos.expline.model.Port;

import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.analysis.mxGraphStructure;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
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
		
		
		for (int i = 0; i < cells.length; i++) {
			mxCell temp = (mxCell)cells[i];
			
			if(temp instanceof Edge){
				Edge edge_temp = (Edge) temp;
				
				if(!(edge_temp.getSource() instanceof Activity) && !(edge_temp.getTarget() instanceof Activity)){
				
					Port port1 = (Port) edge_temp.getSource();
					Port port2 = (Port) edge_temp.getTarget();
					
					Activity actv1 = (Activity) port1.getParent();
					Activity actv2 = (Activity) port2.getParent();
					
					Object new_edge = graph.createEdge(parent, null, "", actv1, actv2, "");
					
					((mxCell)new_edge).setSource(actv1);
					((mxCell)new_edge).setTarget(actv2);
					
					cellList.add(new_edge);
				}
			}
			
		}
		
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
			leaf[0] = getUndirectedLeaf(aGraphCopy);

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
	
	private static Object getUndirectedLeaf(mxAnalysisGraph aGraph)
	{
		Object parent = aGraph.getGraph().getDefaultParent();
		Object[] vertices = aGraph.getChildVertices(parent);
		int vertexNum = vertices.length;
		Object currVertex;

		for (int i = 0; i < vertexNum; i++)
		{
			currVertex = vertices[i];
			int edgeCount = aGraph.getEdges(currVertex, parent, true, true, false, true).length;

			if (edgeCount <= 1)
			{
				return currVertex;
			}
		}

		return null;
	}
	
	/**
	 * @param aGraph
	 * @return true if the graph is connected
	 */
	public static boolean isConnected(mxAnalysisGraph aGraph)
	{
		
		mxGraph graph = aGraph.getGraph();
		mxIGraphModel model = graph.getModel();
		
		Object[] cells = model.cloneCells(aGraph.getChildCells(graph.getDefaultParent(), true, true), true);
		
		Object parent = graph.getDefaultParent();
		ArrayList<Object> cellList = new ArrayList<Object>(Arrays.asList(cells));
		
		for (int i = 0; i < cells.length; i++) {
			mxCell temp = (mxCell)cells[i];
			
			if(temp instanceof Edge){
				Edge edge_temp = (Edge) temp;
				
				if(!(edge_temp.getSource() instanceof Activity) && !(edge_temp.getTarget() instanceof Activity)){
				
					Port port1 = (Port) edge_temp.getSource();
					Port port2 = (Port) edge_temp.getTarget();
					
					Activity actv1 = (Activity) port1.getParent();
					Activity actv2 = (Activity) port2.getParent();
					
					Object new_edge = graph.createEdge(parent, null, "", actv1, actv2, "");
					
					((mxCell)new_edge).setSource(actv1);
					((mxCell)new_edge).setTarget(actv2);
					
					cellList.add(new_edge);
				}
			}
			
		}
		
		
		for (int i = 0; i < cells.length; i++) {
			mxCell temp = (mxCell)cells[i];
			
			if(temp instanceof Port){
				cellList.remove(temp);
				
				Object[] edges = graph.getEdges(temp);
				
				for (Object edge : edges) {
					cellList.remove(temp);
				}
				
			}
			
		}
		
	
		mxGraphModel modelCopy = new mxGraphModel();
		mxGraph graphCopy = new mxGraph(modelCopy);
		Object parentCopy = graphCopy.getDefaultParent();
		graphCopy.addCells(cellList.toArray());
		aGraph = new mxAnalysisGraph();
		aGraph.setGraph(graphCopy);


		
		
		Object[] vertices = aGraph.getChildVertices(aGraph.getGraph().getDefaultParent());
		int vertexNum = vertices.length;

		if (vertexNum == 0)
		{
			throw new IllegalArgumentException();
		}

		//data preparation
		int connectedVertices = 1;
		int[] visited = new int[vertexNum];
		visited[0] = 1;

		for (int i = 1; i < vertexNum; i++)
		{
			visited[i] = 0;
		}

		ArrayList<Object> queue = new ArrayList<Object>();
		queue.add(vertices[0]);

		//repeat the algorithm until the queue is empty
		while (queue.size() > 0)
		{
			//cut out the first vertex
			Object currVertex = queue.get(0);
			queue.remove(0);

			//fill the queue with neighboring but unvisited vertexes
			Object[] neighborVertices = aGraph.getOpposites(aGraph.getEdges(currVertex, null, true, true, false, true), currVertex, true,
					true);

			for (int j = 0; j < neighborVertices.length; j++)
			{
				//get the index of the neighbor vertex
				int index = 0;

				for (int k = 0; k < vertexNum; k++)
				{
					if (vertices[k].equals(neighborVertices[j]))
					{
						index = k;
					}
				}

				if (visited[index] == 0)
				{
					queue.add(vertices[index]);
					visited[index] = 1;
					connectedVertices++;
				}
			}
		}

		// if we visited every vertex, the graph is connected
		if (connectedVertices == vertexNum)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}