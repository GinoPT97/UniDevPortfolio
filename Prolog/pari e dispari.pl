pari([],[]).
pari([H|T],[H|T1]):-0 is H mod 2,pari(T,T1).
pari([_|T],L) :- pari(T,L).

dipari([],[]).
dipari([H|T],[H|T1]):-1 is H mod 2, dipari(T,T1).
dipari([_|T],L) :- dipari(T,L).
