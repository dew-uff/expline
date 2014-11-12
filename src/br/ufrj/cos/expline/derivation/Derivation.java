package br.ufrj.cos.expline.derivation;

import java.io.File;
import java.util.HashMap;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Workflow;

public interface Derivation {

	
	/*
	 * Inicia/reseta o processo de derivação e retorna uma seleção inicial da linha de experimento.
	 * 
	 */
	public void startDerivation();
	
	/*
	 * Retorna as implicações na seleção do workflow baseado nas atividades selecionadas e desselecionadas.
	 * 
	 */
	public HashMap<Activity, Boolean> getImpliedSteps(HashMap<Activity, Boolean> activitySelectionChangeList);
	
	/*
	 * Valida se as atividades selecionadas e desselecionadas geram um estado válido.
	 * 
	 */
	public boolean generatesValidState(HashMap<Activity, Boolean> activitySelectionChangeList);
	
	/*
	 * Registra as atividades selecionadas e desselecionadas na linha de experimento
	 * 
	 */
	public boolean setActivitySelectionChangeList(HashMap<Activity, Boolean> activitySelectionChangeList);
	
	/*
	 * Lista as atividades conflitantes baseado nas atividades selecionadas e desselecionadas. 
	 * 
	 */
	public Activity[] getActivityConflictList(HashMap<Activity, Boolean> activitySelectionChangeList);
	
	/*
	 * Lista as atividades conflitantes de acordo com o estado atual de seleção.  
	 * 
	 */
	public String[] getActivityConflictList();
	
	/*
	 * Lista as possíveis atividades a serem selecionadas de acordo com o estado atual de seleção.  
	 * 
	 */
	public String[] getPossibleActivitySelections();
	
	/*
	 * Lista as atividades selecionadas do estado atual de seleção.  
	 * 
	 */
	public String[] getSelectedActivities();
	
	/*
	 * Verifica se o workflow derivado é válido.
	 * 
	 */
	public boolean validatesDerivedWorklfow(File derivedWorkflow);
	
	/*
	 * Verifica se a configuração atual é um workflow derivado.
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
	
	public boolean selectActivity(Activity activity);
	
	public boolean unselectActivity(Activity activity);
	
	public void beginSelection();
	
	public void commitSelection();
	
	public void rollbackSelection();

	public Workflow derive();
	
}
