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
  