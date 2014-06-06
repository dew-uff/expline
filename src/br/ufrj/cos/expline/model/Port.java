package br.ufrj.cos.expline.model;

import java.io.Serializable;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;

public class Port extends mxCell implements Cloneable, Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -5634462632791886594L;

	public static int INPUT_TYPE = 0;
	
	public static int OUTPUT_TYPE = 1;
	
	private final int PORT_DIAMETER = 20;
	
	private final int PORT_RADIUS = PORT_DIAMETER / 2;
		
	private int type;
	
	private RelationSchema relationSchema;
	
	
	public Port(int type){
		mxGeometry g = null;
		if(type == INPUT_TYPE){
			g = new mxGeometry(0, 0.5, PORT_DIAMETER,
			PORT_DIAMETER);
		}
		else
			g = new mxGeometry(1.03, 0.5, PORT_DIAMETER,
					PORT_DIAMETER);
		
		// Because the origin is at upper left corner, need to translate to
		// position the center of port correctly
		g.setOffset(new mxPoint(-PORT_RADIUS, -PORT_RADIUS));
		g.setRelative(true);
		
		setGeometry(g);
		//setStyle("shape=ellipse;perimter=ellipsePerimeter;fillColor=#000000;gradientColor=#000000;strokeColor=#000000");
		setStyle("triangle;fillColor=#000000;gradientColor=#000000;strokeColor=#000000");
		setVertex(true);
		relationSchema = new RelationSchema();
		this.type = type;
	}


	public RelationSchema getRelationSchema() {
		return relationSchema;
	}


	public void setRelationSchema(RelationSchema relationSchema) {
		this.relationSchema = relationSchema;
	}


	public int getType() {
		return type;
	}
	
}
