% consult("~/LP1/Slide-Piero/Sorgenti-PROLOG/codice/tipi-java.pl").

/* Casi base di assegnabilità (senza riflessività). */
assegnabile0(byte,short).
assegnabile0(short,int).
assegnabile0(int,long).
assegnabile0(long,float).
assegnabile0(float,double).
assegnabile0(char,int).

/* Esperimento: aggiungendo la seguente regola, assegnabile(int,object) diverge. */
%assegnabile0(X,X).

rif(object).
rif(null).
rif(array(_)).
rif(person).
rif(employee).
rif(manager).

/* Casi base di sottotipo (senza riflessività) */
/* Esperimento: togliendo la clausola \+ X=object, sottotipo(object,int) diverge. */
sottotipo0(X,object) :- rif(X), \+ X=object.
sottotipo0(null,X)   :- rif(X), \+ X=null.

sottotipo0(manager,employee).
sottotipo0(employee,person).

/* La regola degli array. */
sottotipo0(array(X), array(Y)) :- sottotipo0(X, Y).

/* Sottotipo è la chiusura riflessiva (limitata ai riferimenti)
   e transitiva di sottotipo0.
   Nota: sottotipo0 è aciclica. */
sottotipo(X,X) :- rif(X).
sottotipo(X,Y) :- sottotipo0(X,Y).
sottotipo(X,Y) :- sottotipo0(X,Z), sottotipo(Z,Y).

/* Assegnabile è la chiusura riflessiva e transitiva di assegnabile0.
   Nota: assegnabile0 è aciclica. */
assegnabile(X,X).
assegnabile(X,Y) :- assegnabile0(X,Y).
assegnabile(X,Y) :- assegnabile0(X,Z), assegnabile(Z,Y). 

/* Aggiungi ad assegnabile la relazione di sottotipo */
assegnabile(X,Y) :- sottotipo(X,Y).




/* Aggiungi a sottotipo la chiusura transitiva di sottotipo0 */
/*
sottotipo_avoiding(X,X,Avoid) :- \+ member(X,Avoid),
				 sottotipo0(X,X).
sottotipo_avoiding(X,Y,Avoid) :- sottotipo0(X,Z),
				 \+ member(Z,Avoid),
				 sottotipo_avoiding(Z,Y,[X|Avoid]).
*/
