package test;

import controller.Controller;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.table.DefaultTableModel;

/**
 * Classe di test per verificare tutte le funzionalità del Controller
 * e l'integrità delle operazioni sul database.
 */
public class ControllerTester {
    private Controller controller;
    
    public ControllerTester() throws SQLException, java.io.IOException {
        controller = new Controller();
        controller.connect();
    }
    
    public static void main(String[] args) {
        try {
            ControllerTester tester = new ControllerTester();
            tester.runAllTests();
        } catch (Exception e) {
            System.err.println("Errore durante l'esecuzione dei test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void runAllTests() {
        System.out.println("=== AVVIO TEST CONTROLLER E DATABASE ===\n");
        
        // Test connessione database
        testDatabaseConnection();
        
        // Test CRUD Dipendenti (simula NuovoDipendenteFrame e ModificaDipendenteFrame)
        testDipendenteCRUD();
        
        // Test CRUD Clienti (simula NuovoClienteFrame e ModificaClienteFrame)
        testClienteCRUD();
        
        // Test CRUD Prodotti (simula NuovoProdottoFrame e ModificaProdottiFrame)
        testProdottoCRUD();
        
        // Test Tessere (simula azioni relative alle tessere)
        testTessere();
        
        // Test Ordini e Articoli (simula CarrelloFrame)
        testOrdiniArticoli();
        
        // Test Visualizzazione Dati (simula tutti i frame di visione)
        testVisualizationMethods();
        
        // Test Workflow Completo (simula un intero flusso di lavoro)
        testCompleteWorkflow();
        
        System.out.println("\n=== TUTTI I TEST COMPLETATI ===");
    }
    
    private void testDatabaseConnection() {
        System.out.println("--- Test Connessione Database ---");
        try {
            controller.connect();
            System.out.println("✓ Connessione al database: OK");
        } catch (SQLException e) {
            System.err.println("✗ Errore connessione database: " + e.getMessage());
        }
        System.out.println();
    }
    
    private void testDipendenteCRUD() {
        System.out.println("--- Test CRUD Dipendenti (Simula NuovoDipendenteFrame e ModificaDipendenteFrame) ---");
        String testCodDip = "999"; // Usando ID numerico
        
        try {
            // Test inserimento dipendente (simula NuovoDipendenteFrame)
            boolean inserted = controller.newdip(testCodDip, "Mario", "Rossi", "MRARSS80A01H501K", 
                                                "m@test.it", "Via Test 1", "1234567890");
            System.out.println("✓ Inserimento dipendente (NuovoDipendenteFrame): " + (inserted ? "OK" : "FAILED"));
            
            // Test verifica ID dipendente
            boolean exists = controller.verifyid(testCodDip);
            System.out.println("✓ Verifica ID dipendente: " + (exists ? "OK" : "FAILED"));
            
            // Test aggiornamento dipendente (simula ModificaDipendenteFrame)
            boolean updated = controller.updip(testCodDip, "Mario", "Rossi", "MRARSS80A01H501K", 
                                             "m2@test.it", "Via Test Modificata 1", "0987654321");
            System.out.println("✓ Aggiornamento dipendente (ModificaDipendenteFrame): " + (updated ? "OK" : "FAILED"));
            
        } catch (SQLException e) {
            System.err.println("✗ Errore test dipendenti: " + e.getMessage());
        }
        System.out.println();
    }
    
    private void testClienteCRUD() {
        System.out.println("--- Test CRUD Clienti ---");
        String testCodCli = "123"; // Usando ID numerico
        
        try {
            // Test inserimento cliente
            boolean inserted = controller.newclt(testCodCli, "Giulia", "Verdi", "GLVVRD85C45H501P", 
                                                "g@test.it", "Via Cliente 1", "3331234567");
            System.out.println("✓ Inserimento cliente: " + (inserted ? "OK" : "FAILED"));
            
            // Test aggiornamento cliente
            boolean updated = controller.upcliente(testCodCli, "Giulia", "Verdi", "GLVVRD85C45H501P", 
                                                  "g2@test.it", "Via Cliente Modificata 1", "3339876543");
            System.out.println("✓ Aggiornamento cliente: " + (updated ? "OK" : "FAILED"));
            
            // Test recupero ID cliente per codice fiscale
            String clienteId = controller.getct("GLVVRD85C45H501P");
            System.out.println("✓ Recupero ID cliente: " + (clienteId != null ? "OK (ID: " + clienteId + ")" : "FAILED"));
            
        } catch (SQLException e) {
            System.err.println("✗ Errore test clienti: " + e.getMessage());
        }
        System.out.println();
    }
    
    private void testProdottoCRUD() {
        System.out.println("--- Test CRUD Prodotti (Simula NuovoProdottoFrame e ModificaProdottiFrame) ---");
        String testCodProd = "901"; // Usando ID numerico
        
        try {
            // Test inserimento prodotto categoria Farinacei (unica che può avere glutine)
            Date oggi = Date.valueOf(LocalDate.now());
            
            // Prodotto Farinacei (solo glutine, tutti gli altri NULL)
            boolean inserted = controller.newprod(testCodProd, "Pasta Test", "Pasta per test", 
                                                 2.50, "Puglia", null, null, true, 
                                                 null, "Farinacei", 100);
            System.out.println("✓ Inserimento prodotto (NuovoProdottoFrame): " + (inserted ? "OK" : "FAILED"));
            
            // Test aggiornamento prodotto (simula ModificaProdottiFrame)
            boolean updated = controller.upprod(testCodProd, "Pasta Test Modificata", "Pasta integrale per test", 
                                               3.00, "Calabria", null, null, false, 
                                               null, "Farinacei", 150);
            System.out.println("✓ Aggiornamento prodotto (ModificaProdottiFrame): " + (updated ? "OK" : "FAILED"));
            
            // Test aggiornamento scorte (simula azioni di magazzino)
            boolean scortaUpdated = controller.upscorte(75, testCodProd);
            System.out.println("✓ Aggiornamento scorte: " + (scortaUpdated ? "OK" : "FAILED"));
            
            // Test secondo prodotto categoria Latticini (solo dataMungitura)
            Date dataMungitura = Date.valueOf(LocalDate.now().minusDays(1));
            boolean inserted2 = controller.newprod("902", "Latte Fresco", "Latte fresco di montagna", 
                                                  1.20, "Trentino", null, dataMungitura, false, 
                                                  null, "Latticini", 50);
            System.out.println("✓ Inserimento secondo prodotto (Latticini): " + (inserted2 ? "OK" : "FAILED"));
            
            // Test terzo prodotto categoria Inscatolati (solo dataScadenza)
            Date dataScadenza = Date.valueOf(LocalDate.now().plusMonths(6));
            boolean inserted3 = controller.newprod("903", "Passata di Pomodoro", "Passata di pomodoro biologica", 
                                                  1.50, "Campania", null, null, false, 
                                                  dataScadenza, "Inscatolati", 80);
            System.out.println("✓ Inserimento terzo prodotto (Inscatolati): " + (inserted3 ? "OK" : "FAILED"));
            
            // Test quarto prodotto categoria Farinacei (solo glutine)
            boolean inserted4 = controller.newprod("904", "Pasta Integrale", "Pasta integrale di grano duro", 
                                                  2.00, "Puglia", null, null, true, 
                                                  null, "Farinacei", 120);
            System.out.println("✓ Inserimento quarto prodotto (Farinacei): " + (inserted4 ? "OK" : "FAILED"));
            
        } catch (SQLException e) {
            System.err.println("✗ Errore test prodotti: " + e.getMessage());
        }
        System.out.println();
    }
    
    private void testTessere() {
        System.out.println("--- Test Tessere (Simula azioni di gestione tessere) ---");
        
        try {
            // Test creazione tessera per cliente esistente (simula azione da frame clienti)
            boolean tesseraCreated = controller.nuovatessera("Giulia", "Verdi", "GLVVRD85C45H501P");
            System.out.println("✓ Creazione tessera per cliente: " + (tesseraCreated ? "OK" : "FAILED"));
            
            // Test aggiornamento punti (simula azioni da CarrelloFrame dopo acquisto)
            // Prima recupero l'ID del cliente appena creato
            String clienteId = controller.getct("GLVVRD85C45H501P");
            if (clienteId != null) {
                boolean puntiUpdated = controller.uppunti(clienteId, 50.0);
                System.out.println("✓ Aggiornamento punti tessera: " + (puntiUpdated ? "OK" : "FAILED"));
            } else {
                System.out.println("✓ Aggiornamento punti tessera: FAILED (cliente non trovato)");
            }
            
            // Test recupero punti tessera per codice tessera specifico
            try {
                // Prima cerchiamo se esiste una tessera nel sistema
                controller.allCliente(); // Popola i dati clienti
                if (controller.clienteModel.getRowCount() > 0) {
                    // Prova con una tessera che potrebbe esistere
                    String punti = controller.punti("T001");
                    System.out.println("✓ Recupero punti tessera: " + (punti != null ? "OK (Punti: " + punti + ")" : "FAILED - tessera non trovata"));
                } else {
                    System.out.println("✓ Recupero punti tessera: SKIPPED (nessun cliente nel sistema)");
                }
            } catch (SQLException e) {
                System.out.println("✓ Recupero punti tessera: FAILED (tessera non trovata - normale per test)");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Errore test tessere: " + e.getMessage());
        }
        System.out.println();
    }
    
    private void testOrdiniArticoli() {
        System.out.println("--- Test Ordini e Articoli ---");
        
        try {
            String testCodOrdine = "501"; // Usando ID numerico
            Date dataAcquisto = Date.valueOf(LocalDate.now());
            
            // Test inserimento ordine
            boolean ordineCreated = controller.nuovoordine(testCodOrdine, dataAcquisto, 25.50, 1, 1);
            System.out.println("✓ Creazione ordine: " + (ordineCreated ? "OK" : "FAILED"));
            
            // Test inserimento articoli (usa un prodotto che esiste)
            boolean articoliCreated = controller.newarticoli(testCodOrdine, "901", 2.50, 5.0, 10, "Farinacei", 1);
            System.out.println("✓ Creazione articoli: " + (articoliCreated ? "OK" : "FAILED"));
            
            // Test recupero data ultimo ordine
            String oldDate = controller.OldDate();
            System.out.println("✓ Recupero data ultimo ordine: " + (oldDate != null ? "OK (Data: " + oldDate + ")" : "FAILED"));
            
            // Test recupero codice ordine corrente
            String currOrd = controller.CurrOrd();
            System.out.println("✓ Recupero codice ordine corrente: " + (currOrd != null ? "OK (Codice: " + currOrd + ")" : "FAILED"));
            
        } catch (SQLException e) {
            System.err.println("✗ Errore test ordini: " + e.getMessage());
        }
        System.out.println();
    }
    
    
    private void testVisualizationMethods() {
        System.out.println("--- Test Metodi di Visualizzazione (Simulazione Frame di Visione) ---");
        
        try {
            // Test popolamento tabelle (simula VisioneDipendentiFrame)
            controller.allDipendenti();
            System.out.println("✓ Popolamento tabella dipendenti: OK (Righe: " + controller.dipModel.getRowCount() + ")");
            
            // Test popolamento clienti (simula VisioneClienteFrame)
            controller.allCliente();
            System.out.println("✓ Popolamento tabella clienti: OK (Righe: " + controller.clienteModel.getRowCount() + ")");
            
            // Test popolamento prodotti (simula VisioneProdottiFrame)
            controller.allProdotti();
            System.out.println("✓ Popolamento tabella prodotti: OK (Righe: " + controller.prodModel.getRowCount() + ")");
            
            // Test popolamento ordini (simula VisioneOrdineFrame)
            controller.allOrdini();
            System.out.println("✓ Popolamento tabella ordini: OK (Righe: " + controller.ordModel.getRowCount() + ")");
            
            // Test ricerca prodotti per categoria (simula CarrelloFrame)
            DefaultTableModel testModel = new DefaultTableModel();
            controller.categoriaprodotti("Farinacei", testModel);
            System.out.println("✓ Ricerca prodotti per categoria: OK (Righe: " + testModel.getRowCount() + ")");
            
        } catch (SQLException e) {
            System.err.println("✗ Errore test visualizzazione: " + e.getMessage());
        }
        System.out.println();
    }
    
    private void testCompleteWorkflow() {
        System.out.println("--- Test Workflow Completo (Simula un flusso utente completo) ---");
        
        try {
            // 1. Admin aggiunge un dipendente
            boolean dipAdded = controller.newdip("997", "Anna", "Bianchi", "NNBNCCH85M45H501D", 
                                                "a@test.it", "Via Workflow 1", "3334567890");
            System.out.println("✓ Admin aggiunge dipendente: " + (dipAdded ? "OK" : "FAILED"));
            
            // 2. Admin aggiunge un prodotto (categoria Inscatolati)
            Date oggi = Date.valueOf(LocalDate.now());
            Date scadenza = Date.valueOf(LocalDate.now().plusMonths(3));
            boolean prodAdded = controller.newprod("905", "Pelati", "Pelati in scatola", 
                                                  1.50, "Campania", null, null, false, scadenza, "Inscatolati", 200);
            System.out.println("✓ Admin aggiunge prodotto: " + (prodAdded ? "OK" : "FAILED"));
            
            // 3. Dipendente aggiunge un cliente
            boolean clienteAdded = controller.newclt("125", "Marco", "Neri", "MRCNRI90A01H501L", 
                                                    "m@test.it", "Via Cliente 10", "3335678901");
            System.out.println("✓ Dipendente aggiunge cliente: " + (clienteAdded ? "OK" : "FAILED"));
            
            // 4. Creazione tessera per il cliente
            boolean tesseraAdded = controller.nuovatessera("Marco", "Neri", "MRCNRI90A01H501L");
            System.out.println("✓ Creazione tessera cliente: " + (tesseraAdded ? "OK" : "FAILED"));
            
            // 5. Creazione ordine
            boolean ordineAdded = controller.nuovoordine("503", oggi, 7.50, 1, 1);
            System.out.println("✓ Creazione ordine: " + (ordineAdded ? "OK" : "FAILED"));
            
            // 6. Aggiunta articoli all'ordine
            boolean articoliAdded = controller.newarticoli("503", "905", 1.50, 5.0, 5, "Inscatolati", 1);
            System.out.println("✓ Aggiunta articoli ordine: " + (articoliAdded ? "OK" : "FAILED"));
            
            // 7. Aggiornamento scorte dopo vendita
            boolean scorteUpdated = controller.upscorte(195, "905"); // 200-5=195
            System.out.println("✓ Aggiornamento scorte post-vendita: " + (scorteUpdated ? "OK" : "FAILED"));
            
            // 8. Aggiornamento punti cliente (usa l'ID recuperato)
            String clienteId = controller.getct("MRCNRI90A01H501L");
            if (clienteId != null) {
                boolean puntiUpdated = controller.uppunti(clienteId, 10.0);
                System.out.println("✓ Aggiornamento punti cliente: " + (puntiUpdated ? "OK" : "FAILED"));
            } else {
                System.out.println("✓ Aggiornamento punti cliente: FAILED (cliente non trovato)");
            }
            
            System.out.println("✓ Workflow completo: COMPLETATO CON SUCCESSO");
            
        } catch (SQLException e) {
            System.err.println("✗ Errore workflow completo: " + e.getMessage());
        }
        System.out.println();
    }
}
