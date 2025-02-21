// server.js
require('dotenv').config();
const express = require('express');
const cors = require('cors');
const db = require('./db');

const app = express();

// Middleware per gestire JSON e CORS
app.use(cors());
app.use(express.json());

// Route di test per verificare la connessione al database
app.get('/', async (req, res) => {
  try {
    const result = await db.query('SELECT NOW()');
    res.json({ message: 'Backend in esecuzione', time: result.rows[0] });
  } catch (error) {
    console.error('Errore nel collegamento al database:', error);
    res.status(500).json({ error: 'Errore di connessione al database' });
  }
});

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
  console.log(`Server in esecuzione sulla porta ${PORT}`);
});
