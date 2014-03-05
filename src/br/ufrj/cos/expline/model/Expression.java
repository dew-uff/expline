package br.ufrj.cos.expline.model;

import java.io.Serializable;
import java.util.List;

public class Expression implements Cloneable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 334751913464640344L;
	
	public final static int SELECTED_ANY = 0;
	
	public final static int SELECTED_SOME = 1;
	
	public final static int SELECTED_ALL = 2;
	
	public final static int NOT_SELECTED_ANY = 3;
	
	public final static int NOT_SELECTED_SOME = 4;
	
	public final static int NOT_SELECTED_ALL = 5;
	
	private int type;
	
	private List<Activity> activities;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	
	
	
	
	

}
