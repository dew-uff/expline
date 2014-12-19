package br.ufrj.cos.expline.derivation;

import java.io.File;
import java.util.HashMap;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Workflow;

public interface Derivation {

	
	/*
	 * Inicia/reseta o processo de deriva��o e retorna uma sele��o inicial da linha de experimento.
	 * 
	 */
	public void startDerivation();
	
	/*
	 * Retorna as implica��es na sele��o do workflow baseado nas atividades selecionadas e desselecionadas.
	 * 
	 */
	public HashMap<Activity, Boolean> getImpliedSteps(HashMap<Activity, Boolean> activitySelectionChangeList);
	
	/*
	 * Valida se as atividades selecionadas e desselecionadas geram um estado v�lido.
	 * 
	 */
	public boolean generatesValidState(HashMap<Activity, Boolean> activitySelectionChangeList);
	
	/*
	 * Lista as atividades conflitantes baseado nas atividades selecionadas e desselecionadas. 
	 * 
	 */
	public Activity[] getActivityConflictList(HashMap<Activity, Boolean> activitySelectionChangeList);
	
	/*
	 * Lista as atividades conflitantes de acordo com o estado atual de sele��o.  
	 * 
	 */
	public String[] getActivityConflictList();
	
	/*
	 * Lista as poss�veis atividades a serem selecionadas de acordo com o estado atual de sele��o.  
	 * 
	 */
	public String[] getPossibleActivitySelections();
	
	/*
	 * Lista as atividades selecionadas do estado atual de sele��o.  
	 * 
	 */
	public String[] getSelectedActivities();
	
	/*
	 * Verifica se o workflow derivado � v�lido.
	 * 
	 */
	public boolean validatesDerivedWorklfow(File derivedWorkflow);
	
	/*
	 * Verifica se a configura��o atual � um workflow derivado.
	 * 
	 */
	public boolean validatesDerivedWorklfow();
	
	/*
	 * Retorna o workflow derivado.  
	 * 
	 */
	public boolean getDerivedWorkflow();
	
	
	/*
	 * Retorna o workflow derivado.  
	 * 
	 */
	public boolean isActivitySelected(Activity activity);
	
	public boolean selectActivity(Activity activity, boolean selected);

	public Workflow derive();

}
