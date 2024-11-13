const { Pool } = require('pg');
const pool = new Pool({
  user: 'postgres',
  host: 'localhost',
  database: 'pressportal_db',
  password: 'your_password',
  port: 5432,
});

const query = async (text, params) => {
  try {
    const res = await pool.query(text, params);
    return res.rows;
  } catch (err) {
    console.error('Database query error:', err);
    throw err;
  }
};

const createUser = async (userData) => {
  const { googleId, facebookId, username } = userData;
  const text = `
    INSERT INTO users (google_id, facebook_id, username)
    VALUES (, , )
    RETURNING id, google_id, facebook_id, username
  `;
  const values = [googleId || facebookId, googleId || facebookId, username];
  const result = await query(text, values);
  return result[0];
};

const findUserByExternalId = async (externalId) => {
  const text = `
    SELECT * FROM users WHERE google_id =  OR facebook_id = 
  `;
  const values = [externalId];
  const result = await query(text, values);
  return result[0];
};

const findUserById = async (id) => {
  const text = `
    SELECT * FROM users WHERE id = 
  `;
  const values = [id];
  const result = await query(text, values);
  return result[0];
};

module.exports = {
  query,
  createUser,
  findUserByExternalId,
  findUserById,
};
