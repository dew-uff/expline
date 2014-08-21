package br.ufrj.cos.expline.derivation;

import java.io.File;
import java.util.HashMap;

import br.ufrj.cos.expline.model.Activity;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;

public class DerivationImp implements Derivation {

	private mxGraphComponent derivationGraphComponent;
	
	public DerivationImp(mxGraphComponent derivationGraphComponent) {
		// TODO Auto-generated constructor stub
		this.derivationGraphComponent = derivationGraphComponent;
		
	}

	@Override
	public void startDerivation() {
		// TODO Auto-generated method stub

		mxCell root =  (mxCell) derivationGraphComponent.getGraph().getModel().getRoot();
		root = (mxCell) root.getChildAt(0);
		
		for (int i = 0; i < root.getChildCount(); i++) {
			
//			root.getChildAt(i).setStyle("strokeWidth=6;strokeColor=#66CC00");
//			root.getChildAt(i).setStyle("strokeWidth=6;strokeColor=#FF0000");
			root.getChildAt(i).setStyle(root.getChildAt(i).getStyle() + ";opacity=20");
			
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getDerivedWorkflow() {
		// TODO Auto-generated method stub
		return false;
	}

}
