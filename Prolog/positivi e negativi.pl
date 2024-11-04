pos([],[]).
pos([H|T],[H|T1]) :- H > 0, pos(T,T1).
pos([H|T],T1):- H < 0, pos(T,T1).


neg([],[]).
neg([H|T],[H|T1]) :- H < 0, neg(T,T1).
neg([H|T],T1):- H > 0, neg(T,T1).
