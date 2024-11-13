const passport = require('passport');
const express = require('express');
const router = express.Router();

// Autenticazione tramite Google
router.get('/google',
  passport.authenticate('google', {
    scope: ['profile', 'email']
  })
);

// Callback di Google per l'autenticazione
router.get('/google/callback',
  passport.authenticate('google', { failureRedirect: '/' }),
  (req, res) => {
    // L'utente è autenticato, reindirizza alla home page
    res.redirect('/');
  }
);

// Registrazione dell'utente (possiamo usare Google per registrare un nuovo utente)
router.post('/register', async (req, res) => {
  try {
    const { username, email, password } = req.body;

    // Verifica se l'utente esiste già
    const existingUser = await db.query('SELECT * FROM users WHERE email = $1', [email]);

    if (existingUser.length > 0) {
      return res.status(400).json({ message: 'User already exists' });
    }

    // Crea un nuovo utente
    const newUser = await db.query('INSERT INTO users (username, email, password) VALUES ($1, $2, $3) RETURNING *',
                                    [username, email, password]);

    // Invia una risposta di successo
    res.status(201).json({ message: 'User registered successfully', user: newUser[0] });
  } catch (error) {
    res.status(500).json({ message: 'Error registering user', error });
  }
});

// Login dell'utente con username e password
router.post('/login', async (req, res) => {
  const { email, password } = req.body;

  try {
    // Verifica se l'utente esiste
    const user = await db.query('SELECT * FROM users WHERE email = $1', [email]);

    if (user.length === 0) {
      return res.status(400).json({ message: 'User not found' });
    }

    // Verifica la password
    if (user[0].password !== password) {
      return res.status(400).json({ message: 'Invalid password' });
    }

    // Se la password è corretta, crea un token per l'utente
    const token = generateToken(user[0]);

    // Risposta con il token (sessione)
    res.status(200).json({ message: 'Login successful', token });
  } catch (error) {
    res.status(500).json({ message: 'Error logging in', error });
  }
});

// Logout dell'utente
router.get('/logout', (req, res) => {
  req.logout((err) => {
    if (err) {
      return next(err);
    }
    res.redirect('/');
  });
});

module.exports = router;
