const express = require('express');
const passport = require('passport');
const { generateToken } = require('../config/auth');
const router = express.Router();

// Rotta di login con Google
router.get('/google',
  passport.authenticate('google', { scope: ['profile', 'email'] })
);

// Rotta di callback di Google
router.get('/google/callback',
  passport.authenticate('google', { failureRedirect: '/login' }),
  (req, res) => {
    // Autenticazione avvenuta con successo, crea il token JWT
    const token = generateToken(req.user);
    res.json({ token });
  }
);

// Rotta di login con Facebook
router.get('/facebook',
  passport.authenticate('facebook', { scope: ['email'] })
);

// Rotta di callback di Facebook
router.get('/facebook/callback',
  passport.authenticate('facebook', { failureRedirect: '/login' }),
  (req, res) => {
    // Autenticazione avvenuta con successo, crea il token JWT
    const token = generateToken(req.user);
    res.json({ token });
  }
);

module.exports = router;
