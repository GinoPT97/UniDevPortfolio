%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% Predicati di base per operare su liste
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% member(X,L): vero se X e' un elemento della lista L

  member(X,[X|_R]).
  member(X,[_Y|R]) :- 
      member(X,R).

% append(L1,L2,L3): vero se la lista L3 e' la concatenazione
% delle liste L1 ed L2

  append([],L2,L2).
  append([X|R1],L2,[X|R3]) :-
      append(R1,L2,R3).

% lungh(L,N): vero se N e' la lunghezza (- numero di elementi)
% della lista L

  lungh([],0).
  lungh([_X|R],N) :-
     lungh(R,NR),
     N is NR +1.

% prefix(L1,L2): vero se la lista L1 e' un prefisso della
% lista L2

  prefix(L1,L2) :-
      append(L1,_R,L2).

% sublst(P,S,N): vero se la lista P e' una sottolista della
% lista S, iniziante dall'elemento N

  sublst(P,S,N) :-
      append(A,_P2,S),
      append(P1,P,A),
      lungh(P1,N).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Goal d'esempio
%
% ?- member(b,[a,b,c]).
% yes
%
% ?- member(X,[a,b,c]).
% X = a;
% X = b;
% X = c;
% no
%
% ?- append([a,b],[c,d],L).
% L = [a,b,c,d]
%
% ?- append(L1,L2,[a,b,c]).
% L1 = [], L2 = [a,b,c]
% L1 = [a], L2 = [b,c];
% L1 = [a,b], L2 = [c];
% L1 = [a,b,c], L2 = [];
%
% ?- prefix([a,b],[a,b,c]).
% yes
%
% ?- prefix([b,c],[a,b,c]).
% no
%
% ?- sublst([b,c],[a,b,c,d,e],N).
% N = 1
%
