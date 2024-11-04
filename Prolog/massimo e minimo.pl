minl([], 0).
minl([H|T], M) :-
    minl(T, TM),
    M is min(H, TM).

maxl([], 0).
maxl([H|T], M) :-
    maxl(T, TM),
    M is max(H, TM).
