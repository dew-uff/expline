package br.ufrj.cos.expline.model;

import java.util.ArrayList;
import java.util.List;

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
	
	protected List<Rule> rules;
	
	
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
	

}
