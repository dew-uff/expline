package br.ufrj.cos.expline.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rule implements Cloneable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 334751913464640344L;
	
	private String name;
	
	private List<Expression> conditions;
	
	private List<Expression> implication;
	
	public Rule(){
		name = "";
		conditions = new ArrayList<Expression>();
		implication = new ArrayList<Expression>();
	}
	
	public List<Expression> getCondition() {
		return conditions;
	}

	public void setCondition(List<Expression> conditions) {
		this.conditions = conditions;
	}

	public List<Expression> getImplication() {
		return implication;
	}

	public void setImplication(List<Expression> implication) {
		this.implication = implication;
	}
	
	public boolean addCondition(Expression expression){
		return conditions.add(expression);
	}
	
	public boolean addImplication(Expression expression){
		return implication.add(expression);
	}
	
	public void clearConditions(){
		conditions.clear();
		
	}
	
	public void clearImplication(){
		implication.clear();
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	

}
