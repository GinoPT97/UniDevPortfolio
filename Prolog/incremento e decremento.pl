incr([],[]).
incr([H|T],[H1|T1]) :-
    incr(T,T1),
    H1 is H+1.

decr([],[]).
decr([H|T],[H1|T1]) :-
    decr(T,T1),
    H1 is H - 1.