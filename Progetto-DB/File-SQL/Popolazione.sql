-- Popolazione database 

-- Inserimento clienti
INSERT INTO cliente (nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES
('Aldo', 'Marzante', 'MRZNLD85M15H501Z', 'Via Don Matteo 1', '3331234567', 'aldo.marzante@email.it'),
('Luca', 'Benson', 'BNSLCU90A10F205X', 'Via Don Corleone 2', '3332345678', 'luca.benson@email.it'),
('Mario', 'Sarni', 'SRNMRA88C20G273Y', 'Via San Giovanni 3', '3333456789', 'mario.sarni@email.it'),
('Alessio', 'Sassi', 'SSSLSS92E15H501W', 'Via Cremoni 4', '3334567890', 'alessio.sassi@email.it'),
('Giorgio', 'Rossi', 'RSSGGI87H10A662V', 'Via Don Carlo 5', '3335678901', 'giorgio.rossi@email.it'),
('Paolo', 'Verdi', 'VRDPLA91L25F839U', 'Via Don Alberto 6', '3336789012', 'paolo.verdi@email.it'),
('Simone', 'Bianchi', 'BNCSMN89M30L219T', 'Via Don Giuseppe 7', '3337890123', 'simone.bianchi@email.it'),
('Enrico', 'Gialli', 'GLLENR93P05H501S', 'Via Don Mario 8', '3338901234', 'enrico.gialli@email.it');

-- Inserimento tessere
INSERT INTO tessera (codcliente, numeropunti, dataemissione, datascadenza) VALUES
(1, 0.00, CURRENT_DATE - INTERVAL '2 years', CURRENT_DATE + INTERVAL '6 months'),
(2, 0.00, CURRENT_DATE - INTERVAL '18 months', CURRENT_DATE + INTERVAL '6 months'),
(3, 0.00, CURRENT_DATE - INTERVAL '1 year', CURRENT_DATE + INTERVAL '1 year'),
(4, 0.00, CURRENT_DATE - INTERVAL '8 months', CURRENT_DATE + INTERVAL '16 months'),
(5, 0.00, CURRENT_DATE - INTERVAL '6 months', CURRENT_DATE + INTERVAL '18 months'),
(6, 0.00, CURRENT_DATE - INTERVAL '4 months', CURRENT_DATE + INTERVAL '20 months'),
(7, 0.00, CURRENT_DATE - INTERVAL '3 months', CURRENT_DATE + INTERVAL '21 months'),
(8, 0.00, CURRENT_DATE - INTERVAL '2 months', CURRENT_DATE + INTERVAL '22 months');

-- Inserimento dipendenti
INSERT INTO dipendente (nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES
('Dario', 'Forte', 'FRTDRA86A15H501A', 'Via Andromeda 1', '3901234567', 'dario.forte@negozio.it'),
('Sandro', 'Romano', 'RMNSDR88B20F205B', 'Via Omega 2', '3902345678', 'sandro.romano@negozio.it'),
('Giulio', 'Cesare', 'CSRGLI90C25G273C', 'Via Roma 3', '3903456789', 'giulio.cesare@negozio.it'),
('Mario', 'Rossi', 'RSSMRA85D30H501D', 'Via Parma 4', '3904567890', 'mario.rossi2@negozio.it'),
('Andrea', 'Verdi', 'VRDNDR89E10A662E', 'Via Milano 5', '3905678901', 'andrea.verdi@negozio.it'),
('Giuseppe', 'Bianchi', 'BNCGPP91F15F839F', 'Via Torino 6', '3906789012', 'giuseppe.bianchi@negozio.it'),
('Marco', 'Gialli', 'GLLMRC87G20L219G', 'Via Napoli 7', '3907890123', 'marco.gialli@negozio.it');

-- Inserimento prodotti con date corrette
INSERT INTO prodotto (nome, descrizione, prezzo, luogoprovenienza, dataraccolta, datamungitura, glutine, datascadenza, dataproduzione, categoria, scorta) VALUES
-- FRUTTA (dataraccolta recente)
('Mela Rossa', 'Mela italiana rossa', 1.50, 'Trentino', CURRENT_DATE - INTERVAL '5 days', NULL, NULL, NULL, NULL, 'FRUTTA', 100),
('Arance', 'Arance siciliane', 1.20, 'Sicilia', CURRENT_DATE - INTERVAL '7 days', NULL, NULL, NULL, NULL, 'FRUTTA', 150),
('Banane', 'Banane ecuadoriane', 2.00, 'Ecuador', CURRENT_DATE - INTERVAL '3 days', NULL, NULL, NULL, NULL, 'FRUTTA', 80),
('Pere', 'Pere Williams', 1.80, 'Emilia-Romagna', CURRENT_DATE - INTERVAL '4 days', NULL, NULL, NULL, NULL, 'FRUTTA', 90),

-- VERDURA (dataraccolta recente)
('Carote', 'Carote fresche', 1.00, 'Emilia-Romagna', CURRENT_DATE - INTERVAL '2 days', NULL, NULL, NULL, NULL, 'VERDURA', 120),
('Zucchine', 'Zucchine fresche', 1.30, 'Lazio', CURRENT_DATE - INTERVAL '3 days', NULL, NULL, NULL, NULL, 'VERDURA', 130),
('Pomodori', 'Pomodori freschi', 2.50, 'Campania', CURRENT_DATE - INTERVAL '1 day', NULL, NULL, NULL, NULL, 'VERDURA', 90),
('Insalata', 'Insalata iceberg', 1.50, 'Lazio', CURRENT_DATE - INTERVAL '1 day', NULL, NULL, NULL, NULL, 'VERDURA', 60),

-- LATTICINI (datamungitura, dataproduzione e datascadenza future)
('Latte Fresco', 'Latte fresco intero', 1.20, 'Lombardia', NULL, CURRENT_DATE - INTERVAL '2 days', NULL, CURRENT_DATE + INTERVAL '5 days', CURRENT_DATE - INTERVAL '1 day', 'LATTICINI', 80),
('Parmigiano Reggiano', 'Parmigiano Reggiano DOP 24 mesi', 18.00, 'Emilia-Romagna', NULL, CURRENT_DATE - INTERVAL '730 days', NULL, CURRENT_DATE + INTERVAL '180 days', CURRENT_DATE - INTERVAL '700 days', 'LATTICINI', 50),
('Mozzarella', 'Mozzarella di bufala campana', 2.50, 'Campania', NULL, CURRENT_DATE - INTERVAL '2 days', NULL, CURRENT_DATE + INTERVAL '7 days', CURRENT_DATE - INTERVAL '1 day', 'LATTICINI', 60),
('Yogurt', 'Yogurt naturale', 0.90, 'Piemonte', NULL, CURRENT_DATE - INTERVAL '5 days', NULL, CURRENT_DATE + INTERVAL '15 days', CURRENT_DATE - INTERVAL '4 days', 'LATTICINI', 90),
('Ricotta', 'Ricotta vaccina fresca', 2.20, 'Puglia', NULL, CURRENT_DATE - INTERVAL '1 day', NULL, CURRENT_DATE + INTERVAL '5 days', CURRENT_DATE, 'LATTICINI', 40),

-- FARINACEI (glutine)
('Spaghetti', 'Spaghetti di grano duro', 1.20, 'Puglia', NULL, NULL, true, NULL, NULL, 'FARINACEI', 300),
('Pane Integrale', 'Pane integrale', 1.50, 'Toscana', NULL, NULL, true, NULL, NULL, 'FARINACEI', 180),
('Pasta Senza Glutine', 'Pasta di riso', 2.50, 'Veneto', NULL, NULL, false, NULL, NULL, 'FARINACEI', 150),
('Farina Doppio Zero', 'Farina di grano tenero tipo 00', 0.80, 'Piemonte', NULL, NULL, true, NULL, NULL, 'FARINACEI', 200),
('Penne Rigate', 'Penne rigate di semola', 1.10, 'Campania', NULL, NULL, true, NULL, NULL, 'FARINACEI', 250),

-- UOVA (datascadenza futura)
('Uova Biologiche', 'Uova da allevamento biologico', 2.50, 'Umbria', NULL, NULL, NULL, CURRENT_DATE + INTERVAL '20 days', NULL, 'UOVA', 150),
('Uova Fresche', 'Uova fresche da galline allevate a terra', 2.00, 'Marche', NULL, NULL, NULL, CURRENT_DATE + INTERVAL '18 days', NULL, 'UOVA', 120),

-- CONFEZIONATI (datascadenza futura)
('Pomodori Pelati', 'Pomodori pelati in scatola', 2.00, 'Campania', NULL, NULL, NULL, CURRENT_DATE + INTERVAL '18 months', NULL, 'CONFEZIONATI', 200),
('Tonno in Scatola', 'Tonno al naturale in scatola', 3.50, 'Sicilia', NULL, NULL, NULL, CURRENT_DATE + INTERVAL '2 years', NULL, 'CONFEZIONATI', 100),
('Biscotti', 'Biscotti al cioccolato', 2.00, 'Lombardia', NULL, NULL, NULL, CURRENT_DATE + INTERVAL '6 months', NULL, 'CONFEZIONATI', 250),
('Cereali', 'Cereali integrali', 3.00, 'Emilia-Romagna', NULL, NULL, NULL, CURRENT_DATE + INTERVAL '8 months', NULL, 'CONFEZIONATI', 150),
('Miele', 'Miele millefiori', 5.00, 'Abruzzo', NULL, NULL, NULL, CURRENT_DATE + INTERVAL '3 years', NULL, 'CONFEZIONATI', 80),
('Passata Pomodoro', 'Passata di pomodoro', 1.80, 'Campania', NULL, NULL, NULL, CURRENT_DATE + INTERVAL '1 year', NULL, 'CONFEZIONATI', 180);

-- Inserimento ordini distribuiti temporalmente
-- Ultimi 3 mesi
INSERT INTO ordine (prezzototale, dataacquisto, codcliente, coddipendente) VALUES
(0, CURRENT_DATE - INTERVAL '10 days', 1, 1),
(0, CURRENT_DATE - INTERVAL '25 days', 2, 2),
(0, CURRENT_DATE - INTERVAL '45 days', 3, 3),
(0, CURRENT_DATE - INTERVAL '60 days', 4, 4),
(0, CURRENT_DATE - INTERVAL '75 days', 5, 5),

-- Ultimi 6 mesi
(0, CURRENT_DATE - INTERVAL '100 days', 6, 6),
(0, CURRENT_DATE - INTERVAL '120 days', 7, 7),
(0, CURRENT_DATE - INTERVAL '140 days', 8, 1),
(0, CURRENT_DATE - INTERVAL '160 days', 1, 2),

-- Ultimi 9 mesi
(0, CURRENT_DATE - INTERVAL '200 days', 2, 3),
(0, CURRENT_DATE - INTERVAL '220 days', 3, 4),
(0, CURRENT_DATE - INTERVAL '240 days', 4, 5),

-- Ultimi 12 mesi
(0, CURRENT_DATE - INTERVAL '280 days', 5, 6),
(0, CURRENT_DATE - INTERVAL '300 days', 6, 7),
(0, CURRENT_DATE - INTERVAL '320 days', 7, 1),
(0, CURRENT_DATE - INTERVAL '340 days', 8, 2);

-- Inserimento articoli negli ordini
-- Ordine 1: Cliente 1 (recente - 10 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(1, 1, 1.50, 3),  -- Mele rosse
(1, 5, 1.00, 2),  -- Carote
(1, 14, 1.20, 1); -- Spaghetti

-- Ordine 2: Cliente 2 (25 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(2, 2, 1.20, 5),  -- Arance
(2, 10, 18.00, 1); -- Parmigiano

-- Ordine 3: Cliente 3 (45 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(3, 9, 1.20, 2),  -- Latte fresco
(3, 19, 2.50, 6), -- Uova biologiche
(3, 21, 2.00, 4); -- Pomodori pelati

-- Ordine 4: Cliente 4 (60 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(4, 6, 1.30, 3),  -- Zucchine
(4, 11, 2.50, 2), -- Mozzarella
(4, 22, 3.50, 1); -- Tonno in scatola

-- Ordine 5: Cliente 5 (75 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(5, 15, 1.50, 2), -- Pane integrale
(5, 20, 2.00, 4); -- Uova fresche

-- Ordine 6: Cliente 6 (100 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(6, 3, 2.00, 2),  -- Banane
(6, 12, 0.90, 6); -- Yogurt

-- Ordine 7: Cliente 7 (120 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(7, 7, 2.50, 1),  -- Pomodori freschi
(7, 23, 2.00, 3); -- Biscotti

-- Ordine 8: Cliente 8 (140 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(8, 16, 2.50, 2), -- Pasta senza glutine
(8, 25, 5.00, 1); -- Miele

-- Ordine 9: Cliente 1 (160 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(9, 17, 0.80, 3), -- Farina
(9, 24, 3.00, 2); -- Cereali

-- Ordine 10: Cliente 2 (200 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(10, 1, 1.50, 4), -- Mele rosse
(10, 5, 1.00, 3), -- Carote
(10, 9, 1.20, 2); -- Latte fresco

-- Ordine 11: Cliente 3 (220 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(11, 10, 18.00, 1), -- Parmigiano
(11, 14, 1.20, 5);  -- Spaghetti

-- Ordine 12: Cliente 4 (240 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(12, 2, 1.20, 6),  -- Arance
(12, 19, 2.50, 4); -- Uova biologiche

-- Ordine 13: Cliente 5 (280 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(13, 21, 2.00, 3), -- Pomodori pelati
(13, 22, 3.50, 2); -- Tonno in scatola

-- Ordine 14: Cliente 6 (300 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(14, 6, 1.30, 2),  -- Zucchine
(14, 11, 2.50, 3); -- Mozzarella

-- Ordine 15: Cliente 7 (320 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(15, 23, 2.00, 4), -- Biscotti
(15, 25, 5.00, 1); -- Miele

-- Ordine 16: Cliente 8 (340 giorni fa)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
(16, 4, 1.80, 3),  -- Pere
(16, 13, 2.20, 2); -- Ricotta

-- Aggiornamento manuale del prezzo totale degli ordini
UPDATE ordine o
SET prezzototale = sub.totale
FROM (
    SELECT codordine, SUM(prezzo * numeroarticoli) AS totale
    FROM articoliordine
    GROUP BY codordine
) AS sub
WHERE o.codordine = sub.codordine;

-- Aggiornamento punti tessera clienti (10% del totale speso)
UPDATE tessera t
SET numeropunti = sub.punti
FROM (
    SELECT c.codcliente, SUM(a.prezzo * a.numeroarticoli * 0.10) AS punti
    FROM cliente c
    JOIN ordine o ON o.codcliente = c.codcliente
    JOIN articoliordine a ON a.codordine = o.codordine
    GROUP BY c.codcliente
) AS sub
WHERE t.codcliente = sub.codcliente;