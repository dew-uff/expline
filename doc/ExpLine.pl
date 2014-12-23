selectElement([[E, true, Rule], Path]) :- write(E),
    not isSelected(E,Path),
    not isDesselected(E,Path),
    write(' notSelected '),
    write(Path),
    assertz(currentSelection(E,Path, true)),
    write('Before flag '),
    flagAsIncomplete(Path),
    write('Selected '),
    rule([[E, true, Rule]|Path]), !, write('EndSelect ').

selectElement([[E, true, Rule], Path]) :- write(E),
    isDesselected(E,Path),
    flagAsIncomplete(Path).
       
selectElement([[E, false, Rule], Path]) :- write(E),
    not isDesselected(E,Path),
    not isSelected(E,Path),
    write(' notSelected '),
    write(Path),
    assertz(currentDesselection(E,Path, true)),
    write('Before flag '),
    flagAsIncomplete(Path),
    write('Selected '),
    rule([[E, false, Rule]|Path]), !, write('EndSelect ').
   
selectElement([[E, false, Rule], Path]) :- write(E),
    isSelected(E,Path),
    flagAsIncomplete(Path).

flagAsIncomplete([[E, true, _]| Path]):- write('flag1.1 '), write(E), retract(currentSelection(E, Path, true)), write('flag1.2 '), assertz(currentSelection(E, Path, false)), !.
flagAsIncomplete([[E, true, _]| Path]):- write('flag1a.1 '), currentSelection(E, Path, false), write('flag1a.2 '), !.
flagAsIncomplete([[E, false, _]| Path]):- write('flag2.1 '), retract(currentDesselection(E, Path, true)), write('flag2.2 '), assertz(currentDesselection(E, Path, false)), !.
flagAsIncomplete([[E, false, _]| Path]):- write('flag2a.1 '), currentDesselection(E, Path, false), write('flag2a.2 '), !.
flagAsIncomplete([]):- write('flag0').

isSelected(E, [[E, true, _]|_]) :- !.
isSelected(E,[Head|Path]) :- isSelected(E,Path). 
 
isDesselected(E, [[E, false, _]|_]) :- !.
isDesselected(E,[Head|Path]) :- isDesselected(E,Path).
       
ruleAlreadyUsed(Rule, [[_, _, Rule]|_]) :- !.
ruleAlreadyUsed(Rule,[Head|Path]) :- ruleAlreadyUsed(Rule,Path).
   
processResults(_) :- currentSelection(A, B1, true),
        findall(B2, currentSelection(A, B2, true),B2s),
        length(B2s, Length),
        Length > 1,
        maxLength(B2s, BMax),
        append([[A, true, '']], BMax, C),
        not option(C),
        assertz(option(C)).
       
processResults(_) :- currentSelection(A, B1, true),
        findall(B2, currentSelection(A, B2, true),B2s),
        length(B2s, Length),
        Length = 1,
        append([[A, true, '']], B1, C),
        assertz(option(C)).
processResults(_) :- currentDesselection(A, B1, true),
        findall(B2, currentDesselection(A, B2, true),B2s),
        length(B2s, Length),
        Length > 1,
        maxLength(B2s, BMax),
        append([[A, false, '']], BMax, C),
        not option(C),
        assertz(option(C)). 
processResults(_) :- currentDesselection(A, B1, true),
        findall(B2, currentDesselection(A, B2, true),B2s),
        length(B2s, Length),
        Length = 1,
        append([[A, false, '']], B1, C),
        assertz(option(C)).
       
removeRedundants(_) :-   option(Path),
            option(Path2),
            not (Path = Path2),
            length(Path, Length),
            length(Path2, Length),
            isEqual(Path, Path2),
            retract(option(Path)).

