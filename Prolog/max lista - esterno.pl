%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% Programma per il calcolo del massimo in una lista di numeri.
% 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% Soluzione dichiarativa: max(L,X) e' vero se X e' un elemento
% di L e, per ogni Y appartente ad L, e' vero che X >= Y
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%% max_decl(L,X): vero se X e' il massimo della lista non-vuota L.

max_decl(L,X):-
    member(X,L),
    testmax(X,L).

%%% testmax(X,L): vero se X e' un numero maggiore o uguale di
%%% tutti gli elementi contenuti in L (L anche vuota)
%%% (ovvero, forall Y (Y in L -> X >= Y))

testmax(_,[]).          
testmax(X,[Y|R]) :-
     X >= Y,
     testmax(X,R).

member(X,[X|_L]).
member(X,[_Y|L]) :- 
     member(X,L). 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% Soluzione ricorsiva 
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%% max_ric(L,X): vero se X e' il massimo della lista non-vuota L.

max_ric([X],X).
max_ric([X|R],MaxFinale) :-
    max_ric(R,TempMax),
    maggiore2(X,TempMax,MaxFinale).

%%% maggiore2(X,Y,Max): vero se Max e' il maggiore 
%%% tra X e Y 

maggiore2(X,Y,X) :-
    X >= Y. 
maggiore2(X,Y,Y) :-
    X < Y.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% Soluzione iterativa ("tail-recursive")
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%% max_iter(L,X): vero se X e' il massimo della lista non-vuota L.

max_iter(L,X) :-
    max_iter_cont(L,0,X).

max_iter_cont([X],MaxAttuale,MaxFinale) :-
    maggiore2(X,MaxAttuale,MaxFinale).
max_iter_cont([X|R],MaxAttuale,MaxFinale) :-
    maggiore2(X,MaxAttuale,MaxNuovo),
    max_iter_cont(R,MaxNuovo,MaxFinale).
