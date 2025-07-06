-- Popolazione database conforme alla traccia accademica e allo script delle tabelle

-- Inserimento clienti
INSERT INTO cliente (nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES
('Aldo', 'Marzante', 'BBBBBB11B11B111B', 'Via Don Matteo 1', '3331234567', 'aldo.marzante@email.it'),
('Luca', 'Benson', 'AAAAAA22A22A222A', 'Via Don Corleone 2', '3332345678', 'luca.benson@email.it'),
('Mario', 'Sarni', 'CCCCCC33C33C333C', 'Via San Giovanni 3', '3333456789', 'mario.sarni@email.it'),
('Alessio', 'Sassi', 'DDDDDD44D44D444D', 'Via Cremoni 4', '3334567890', 'alessio.sassi@email.it'),
('Giorgio', 'Rossi', 'EEEEEE55E55E555E', 'Via Don Carlo 5', '3335678901', 'giorgio.rossi@email.it'),
('Paolo', 'Verdi', 'FFFFFF66F66F666F', 'Via Don Alberto 6', '3336789012', 'paolo.verdi@email.it'),
('Simone', 'Bianchi', 'GGGGGG77G77G777G', 'Via Don Giuseppe 7', '3337890123', 'simone.bianchi@email.it'),
('Enrico', 'Gialli', 'HHHHHH88H88H888H', 'Via Don Mario 8', '3338901234', 'enrico.gialli@email.it');

-- Inserimento tessere (riferimento ai clienti inseriti)
INSERT INTO tessera (codcliente, numeropunti) VALUES
(1, 20.00),
(2, 30.00),
(3, 100.00),
(4, 0.00),
(5, 50.00),
(6, 80.00),
(7, 10.00),
(8, 150.00);

-- Inserimento dipendenti
INSERT INTO dipendente (nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES
('Dario', 'Forte', 'IIIIII11I11I111I', 'Via Andromeda 1', '3901234567', 'dario.forte@negozio.it'),
('Sandro', 'Romano', 'JJJJJJ22J22J222J', 'Via Omega 2', '3902345678', 'sandro.romano@negozio.it'),
('Giulio', 'Cesare', 'KKKKKK33K33K333K', 'Via Roma 3', '3903456789', 'giulio.cesare@negozio.it'),
('Mario', 'Rossi', 'LLLLLL44L44L444L', 'Via Parma 4', '3904567890', 'mario.rossi@negozio.it'),
('Andrea', 'Verdi', 'MMMMMM55M55M555M', 'Via Milano 5', '3905678901', 'andrea.verdi@negozio.it'),
('Giuseppe', 'Bianchi', 'NNNNNN66N66N666N', 'Via Torino 6', '3906789012', 'giuseppe.bianchi@negozio.it'),
('Marco', 'Gialli', 'OOOOOO77O77O777O', 'Via Napoli 7', '3907890123', 'marco.gialli@negozio.it');

-- Inserimento prodotti conformi alla traccia accademica
INSERT INTO prodotto (nome, descrizione, prezzo, luogoprovenienza, dataraccolta, datamungitura, glutine, datascadenza, dataproduzione, categoria, scorta) VALUES
-- FRUTTA (deve avere dataraccolta)
('Mela Rossa', 'Mela italiana rossa', 1.50, 'Trentino', '2023-09-15', NULL, NULL, NULL, NULL, 'FRUTTA', 100),
('Arance', 'Arance siciliane', 1.20, 'Sicilia', '2023-11-20', NULL, NULL, NULL, NULL, 'FRUTTA', 150),
('Banane', 'Banane ecuadoriane', 2.00, 'Ecuador', '2023-12-01', NULL, NULL, NULL, NULL, 'FRUTTA', 80),

-- VERDURA (deve avere dataraccolta)
('Carote', 'Carote fresche', 1.00, 'Emilia-Romagna', '2023-10-10', NULL, NULL, NULL, NULL, 'VERDURA', 120),
('Zucchine', 'Zucchine fresche', 1.30, 'Lazio', '2023-11-05', NULL, NULL, NULL, NULL, 'VERDURA', 130),
('Pomodori', 'Pomodori freschi', 2.50, 'Campania', '2023-08-25', NULL, NULL, NULL, NULL, 'VERDURA', 90),

-- LATTICINI (deve avere datamungitura, dataproduzione e datascadenza)
('Latte Fresco', 'Latte fresco intero', 1.20, 'Lombardia', NULL, '2023-12-10', NULL, '2023-12-15', '2023-12-11', 'LATTICINI', 80),
('Parmigiano Reggiano', 'Parmigiano Reggiano DOP', 18.00, 'Emilia-Romagna', NULL, '2022-06-01', NULL, '2023-12-31', '2023-01-15', 'LATTICINI', 50),
('Mozzarella', 'Mozzarella di bufala', 2.50, 'Campania', NULL, '2023-12-08', NULL, '2023-12-15', '2023-12-09', 'LATTICINI', 60),
('Yogurt', 'Yogurt naturale', 0.90, 'Piemonte', NULL, '2023-12-05', NULL, '2023-12-20', '2023-12-06', 'LATTICINI', 90),

-- FARINACEI (deve avere glutine)
('Spaghetti', 'Spaghetti di grano duro', 1.20, 'Puglia', NULL, NULL, true, NULL, NULL, 'FARINACEI', 300),
('Pane Integrale', 'Pane integrale', 1.50, 'Toscana', NULL, NULL, true, NULL, NULL, 'FARINACEI', 180),
('Pasta Senza Glutine', 'Pasta di riso', 2.50, 'Veneto', NULL, NULL, false, NULL, NULL, 'FARINACEI', 150),
('Farina 00', 'Farina di grano tenero tipo 00', 0.80, 'Piemonte', NULL, NULL, true, NULL, NULL, 'FARINACEI', 200),

-- UOVA (deve avere datascadenza)
('Uova Biologiche', 'Uova da allevamento biologico', 2.50, 'Umbria', NULL, NULL, NULL, '2023-12-20', NULL, 'UOVA', 150),
('Uova Fresche', 'Uova fresche da galline allevate a terra', 2.00, 'Marche', NULL, NULL, NULL, '2023-12-18', NULL, 'UOVA', 120),

-- CONFEZIONATI (deve avere datascadenza)
('Pomodori Pelati', 'Pomodori pelati in scatola', 2.00, 'Campania', NULL, NULL, NULL, '2025-06-30', NULL, 'CONFEZIONATI', 200),
('Tonno in Scatola', 'Tonno al naturale in scatola', 3.50, 'Sicilia', NULL, NULL, NULL, '2025-03-15', NULL, 'CONFEZIONATI', 100),
('Biscotti', 'Biscotti al cioccolato', 2.00, 'Lombardia', NULL, NULL, NULL, '2024-12-31', NULL, 'CONFEZIONATI', 250),
('Cereali', 'Cereali integrali', 3.00, 'Emilia-Romagna', NULL, NULL, NULL, '2024-08-20', NULL, 'CONFEZIONATI', 150),
('Miele', 'Miele millefiori', 5.00, 'Abruzzo', NULL, NULL, NULL, '2025-12-31', NULL, 'CONFEZIONATI', 80);

-- Inserimento ordini
INSERT INTO ordine (prezzototale, dataacquisto, codcliente, coddipendente) VALUES
(0.00, '2023-12-01', 1, 1),
(0.00, '2023-12-02', 2, 2),
(0.00, '2023-12-03', 3, 3),
(0.00, '2023-12-04', 4, 4),
(0.00, '2023-12-05', 5, 5),
(0.00, '2023-12-06', 6, 6),
(0.00, '2023-12-07', 7, 7),
(0.00, '2023-12-08', 8, 1),
(0.00, '2023-12-09', 1, 2),
(0.00, '2023-12-10', 2, 3),
(0.00, '2023-12-11', 3, 4),
(0.00, '2023-12-12', 4, 5),
(0.00, '2023-12-13', 5, 6),
(0.00, '2023-12-14', 6, 7),
(0.00, '2023-12-15', 7, 1);

-- Inserimento articoli negli ordini (prezzo sarà calcolato automaticamente dai trigger)
INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES
-- Ordine 1: Cliente 1
(1, 1, 1.50, 3),  -- Mele rosse
(1, 4, 1.00, 2),  -- Carote
(1, 13, 1.20, 1), -- Spaghetti

-- Ordine 2: Cliente 2
(2, 2, 1.20, 5),  -- Arance
(2, 8, 18.00, 1), -- Parmigiano

-- Ordine 3: Cliente 3
(3, 7, 1.20, 2),  -- Latte fresco
(3, 15, 2.50, 6), -- Uova biologiche
(3, 17, 2.00, 4), -- Pomodori pelati

-- Ordine 4: Cliente 4
(4, 5, 1.30, 3),  -- Zucchine
(4, 9, 2.50, 2),  -- Mozzarella
(4, 18, 3.50, 1), -- Tonno in scatola

-- Ordine 5: Cliente 5
(5, 11, 1.50, 2), -- Pane integrale
(5, 16, 2.00, 4), -- Uova fresche

-- Ordine 6: Cliente 6
(6, 3, 2.00, 2),  -- Banane
(6, 10, 0.90, 6), -- Yogurt

-- Ordine 7: Cliente 7
(7, 6, 2.50, 1),  -- Pomodori freschi
(7, 19, 2.00, 3), -- Biscotti

-- Ordine 8: Cliente 8
(8, 12, 2.50, 2), -- Pasta senza glutine
(8, 21, 5.00, 1), -- Miele

-- Ordine 9: Cliente 1 (secondo ordine)
(9, 14, 0.80, 3), -- Farina
(9, 20, 3.00, 2), -- Cereali

-- Ordine 10: Cliente 2 (secondo ordine)
(10, 1, 1.50, 4), -- Mele rosse
(10, 4, 1.00, 3), -- Carote
(10, 7, 1.20, 2), -- Latte fresco

-- Ordine 11: Cliente 3 (secondo ordine)
(11, 8, 18.00, 1), -- Parmigiano
(11, 13, 1.20, 5), -- Spaghetti

-- Ordine 12: Cliente 4 (secondo ordine)
(12, 2, 1.20, 6), -- Arance
(12, 15, 2.50, 4), -- Uova biologiche

-- Ordine 13: Cliente 5 (secondo ordine)
(13, 17, 2.00, 3), -- Pomodori pelati
(13, 18, 3.50, 2), -- Tonno in scatola

-- Ordine 14: Cliente 6 (secondo ordine)
(14, 5, 1.30, 2), -- Zucchine
(14, 9, 2.50, 3), -- Mozzarella

-- Ordine 15: Cliente 7 (secondo ordine)
(15, 19, 2.00, 4), -- Biscotti
(15, 21, 5.00, 1); -- Miele