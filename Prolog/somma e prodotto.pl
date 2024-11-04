somma([],0).
somma([H|T],N) :-
    somma(T,X),
    N is  H + X.

prod([],1).
prod([H|T],N) :-
    prod(T,X),
    N is X * H.
