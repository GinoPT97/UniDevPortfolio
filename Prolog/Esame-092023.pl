hasSize(file1, 10, mega).
hasSize(file2, 11, mega).
hasSize(file3, 5,5 , giga).
thousand(kilo , mega).
thousand(mega,gia).
thousand(giga, tera).
bigger(A,B) :- hasSize(A,SA,Unit),hasSize(B,SB,Unit), SA>SB.
bigger(A,B) :- hasSize(A, _ , UnitA), hasSize(B,SB,UnitB), thousand(unitB,UnitA),SA<1000.
