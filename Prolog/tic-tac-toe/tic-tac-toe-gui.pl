:- op(1, fx, @).  /* serve per compilazione stand-alone */

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



/* GUI logic */

gui :-
   /* costruzione bottoni */
   new(Msg,button('Your turn')),
   setof(B, N^(between(1,9,N), new(B,button('\n'))), Buttons),
   Buttons = [B1,B2,B3,B4,B5,B6,B7,B8,B9],
   /* costruzione finestra */
   new(Dialog, dialog('Tic Tac Toe')),
   send(Dialog, gap, size(10,10)),
   send(Dialog, append, B1),
   send(Dialog, append, B2, right),
   send(Dialog, append, B3, right),
   send(Dialog, append, B4, below),
   send(Dialog, append, B5, right),
   send(Dialog, append, B6, right),
   send(Dialog, append, B7, below),
   send(Dialog, append, B8, right),
   send(Dialog, append, B9, right),
   send(Dialog, append, Msg, below),
   send(Msg, alignment, left),
   send(Dialog, open),
   /* il gioco inizia */
   gui_go(Msg,Buttons).

click(I, B1,B2,B3,B4,B5,B6,B7,B8,B9,Msg) :-
   Buttons= [B1,B2,B3,B4,B5,B6,B7,B8,B9],
   /* applica mossa utente */
   gui_move(I, Buttons, 'O', o),
   /* turno della cpu */
   read_board(Board),
   (
     final(Board) -> final_msg(Msg,Board,Buttons)
   ;
     (has_win_strat(x,Move,Board); has_tie_strat(x,Move,Board)), 
     /* applica la mossa */
     gui_move(Move, Buttons, 'X', x),
     /* controlla risultato */
     read_board(Brd2),
     (final(Brd2) -> final_msg(Msg,Brd2,Buttons) ; true)
   ).

gui_move(Move, Buttons, Label, Player) :-
     nth1(Move, Buttons, Bm),
     send(Bm, label, Label),
     send(Bm, size, size(45,45)),
     send(Bm, message, message(@prolog, true)),
     set_cell(Move, Player).

gui_go(B1,B2,B3,B4,B5,B6,B7,B8,B9,Msg) :-
   Buttons= [B1,B2,B3,B4,B5,B6,B7,B8,B9],
   gui_go(Msg,Buttons).

gui_go(Msg,Buttons) :-
   gui_init(Msg, Buttons),
   random_start(Buttons).

gui_init(Msg, Buttons) :-
   Buttons= [B1,B2,B3,B4,B5,B6,B7,B8,B9],
   send(Msg, message, message(@prolog, true)),
   send(Msg, label, 'Your turn'),
   send(Msg, size, size(165,30)),
   forall( between(1,9,I), 
      (
      nth1(I,Buttons, Bn),
      send(Bn, message, message(@prolog, click, I, B1,B2,B3,B4,B5,B6,B7,B8,B9,Msg)),
      send(Bn, label, '\n'),
      send(Bn, size, size(45,45))
      )
   ),
   reset_board.

random_start(Buttons) :-
   First is random(2),
   ( First=0 -> /* inizia la cpu */
      Move is random(9)+1,
      /* applica la mossa */
      gui_move(Move, Buttons, 'X', x)
     ;
     true /* si passa il controllo all'utente */
   ).
   

   
final_msg(Msg,Board,Buttons) :-
   (
   win(x,Board) -> 
      send(Msg, label, 'THE CPU WINS')
   ;
      send(Msg, label, 'THE GAME ENDS IN A TIE')
   ),
   send(Msg, size, size(165,30)),
   Buttons= [B1,B2,B3,B4,B5,B6,B7,B8,B9],
   send(Msg, message, message(@prolog, gui_go, B1,B2,B3,B4,B5,B6,B7,B8,B9,Msg)).

:- dynamic cell/2.

reset_board :-
   once(retractall(cell(_,_))),
   forall( between(1,9,N), assert(cell(N,N)) ).

set_cell(N,Sym) :-
   retract(cell(N,N)),
   assert(cell(N,Sym)).

read_board(Board) :-
   Board = [Row1, Row2, Row3],
   findall(Cell, (between(1,3,N),cell(N,Cell)), Row1),
   findall(Cell, (between(4,6,N),cell(N,Cell)), Row2),
   findall(Cell, (between(7,9,N),cell(N,Cell)), Row3).


