const passport = require('passport');
const GoogleStrategy = require('passport-google-oauth20').Strategy;
const FacebookStrategy = require('passport-facebook').Strategy;
const User = require('./db'); // Importa il modello utente
const jwt = require('jsonwebtoken');

passport.serializeUser((user, done) => {
  done(null, user.id);
});

passport.deserializeUser(async (id, done) => {
  const user = await User.findUserById(id);
  done(null, user);
});

// Google Strategy
passport.use(new GoogleStrategy({
  clientID: 'YOUR_GOOGLE_CLIENT_ID',
  clientSecret: 'YOUR_GOOGLE_CLIENT_SECRET',
  callbackURL: 'http://localhost:3000/auth/google/callback',
}, async (accessToken, refreshToken, profile, done) => {
  let user = await User.findUserByExternalId(profile.id);
    user = await User.createUser({
      googleId: profile.id,
      username: profile.displayName,
    });
  }
  return done(null, user);
}));

// Facebook Strategy
passport.use(new FacebookStrategy({
  clientID: 'YOUR_FACEBOOK_APP_ID',
  clientSecret: 'YOUR_FACEBOOK_APP_SECRET',
  callbackURL: 'http://localhost:3000/auth/facebook/callback',
}, async (accessToken, refreshToken, profile, done) => {
  let user = await User.findUserByExternalId(profile.id);
    user = await User.createUser({
      facebookId: profile.id,
      username: profile.displayName,
    });
  }
  return done(null, user);
}));
