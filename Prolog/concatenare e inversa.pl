ap([],L,L).
ap([H|T],L,[H|Z]):-
    ap(T,L,Z).

inversa([],[]).
inversa([X|L],R):-
  inversa(L,RL),
  append(RL,[X],R).

