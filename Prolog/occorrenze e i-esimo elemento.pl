occ([],_,0).
occ([H|T],H,N) :- occ(T,H,N1), N is N1 + 1.
occ([H|T],H1,N) :- occ(T,H1,N), H\=H1.

elemi([],N,false):- N > 0.
elemi([H|_],1,H).
elemi([_|T],N,H1) :- N1 is N-1 , elemi(T,N1,H1).