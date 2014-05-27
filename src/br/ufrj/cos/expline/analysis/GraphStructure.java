/**
 * $Id: GraphStructure.java,v 1.3 2012/11/21 14:16:01 mate Exp $
 * Copyright (c) 2012, JGraph Ltd
 */
package br.ufrj.cos.expline.analysis;

import java.util.ArrayList;
import java.util.Arrays;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Port;

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
		//TODO: Criar uma conexão a porta de saída e não com a entrada. (Vai funcionar só com atividades com uma porta de entrada e saída.) (Não vai funcionar... Tem que fazer pra todas atividades)
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
			
			if(temp instanceof Activity){
				Activity actv = (Activity) temp;
				
				for (int j = 0; j < actv.getChildCount(); j++) {
					Port port1 = (Port) actv.getChildAt(j);
					
					if(port1.getType() == Port.INPUT_TYPE){
						for (int k = 0; k < actv.getChildCount(); k++) {
							Port port2 = (Port) actv.getChildAt(k);
							if(port2.getType() == Port.OUTPUT_TYPE){
								Object edgeTemp = graph.createEdge(parent, null, "", port1, port2, "");
								((mxCell)edgeTemp).setSource(port1);
								((mxCell)edgeTemp).setTarget(port2);
								cellList.add(edgeTemp);
								break;
							}
						}
					}
				}
				
//				for (Port inputPort : actv.getInputPorts()) {
//					Object edgeTemp = graph.createEdge(parent, null, "", inputPort, actv.getOutputPort(), "");
//					
//					cellList.add(edgeTemp);
//				} 
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