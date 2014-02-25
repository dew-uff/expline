package br.ufrj.cos.expline.model;

import java.io.Serializable;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

public class Activity extends mxCell implements Cloneable, Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -5634462632791886594L;

	public String type;
	
	public String algebraicOperator;
	
	public Activity(Object value, mxGeometry geometry, String style)
	{
		super(value, geometry, style);
		
		type = "invariant";
		algebraicOperator = "map";
	}
	
	
	public Activity() {
		// TODO Auto-generated constructor stub
	}


	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAlgebraicOperator() {
		return algebraicOperator;
	}
	public void setAlgebraicOperator(String algebraicOperator) {
		this.algebraicOperator = algebraicOperator;
	}
	
	
	
}
