const express = require('express');
const cors = require('cors');
const { Sequelize, DataTypes } = require('sequelize');

// Configurazione dell'app Express
const app = express();
const PORT = process.env.PORT || 5000;

app.use(cors());
app.use(express.json());

// Configurazione di Sequelize per PostgreSQL
const sequelize = new Sequelize('hivemind', 'username', 'password', {
  host: 'localhost',
  dialect: 'postgres'
});

// Test della connessione a PostgreSQL
sequelize.authenticate()
  .then(() => {
    console.log('Connection has been established successfully.');
  })
  .catch(err => {
    console.error('Unable to connect to the database:', err);
  });

// Definizione del modello Idea
const Idea = sequelize.define('Idea', {
  title: {
    type: DataTypes.STRING,
    allowNull: false
  },
  description: {
    type: DataTypes.TEXT,
    allowNull: false
  },
  upvotes: {
    type: DataTypes.INTEGER,
    defaultValue: 0
  },
  downvotes: {
    type: DataTypes.INTEGER,
    defaultValue: 0
  }
}, {
  timestamps: true
});

// Sincronizza i modelli con il database
sequelize.sync()
  .then(() => {
    console.log('Database & tables created!');
  });

// Rotte
app.get('/', (req, res) => {
  res.send('Welcome to Hivemind API');
});

app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});