rule(Path) :-
    (not ruleAlreadyUsed('R1', Path), isSelected('A34',Path)) -> (selectElement([['A35', false, 'R1'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R2', Path), isSelected('A34',Path)) -> (selectElement([['A36', false, 'R2'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R3', Path), isSelected('A34',Path)) -> (selectElement([['A37', false, 'R3'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R4', Path), isSelected('A34',Path)) -> (selectElement([['A38', false, 'R4'], Path])).

rule(Path) :-
    (not ruleAlreadyUsed('R5', Path), isSelected('A35',Path)) -> (selectElement([['A34', false, 'R5'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R6', Path), isSelected('A35',Path)) -> (selectElement([['A36', false, 'R6'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R7', Path), isSelected('A35',Path)) -> (selectElement([['A37', false, 'R7'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R8', Path), isSelected('A35',Path)) -> (selectElement([['A38', false, 'R8'], Path])).


rule(Path) :-
    (not ruleAlreadyUsed('R9', Path), isSelected('A36',Path)) -> (selectElement([['A34', false, 'R9'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R10', Path), isSelected('A36',Path)) -> (selectElement([['A35', false, 'R10'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R11', Path), isSelected('A36',Path)) -> (selectElement([['A37', false, 'R11'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R12', Path), isSelected('A36',Path)) -> (selectElement([['A38', false, 'R12'], Path])).

rule(Path) :-
    (not ruleAlreadyUsed('R13', Path), isSelected('A37',Path)) -> (selectElement([['A34', false, 'R13'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R14', Path), isSelected('A37',Path)) -> (selectElement([['A35', false, 'R14'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R15', Path), isSelected('A37',Path)) -> (selectElement([['A36', false, 'R15'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R16', Path), isSelected('A37',Path)) -> (selectElement([['A38', false, 'R16'], Path])).


rule(Path) :-
    (not ruleAlreadyUsed('R17', Path), isSelected('A38',Path)) -> (selectElement([['A34', false, 'R17'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R18', Path), isSelected('A38',Path)) -> (selectElement([['A35', false, 'R18'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R19', Path), isSelected('A38',Path)) -> (selectElement([['A36', false, 'R19'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R20', Path), isSelected('A38',Path)) -> (selectElement([['A37', false, 'R20'], Path])).

rule(Path) :-
    (not ruleAlreadyUsed('R21', Path), isSelected('A39',Path)) -> (selectElement([['A40', false, 'R21'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R22', Path), isSelected('A39',Path)) -> (selectElement([['A41', false, 'R22'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R23', Path), isSelected('A39',Path)) -> (selectElement([['A42', false, 'R23'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R24', Path), isSelected('A39',Path)) -> (selectElement([['A43', false, 'R24'], Path])).

rule(Path) :-
    (not ruleAlreadyUsed('R25', Path), isSelected('A40',Path)) -> (selectElement([['A39', false, 'R25'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R26', Path), isSelected('A40',Path)) -> (selectElement([['A41', false, 'R26'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R27', Path), isSelected('A40',Path)) -> (selectElement([['A42', false, 'R27'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R28', Path), isSelected('A40',Path)) -> (selectElement([['A43', false, 'R28'], Path])).


rule(Path) :-
    (not ruleAlreadyUsed('R29', Path), isSelected('A41',Path)) -> (selectElement([['A39', false, 'R29'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R30', Path), isSelected('A41',Path)) -> (selectElement([['A40', false, 'R30'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R31', Path), isSelected('A41',Path)) -> (selectElement([['A42', false, 'R31'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R32', Path), isSelected('A41',Path)) -> (selectElement([['A43', false, 'R32'], Path])).

rule(Path) :-
    (not ruleAlreadyUsed('R33', Path), isSelected('A42',Path)) -> (selectElement([['A39', false, 'R33'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R34', Path), isSelected('A42',Path)) -> (selectElement([['A40', false, 'R34'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R35', Path), isSelected('A42',Path)) -> (selectElement([['A41', false, 'R35'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R36', Path), isSelected('A42',Path)) -> (selectElement([['A43', false, 'R36'], Path])).


rule(Path) :-
    (not ruleAlreadyUsed('R37', Path), isSelected('A43',Path)) -> (selectElement([['A39', false, 'R37'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R38', Path), isSelected('A43',Path)) -> (selectElement([['A40', false, 'R38'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R39', Path), isSelected('A43',Path)) -> (selectElement([['A41', false, 'R39'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R40', Path), isSelected('A43',Path)) -> (selectElement([['A42', false, 'R40'], Path])).

rule(Path) :-
    (not ruleAlreadyUsed('R41', Path), isSelected('A26',Path)) -> (selectElement([['A27', false, 'R41'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R42', Path), isSelected('A26',Path)) -> (selectElement([['A28', false, 'R42'], Path])).

rule(Path) :-
    (not ruleAlreadyUsed('R43', Path), isSelected('A27',Path)) -> (selectElement([['A26', false, 'R43'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R44', Path), isSelected('A27',Path)) -> (selectElement([['A28', false, 'R44'], Path])).

rule(Path) :-
    (not ruleAlreadyUsed('R45', Path), isSelected('A28',Path)) -> (selectElement([['A26', false, 'R45'], Path])).
rule(Path) :-
    (not ruleAlreadyUsed('R46', Path), isSelected('A28',Path)) -> (selectElement([['A27', false, 'R46'], Path])).

rule(Path) :-
    (not ruleAlreadyUsed('R47', Path), isSelected('A39',Path)) -> (selectElement([['A27', true, 'R47'], Path]), selectElement([['A34', true, 'R47'], Path])).
    
rule(Path).   
 