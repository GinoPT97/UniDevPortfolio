-- Cliente
INSERT INTO cliente (codcliente, nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES
('11111','aldo','marzante','BBBBBB11B11B111B', 'Via Don Matteo','1234567890','aldo@arte.it'),
('22222','luca','benson','AAAAAA22A22A222A', 'Via Don Corleone','1234567890','luca@arte.it'),
('33333','mario','sarni','CCCCCC33C33C333C','Via San giovanni','1234567890','mario@arte.it'),
('77777','alessio','sassi','DDDDDD44D44D444D','Via cremoni','1234567890','alessio@arte.it'),
('44455','giorgio','rossi','WWWWWW55W55W555W', 'Via Don Carlo','1234567890','giorgio@arte.it'),
('55566','paolo','verdi','QQQQQQ66Q66Q666Q', 'Via Don Alberto','1234567890','paolo@arte.it'),
('66677','simone','bianchi','UUUUUU77U77U777U','Via Don Giuseppe','1234567890','simone@arte.it'),
('88899','enrico','gialli','VVVVVV99V99V999V','Via Don Mario','1234567890','enrico@arte.it');

-- Dipendente
INSERT INTO dipendente (coddipendente, nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES
('89899','dario','forte','FFFFFF11F11F111F','via andromeda','1234567890','dario@arte.it'),
('79799','sandro','romano','LLLLLL22L22L222L','via omega','1234567890','sandro@arte.it'),
('34345','giulio','cesare','PPPPPP88P88P888P','via roma','1234567890','giulio@arte.it'),
('11111','mario','rossi','YYYYYY11Y11Y111Y','via parma','1234567890','mario@arte.it'),
('22222','andrea','verdi','HHHHHH22H22H222H','via milano','1234567890','andrea@arte.it'),
('33333','giuseppe','bianchi','KKKKKK88K88K888K','via torino','1234567890','giuseppe@arte.it'),
('44444','marco','gialli','GGGLLN80A01H501P','via napoli','1234567890','marco@arte.it');

-- Tessera
INSERT INTO tessera (codtessera, numeropunti, codcliente) VALUES
('55555','20','11111'),
('44444','30','22222'),
('66666','100','33333'),
('88888','0','77777'),
('77777','50','44455'),
('66667','80','55566'),
('55551','10','66677'),
('33333','150','88899');

-- Prodotto
INSERT INTO prodotto (codprodotto, nome, descrizione, prezzo, luogoprovenienza, dataraccolta, datamungitura, glutine, datascadenza, categoria, scorta) VALUES
('11111', 'Mela Rossa', 'Mela italiana rossa', 1.50, 'Italia', '2022-07-01', NULL, NULL, NULL, 'Ortofrutticoli', 100),
('22222', 'Formaggio Parmigiano', 'Formaggio Parmigiano Reggiano', 15.00, 'Italia', NULL, '2023-06-01', NULL, NULL, 'Latticini', 50),
('33333', 'Pomodori in scatola', 'Pomodori pelati in scatola', 2.00, 'Italia', NULL, NULL, NULL, '2024-12-31', 'Inscatolati', 200),
('44444', 'Spaghetti', 'Spaghetti di grano duro', 1.20, 'Italia', NULL, NULL, TRUE, NULL, 'Farinacei', 300),
('55555', 'Arance', 'Arance siciliane', 1.20, 'Italia', '2022-07-01', NULL, NULL, NULL, 'Ortofrutticoli', 150),
('66666', 'Parmigiano', 'Parmigiano Reggiano DOP', 18.00, 'Italia', NULL, '2023-06-01', NULL, NULL, 'Latticini', 70),
('77777', 'Tonno in scatola', 'Tonno al naturale in scatola', 3.50, 'Italia', NULL, NULL, NULL, '2024-12-31', 'Inscatolati', 100),
('88888', 'Farina', 'Farina di grano tenero tipo "00"', 0.80, 'Italia', NULL, NULL, FALSE, NULL, 'Farinacei', 200);

-- Ordine
INSERT INTO ordine (codordine, prezzototale, dataacquisto, codcliente, coddipendente) VALUES
('12122','57','2001-02-12','11111','89899'),
('11112','45','2010-03-22','11111','89899'),
('11132','47','2011-07-02','11111','89899'),
('13112','105','2017-11-22','11111','79799'),
('13312','185','2007-10-02','11111','79799'),
('13512','15','2004-12-09','11111','34345'),
('14142','20','2009-11-08','55566','11111'),
('12131','55','2015-03-12','66677','22222'),
('15151','30','2023-05-15','88899','34345'),
('16161','80','2023-08-22','55566','79799'),
('17171','25','2024-01-10','33333','44444'),
('12132','120','2013-06-17','88899','33333'),
('18181','40','2023-12-05','44455','22222'),
('19191','65','2024-04-18','66677','33333'),
('20202','55','2024-02-28','88899','79799');

INSERT INTO articoliordine (CodOrdine, CodProdotto, CodCliente, prezzo, numeropunti, numeroarticoli, categoria) VALUES
('12122', '44444', '77777', '1.20', '3.00', '2', 'Farinacei'),
('13112', '22222', '55566', '15.00', '30.00', '2', 'Latticini'),
('11112', '33333', '77777', '2.00', '6.00', '3', 'Inscatolati'),
('13512', '22222', '66677', '15.00', '30.00', '2', 'Latticini'),
('13312', '33333', '44455', '2.00', '4.50', '3', 'Inscatolati'),
('12131', '33333', '88899', '2.00', '6.00', '3', 'Inscatolati'),
('12132', '44444', '77777', '1.50', '3.00', '2', 'Farinacei'),
('12122', '55555', '44455', '1.00', '2.50', '2', 'Ortofrutticoli'),
('12122', '33333', '77777', '1.80', '4.50', '3', 'Inscatolati'),
('13312', '77777', '44455', '1.50', '3.50', '2', 'Ortofrutticoli'),
('13512', '55555', '66677', '14.50', '29.00', '3', 'Latticini'),
('14142', '44444', '55566', '2.00', '4.00', '4', 'Inscatolati'),
('12131', '77777', '77777', '2.50', '6.00', '3', 'Farinacei'),
('15151', '33333', '88899', '2.00', '4.00', '3', 'Inscatolati'),
('15151', '77777', '44455', '1.50', '3.00', '2', 'Ortofrutticoli'),
('16161', '55555', '66677', '14.50', '29.00', '3', 'Latticini'),
('16161', '44444', '55566', '2.00', '4.00', '4', 'Inscatolati'),
('17171', '77777', '44455', '1.50', '3.50', '2', 'Ortofrutticoli'),
('18181', '77777', '77777', '2.50', '6.00', '3', 'Farinacei'),
('18181', '33333', '77777', '1.80', '4.50', '3', 'Inscatolati'),
('19191', '22222', '55566', '15.00', '30.00', '2', 'Latticini'),
('19191', '77777', '77777', '2.50', '6.00', '3', 'Farinacei'),
('20202', '44444', '77777', '1.20', '3.00', '2', 'Farinacei'),
('20202', '55555', '44455', '1.00', '2.50', '2', 'Ortofrutticoli');

