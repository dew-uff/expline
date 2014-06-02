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
	
	/**
	 * Returns a clone of the cell.
	 */
	public Object clone()
	{
		RelationSchema clone = new RelationSchema();

		List<RelationSchemaAttribute> cloneAttrList = new ArrayList<RelationSchemaAttribute>();
		
		for (int i = 0; i < attributes.size(); i++) {
			RelationSchemaAttribute attrClone = (RelationSchemaAttribute) attributes.get(i).clone();
			cloneAttrList.add(attrClone);
		}
		
		clone.setAttributes(cloneAttrList);

		return clone;
	}

}
