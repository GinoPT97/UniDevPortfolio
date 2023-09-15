lung([],0).
lung([_|T],N):-
     lung(T,N1),
     N is N1+1.

divid([],N,[]).
divid([H|T],N,[H1|T1]) :-
     divid(T,N,T1),
     H1 is H / N.

divid(L,L1) :-
    lung(L,N),
    divid(L,N,L1).
