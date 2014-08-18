package br.ufrj.cos.expline.derivation;

import java.io.File;
import java.util.HashMap;

public class DerivationImp implements Derivation {

	@Override
	public void startDerivation() {
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap<String, Boolean> getImpliedSteps(
			HashMap<String, Boolean> activitySelectionChangeList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean generatesValidState(
			HashMap<String, Boolean> activitySelectionChangeList) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setActivitySelectionChangeList(
			HashMap<String, Boolean> activitySelectionChangeList) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getActivityConflictList(
			HashMap<String, Boolean> activitySelectionChangeList) {
		// TODO Auto-generated method stub
		return null;
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
