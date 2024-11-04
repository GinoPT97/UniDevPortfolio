lun([],0).
lun([_|T],N) :-
    lun(T,T1),
    N is T1+1.

somma([],0).
somma([H|T],S):-
  somma(T,T1),
  S is T1 + H.

media([],0).
media(L,M) :-
      somma(L,S),
      lun(L,N),
      M is S / N.
