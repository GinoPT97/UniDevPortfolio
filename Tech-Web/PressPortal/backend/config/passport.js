const passport = require('passport');
const GoogleStrategy = require('passport-google-oauth20').Strategy;

// Configura la serializzazione e deserializzazione dell'utente nella sessione
passport.serializeUser((user, done) => {
  done(null, user.id);
});

passport.deserializeUser(async (id, done) => {
  // Recupera i dettagli dell'utente dal database usando l'id (puoi adattarlo alla tua logica)
  const user = await db.query('SELECT * FROM users WHERE id = $1', [id]);
  done(null, user[0]);
});

// Configura la strategia di Google OAuth
passport.use(new GoogleStrategy({
  clientID: 'YOUR_GOOGLE_CLIENT_ID',   // Ottieni dal Google Developer Console
  clientSecret: 'YOUR_GOOGLE_CLIENT_SECRET',  // Ottieni dal Google Developer Console
  callbackURL: 'http://localhost:3000/auth/google/callback', // Cambia con il tuo URL di callback
}, async (accessToken, refreshToken, profile, done) => {
  try {
    // Verifica se l'utente esiste già nel database
    let user = await db.query('SELECT * FROM users WHERE google_id = $1', [profile.id]);

    if (user.length === 0) {
      // Se l'utente non esiste, crealo nel database
      user = await db.query('INSERT INTO users (google_id, username, email) VALUES ($1, $2, $3) RETURNING *',
        [profile.id, profile.displayName, profile.emails[0].value]);

      user = user[0]; // Prendi il primo risultato
    }
    // Passa l'utente alla funzione di callback
    done(null, user);
  } catch (err) {
    console.error(err);
    done(err);
  }
}));
