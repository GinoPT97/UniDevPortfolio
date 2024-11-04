concatena([], L, L).
concatena([X|Rest1], L2, [X|Rest2]) :- concatena(Rest1, L2, Rest2).

sottolista([], _).
sottolista([X|Rest], Lista) :-
    append(_, [X|RestSottolista], Lista),
    sottolista(Rest, RestSottolista).

ultimo([X], X).
ultimo([_|Rest], Last) :- ultimo(Rest, Last).

duplica([], []).
duplica([X|Rest], [X,X|DupRest]) :- duplica(Rest, DupRest).

sottoinsieme([], _).
sottoinsieme([X|Rest], Insieme) :-
    member(X, Insieme),
    sottoinsieme(Rest, Insieme).

rimuovi_duplicati([], []).
rimuovi_duplicati([X|Rest], SenzaDuplicati) :-
    member(X, Rest),
    rimuovi_duplicati(Rest, SenzaDuplicati).
rimuovi_duplicati([X|Rest], [X|SenzaDuplicati]) :-
    \+ member(X, Rest),
    rimuovi_duplicati(Rest, SenzaDuplicati).

ordinata([]).
ordinata([_]).
ordinata([X,Y|Rest]) :-
    X =< Y,
    ordinata([Y|Rest]).

unisci([], L, L).
unisci(L, [], L).
unisci([X|Rest1], [Y|Rest2], [X|Resta]) :-
    X =< Y,
    unisci(Rest1, [Y|Rest2], Resta).
unisci([X|Rest1], [Y|Rest2], [Y|Resta]) :-
    X > Y,
    unisci([X|Rest1], Rest2, Resta).

