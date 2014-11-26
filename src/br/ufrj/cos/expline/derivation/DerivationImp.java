package br.ufrj.cos.expline.derivation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Edge;
import br.ufrj.cos.expline.model.ExpLine;
import br.ufrj.cos.expline.model.Expression;
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
		
	public DerivationImp(mxGraphComponent derivationGraphComponent) {
		// TODO Auto-generated constructor stub
		this.derivationGraphComponent = derivationGraphComponent;
		
		try {

			charon = new Charon("doc");
		} catch (CharonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
						
						if(actv.getType() == Activity.INVARIANT_TYPE){
							charonAPI.insertMandatory(actv.getId());
							
							selectActivity(actv);
						
						}
						else
						if(actv.getType() == Activity.OPTIONAL_INVARIANT_TYPE){
							charonAPI.insertOptional(actv.getId());
							
							root.getChildAt(i).setStyle(root.getChildAt(i).getStyle() + ";opacity=20");
						}
						if(actv.getType() == Activity.OPTIONAL_VARIATION_POINT_TYPE){
							charonAPI.insertVariationPoint(actv.getId(), false);
							
							root.getChildAt(i).setStyle(root.getChildAt(i).getStyle() + ";opacity=20");
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
						}
						else
						if(actv.getType() == Activity.VARIATION_POINT_TYPE){
							charonAPI.insertVariationPoint(actv.getId(), true);
							
							selectActivity(actv);

						}	
						
					}
	
				}		
			}
			catch(CharonException e){
				e.printStackTrace();
			}
		}
		
		

		ExpLine model = (ExpLine) derivationGraphComponent.getGraph().getModel();
		
//		insertRules(model.getRules());
		
	}

	private void insertRules(List<Rule> rules) {
		
		
		for (Rule rule : rules) {
			
			for (Expression exp : rule.getConditions()){
			
				
				List<String> activityIds = new ArrayList<>();
				
				for (Activity activity : exp.getActivities()) {
					
					activityIds.add(activity.getId());
				}
				
//				charon.getCharonAPI().insertRule(exp.getOperation(),   exp.getModifier(), activityIds);
				
			}
		}
		
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
	public boolean setActivitySelectionChangeList(
			HashMap<Activity, Boolean> activitySelectionChangeList) {
		// TODO Auto-generated method stub
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
			result = charonAPI.isValidDerivatedWorkflow();
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
	public boolean selectActivity(Activity activity) {
		
		CharonAPI charonAPI = charon.getCharonAPI();
		
		boolean result = false;
		
		try {
			result = charonAPI.selectElement(activity.getId());
		} catch (CharonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(result){
			activity.setStyle(activity.getStyle().replace(";opacity=20", ""));
		}	
		
		return result;
	}


	@Override
	public boolean unselectActivity(Activity activity) {
		CharonAPI charonAPI = charon.getCharonAPI();
		
		boolean result = false;
		
		try {
			result = charonAPI.unselectElement(activity.getId());
		} catch (CharonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(result){
			activity.setStyle(activity.getStyle() + ";opacity=20");
		}
		
		
		return result;
	}





	@Override
	public void beginSelection() {
		// TODO Auto-generated method stub
		
		CharonAPI charonAPI = charon.getCharonAPI();
		
		try {
			charonAPI.beginSelection();
		} catch (CharonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





	@Override
	public void commitSelection() {
		// TODO Auto-generated method stub
		
		CharonAPI charonAPI = charon.getCharonAPI();
		
		try {
			charonAPI.commitSelection();
		} catch (CharonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void rollbackSelection() {

		CharonAPI charonAPI = charon.getCharonAPI();
		
		try {
			charonAPI.rollbackSelection();
		} catch (CharonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
