package br.ufrj.cos.expline.derivation;

import java.io.File;

import br.ufrj.cos.expline.model.Activity;
import br.ufrj.cos.expline.model.Workflow;

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
	
	public boolean selectActivity(Activity activity, boolean selected);

	public Workflow derive();

}
