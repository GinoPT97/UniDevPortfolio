const jwt = require('jsonwebtoken');
const passport = require('passport');
const GoogleStrategy = require('passport-google-oauth20').Strategy;
const FacebookStrategy = require('passport-facebook').Strategy;
const User = require('../models/user'); // Modello utente per salvare i dati dell'utente

const secretKey = 'your_secret_key'; // Usa una chiave sicura

// Funzione per generare JWT
const generateToken = (user) => {
  return jwt.sign({ id: user.id, username: user.username }, secretKey, { expiresIn: '1h' });
};

// Funzione per verificare il JWT
const verifyToken = (token) => {
  return jwt.verify(token, secretKey);
};

// Configurazione di Passport con Google
passport.use(new GoogleStrategy({
  clientID: 'YOUR_GOOGLE_CLIENT_ID',
  clientSecret: 'YOUR_GOOGLE_CLIENT_SECRET',
  callbackURL: 'http://localhost:3000/auth/google/callback',
}, async (accessToken, refreshToken, profile, done) => {
  // Logica per trovare o creare l'utente nel DB
  let user = await User.findOne({ googleId: profile.id });
  if (!user) {
    user = await User.create({ googleId: profile.id, username: profile.displayName });
  }
  return done(null, user);
}));

// Configurazione di Passport con Facebook
passport.use(new FacebookStrategy({
  clientID: 'YOUR_FACEBOOK_APP_ID',
  clientSecret: 'YOUR_FACEBOOK_APP_SECRET',
  callbackURL: 'http://localhost:3000/auth/facebook/callback',
}, async (accessToken, refreshToken, profile, done) => {
  // Logica per trovare o creare l'utente nel DB
  let user = await User.findOne({ facebookId: profile.id });
  if (!user) {
    user = await User.create({ facebookId: profile.id, username: profile.displayName });
  }
  return done(null, user);
}));

// Serializzazione dell'utente
passport.serializeUser((user, done) => {
  done(null, user.id);
});

// Deserializzazione dell'utente
passport.deserializeUser(async (id, done) => {
  const user = await User.findById(id); // Recupera l'utente dal DB
  done(null, user);
});

module.exports = {
  generateToken,
  verifyToken,
  passport,
};
