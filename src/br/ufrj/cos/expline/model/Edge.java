package br.ufrj.cos.expline.model;

import java.io.Serializable;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

public class Edge extends mxCell implements Cloneable, Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -5634462632791886594L;

	public static int WORKFLOW_TYPE = 0;
	
	public static int VARIANT_RELATIONSHIP_TYPE = 1;
	
	private int type;
	
	
	public Edge(Object value, mxGeometry geometry, String style, int type)
	{
		super(value, geometry, style);
		
		this.type = type;
		this.edge = true;
	}
	
	public Edge(Object value, mxGeometry geometry, int type)
	{
		super();
		
		setValue(value);
		setGeometry(geometry);
		changeType(type);
		
		this.edge = true;
		
	}
	
	public Edge(){
		
	}

	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void changeType(int newType){
		
		if (newType == WORKFLOW_TYPE){
//			style = "noEdgeStyle=1;horizontal;strokeWidth=8;strokeColor=#000000";
			style = "strokeWidth=5;startArrow=none;endArrow=none;strokeColor=#000000";
//			style = "strokeWidth=5;straight;endArrow=none;strokeColor=#000000";
			type = newType;
		}
		else
		if (newType == VARIANT_RELATIONSHIP_TYPE){
//			style = "arrow;strokeWidth=3;strokeColor=#000000";
			style = "vertical;strokeWidth=5;startArrow=none;endArrow=none;strokeColor=#000000";
			type = newType;
		}
	}
	
	
}
