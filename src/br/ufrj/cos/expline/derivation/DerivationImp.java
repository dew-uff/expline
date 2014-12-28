package br.ufrj.cos.expline.derivation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Edge;
import br.ufrj.cos.expline.model.ExpLine;
import br.ufrj.cos.expline.model.Port;
import br.ufrj.cos.expline.model.Rule;
import br.ufrj.cos.expline.model.Workflow;
import br.ufrj.cos.lens.odyssey.tools.charon.Charon;
import br.ufrj.cos.lens.odyssey.tools.charon.CharonAPI;
import br.ufrj.cos.lens.odyssey.tools.charon.CharonException;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;

public class DerivationImp implements Derivation {

	private mxGraphComponent derivationGraphComponent;
	
	private Charon charon;
		
	private Map<String, Activity> explineActivities;
	
	private Map<String, Boolean> currentState;

		
	public DerivationImp(mxGraphComponent derivationGraphComponent) {
		// TODO Auto-generated constructor stub
		this.derivationGraphComponent = derivationGraphComponent;
		
		explineActivities = new HashMap<String, Activity>();
		
		currentState = new HashMap<String, Boolean>();
		
		try {
			
			InputStream is = new FileInputStream(new File("doc/ExpLine.pl"));
			
	        byte[] info = new byte[is.available()];
	        is.read(info);
	        String theory = new String(info);
			
			
			ExpLine model = (ExpLine) derivationGraphComponent.getGraph().getModel();

			
			String rules = createRules(model.getRules());
			
			System.out.println(rules);
			
			
			charon = new Charon(theory+"\n"+rules);
//			charon = new Charon(theory);
//			
			startDerivation();
			
		
		} catch (CharonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	

	private void listValidConfigurations() {

		List<Map<String, Object>> solutions = null;
		
//		try {
//			solutions = charon.getCharonAPI().listValidConfigurations(query, "26", true);
//		} catch (CharonException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Map<String, List<Map<String, Object>>> selectedOptions = new HashMap<String, List<Map<String, Object>>>();
		Map<String, List<Map<String, Object>>> desselectedOptions = new HashMap<String, List<Map<String, Object>>>();
		
		for (String id : explineActivities.keySet()) {
			
			Activity actv = explineActivities.get(id);
			
			if(actv.getType() != Activity.INVARIANT_TYPE && actv.getType() != actv.VARIANT_TYPE){
				selectedOptions.put(id, new ArrayList<Map<String,Object>>());
				desselectedOptions.put(id, new ArrayList<Map<String,Object>>());

			}
		}

		
		for (Map<String, Object> solution : solutions) {
			for (String elementId : solution.keySet()) {
				String id = elementId.substring(1, elementId.length());
				
				String booleanValue = (String)solution.get(elementId);
				
				if(booleanValue.equals("true")){
					selectedOptions.get(id).add(solution);
				}
				else{
					desselectedOptions.get(id).add(solution);
				}
					

			}
		}
		
	}





	@Override
	public void startDerivation() {

		mxCell root =  (mxCell) derivationGraphComponent.getGraph().getModel().getRoot();
		root = (mxCell) root.getChildAt(0);
		
		CharonAPI charonAPI = charon.getCharonAPI();
		
		for (int i = 0; i < root.getChildCount(); i++) {
			
			mxICell cell = root.getChildAt(i);
			
			try{
			
				if(cell.isVertex()){	
				
					if (cell instanceof Activity) {
						Activity actv = (Activity) cell;
						explineActivities.put(actv.getId(), actv);
						
						if(actv.getType() == Activity.INVARIANT_TYPE){
							charonAPI.insertMandatory(actv.getId());
							currentState.put(actv.getId(), true);
						}
						else
						if(actv.getType() == Activity.OPTIONAL_INVARIANT_TYPE){
							charonAPI.insertOptional(actv.getId());
							
							currentState.put(actv.getId(), false);

							
							root.getChildAt(i).setStyle(root.getChildAt(i).getStyle() + ";opacity=20");
						}
						if(actv.getType() == Activity.OPTIONAL_VARIATION_POINT_TYPE){
							charonAPI.insertVariationPoint(actv.getId(), false);
							
							root.getChildAt(i).setStyle(root.getChildAt(i).getStyle() + ";opacity=20");
							
							currentState.put(actv.getId(), false);

						}
						else
						if(actv.getType() == Activity.VARIANT_TYPE){
							
							Object[] edges = derivationGraphComponent.getGraph().getEdges(actv, null);
							
							Activity temp = (Activity)((Edge)edges[0]).getSource();
							
							Activity actv2 = null;
							
							if(temp.getType() == Activity.VARIATION_POINT_TYPE)
								actv2 = temp;
							else
								actv2 = (Activity)((Edge)edges[0]).getTarget();
							
							
							charonAPI.insertVariant(actv.getId(), actv2.getId());
							
							actv2.getValue();
							
							root.getChildAt(i).setStyle(root.getChildAt(i).getStyle() + ";opacity=20");
							
							currentState.put(actv.getId(), false);
						}
						else
						if(actv.getType() == Activity.VARIATION_POINT_TYPE){
							charonAPI.insertVariationPoint(actv.getId(), true);

							currentState.put(actv.getId(), true);
						}	
						
					}
	
				}		
			}
			catch(CharonException e){
				e.printStackTrace();
			}
		}
				
	}

	private String createRules(List<Rule> rules) {
		
		mxCell root =  (mxCell) derivationGraphComponent.getGraph().getModel().getRoot();
		root = (mxCell) root.getChildAt(0);
		
		String rulesExpression="";
		int ruleNumber = 1;
		
		for (int i = 0; i < root.getChildCount(); i++) {
			
			mxICell cell = root.getChildAt(i);
			
				if(cell.isVertex()){	
				
					if (cell instanceof Activity) {
						Activity actv = (Activity) cell;
						

						if(actv.getType() == Activity.OPTIONAL_VARIATION_POINT_TYPE){
							
							List<Activity> variants = getVariants(actv);
							
							for (Activity variant1 : variants) {
								for (Activity variant2 : variants) {
									if(variant2 != variant1){
										rulesExpression += "\n\nrule(Path) :-"
																+"(not ruleAlreadyUsed('R"+ruleNumber+"', Path), isSelected('A"+variant1.getId()+"',Path)) -> (selectElement([['A"+variant2.getId()+"', false, 'R"+ruleNumber+"'], Path])).";
										ruleNumber++;
									}
								}
								rulesExpression += "\n\nrule(Path) :-"
										+"(not ruleAlreadyUsed('R"+ruleNumber+"', Path), isSelected('A"+variant1.getId()+"',Path)) -> (selectElement([['A"+actv.getId()+"', true, 'R"+ruleNumber+"'], Path])).";
								ruleNumber++;
							}
							
						}
						else
						if(actv.getType() == Activity.VARIATION_POINT_TYPE){
							
							List<Activity> variants = getVariants(actv);
							
							for (Activity variant1 : variants) {
								for (Activity variant2 : variants) {
									if(variant2 != variant1){
										rulesExpression += "\n\nrule(Path) :-"
																+"(not ruleAlreadyUsed('R"+ruleNumber+"', Path), isSelected('A"+variant1.getId()+"',Path)) -> (selectElement([['A"+variant2.getId()+"', false, 'R"+ruleNumber+"'], Path])).";
										ruleNumber++;
									}
								}
							}
							
						}
						
					}
	
				}		
		}
		
		for (Rule rule : rules) {
	            
	            Activity conditionActvElement = rule.getConditionElement();
	            
	            if(rule.getImplicationOperation() == Rule.OPERATION_OR){
	                
	                if(rule.isConditionElementOperationSelection()){
	                
	                	rulesExpression += "\n\nrule(Path) :- "+
	                             "(not ruleAlreadyUsed('"+rule.getName()+"', Path), isSelected('A"+conditionActvElement.getId()+"',Path)) -> (";
	                }
	                else{
	                
	                	rulesExpression += "\n\nrule(Path) :- "+
	                             "(not ruleAlreadyUsed('"+rule.getName()+"', Path), isDesselected('A"+conditionActvElement.getId()+"',Path)) -> (";
	                }
	                
	                
	                if(rule.getImplicationElements().keySet().size() > 1){
	                            
	                    for (Activity activity : rule.getImplicationElements().keySet()){
	                        
	                    	rulesExpression += "selectElement([['A"+activity.getId()+"', "+rule.getImplicationElements().get(activity).toString()+", '"+rule.getName()+"'], Path]), ";
	                        
	                    }
	                    
	                    rulesExpression = rulesExpression.substring(0, rulesExpression.length()-2) + ").";
	                            
	                }
	                else{
	                    
	                    Activity activity = rule.getImplicationElements().keySet().iterator().next();
	                    
	                    rulesExpression += "selectElement([['A"+activity.getId()+"', "+rule.getImplicationElements().get(activity).toString()+", '"+rule.getName()+"'], Path])).";
	                    
	                }
	                
	            }
	            else{
	                
	                if(rule.isConditionElementOperationSelection()){
	                    
	                    for (Activity activity : rule.getImplicationElements().keySet()){

	                        
	                        rulesExpression += "\n\nrule(Path) :- "+
	                                 "(not ruleAlreadyUsed('"+rule.getName()+"_"+ruleNumber+"', Path), isSelected('A"+conditionActvElement.getId()+"',Path)) -> (";
	                        
	                        rulesExpression += "selectElement([['A"+activity.getId()+"', "+rule.getImplicationElements().get(activity).toString()+", '"+rule.getName()+"_"+ruleNumber+"'], Path])). ";
	                        
	                        ruleNumber++;
	                        
	                    }
	                }
	                else{
	                    
	                    for (Activity activity : rule.getImplicationElements().keySet()){

	                        
	                        rulesExpression += "\n\nrule(Path) :- "+
	                                 "(not ruleAlreadyUsed('"+rule.getName()+ruleNumber+"', Path), isDesselected('A"+conditionActvElement.getId()+"',Path)) -> (";
	                        
	                        rulesExpression += "selectElement([['A"+activity.getId()+"', "+rule.getImplicationElements().get(activity).toString()+", '"+rule.getName()+ruleNumber+"'], Path])). ";
	                        
	                        ruleNumber++;
	                    }
	                }
	                
	            }
	            
	    }
	        
		rulesExpression += "\n\nrule(Path).";
		
		return rulesExpression;
		
	}
	
	
	private List<Activity> getVariants(Activity actv) {
		
		Object[] edges = derivationGraphComponent.getGraph().getEdges(actv, null);
		
		List<Activity> variants = new ArrayList<Activity>();
		
		for (int i = 0; i < edges.length; i++) {
			Activity temp = (Activity)((Edge)edges[i]).getSource();
						
			if(temp.getType() == Activity.VARIANT_TYPE)
				variants.add(temp);
			else
				variants.add((Activity)((Edge)edges[i]).getTarget());
		}
		
		return variants;
	}





	private void insertRules2(List<Rule> rules) {
		
		CharonAPI charonAPI = charon.getCharonAPI();
		
		mxCell root =  (mxCell) derivationGraphComponent.getGraph().getModel().getRoot();
		root = (mxCell) root.getChildAt(0);
		
		List<String> expressionList = new ArrayList<String>();
		
		String variableList = "";
		String variableInstantiation = "";
		String assertzBooleanValues = "";
		
		for (int i = 0; i < root.getChildCount(); i++) {
			
			mxICell cell = root.getChildAt(i);
			
				if(cell.isVertex()){	
				
					if (cell instanceof Activity) {
						Activity actv = (Activity) cell;
						

						if(actv.getType() == Activity.OPTIONAL_VARIATION_POINT_TYPE || actv.getType() == Activity.VARIATION_POINT_TYPE){
							
							Object[] edges = derivationGraphComponent.getGraph().getEdges(actv, null);
							
							String variationPointExp = "";
							
							if(edges.length>1){
								
								Activity temp = (Activity)((Edge)edges[0]).getSource();
								
								Activity variant1 = null;
								
								if(temp.getType() == Activity.VARIANT_TYPE)
									variant1 = temp;
								else
									variant1 = (Activity)((Edge)edges[0]).getTarget();
								
								
								temp = (Activity)((Edge)edges[1]).getSource();
								
								Activity variant2 = null;
								
								if(temp.getType() == Activity.VARIANT_TYPE)
									variant2 = temp;
								else
									variant2 = (Activity)((Edge)edges[1]).getTarget();
								
								variationPointExp = variationPointExp + "";
								
								variationPointExp = "xor(A"+variant1.getId()+", A" + variant2.getId() +")";
								
								
								variableList = variableList + "A"+variant1.getId() + ", A"+variant2.getId()+", ";
								
								variableInstantiation = variableInstantiation + "boolean"+variant1.getId()+"(A"+variant1.getId() + "), boolean"+variant2.getId()+"(A"+variant2.getId()+"), ";
								
								assertzBooleanValues = assertzBooleanValues + "assertz(boolean"+variant1.getId()+"(true)), assertz(boolean"+variant1.getId()+"(false)), assertz(boolean"+variant2.getId()+"(true)), assertz(boolean"+variant2.getId()+"(false)), ";
							}
							for (int j=2; j<edges.length; j++) {
								
								Activity temp = (Activity)((Edge)edges[j]).getSource();
								
								Activity variant = null;
								
								if(temp.getType() == Activity.VARIANT_TYPE)
									variant = temp;
								else
									variant = (Activity)((Edge)edges[j]).getTarget();
								
								variationPointExp = "xor(" +variationPointExp + ", A"+variant.getId()+")";
								
								variableList = variableList +"A" + variant.getId() +", ";
								
								variableInstantiation = variableInstantiation+ "boolean"+variant.getId()+"(A"+variant.getId() + "), ";
								
								assertzBooleanValues = assertzBooleanValues+ "assertz(boolean"+variant.getId()+"(true), assertz(boolean"+variant.getId()+"(false)), ";
								
							}
							
							
							expressionList.add(variationPointExp);
							
						}
	
						
					}
	
				}		
		}
		
		String expression = "";
		
		if(expressionList.size()>0){
			if(expressionList.size()>1){
				expression = "and("+expressionList.get(0)+", " + expressionList.get(1) +")";
				
				for (int j=2; j<expressionList.size(); j++) {
					
					expression = "and("+ expression + ", "+expressionList.get(j)+")";
					
				}
				
				
			}
			else
				expressionList.get(0);
		}
		
		expression = assertzBooleanValues+ "assertz(evaluateState(" + variableList +"E) :- "+ variableInstantiation + " evaluate(E, Result)).";
		
		System.out.println(expression);
		
//		try {
//			System.out.println(charonAPI.insertRules(expression));
//		} catch (CharonException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
//		for (Rule rule : rules) {
//			
//			for (Expression exp : rule.getConditions()){
//			
//				
//				List<String> activityIds = new ArrayList<>();
//				
//				for (Activity activity : exp.getActivities()) {
//					
//					activityIds.add(activity.getId());
//				}
//				
////				charon.getCharonAPI().insertRule(exp.getOperation(),   exp.getModifier(), activityIds);
//				
//			}
//		}
		
	}





	@Override
	public HashMap<Activity, Boolean> getImpliedSteps(
			HashMap<Activity, Boolean> activitySelectionChangeList) {
		// TODO Auto-generated method stub
		return new HashMap<Activity, Boolean>();
	}

	@Override
	public boolean generatesValidState(
			HashMap<Activity, Boolean> activitySelectionChangeList) {
		
		return true;
	}

	@Override
	public Activity[] getActivityConflictList(
			HashMap<Activity, Boolean> activitySelectionChangeList) {
		// TODO Auto-generated method stub
		
		mxCell root =  (mxCell) derivationGraphComponent.getGraph().getModel().getRoot();
		root = (mxCell) root.getChildAt(0);
		
		return new Activity [] {(Activity)root.getChildAt(9), (Activity)root.getChildAt(13)};
		

	}

	@Override
	public String[] getActivityConflictList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPossibleActivitySelections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getSelectedActivities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validatesDerivedWorklfow(File derivedWorkflow) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validatesDerivedWorklfow() {
		CharonAPI charonAPI = charon.getCharonAPI();
		
		boolean result = false;
		
		try {
			result = charonAPI.isValidDerivedWorkflow();
		} catch (CharonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public boolean getDerivedWorkflow() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isActivitySelected(Activity activity) {
		CharonAPI charonAPI = charon.getCharonAPI();
		
		boolean result = false;
		
		try {
			result = charonAPI.isElementSelected(activity.getId());
		} catch (CharonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}


	@Override
	public boolean selectActivity(Activity activity, boolean selected) {
		
		CharonAPI charonAPI = charon.getCharonAPI();
					
		List<Map<String, Object>> selectImplications = null;
		try {
			selectImplications = listImplications(activity, selected);
		} catch (CharonException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(selectImplications != null){
			
			if(!selectImplications.isEmpty()){
				
				//abrir tela que lista op��es de sele��o
//					List<Map<String, Boolean>> processedImplications = processImplications(selectImplications);
//					
//					System.out.println(processedImplications);
				
//				JOptionPane.showMessageDialog(derivationGraphComponent,
//						"Derivation Status: Selection is Valid");
				
				ArrayList<List> elements = (ArrayList) ((Map<String, Object>)selectImplications.get(0)).get("A");
				
				for (int i = 0; i < elements.size(); i++) {
					
					ArrayList<String> element = (ArrayList) elements.get(i);
					
					String activityId = element.get(0).substring(2, element.get(0).length()-1);
					
					Boolean activitySelect =  Boolean.valueOf(element.get(1));
					
					if(!currentState.get(activityId).equals(activitySelect)){
						currentState.put(activityId, activitySelect);
						if(activitySelect)
							explineActivities.get(activityId).setStyle(activity.getStyle().replace(";opacity=20", ""));
						else
							explineActivities.get(activityId).setStyle(activity.getStyle() + ";opacity=20");
					}
										 
				}
								
			}
			else{
				try {
					charonAPI.selectElement(activity.getId());
				} catch (CharonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(selected)
					activity.setStyle(activity.getStyle().replace(";opacity=20", ""));
				else
					activity.setStyle(activity.getStyle() + ";opacity=20");
			}
			
			return true;
			
		}
		else
			return false;
				

	}
	
	public List<Map<String, Boolean>> processImplications(List<Map<String, Object>> selectImplications){
		
		List<Map<String, Boolean>> processedImplications = new ArrayList<Map<String, Boolean>>();
		
				
		for (Map<String, Object> implication : selectImplications) {
			
			Map<String, Boolean> processedImplication = new HashMap<String, Boolean>();
			
			
			for (String elementId : implication.keySet()) {
				
				String id = elementId.substring(1, elementId.length());
				
				if(!implication.get(elementId).equals(explineActivities.get(id).isSelected()))
					processedImplication.put(elementId, explineActivities.get(id).isSelected());	
			}
			
			processedImplications.add(processedImplication);
		}
		
		
		return processedImplications;
	}
	
	private List<Map<String, Object>> listImplications(Activity activity, boolean selected) throws CharonException {
		// TODO Auto-generated method stub
		
		//verifico se essa configura��o � valida
		//se sim, retorno uma lista vazia
		//se n�o �, verifico se exista  eu coleto as implica��es necess�rias
		
		CharonAPI charonAPI = charon.getCharonAPI();
		List<Map<String, Object>> solutions = charonAPI.listImplications(activity.getId(), selected);
		
		for(Map<String, Object> solution : solutions){
			if(((ArrayList)solution.get("A")).size()>1)
				return solutions;
		}

		return new ArrayList<Map<String, Object>>();
		
	}
	
	public Workflow derive() {
		
		
		
		//Creating temporary graph structure to derivate the workflow to not propagate to the original one that is being manipulated by the user on the screen.
		//
		ExpLineDerivationGraph expLineDerGraphTemp = new ExpLineDerivationGraph(null);
		
		expLineDerGraphTemp.getModel().setRoot(derivationGraphComponent.getGraph().getModel().getRoot());
		
		
		ExpLine expline = (ExpLine)  expLineDerGraphTemp.getModel();
			
		Workflow workflow = new Workflow();
		
		workflow.setRoot(expline.getRoot());
		
		
		
		if(validatesDerivedWorklfow()){
			
			mxCell root =  (mxCell) workflow.getRoot();
			root = (mxCell) root.getChildAt(0);
			
			CharonAPI charonAPI = charon.getCharonAPI();
			
			for (int i = 0; i < root.getChildCount(); i++) {
							
				mxICell cell = root.getChildAt(i);
				
				try{
				
					if(cell.isVertex()){	
					
						if (cell instanceof Activity) {
							Activity actv = (Activity) cell;
							

							if(actv.getType() == Activity.OPTIONAL_INVARIANT_TYPE){
								charonAPI.insertOptional(actv.getId());
								
								if(!charonAPI.isElementSelected(actv.getId())){
									
									if(!actv.getAlgebraicOperator().equals("Join")){
										
										Port inputPort = actv.getInputPort(0);
										Port outputPort = actv.getOutputPort();
										
										Object[] edges = expLineDerGraphTemp.getEdges(inputPort, null);
										
										Port earlyPort = null;
										Port afterPort = null;
										
										
										if(edges.length > 0){
										
											earlyPort = (Port)((Edge)edges[0]).getSource();
											
											if(earlyPort.getId().equals(inputPort.getId())){
												
												earlyPort = (Port)((Edge)edges[0]).getTarget();
											}
										}
										
										
										
										edges = expLineDerGraphTemp.getEdges(outputPort, null);
										
										
										if(edges.length > 0){
										
											afterPort = (Port)((Edge)edges[0]).getTarget();
											
											if(afterPort.getId().equals(outputPort.getId())){
												
												afterPort = (Port)((Edge)edges[0]).getSource();
											}
										}
										
										
										if(earlyPort != null && afterPort != null)
											expLineDerGraphTemp.insertEdge(actv.getParent(), null, "", earlyPort, afterPort);
										
										
										expLineDerGraphTemp.removeCells(new Object[]{actv}, true);
										
										
										workflow = new Workflow();
										
										workflow.setRoot(expline.getRoot());
										
										
										
									}
									
								}
								
							}
							if(actv.getType() == Activity.OPTIONAL_VARIATION_POINT_TYPE){
								
								charonAPI.insertOptional(actv.getId());
								
								if(!charonAPI.isElementSelected(actv.getId())){
									
									if(!actv.getAlgebraicOperator().equals("Join")){
										
										Port inputPort = actv.getInputPort(0);
										Port outputPort = actv.getOutputPort();
										
										Object[] edges = expLineDerGraphTemp.getEdges(inputPort, null);
										
										Port earlyPort = null;
										Port afterPort = null;
										
										
										if(edges.length > 0){
										
											earlyPort = (Port)((Edge)edges[0]).getSource();
											
											if(earlyPort.getId().equals(inputPort.getId())){
												
												earlyPort = (Port)((Edge)edges[0]).getTarget();
											}
										}
										
										
										
										edges = expLineDerGraphTemp.getEdges(outputPort, null);
										
										
										if(edges.length > 0){
										
											afterPort = (Port)((Edge)edges[0]).getTarget();
											
											if(afterPort.getId().equals(outputPort.getId())){
												
												afterPort = (Port)((Edge)edges[0]).getSource();
											}
										}
										
										
										if(earlyPort != null && afterPort != null)
											expLineDerGraphTemp.insertEdge(actv.getParent(), null, "", earlyPort, afterPort);
										
										
										expLineDerGraphTemp.removeCells(new Object[]{actv}, true);
										
										
										workflow = new Workflow();
										
										workflow.setRoot(expline.getRoot());
										
										
										
									}
									
								}
								else{
									
									Object[] edges = expLineDerGraphTemp.getEdges(actv, null);
									
									for (Object object : edges) {
										Edge edge = (Edge) object;
										
										if(!(edge.getSource() instanceof Port) && !(edge.getTarget() instanceof Port)){
											
											Activity variant = (Activity) edge.getSource();
											
											if(variant.getType() != Activity.VARIANT_TYPE)
												variant = (Activity) edge.getTarget();
																						
											if(charonAPI.isElementSelected(variant.getId())){
												
												actv.setValue(((String)actv.getValue())+" ("+((String)variant.getValue()) +")");
												
											}
											
											expLineDerGraphTemp.removeCells(new Object[]{variant}, true);

										}
										
									}
									
								}

							}
							else
							if(actv.getType() == Activity.VARIATION_POINT_TYPE){

								Object[] edges = expLineDerGraphTemp.getEdges(actv, null);
								
								for (Object object : edges) {
									Edge edge = (Edge) object;
									
									if(!(edge.getSource() instanceof Port) && !(edge.getTarget() instanceof Port)){
										
										Activity variant = (Activity) edge.getSource();
										
										if(variant.getType() != Activity.VARIANT_TYPE)
											variant = (Activity) edge.getTarget();
										
										if(charonAPI.isElementSelected(variant.getId())){
																					
											actv.setValue(((String)actv.getValue())+" ("+((String)variant.getValue()) +")");
											
										}
										
										expLineDerGraphTemp.removeCells(new Object[]{variant}, true);

									}
									
								}
							}	
							
						}
		
					}		
				}
				catch(CharonException e){
					e.printStackTrace();
				}
			}

			
			return workflow;
		}
		else
			return null;
	}

	
}
