/* Descrizione del gioco */

start([[1,2,3],[4,5,6],[7,8,9]]).

adversary(x,o).
adversary(o,x).

move(XorO, N, Board1, Board2) :-
   \+ win(_,Board1),
   append(RowsBefore, [Row|RowsAfter], Board1),
   append(CellsBefore, [N|CellsAfter], Row),
   number(N),
   append(CellsBefore, [XorO|CellsAfter], NewRow),
   append(RowsBefore, [NewRow|RowsAfter], Board2).

win(P, [[P,P,P],_,_]).
win(P, [_,[P,P,P],_]).
win(P, [_,_,[P,P,P]]).
win(P, [[P,_,_],[P,_,_],[P,_,_]]).
win(P, [[_,P,_],[_,P,_],[_,P,_]]).
win(P, [[_,_,P],[_,_,P],[_,_,P]]).
win(P, [[P,_,_],[_,P,_],[_,_,P]]).
win(P, [[_,_,P],[_,P,_],[P,_,_]]).

non_final(Board) :-
   \+ win(_,Board),
   member(Row,Board),
   member(Cell,Row),
   number(Cell).

final(Board) :-
   \+ non_final(Board).

/* strategie */

has_tie_strat(_,_,Board) :-
   final(Board),
   \+ win(_,Board).
has_tie_strat(P,Move,Board) :-
   move(P,Move,Board,Board2),
   adversary(P,Adv),
   \+ has_win_strat(Adv,_, Board2).

has_win_strat(P,_,Board) :-
   win(P,Board).
has_win_strat(P,Move,Board) :-
   move(P,Move,Board,Board2),
   adversary(P,Adv),
   \+ has_tie_strat(Adv,_,Board2).


/* svolgimento */

main :-
   Choice is random(2),
   nth0(Choice,[(user,o),(cpu,x)],(First,Symbol)),
   format('\nThe ~a starts \n', [First]),
   start(Board),
   turn(First,Symbol,Board),
   halt.

go(First,Symbol) :-
   member(First,[user,cpu]),
   start(Board),
   turn(First,Symbol,Board).

turn(_,_,Board) :-
   final(Board),
   \+ win(_,Board),
   format('\nThe game ends in a tie.\n').

turn(P,_,Board) :-
   win(_,Board),
   member(Adv,[user,cpu]), Adv \= P,
   format('\nThe ~a wins! \n', [Adv]).
   
turn(user,P,Board) :-
   read_move(P,M),
   move(P, M, Board, Board2),
   print_board(Board2),
   adversary(P,Adv),
   turn(cpu,Adv,Board2).
   
turn(cpu,P,Board) :-
   (has_win_strat(P,Move,Board); has_tie_strat(P,Move,Board)),
   format('\ncpu move: ~a \n', [Move]),
   move(P, Move, Board, Board2),
   print_board(Board2),
   adversary(P,Adv),
   turn(user,Adv,Board2).


/* user interface */

print_board([]).
print_board([Row|Rest]) :-
   format('~a|~a|~a \n', Row),
   print_board(Rest).

read_move(Player,Move) :-
   format('\nPlayer ~a insert your move [1-9]: ', [Player]),
   get_single_char(Char), put_char(Char), nl,
   Move is Char-48,
   Move >= 1, Move =< 9.

