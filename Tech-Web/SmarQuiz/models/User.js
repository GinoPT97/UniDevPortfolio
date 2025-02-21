const config = require('../backend/config');
const bcrypt = require('bcrypt');

const createUser = async (name, email, password) => {
  const hashedPassword = await bcrypt.hash(password, 10);
  const query = 'INSERT INTO utenti (nome, email, password) VALUES ($1, $2, $3) RETURNING id, nome, email';
  const values = [name, email, hashedPassword];
  const result = await config.query(query, values);
  return result.rows[0];
};

const getUserByEmail = async (email) => {
  const query = 'SELECT * FROM utenti WHERE email = $1';
  const result = await config.query(query, [email]);
  return result.rows[0];
};

module.exports = { createUser, getUserByEmail };
