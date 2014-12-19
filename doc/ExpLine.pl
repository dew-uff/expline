selectElement([[E, true], Path]) :- write(E),
    not isSelected(E,Path),
    not isDesselected(E,Path),
    write(' notSelected '),
    write(Path),
    assertz(currentSelection(E,Path, true)),
%    write('Before flag '),
%    flagAsIncomplete(Path),
    write('Selected '),
    rule([[E, true]|Path]), write('EndSelect ').

selectElement([[E, false], Path]) :- write(E),
    not isDesselected(E,Path),
    not isSelected(E,Path),
    write(' notSelected '),
    write(Path),
    assertz(currentDesselection(E,Path, true)),
%    write('Before flag '),
%    flagAsIncomplete(Path),
    write('Selected '),
    rule([[E, true]|Path]), write('EndSelect ').    

flagAsIncomplete([[E, true]| Path]):- write('flag1.1 '), write(E), retract(currentSelection(E, Path, true)), write('flag1.2 '), assertz(currentSelection(E, Path, false)), !.
flagAsIncomplete([[E, true]| Path]):- write('flag1a.1 '), currentSelection(E, Path, false), write('flag1a.2 '), !.
flagAsIncomplete([[E, false]| Path]):- write('flag2.1 '), retract(currentDesselection(E, Path, true)), write('flag2.2 '), assertz(currentDesselection(E, Path, false)), !.
flagAsIncomplete([[E, false]| Path]):- write('flag2a.1 '), currentDesselection(E, Path, false), write('flag2a.2 '), !.
flagAsIncomplete([]):- write('flag0').
   
isSelected(E,Path) :-
    currentSelection(E,Path, _), 
    !.
   
isSelected(E,[Head|Path]) :-
    not currentSelection(E,[Head|Path], _),  
    isSelected(E,Path).
   
isDesselected(E,Path) :-
    currentDesselection(E,Path, _),
    !.
   
isDesselected(E,[Head|Path]) :-
    not currentDesselection(E,[Head|Path], _),
    isDesselected(E,Path).
   
rule(Path) :- write('rule1 '),
    (abstractWorkflow('B1') ; isSelected('B1',Path)) -> (add_to_queue([['E1', true], Path]) ; add_to_queue([['D1', true], Path])). 
rule(Path) :- write('rule2 '),
    (abstractWorkflow('D1') ; isSelected('D1',Path)) -> add_to_queue([['F1', true], Path]).
rule(Path) :- write('rule3 '),
    (abstractWorkflow('F1') ; isSelected('F1',Path)) -> add_to_queue([['B1', false], Path]).
rule(Path).

%queue([]).
queue([[['B1', true], []]]).  
%queue([[['B1'], []], [['D1'], ['B1']], [['F1'], ['D1', 'B1']]]).

add_to_queue(E) :- write('adicionando '), write(E), retract(queue(Queue)), add_to_queue2(E, Queue, NewQueue), assertz(queue(NewQueue)), !.
add_to_queue2(E, [], [E]).
add_to_queue2(E, [H|T], [H|Tnew]) :- add_to_queue2(E, T, Tnew). 
   
next_from_queue(E) :- write('proximo '), retract(queue(Queue)), next_from_queue2(E, Queue, NewQueue), write(E), assertz(queue(NewQueue)), !.
       
next_from_queue2(E, [E|T], T).            
processImplications(_) :- write('BeforeQueue1 '), next_from_queue(Next), write('AfterQueue '), selectElement(Next).
%processImplications(_) :- write('BeforeQueue1 '), next_from_queue([[E, true], Path]), write('AfterQueue '), flagAsIncomplete(Path), selectElement([[E, true], Path]).
processImplications(_). 