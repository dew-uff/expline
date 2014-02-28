package br.ufrj.cos.expline.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RelationSchema implements Cloneable, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2147157272518539849L;
	
	private List<RelationSchemaAttribute> attributes;

    public RelationSchema() {
        attributes = new ArrayList<RelationSchemaAttribute>();
    }

	public List<RelationSchemaAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<RelationSchemaAttribute> attributes) {
		this.attributes = attributes;
	}
	
	public boolean addAttribute(RelationSchemaAttribute attr){
		return attributes.add(attr);
	}

	public RelationSchemaAttribute remove(int index) {
		return attributes.remove(index);
	}

}
