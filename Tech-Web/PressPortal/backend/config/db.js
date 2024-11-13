const { Pool } = require('pg');

// Configurazione della connessione al database
const pool = new Pool({
  user: 'postgres',         // Nome utente del database
  host: 'localhost',        // Host del database
  database: 'pressportal',  // Nome del database
  password: 'admin',// Password del database
  port: 5432,               // Porta predefinita di PostgreSQL
});

// Funzione per eseguire una query
const query = async (text, params) => {
  try {
    const res = await pool.query(text, params);
    return res.rows;
  } catch (err) {
    console.error('Errore durante l\'esecuzione della query:', err);
    throw err;
  }
};

// Funzione per chiudere la connessione al database
const closeConnection = async () => {
  await pool.end();
};

module.exports = {
  query,
  closeConnection,
};
