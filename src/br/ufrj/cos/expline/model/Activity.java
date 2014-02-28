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

	private String type;
	
	private String algebraicOperator;
	
	private List<RelationSchema> inputRelationsSchemas;
	
	private RelationSchema outputRelationSchema;
	
	
	public Activity(Object value, mxGeometry geometry, String style)
	{
		super(value, geometry, style);
		
		type = "invariant";
		algebraicOperator = "map";
		
		inputRelationsSchemas = new ArrayList<RelationSchema>();
		outputRelationSchema = new RelationSchema();
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
	
	
	
}
