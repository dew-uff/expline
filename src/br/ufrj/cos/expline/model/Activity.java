package br.ufrj.cos.expline.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxResources;

public class Activity extends mxCell implements Cloneable, Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -5634462632791886594L;

	public static int INVARIANT_TYPE = 0;
	
	public static int OPTIONAL_INVARIANT_TYPE = 1;
	
	public static int VARIATION_POINT_TYPE = 2;
	
	public static int OPTIONAL_VARIATION_POINT_TYPE = 3;
	
	public static int VARIANT_TYPE = 4;
	
	private int type;
	
	private String algebraicOperator;
	
	
	public Activity(Object value, mxGeometry geometry, String style, int type)
	{
		super(value, geometry, style);
		
		this.type = type;
		this.algebraicOperator = mxResources.get("map");
		
		//this.initializeDefaultPorts();
	}
	
	public Activity(Object value, mxGeometry geometry, int type)
	{
		super(value, geometry, "");
		
		this.type = type;
		this.algebraicOperator = mxResources.get("map");
		
		setStyle(type);
		
		//this.initializeDefaultPorts();
	}
	
	
	public Activity() {
		// TODO Auto-generated constructor stub
		
		this.algebraicOperator = mxResources.get("map");
		
		//this.initializeDefaultPorts();
	}


	public void initializeDefaultPorts(){
		
		if(type != Activity.VARIANT_TYPE){
			
			Port inputPort = new Port(Port.INPUT_TYPE);
				
			Port outputPort = new Port(Port.OUTPUT_TYPE);
			
			this.addInputPort(inputPort);
			this.setOutputPort(outputPort);
			
			
			if(algebraicOperator.equals(mxResources.get("join"))){
				Port inputPort2 = new Port(Port.INPUT_TYPE);
				
				this.addInputPort(inputPort2);
			}
			
		}
		
	}
	
	public void refreshPortsDefinition(){
		
		if(!algebraicOperator.equals(mxResources.get("join"))){
			
			if(getInputPorts().size() > 1){
				
				removeInputPort(getInputPort(1));
				
				getInputPort(0).getGeometry().setY(0.5);
			}			
		}
		else{
			
			if(getInputPorts().size() == 1){
				getInputPort(0).getGeometry().setY(0.3);
				
				Port inputPort2 = new Port(Port.INPUT_TYPE);
				
				inputPort2.getGeometry().setY(0.7);
				
				this.addInputPort(inputPort2);
			}
		}
		
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getAlgebraicOperator() {
		return algebraicOperator;
	}
	
	public void setAlgebraicOperator(String algebraicOperator) {
		this.algebraicOperator = algebraicOperator;
	}


	public List<Port> getInputPorts() {
		
		
		List<Port> inputPorts = new ArrayList<Port>();
		
		for (int i = 0; i < this.getChildCount(); i++) {
			Port port = (Port) this.getChildAt(i);
			
			if(port.getType() == Port.INPUT_TYPE){
				inputPorts.add(port);
			}

		}
		
		return inputPorts;
	}
	
	public Port getInputPort (int position){
		return getInputPorts().get(position);
	}
	
	public void addInputPort(Port port){
		
		List<Port> inputPorts = getInputPorts();
		
		for (Port port2 : inputPorts) {
			if(port2.equals(port))
				return;
		}
		
		this.insert(port);
	}
	
	public void removeInputPort(Port port){
		remove(port);
	}
	
	public Port getOutputPort (){
		
		for (int i = 0; i < this.getChildCount(); i++) {
			Port port = (Port) this.getChildAt(i);
			
			if(port.getType() == Port.OUTPUT_TYPE){
				return port;
			}

		}
		
		return null;
	}
	
	public void setOutputPort(Port port){
		
		Port port_temp = null;
		
		for (int i = 0; i < this.getChildCount(); i++) {
			port_temp = (Port) this.getChildAt(i);
			
			if(port_temp.getType() == Port.OUTPUT_TYPE){
				
				if(port_temp.equals(port)){
					return;
				}
				else{
					removeOutputPort(port_temp);
					this.insert(port);
				}
				
				return;
			}

		}
		
		this.insert(port);

	}
	
	public void removeOutputPort(Port port){
		remove(port);
	}
	
	public void setStyle(int type){
		
		if (type == INVARIANT_TYPE){
			style = "rounded=1;fillColor=#E4E9AF;gradientColor=#E4E9AF;strokeWidth=3;strokeColor=#000000;fontSize=22;fontColor=#000000";
		}
		else
		if (type == OPTIONAL_INVARIANT_TYPE){
			style = "rounded=1;dashed=1;fillColor=#E4E9AF;gradientColor=#E4E9AF;strokeWidth=3;strokeColor=#000000;fontSize=22;fontColor=#000000";
		}
		else
		if (type == VARIATION_POINT_TYPE){
			style = "rectangle;shape=doubleRectangle;rounded=1;fillColor=#E4E9AF;gradientColor=#E4E9AF;strokeWidth=3;strokeColor=#000000;fontSize=22;fontColor=#000000";
		}
		else
		if (type == OPTIONAL_VARIATION_POINT_TYPE){
			style = "rectangle;shape=doubleRectangle;dashed=1;rounded=1;fillColor=#E4E9AF;gradientColor=#E4E9AF;strokeWidth=3;strokeColor=#000000;fontSize=22;fontColor=#000000";
		}
		else
		if (type == VARIANT_TYPE){
			style = "strokeWidth=5;fillColor=#FF0033;fillColor=#C5D4E1;gradientColor=#C5D4E1;strokeWidth=3;strokeColor=#000000;fontSize=22;fontColor=#000000";
		}
		
	}	
}
