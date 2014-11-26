/*
Initialize derivation (Select mandatory activities to the abstract workflow) (Deveria tentar achar uma solu��o para as regras de composi��o e selecionar as variabilidades e opcionalidades)
*/
initializeDerivation(_) :-
	findall(ActivityId,mandatory(ActivityId),ActivityIdList),
	selectMandatoryActivities(ActivityIdList).

/*
Validate Derived Workflow
*/
isValidDerivedWorkflow(_) :-
	findall(ActivityIdd,mandatory(ActivityIdd),MandatoryList),
	findall(ActivityId,abstractWorkflow(ActivityId),AbstActvList),
	containsAllMandatories(MandatoryList,AbstActvList),
	findall(VPId,variationPoint(VPId),VPList),
	checkVariationPoints(VPList,AbstActvList). 
%	findall(RuleId,validationRule(RuleId),'[]').
		
validateRules('[]').

validateRules([Rule|RuleList]) :-
	validationRule(Rule),
	validateRules(RuleList).    

/*
Verify if all mandatory activities are included in the abstract workflow.
*/
containsAllMandatories('[]',_).

containsAllMandatories([M|MandatoryList],AbstActvList) :-
	member(M,AbstActvList),
	containsAllMandatories(MandatoryList,AbstActvList).

/*
Verify if one, and only one, variant is selected for every variation point.
*/
checkVariationPoints('[]',_).

checkVariationPoints([VP|VPList],AbstActvList) :-
	optional(VP),
	not abstractWorkflow(VP),
	checkVariationPoints(VPList,AbstActvList).    

checkVariationPoints([VP|VPList],AbstActvList) :-
	findall(VariantId,variant(VariantId,VP),VariantIdList),
	isVariantSelected(VP,VariantIdList,AbstActvList),
	not isThereAnyOtherVariantSelected(VP,VariantIdList,AbstActvList),
	checkVariationPoints(VPList,AbstActvList).
	

isMoreVariantSelected(VariationPoint,[Var|VariantList],AbstActvList) :-
	member(Var,AbstActvList),
	not currentDeselection(Var).

isMoreVariantSelected(VariationPoint,[Var|VariantList],AbstActvList) :-
	member(Var,AbstActvList),
	currentDeselection(Var),
	isMoreVariantSelected(VariationPoint,VariantList,AbstActvList).

isMoreVariantSelected(VariationPoint,[Var|VariantList],AbstActvList) :-
	not member(Var,AbstActvList),
	isMoreVariantSelected(VariationPoint,VariantList,AbstActvList).
	

isThereAnyOtherVariantSelected(VariationPoint,[Var|VariantList],AbstActvList) :-
	member(Var,AbstActvList),
	not currentDeselection(Var),
	isMoreVariantSelected(VariationPoint,VariantList,AbstActvList).

isThereAnyOtherVariantSelected(VariationPoint,[Var|VariantList],AbstActvList) :-
	member(Var,AbstActvList),
	currentDeselection(Var),
	isThereAnyOtherVariantSelected(VariationPoint,VariantList,AbstActvList).

isThereAnyOtherVariantSelected(VariationPoint,[Var|VariantList],AbstActvList) :-
	not member(Var,AbstActvList),
	isThereAnyOtherVariantSelected(VariationPoint,VariantList,AbstActvList).
	
/*
Function to select all mandatory activities in the derived abstract workflow.
*/
selectMandatoryActivities('[]').

selectMandatoryActivities([M|MandatoryList]) :-
	assertz(abstractWorkflow(M)),
	selectMandatoryActivities(MandatoryList).

/*
Regra de inser��o pra mandatorio.
*/
selectElement(E) :-
	not abstractWorkflow(E),
	mandatory(E),
	selectMandatoryActivities([E]). 

selectElement(E) :-
	abstractWorkflow(E),
	mandatory(E).


/*
Regra de inser��o pra ponto de varia��o opcional.
*/
selectElement(E) :-
	not abstractWorkflow(E),
	optional(E),
	variationPoint(E),
	assertz(abstractWorkflow(E)).

selectElement(E) :-
	abstractWorkflow(E),
	optional(E),
	variationPoint(E).

unselectElement(E) :-
	abstractWorkflow(E),
	optional(E),
	variationPoint(E),
	retract(abstractWorkflow(E)). 

/*
Regra de inser��o pra opcional nao ponto de varia��o.
*/
selectElement(E) :-
	not abstractWorkflow(E),
	optional(E),
	assertz(abstractWorkflow(E)).

selectElement(E) :-
	abstractWorkflow(E),
	optional(E).


/*
BEGIN: Regras de inser��o e remo��o de variantes
*/
%N�o checa se tem mais de uma variante selecionada (isso nao acontece pq a funcao selectVariant nao existe) e da erro se o conjunto de variantes for vazio.
isVariantSelected(VariationPoint,[Var|VariantList],AbstActvList) :-
	member(Var,AbstActvList).

isVariantSelected(VariationPoint,[Var|VariantList],AbstActvList) :-
	not member(Var,AbstActvList),
	isVariantSelected(VariationPoint,VariantList,AbstActvList).

/*
Function to select one variant of a variation point. Before select the functions checks if there is other variant already selected.
*/
selectVariant(Var) :-
	findall(ActivityId,abstractWorkflow(ActivityId),AbstActvList),
	variant(Var,VP),
	findall(Var1,variant(Var1,VP),VariantList),
	findall(ActivityId2,currentSelection(ActivityId2),CurrentSelectList),
	append(AbstActvList,CurrentSelectList,CompleteList),
	not isVariantSelected(VP,VariantList,CompleteList),
	assertz(abstractWorkflow(Var)),
	selectElement(VP).
	
selectElement(E) :-
	not abstractWorkflow(E),
	variant(E,_),
	selectVariant(E).

selectElement(E) :-
	abstractWorkflow(E),
	variant(E,_).

/*
acho que a dessele��o ta errada, pois nao crio um caso quando o elemento jah esta desselecionado
*/
unselectElement(E) :-
	abstractWorkflow(E),
	variant(E,_583210292),
	retract(abstractWorkflow(E)).


/*
END: Regras de inser��o e remo��o de variantes
*/


/*
mandatory('A').
variationPoint('B').
mandatory('B').
variant('B1', 'B').
variant('B2', 'B'). 
optional('C'). 
variationPoint('D').
mandatory('D').
variant('D1', 'D').
variant('D2', 'D').
mandatory('E').
variationPoint('E').
variant('E1', 'E').
variant('E2', 'E'). 
	

/*	 
abstractWorkflow('A'). 
abstractWorkflow('B').
%abstractWorkflow('D').
abstractWorkflow('E').
%abstractWorkflow('C').  
abstractWorkflow('B1'). 
%abstractWorkflow('D1'). 
abstractWorkflow('E1').
%abstractWorkflow('E2').
%abstractWorkflow('B2').

*/