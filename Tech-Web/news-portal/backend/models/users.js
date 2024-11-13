const pool = require('../config/db');

const User = {
  // Crea un nuovo utente nel database
  create: async (userData) => {
    const { googleId, facebookId, username } = userData;
    const res = await pool.query(
      'INSERT INTO users (google_id, facebook_id, username) VALUES ($1, $2, $3) RETURNING *',
      [googleId || facebookId, googleId || facebookId, username]
    );
    return res.rows[0];
  },

  // Trova un utente tramite Google ID
  findOne: async (userId) => {
    const res = await pool.query('SELECT * FROM users WHERE google_id = $1 OR facebook_id = $2', [userId, userId]);
    return res.rows[0];
  },

  // Trova un utente tramite ID
  findById: async (id) => {
    const res = await pool.query('SELECT * FROM users WHERE id = $1', [id]);
    return res.rows[0];
  }
};

module.exports = User;
