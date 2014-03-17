package br.ufrj.cos.expline.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

public class Activity extends mxCell implements Cloneable, Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -5634462632791886594L;

	public static int INVARIANT_TYPE = 0;
	
	public static int OPTIONAL_INVARIANT_TYPE = 1;
	
	public static int VARIATION_POINT_TYPE = 2;
	
	public static int OPTIONAL_VARIATION_POINT_TYPE = 3;
	
	public static int VARIANT_TYPE = 4;
	
	private int type;
	
	private String algebraicOperator;
	
	private List<RelationSchema> inputRelationsSchemas;
	
	private RelationSchema outputRelationSchema;
	
	
	public Activity(Object value, mxGeometry geometry, String style, int type)
	{
		super(value, geometry, style);
		
		this.type = type;
		this.algebraicOperator = "map";
		
		this.inputRelationsSchemas = new ArrayList<RelationSchema>();
		this.outputRelationSchema = new RelationSchema();
	}
	
	public Activity(Object value, mxGeometry geometry, int type)
	{
		super(value, geometry, "");
		
		this.type = type;
		this.algebraicOperator = "map";
		
		setStyle(type);
		
		this.inputRelationsSchemas = new ArrayList<RelationSchema>();
		this.outputRelationSchema = new RelationSchema();
	}
	
	
	public Activity() {
		// TODO Auto-generated constructor stub
		this.inputRelationsSchemas = new ArrayList<RelationSchema>();
		this.outputRelationSchema = new RelationSchema();
	}


	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getAlgebraicOperator() {
		return algebraicOperator;
	}
	
	public void setAlgebraicOperator(String algebraicOperator) {
		this.algebraicOperator = algebraicOperator;
	}


	public List<RelationSchema> getInputRelationsSchemas() {
		return inputRelationsSchemas;
	}


	public void setInputRelationsSchemas(List<RelationSchema> inputRelationsSchemas) {
		this.inputRelationsSchemas = inputRelationsSchemas;
	}
	
	public void clearInputRelationsSchemas() {
		this.inputRelationsSchemas.clear();
	}

	public RelationSchema getOutputRelationSchema() {
		return outputRelationSchema;
	}


	public void setOutputRelationSchema(RelationSchema outputRelationSchema) {
		this.outputRelationSchema = outputRelationSchema;
	}


	public void addInputRelationSchema(RelationSchema relationSchema) {
		this.inputRelationsSchemas.add(relationSchema);
		
	}
	
	public void setStyle(int type){
		
		if (type == INVARIANT_TYPE){
			style = "rounded=1;fillColor=#E4E9AF;gradientColor=#E4E9AF;strokeWidth=3;strokeColor=#000000;fontSize=22;fontColor=#000000";
		}
		else
		if (type == OPTIONAL_INVARIANT_TYPE){
			style = "rounded=1;dashed=1;fillColor=#E4E9AF;gradientColor=#E4E9AF;strokeWidth=3;strokeColor=#000000;fontSize=22;fontColor=#000000";
		}
		else
		if (type == VARIATION_POINT_TYPE){
			style = "rectangle;shape=doubleRectangle;rounded=1;fillColor=#E4E9AF;gradientColor=#E4E9AF;strokeWidth=3;strokeColor=#000000;fontSize=22;fontColor=#000000";
		}
		else
		if (type == OPTIONAL_VARIATION_POINT_TYPE){
			style = "rectangle;shape=doubleRectangle;dashed=1;rounded=1;fillColor=#E4E9AF;gradientColor=#E4E9AF;strokeWidth=3;strokeColor=#000000;fontSize=22;fontColor=#000000";
		}
		else
		if (type == VARIANT_TYPE){
			style = "strokeWidth=5;fillColor=#FF0033;fillColor=#C5D4E1;gradientColor=#C5D4E1;strokeWidth=3;strokeColor=#000000;fontSize=22;fontColor=#000000";
		}
		
	}
	
	
}
