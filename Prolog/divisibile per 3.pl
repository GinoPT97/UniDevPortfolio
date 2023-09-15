divisibile([],[]).
divisibile([H|T],[H|T1]) :- H mod 3 =:= 0, divisibile(T, T1).
divisibile([H|T],T1) :- H mod 3 =\= 0, divisibile(T, T1).
