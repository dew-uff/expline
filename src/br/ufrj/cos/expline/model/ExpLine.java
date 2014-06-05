package br.ufrj.cos.expline.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mxgraph.model.mxGraphModel;

/**
 * A graph that creates new edges from a given template edge.
 */
public class ExpLine extends mxGraphModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5685023531009138220L;
	
	private List<Rule> rules;
	
	
	public ExpLine(){
		rules = new ArrayList<Rule>();
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	
	public boolean addRule(Rule rule) {
		return rules.add(rule);
	}
	
	public boolean removeRule(Rule rule) {
		return rules.remove(rule);
	}
	
	public void clearRules() {
		rules.clear();
	}

	@Override
	protected Object cloneCell(Object cell, Map<Object, Object> mapping,
			boolean includeChildren) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Object obj =  super.cloneCell(cell, mapping, includeChildren);
		
		if(obj instanceof Activity){
			Activity actv = (Activity) obj;
			
			int j = 0;
			
			for (int i = 0; i < actv.getChildCount(); i++) {
				Port port = (Port) actv.getChildAt(i);
				
				printPort(port);
				
				if(port.getType() == Port.INPUT_TYPE){printPort(actv.getInputPorts().get(j));
					port.setRelationSchema(actv.getInputPorts().get(j).getRelationSchema());
					actv.getInputPorts().set(j, port);
					j++;
				}
				else
				if(port.getType() == Port.OUTPUT_TYPE)
					actv.setOutputPort(port);
			}
		}
		
		return obj;
	}
	
	
	
	@Override
	protected void restoreClone(Object clone, Object cell,
			Map<Object, Object> mapping) {
		// TODO Auto-generated method stub
		super.restoreClone(clone, cell, mapping);
		
		if(clone instanceof Activity){
			Activity actv = (Activity) clone;
			
			int j = 0;
			
			for (int i = 0; i < actv.getChildCount(); i++) {
				Port port = (Port) actv.getChildAt(i);
				
				
				if(port.getType() == Port.INPUT_TYPE){
					port.setRelationSchema(actv.getInputPorts().get(j).getRelationSchema());
					actv.getInputPorts().set(j, port);
					j++;
				}
				else
				if(port.getType() == Port.OUTPUT_TYPE)
					actv.setOutputPort(port);
			}
			
			
			Activity actv2 = (Activity) cell;
			
			j = 0;
			
			for (int i = 0; i < actv2.getChildCount(); i++) {
				Port port = (Port) actv2.getChildAt(i);
				
				
				if(port.getType() == Port.INPUT_TYPE){
					port.setRelationSchema(actv2.getInputPorts().get(j).getRelationSchema());
					actv2.getInputPorts().set(j, port);
					j++;
				}
				else
				if(port.getType() == Port.OUTPUT_TYPE)
					actv2.setOutputPort(port);
			}
		}
	}

	public void printPort(Port p){
		
		List<RelationSchemaAttribute> attr = p.getRelationSchema().getAttributes();
		
		if(!attr.isEmpty())
			System.out.println(p.getRelationSchema().getAttributes().get(0).getType());
	}

	
	
}
