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
	
	private List<Port> inputPorts;
	
	private Port outputPort;
	
	
	public Activity(Object value, mxGeometry geometry, String style, int type)
	{
		super(value, geometry, style);
		
		this.type = type;
		this.algebraicOperator = mxResources.get("map");
		
		this.inputPorts = new ArrayList<Port>();
		
		this.initializeDefaultPorts();
	}
	
	public Activity(Object value, mxGeometry geometry, int type)
	{
		super(value, geometry, "");
		
		this.type = type;
		this.algebraicOperator = mxResources.get("map");
		
		setStyle(type);
		
		this.inputPorts = new ArrayList<Port>();
		
		this.initializeDefaultPorts();
	}
	
	
	public Activity() {
		// TODO Auto-generated constructor stub
		this.inputPorts = new ArrayList<Port>();
		
		this.algebraicOperator = mxResources.get("map");
		
		this.initializeDefaultPorts();
	}


	public void initializeDefaultPorts(){
		
		inputPorts.clear();
		outputPort = null;
		
		if(type != Activity.VARIANT_TYPE){
			
			Port inputPort = new Port(Port.INPUT_TYPE, this);
				
			Port outputPort = new Port(Port.OUTPUT_TYPE, this);
			
			this.addInputPort(inputPort);
			this.insert(inputPort);
			this.setOutputPort(outputPort);
			this.insert(outputPort);
			
			if(algebraicOperator.equals(mxResources.get("join"))){
				Port inputPort2 = new Port(Port.INPUT_TYPE, this);
				
				this.addInputPort(inputPort2);
			}
			
		}
		
	}
	
	public void refreshPortsDefinition(){
		
		if(!algebraicOperator.equals(mxResources.get("join"))){
			
			if(inputPorts.size() > 1){
				
				remove(inputPorts.get(1));
				
				inputPorts.remove(1);
				
				inputPorts.get(0).getGeometry().setY(0.5);
			}			
		}
		else{
			
			if(inputPorts.size() == 1){
				inputPorts.get(0).getGeometry().setY(0.3);
				
				Port inputPort2 = new Port(Port.INPUT_TYPE, this);
				
				inputPort2.getGeometry().setY(0.7);
				
				this.addInputPort(inputPort2);
				this.insert(inputPort2);
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
		return inputPorts;
	}


	public void setInputPorts(List<Port> inputPorts) {
		this.inputPorts = inputPorts;
	}
	
	public void clearInputRelationsSchemas() {
		this.inputPorts.clear();
	}

	public Port getOutputPort() {
		return outputPort;
	}


	public void setOutputPort(Port outputPort) {
		this.outputPort = outputPort;
	}


	public void addInputPort(Port inputPort) {
		this.inputPorts.add(inputPort);	
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

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Activity activity = (Activity) super.clone();
		
		for (int i = 0; i < activity.getChildCount(); i++) {
			Port port = (Port) getChildAt(i);
			
			if(port.getType() == Port.INPUT_TYPE)
				activity.getInputPorts().set(0, port);
			else
			if(port.getType() == Port.OUTPUT_TYPE)
				activity.outputPort = port;
		}
		
		return activity;
		
	}
	
	
	
}
