package br.ufrj.cos.expline.model;

import java.io.Serializable;
import java.util.List;

public class Rule implements Cloneable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 334751913464640344L;
	
	private List<Expression> conditions;
	
	private List<Expression> implication;
	
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

}
