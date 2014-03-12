package br.ufrj.cos.expline.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Expression implements Cloneable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 334751913464640344L;
	
	public final static int OPERATION_SELECTED = 0;
	
	public final static int OPERATION_NOT_SELECTED = 1;
	
	public final static int MODIFIER_ANY = 2;
	
	public final static int MODIFIER_SOME = 3;
	
	public final static int MODIFIER_ALL = 4;
	
	public final static int FILTER_NONE = 5;
	
	public final static int FILTER_VARIANT = 6;
	
	public final static int FILTER_OPTIONAL = 7;
	
	private int operation;
	
	private int modifier;
	
	private int filter;
	
	private List<Activity> activities;
	
	public Expression(){
		activities = new ArrayList<Activity>();
		
		operation = OPERATION_SELECTED;
		modifier = MODIFIER_ALL;
		filter = FILTER_NONE;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	
	public void addActivity(Activity activity) {
		this.activities.add(activity);
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public int getModifier() {
		return modifier;
	}

	public void setModifier(int modifier) {
		this.modifier = modifier;
	}

	public int getFilter() {
		return filter;
	}

	public void setFilter(int filter) {
		this.filter = filter;
	}
	
	
	
	
	
	

}
