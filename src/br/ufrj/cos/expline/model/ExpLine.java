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
			
			for (int i = 0; i < actv.getChildCount(); i++) {
				Port port = (Port) actv.getChildAt(i);
				
				if(port.getType() == Port.INPUT_TYPE)
					actv.getInputPorts().set(0, port);
				else
				if(port.getType() == Port.OUTPUT_TYPE)
					actv.setOutputPort(port);
			}
		}
		
		return obj;
	}
	

	
	
}
