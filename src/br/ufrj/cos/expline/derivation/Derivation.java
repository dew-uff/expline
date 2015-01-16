package br.ufrj.cos.expline.derivation;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.AbstractWorkflow;

public interface Derivation {

	
	/*
	 * Inicia/reseta o processo de deriva��o e retorna uma sele��o inicial da linha de experimento.
	 * 
	 */
	public void startDerivation();
	
	/*
	 * Verifica se a configura��o atual � um workflow derivado.
	 * 
	 */
	public boolean validatesDerivedWorkflow();
	
	public boolean isActivitySelected(Activity activity);
	
	public void setActivitySelection(Activity activity, boolean selected);
	
	public boolean selectActivity(Activity activity, boolean selected);

	public AbstractWorkflow derive();

}
