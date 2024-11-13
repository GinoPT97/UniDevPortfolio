const express = require('express');
const passport = require('passport');
const session = require('express-session');
const app = express();

// Configura la sessione
app.use(session({
  secret: 'your_session_secret',
  resave: false,
  saveUninitialized: true,
}));

// Inizializza Passport
app.use(passport.initialize());
app.use(passport.session());

// Rotta per avviare il login con Google
app.get('/auth/google',
  passport.authenticate('google', {
    scope: ['profile', 'email'],
  })
);

// Rotta di callback di Google dopo l'autenticazione
app.get('/auth/google/callback',
  passport.authenticate('google', { failureRedirect: '/' }),
  (req, res) => {
    // Successo del login, reindirizza alla home
    res.redirect('/');
  }
);

// Rotta di logout
app.get('/logout', (req, res) => {
  req.logout((err) => {
    if (err) {
      return next(err);
    }
    res.redirect('/');
  });
});

// Rotta per la home
app.get('/', (req, res) => {
  if (req.isAuthenticated()) {
    res.send(`Ciao, ${req.user.username}!`);
  } else {
    res.send('Benvenuto! <a href="/auth/google">Login con Google</a>');
  }
});

// Avvia il server
app.listen(3000, () => {
  console.log('Server in ascolto sulla porta 3000');
});
