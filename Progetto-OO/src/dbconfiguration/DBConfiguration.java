package dbconfiguration;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConfiguration {
    private static final Logger logger = Logger.getLogger(DBConfiguration.class.getName());
    private Connection connection = null;

    public DBConfiguration(Connection connection) {
        this.connection = connection;
    }

    private boolean connectionExists() {
        return connection != null;
    }

    private boolean tableExists(String tableName) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        try (ResultSet tables = metadata.getTables(null, null, tableName, null)) {
            return tables.next();
        }
    }

    public int createTableCliente() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement statement = connection.createStatement()) {
                if (!tableExists("cliente")) {
                    String sql = """
                            CREATE TABLE IF NOT EXISTS cliente(
                                codcliente SERIAL PRIMARY KEY,
                                nome VARCHAR(255) NOT NULL CHECK(nome ~* '^[A-Za-z ]+$'),
                                cognome VARCHAR(255) NOT NULL CHECK(cognome ~* '^[A-Za-z ]+$'),
                                codicefiscale CHAR(16) NOT NULL UNIQUE CHECK(codicefiscale ~* '^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$'),
                                indirizzo VARCHAR(255) NOT NULL,
                                telefono VARCHAR(20) CHECK(telefono ~* '^[0-9+ ]+$'),
                                email VARCHAR(255) NOT NULL UNIQUE CHECK(email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$')
                            );
                            """;

                    result = statement.executeUpdate(sql);
                } else {
                    logger.info("Table Cliente already exists!");
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "SQL Exception in creation table Cliente: ", ex);
            }
        } else {
            throw new ConnectionException("A connection must exist!");
        }

        return result;
    }

    public int createTableDipendente() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement statement = connection.createStatement()) {
                if (!tableExists("dipendente")) {
                    String sql = """
                            CREATE TABLE IF NOT EXISTS dipendente(
                                coddipendente SERIAL PRIMARY KEY,
                                nome VARCHAR(255) NOT NULL CHECK(nome ~* '^[A-Za-z ]+$'),
                                cognome VARCHAR(255) NOT NULL CHECK(cognome ~* '^[A-Za-z ]+$'),
                                codicefiscale CHAR(16) NOT NULL UNIQUE CHECK(codicefiscale ~* '^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$'),
                                indirizzo VARCHAR(255) NOT NULL,
                                telefono VARCHAR(20) CHECK(telefono ~* '^[0-9+ ]+$'),
                                email VARCHAR(255) NOT NULL UNIQUE CHECK(email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$')
                            );
                            """;

                    result = statement.executeUpdate(sql);
                } else {
                    logger.info("Table Dipendente already exists!");
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "SQL Exception in creation table Dipendente: ", ex);
            }
        } else {
            throw new ConnectionException("A connection must exist!");
        }

        return result;
    }

    public int createTableTessera() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement statement = connection.createStatement()) {
                if (!tableExists("tessera")) {
                    String sql = """
                            CREATE TABLE IF NOT EXISTS tessera(
                                codtessera SERIAL PRIMARY KEY,
                                codcliente INTEGER NOT NULL UNIQUE,
                                numeropunti NUMERIC NOT NULL DEFAULT 0.00 CHECK(numeropunti >= 0.00),
                                dataemissione DATE NOT NULL DEFAULT CURRENT_DATE,
                                datascadenza DATE NOT NULL DEFAULT (CURRENT_DATE + INTERVAL '2 years'),
                                stato STATOTESSERA NOT NULL DEFAULT 'ATTIVA',
                                CONSTRAINT tessera_cliente_fk FOREIGN KEY(codcliente)
                                    REFERENCES cliente(codcliente)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE
                            );
                            """;

                    result = statement.executeUpdate(sql);
                } else {
                    logger.info("Table Tessera already exists!");
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "SQL Exception in creation table Tessera: ", ex);
            }
        } else {
            throw new ConnectionException("A connection must exist!");
        }

        return result;
    }

    public int createTableProdotto() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement statement = connection.createStatement()) {
                if (!tableExists("prodotto")) {
                    String sql = """
                            CREATE TABLE IF NOT EXISTS prodotto(
                                codprodotto SERIAL PRIMARY KEY,
                                nome VARCHAR(255) NOT NULL CHECK(nome ~* '^[A-Za-z ]+$'),
                                descrizione VARCHAR(500),
                                prezzo NUMERIC DEFAULT 0.00 CHECK(prezzo >= 0.00),
                                luogoprovenienza VARCHAR(255),
                                dataraccolta DATE,
                                datamungitura DATE,
                                glutine BOOLEAN,
                                datascadenza DATE,
                                dataproduzione DATE,
                                categoria TIPOLOGIA,
                                scorta INT CHECK(scorta >= 0)
                            );
                            """;

                    result = statement.executeUpdate(sql);
                    
                    // Aggiungere il vincolo CHECK per le categorie
                    String constraintSql = """
                            ALTER TABLE prodotto ADD CONSTRAINT checkCategoria CHECK (
                                -- FRUTTA: deve avere data di raccolta
                                (categoria = 'FRUTTA' AND dataraccolta IS NOT NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NULL AND glutine IS NULL) OR
                                -- VERDURA: deve avere data di raccolta  
                                (categoria = 'VERDURA' AND dataraccolta IS NOT NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NULL AND glutine IS NULL) OR
                                -- LATTICINI: devono avere data di mungitura del latte e data di produzione
                                (categoria = 'LATTICINI' AND dataraccolta IS NULL AND datamungitura IS NOT NULL AND dataproduzione IS NOT NULL AND datascadenza IS NOT NULL AND glutine IS NULL) OR
                                -- FARINACEI: informazioni sul glutine
                                (categoria = 'FARINACEI' AND dataraccolta IS NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NULL AND glutine IS NOT NULL) OR
                                -- UOVA: solo data di scadenza
                                (categoria = 'UOVA' AND dataraccolta IS NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NOT NULL AND glutine IS NULL) OR
                                -- CONFEZIONATI: solo data di scadenza
                                (categoria = 'CONFEZIONATI' AND dataraccolta IS NULL AND datamungitura IS NULL AND dataproduzione IS NULL AND datascadenza IS NOT NULL AND glutine IS NULL)
                            );
                            """;
                    
                    statement.executeUpdate(constraintSql);
                } else {
                    logger.info("Table Prodotto already exists!");
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "SQL Exception in creation table Prodotto: ", ex);
            }
        } else {
            throw new ConnectionException("A connection must exist!");
        }

        return result;
    }

    public int createTableOrdine() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement statement = connection.createStatement()) {
                if (!tableExists("ordine")) {
                    String sql = """
                            CREATE TABLE IF NOT EXISTS ordine(
                                codordine SERIAL PRIMARY KEY,
                                prezzototale REAL NOT NULL DEFAULT 0.00 CHECK(prezzototale >= 0),
                                dataacquisto DATE NOT NULL,
                                codcliente INTEGER NOT NULL,
                                coddipendente INTEGER NOT NULL,
                                CONSTRAINT ordineclientefk FOREIGN KEY(codcliente)
                                    REFERENCES cliente(codcliente)
                                    ON UPDATE CASCADE
                                    ON DELETE NO ACTION,
                                CONSTRAINT ordinedipendentefk FOREIGN KEY(coddipendente)
                                    REFERENCES dipendente(coddipendente)
                                    ON UPDATE CASCADE
                                    ON DELETE NO ACTION
                            );
                            """;

                    result = statement.executeUpdate(sql);
                } else {
                    logger.info("Table Ordine already exists!");
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "SQL Exception in creation table Ordine: ", ex);
            }
        } else {
            throw new ConnectionException("A connection must exist!");
        }

        return result;
    }

    public int createTableArticoliOrdine() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement statement = connection.createStatement()) {
                if (!tableExists("articoliordine")) {
                    String sql = """
                            CREATE TABLE IF NOT EXISTS articoliordine(
                                codordine INTEGER NOT NULL,
                                codprodotto INTEGER NOT NULL,
                                prezzo NUMERIC NOT NULL DEFAULT 0.00 CHECK(prezzo >= 0.00),
                                numeroarticoli INT NOT NULL CHECK(numeroarticoli > 0),
                                CONSTRAINT PK_ArticoliOrdine PRIMARY KEY(codordine, codprodotto),
                                CONSTRAINT articoliordineprodottofk FOREIGN KEY(codprodotto)
                                    REFERENCES prodotto(codprodotto)
                                    ON UPDATE CASCADE
                                    ON DELETE NO ACTION,
                                CONSTRAINT articoliordineordinek FOREIGN KEY(codordine)
                                    REFERENCES ordine(codordine)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE
                            );
                            """;

                    result = statement.executeUpdate(sql);
                } else {
                    logger.info("Table ArticoliOrdine already exists!");
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "SQL Exception in creation table ArticoliOrdine: ", ex);
            }
        } else {
            throw new ConnectionException("A connection must exist!");
        }

        return result;
    }

    public int createTipologie() throws ConnectionException {
        int result = -1;

        if (connectionExists()) {
            try (Statement statement = connection.createStatement()) {
                String sql = "DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'tipologia') THEN CREATE TYPE TIPOLOGIA AS ENUM('FRUTTA','VERDURA','FARINACEI','LATTICINI','UOVA','CONFEZIONATI'); END IF; END $$;";
                result = statement.executeUpdate(sql);
                
                // Creazione del tipo STATOTESSERA
                String sqlStato = "DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'statotessera') THEN CREATE TYPE STATOTESSERA AS ENUM('ATTIVA','SOSPESA','SCADUTA'); END IF; END $$;";
                result += statement.executeUpdate(sqlStato);
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "SQL Exception in creation type tipologia: ", ex);
            }
        } else {
            throw new ConnectionException("A connection must exist!");
        }

        return result;
    }

    public int formatTables() throws ConnectionException {
        int result = 0;

        if (!connectionExists()) {
            throw new ConnectionException("A connection must exist!");
        }

        String sql = """
            DO $$
            DECLARE
                rec RECORD;
            BEGIN
                -- Drop tutte le tabelle nello schema public
                FOR rec IN SELECT tablename FROM pg_tables WHERE schemaname = 'public' LOOP
                    EXECUTE format('DROP TABLE IF EXISTS public.%I CASCADE', rec.tablename);
                END LOOP;

                -- Drop tutti i tipi ENUM nello schema public
                FOR rec IN
                    SELECT n.nspname AS enum_schema, t.typname AS enum_type
                    FROM pg_type t
                    JOIN pg_enum e ON t.oid = e.enumtypid
                    JOIN pg_namespace n ON n.oid = t.typnamespace
                    WHERE n.nspname = 'public'
                    GROUP BY n.nspname, t.typname
                LOOP
                    EXECUTE format('DROP TYPE IF EXISTS %I.%I CASCADE', rec.enum_schema, rec.enum_type);
                END LOOP;

                -- Reset di tutte le sequenze nello schema public
                FOR rec IN SELECT sequence_name FROM information_schema.sequences WHERE sequence_schema = 'public' LOOP
                    EXECUTE format('ALTER SEQUENCE public.%I RESTART WITH 1', rec.sequence_name);
                END LOOP;
            END $$;
            """;

        try (Statement statement = connection.createStatement()) {
            result = statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while formatting tables: ", e);
        }

        return result;
    }

    public int populateDatabase() throws ConnectionException {
        int result = 0;

        if (!connectionExists()) {
            throw new ConnectionException("A connection must exist!");
        }

        try (Statement statement = connection.createStatement()) {
            // Inserimento clienti conformi a Popolazione.sql
            String sqlCliente = """
                INSERT INTO cliente (nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES
                ('Aldo', 'Marzante', 'BBBBBB11B11B111B', 'Via Don Matteo 1', '3331234567', 'aldo.marzante@email.it'),
                ('Luca', 'Benson', 'AAAAAA22A22A222A', 'Via Don Corleone 2', '3332345678', 'luca.benson@email.it'),
                ('Mario', 'Sarni', 'CCCCCC33C33C333C', 'Via San Giovanni 3', '3333456789', 'mario.sarni@email.it'),
                ('Alessio', 'Sassi', 'DDDDDD44D44D444D', 'Via Cremoni 4', '3334567890', 'alessio.sassi@email.it'),
                ('Giorgio', 'Rossi', 'EEEEEE55E55E555E', 'Via Don Carlo 5', '3335678901', 'giorgio.rossi@email.it'),
                ('Paolo', 'Verdi', 'FFFFFF66F66F666F', 'Via Don Alberto 6', '3336789012', 'paolo.verdi@email.it'),
                ('Simone', 'Bianchi', 'GGGGGG77G77G777G', 'Via Don Giuseppe 7', '3337890123', 'simone.bianchi@email.it'),
                ('Enrico', 'Gialli', 'HHHHHH88H88H888H', 'Via Don Mario 8', '3338901234', 'enrico.gialli@email.it')
                ON CONFLICT (email) DO NOTHING;
                """;
            result += statement.executeUpdate(sqlCliente);

            // Inserimento tessere conformi a Popolazione.sql
            String sqlTessera = """
                INSERT INTO tessera (codcliente, numeropunti) VALUES
                (1, 20.00),
                (2, 30.00),
                (3, 100.00),
                (4, 0.00),
                (5, 50.00),
                (6, 80.00),
                (7, 10.00),
                (8, 150.00)
                ON CONFLICT (codcliente) DO NOTHING;
                """;
            result += statement.executeUpdate(sqlTessera);

            // Inserimento dipendenti conformi a Popolazione.sql
            String sqlDipendente = """
                INSERT INTO dipendente (nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES
                ('Dario', 'Forte', 'IIIIII11I11I111I', 'Via Andromeda 1', '3901234567', 'dario.forte@negozio.it'),
                ('Sandro', 'Romano', 'JJJJJJ22J22J222J', 'Via Omega 2', '3902345678', 'sandro.romano@negozio.it'),
                ('Giulio', 'Cesare', 'KKKKKK33K33K333K', 'Via Roma 3', '3903456789', 'giulio.cesare@negozio.it'),
                ('Mario', 'Rossi', 'LLLLLL44L44L444L', 'Via Parma 4', '3904567890', 'mario.rossi@negozio.it'),
                ('Andrea', 'Verdi', 'MMMMMM55M55M555M', 'Via Milano 5', '3905678901', 'andrea.verdi@negozio.it'),
                ('Giuseppe', 'Bianchi', 'NNNNNN66N66N666N', 'Via Torino 6', '3906789012', 'giuseppe.bianchi@negozio.it'),
                ('Marco', 'Gialli', 'OOOOOO77O77O777O', 'Via Napoli 7', '3907890123', 'marco.gialli@negozio.it')
                ON CONFLICT (email) DO NOTHING;
                """;
            result += statement.executeUpdate(sqlDipendente);

            // Inserimento prodotti conformi a Popolazione.sql
            String sqlProdotto = """
                INSERT INTO prodotto (nome, descrizione, prezzo, luogoprovenienza, dataraccolta, datamungitura, glutine, datascadenza, dataproduzione, categoria, scorta) VALUES
                -- FRUTTA
                ('Mela Rossa', 'Mela italiana rossa', 1.50, 'Trentino', '2023-09-15', NULL, NULL, NULL, NULL, 'FRUTTA', 100),
                ('Arance', 'Arance siciliane', 1.20, 'Sicilia', '2023-11-20', NULL, NULL, NULL, NULL, 'FRUTTA', 150),
                ('Banane', 'Banane ecuadoriane', 2.00, 'Ecuador', '2023-12-01', NULL, NULL, NULL, NULL, 'FRUTTA', 80),
                -- VERDURA
                ('Carote', 'Carote fresche', 1.00, 'Emilia-Romagna', '2023-10-10', NULL, NULL, NULL, NULL, 'VERDURA', 120),
                ('Zucchine', 'Zucchine fresche', 1.30, 'Lazio', '2023-11-05', NULL, NULL, NULL, NULL, 'VERDURA', 130),
                ('Pomodori', 'Pomodori freschi', 2.50, 'Campania', '2023-08-25', NULL, NULL, NULL, NULL, 'VERDURA', 90),
                -- LATTICINI
                ('Latte Fresco', 'Latte fresco intero', 1.20, 'Lombardia', NULL, '2023-12-10', NULL, '2023-12-15', '2023-12-11', 'LATTICINI', 80),
                ('Parmigiano Reggiano', 'Parmigiano Reggiano DOP', 18.00, 'Emilia-Romagna', NULL, '2022-06-01', NULL, '2023-12-31', '2023-01-15', 'LATTICINI', 50),
                ('Mozzarella', 'Mozzarella di bufala', 2.50, 'Campania', NULL, '2023-12-08', NULL, '2023-12-15', '2023-12-09', 'LATTICINI', 60),
                ('Yogurt', 'Yogurt naturale', 0.90, 'Piemonte', NULL, '2023-12-05', NULL, '2023-12-20', '2023-12-06', 'LATTICINI', 90),
                -- FARINACEI
                ('Spaghetti', 'Spaghetti di grano duro', 1.20, 'Puglia', NULL, NULL, true, NULL, NULL, 'FARINACEI', 300),
                ('Pane Integrale', 'Pane integrale', 1.50, 'Toscana', NULL, NULL, true, NULL, NULL, 'FARINACEI', 180),
                ('Pasta Senza Glutine', 'Pasta di riso', 2.50, 'Veneto', NULL, NULL, false, NULL, NULL, 'FARINACEI', 150),
                ('Farina Doppio Zero', 'Farina di grano tenero tipo 00', 0.80, 'Piemonte', NULL, NULL, true, NULL, NULL, 'FARINACEI', 200),
                -- UOVA
                ('Uova Biologiche', 'Uova da allevamento biologico', 2.50, 'Umbria', NULL, NULL, NULL, '2023-12-20', NULL, 'UOVA', 150),
                ('Uova Fresche', 'Uova fresche da galline allevate a terra', 2.00, 'Marche', NULL, NULL, NULL, '2023-12-18', NULL, 'UOVA', 120),
                -- CONFEZIONATI
                ('Pomodori Pelati', 'Pomodori pelati in scatola', 2.00, 'Campania', NULL, NULL, NULL, '2025-06-30', NULL, 'CONFEZIONATI', 200),
                ('Tonno in Scatola', 'Tonno al naturale in scatola', 3.50, 'Sicilia', NULL, NULL, NULL, '2025-03-15', NULL, 'CONFEZIONATI', 100),
                ('Biscotti', 'Biscotti al cioccolato', 2.00, 'Lombardia', NULL, NULL, NULL, '2024-12-31', NULL, 'CONFEZIONATI', 250),
                ('Cereali', 'Cereali integrali', 3.00, 'Emilia-Romagna', NULL, NULL, NULL, '2024-08-20', NULL, 'CONFEZIONATI', 150),
                ('Miele', 'Miele millefiori', 5.00, 'Abruzzo', NULL, NULL, NULL, '2025-12-31', NULL, 'CONFEZIONATI', 80)
                ON CONFLICT (codprodotto) DO NOTHING;
                """;
            result += statement.executeUpdate(sqlProdotto);

            // Inserimento ordini conformi a Popolazione.sql  
            String sqlOrdine = """
                INSERT INTO ordine (prezzototale, dataacquisto, codcliente, coddipendente) VALUES
                (7.70, '2023-12-01', 1, 1),
                (24.00, '2023-12-02', 2, 2),
                (25.40, '2023-12-03', 3, 3),
                (12.40, '2023-12-04', 4, 4),
                (11.00, '2023-12-05', 5, 5),
                (9.40, '2023-12-06', 6, 6),
                (8.50, '2023-12-07', 7, 7),
                (10.00, '2023-12-08', 8, 1),
                (8.40, '2023-12-09', 1, 2),
                (11.40, '2023-12-10', 2, 3),
                (24.00, '2023-12-11', 3, 4),
                (17.20, '2023-12-12', 4, 5),
                (13.00, '2023-12-13', 5, 6),
                (10.10, '2023-12-14', 6, 7),
                (13.00, '2023-12-15', 7, 1)
                ON CONFLICT (codordine) DO NOTHING;
                """;
            result += statement.executeUpdate(sqlOrdine);

            // Inserimento articoli negli ordini conformi a Popolazione.sql
            String sqlArticoliOrdine = """
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
                (15, 21, 5.00, 1)  -- Miele
                ON CONFLICT (codordine, codprodotto) DO NOTHING;
                """;
            result += statement.executeUpdate(sqlArticoliOrdine);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while populating database: ", e);
        }

        return result;
    }
    
    /**
     * Metodo per inizializzare completamente il database
     * Crea prima i tipi, poi le tabelle e infine popola con i dati
     */
    public int initializeCompleteDatabase() throws ConnectionException {
        int result = 0;
        
        if (!connectionExists()) {
            throw new ConnectionException("A connection must exist!");
        }
        
        try {
            // 1. Crea tipi ENUM
            result += createTipologie();
            logger.info("Tipi ENUM creati");
            
            // 2. Crea tabelle nell'ordine corretto
            result += createTableCliente();
            result += createTableTessera();  
            result += createTableDipendente();
            result += createTableProdotto();
            result += createTableOrdine();
            result += createTableArticoliOrdine();
            logger.info("Tabelle create");
            
            // 3. Popola con dati di test
            result += populateDatabase();
            logger.info("Database popolato con dati di test");
            
            logger.info("Inizializzazione database completata con successo!");
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore durante l'inizializzazione del database: ", e);
            throw new ConnectionException("Errore durante l'inizializzazione: " + e.getMessage());
        }
        
        return result;
    }
}
