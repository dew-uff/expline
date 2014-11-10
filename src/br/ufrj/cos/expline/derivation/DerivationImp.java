package br.ufrj.cos.expline.derivation;

import java.io.File;
import java.util.HashMap;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Edge;
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
			
//			root.getChildAt(i).setStyle("strokeWidth=6;strokeColor=#66CC00");
//			root.getChildAt(i).setStyle("strokeWidth=6;strokeColor=#FF0000");
			
			mxICell cell = root.getChildAt(i);
			
			try{
			
				if(cell.isVertex()){	
				
					if (cell instanceof Activity) {
						Activity actv = (Activity) cell;
						
						if(actv.getType() == Activity.INVARIANT_TYPE){
							charonAPI.insertMandatory(actv.getId());
							
							beginSelection();
							
							System.out.println(selectActivity(actv));
						
							commitSelection();
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
							
							beginSelection();
							
							selectActivity(actv);
						
							commitSelection();
						}	
						
					}
	
				}		
			}
			catch(CharonException e){
				e.printStackTrace();
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

}
