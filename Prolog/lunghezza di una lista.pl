lunghezza([],0).
lunghezza([_|T],N):-
    lunghezza(T,T1),
    N is T1 + 1.
