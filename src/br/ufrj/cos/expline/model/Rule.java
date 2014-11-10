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
	
	private List<Expression> implications;
	
	public Rule(){
		name = "";
		conditions = new ArrayList<Expression>();
		implications = new ArrayList<Expression>();
	}
	
	public List<Expression> getConditions() {
		return conditions;
	}

	public void setConditions(List<Expression> conditions) {
		this.conditions = conditions;
	}

	public List<Expression> getImplications() {
		return implications;
	}

	public void setImplications(List<Expression> implications) {
		this.implications = implications;
	}
	
	public boolean addCondition(Expression expression){
		return conditions.add(expression);
	}
	
	public boolean addImplication(Expression expression){
		return implications.add(expression);
	}
	
	public void clearConditions(){
		conditions.clear();
		
	}
	
	public void clearImplications(){
		implications.clear();
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
