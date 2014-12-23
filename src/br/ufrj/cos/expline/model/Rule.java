package br.ufrj.cos.expline.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Rule implements Cloneable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 334751913464640344L;
	
	public final static int OPERATION_OR = 0;
	
	public final static int OPERATION_AND = 1;
	
	private String name;
	
	private Activity conditionElement;
	
	private boolean conditionElementOperationSelection;
	
	private int implicationOperation;
	
	private Map<Activity, Boolean> implicationElements;
	
	
	public Rule(){
		name = "";	
		implicationElements = new HashMap<Activity, Boolean>();
	}
	
	public void addImplicationElement(Activity actv, boolean selected){
		
		implicationElements.put(actv, selected);
	}
	
	public void addConditionElement(Activity actv, boolean selected){
		
		conditionElement = actv;
		conditionElementOperationSelection = selected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getImplicationOperation() {
		return implicationOperation;
	}

	public void setImplicationOperation(int implicationOperation) {
		this.implicationOperation = implicationOperation;
	}

	public Activity getConditionElement() {
		return conditionElement;
	}

	public void setConditionElement(Activity conditionElement) {
		this.conditionElement = conditionElement;
	}

	public boolean isConditionElementOperationSelection() {
		return conditionElementOperationSelection;
	}

	public void setConditionElementOperationSelection(
			boolean conditionElementOperationSelection) {
		this.conditionElementOperationSelection = conditionElementOperationSelection;
	}

	public Map<Activity, Boolean> getImplicationElements() {
		return implicationElements;
	}

	public void setImplicationElements(Map<Activity, Boolean> implicationElements) {
		this.implicationElements = implicationElements;
	}
	
	
	
	
}
