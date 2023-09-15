--Cliente

INSERT INTO Cliente VALUES ('11111','aldo','marzante','BBBBBB11B11B111B', 'Via Don Matteo','1234567890','aldo@arte.it');
INSERT INTO Cliente VALUES ('22222','luca','benson','AAAAAA22A22A222A', 'Via Don Corleone','1234567890','luca@arte.it');
INSERT INTO cliente VALUES ('33333','mario','sarni','CCCCCC33C33C333C','Via San giovanni','1234567890','mario@arte.it');
INSERT INTO cliente VALUES ('77777','alessio','sassi','DDDDDD44D44D444D','Via cremoni','1234567890','alessio@arte.it');
INSERT INTO Cliente VALUES ('44455','giorgio','rossi','WWWWWW55W55W555W', 'Via Don Carlo','1234567890','giorgio@arte.it');
INSERT INTO Cliente VALUES ('55566','paolo','verdi','QQQQQQ66Q66Q666Q', 'Via Don Alberto','1234567890','paolo@arte.it');
INSERT INTO cliente VALUES ('66677','simone','bianchi','UUUUUU77U77U777U','Via Don Giuseppe','1234567890','simone@arte.it');
INSERT INTO cliente VALUES ('88899','enrico','gialli','VVVVVV99V99V999V','Via Don Mario','1234567890','enrico@arte.it');

--Dipendente

INSERT INTO dipendente VALUES ('89899','dario','forte','FFFFFF11F11F111F','via andromeda','1234567890','dario@arte.it');
INSERT INTO dipendente VALUES ('79799','sandro','romano','LLLLLL22L22L222L','via omega','1234567890','sandro@arte.it');
INSERT INTO dipendente VALUES ('34345','giulio','cesare','PPPPPP88P88P888P','via roma','1234567890','giulio@arte.it');
INSERT INTO dipendente VALUES ('11111','mario','rossi','YYYYYY11Y11Y111Y','via parma','1234567890','mario@arte.it');
INSERT INTO dipendente VALUES ('22222','andrea','verdi','HHHHHH22H22H222H','via milano','1234567890','andrea@arte.it');
INSERT INTO dipendente VALUES ('33333','giuseppe','bianchi','KKKKKK88K88K888K','via torino','1234567890','giuseppe@arte.it');
INSERT INTO dipendente VALUES ('44444','marco','gialli','GGGLLN80A01H501P','via napoli','1234567890','marco@arte.it');

--Tessera

INSERT INTO tessera VALUES ('55555','20','11111');
INSERT INTO tessera VALUES ('44444','30','22222');
INSERT INTO tessera VALUES ('66666','100','33333');
INSERT INTO tessera VALUES ('88888','0','77777');
INSERT INTO tessera VALUES ('77777','50','44455');
INSERT INTO tessera VALUES ('66667','80','55566');
INSERT INTO tessera VALUES ('55551','10','66677');
INSERT INTO tessera VALUES ('33333','150','88899');

--Ordine

INSERT INTO ordine VALUES('12122','57','12/02/2001','11111','89899');
INSERT INTO ordine VALUES('11112','45','22/03/2010','11111','89899');
INSERT INTO ordine VALUES('11132','47','02/07/2011','11111','89899');
INSERT INTO ordine VALUES('13112','105','22/11/2017','11111','79799');
INSERT INTO ordine VALUES('13312','185','02/10/2007','11111','79799');
INSERT INTO ordine VALUES('13512','15','09/12/2004','11111','34345');
INSERT INTO ordine VALUES('14142','20','08/11/2009','55566','11111');
INSERT INTO ordine VALUES('12131','55','12/03/2015','66677','22222');
INSERT INTO ordine VALUES('12132','120','17/06/2013','88899','33333');

--Prodotto

INSERT INTO prodotto VALUES ('11111', 'Mela Rossa', 'Mela italiana rossa', 1.50, 'Italia', '2022-07-15', NULL, NULL, NULL, 'Ortofrutticoli', 100);
INSERT INTO prodotto VALUES ('22222', 'Formaggio Parmigiano', 'Formaggio Parmigiano Reggiano', 15.00, 'Italia', NULL, '2023-06-01', NULL, NULL, 'Latticini', 50);
INSERT INTO prodotto VALUES ('33333', 'Pomodori in scatola', 'Pomodori pelati in scatola', 2.00, 'Italia', NULL, NULL, NULL, '2024-12-31', 'Inscatolati', 200);
INSERT INTO prodotto VALUES ('44444', 'Spaghetti', 'Spaghetti di grano duro', 1.20, 'Italia', NULL, NULL, TRUE, NULL, 'Farinacei', 300);
INSERT INTO prodotto VALUES ('55555', 'Arance', 'Arance siciliane', 1.20, 'Italia', '2022-07-15', NULL, NULL, NULL, 'Ortofrutticoli', 150);
INSERT INTO prodotto VALUES ('66666', 'Parmigiano', 'Parmigiano Reggiano DOP', 18.00, 'Italia', NULL, '2023-06-01', NULL, NULL, 'Latticini', 70);
INSERT INTO prodotto VALUES ('77777', 'Tonno in scatola', 'Tonno al naturale in scatola', 3.50, 'Italia', NULL, NULL, NULL, '2024-12-31', 'Inscatolati', 100);
INSERT INTO prodotto VALUES ('88888', 'Farina', 'Farina di grano tenero tipo "00"', 0.80, 'Italia', NULL, NULL, FALSE, NULL, 'Farinacei', 200);

--Articoliordine

INSERT INTO articoliordine VALUES('12122', '44444', '77777', '1.20', '3.00', '2', 'Farinacei');
INSERT INTO articoliordine VALUES('11112', '33333', '77777', '2.00', '6.00', '3', 'Inscatolati');
INSERT INTO articoliordine VALUES('11132', '77777', '77777', '1.50', '3.50', '2', 'Ortofrutticoli');
INSERT INTO articoliordine VALUES('13112', '22222', '55566', '15.00', '30.00', '2', 'Latticini');
INSERT INTO articoliordine VALUES('13312', '33333', '44455', '2.00', '4.50', '3', 'Inscatolati');
INSERT INTO articoliordine VALUES('13512', '22222', '66677', '15.00', '30.00', '2', 'Latticini');
INSERT INTO articoliordine VALUES('12131', '33333', '88899', '2.00', '6.00', '3', 'Inscatolati');
INSERT INTO articoliordine VALUES('12132', '44444', '77777', '1.50', '3.00', '2', 'Farinacei');
INSERT INTO articoliordine VALUES ('12122', '33333', '77777', '1.80', '4.50', '3', 'Inscatolati');
INSERT INTO articoliordine VALUES ('12122', '55555', '44455', '1.00', '2.50', '2', 'Ortofrutticoli');
INSERT INTO articoliordine VALUES ('13312', '77777', '44455', '1.50', '3.50', '2', 'Ortofrutticoli');
INSERT INTO articoliordine VALUES ('13512', '55555', '66677', '14.50', '29.00', '3', 'Latticini');
INSERT INTO articoliordine VALUES ('14142', '44444', '55566', '2.00', '4.00', '4', 'Inscatolati');
INSERT INTO articoliordine VALUES ('12131', '77777', '77777', '2.50', '6.00', '3', 'Farinacei');

