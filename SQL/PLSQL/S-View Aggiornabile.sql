Le viste sono aggiornabili se:
1) Costruite con SELECT che non usano giunzioni nella clausola FROM
2) Proiettano i campi chiave
3) Non devono contenere raggruppamenti (niente GROUP BY)

Operazioni si possono fare se:
1) Delete: se si lavora su colonne della tabella principale
2) Update: stesso motivo,  no su attributi invisibili
3) Insert: solo se la view include tutti attributi NOT NULL
____________
__Esempio:__

Studente(CF, Matr)
Esami(Matr, Corso)

$ CREATE VIEW view1(newcol1, newcol2, newcol3, newcol4)
$ AS 
$ SELECT *
$ FROM Studenti AS S
$ LEFT OUTER JOIN Esami AS E
$ ON S.Matr = E.Matr
$ 
$ CREATE TRIGGER ins_v
$ INSTEAD OF INSERT ON view1
$ FOR EACH ROW
$ BEGIN
$   IF(New.Corso IS NULL) THEN
$     INSERT INTO Studente VALUES(New.CF, ...)
$   ELSE
$     INSERT INTO Esami VALUES(New.Matr, ...)
$   END IF
$ END



