package br.ufrj.cos.expline.model;

import java.io.Serializable;

public class RelationSchemaAttribute implements Cloneable, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6135670901828126665L;
	
	private String name;
    private String type;

    public RelationSchemaAttribute() {
    }
    
    public RelationSchemaAttribute(String type, String name) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

}
