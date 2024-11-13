const express = require('express');
const session = require('express-session');
const passport = require('passport');
const app = express();
const port = 3000;

// Middleware
app.use(express.json());
app.use(session({
  secret: 'your_session_secret',
  resave: false,
  saveUninitialized: true,
}));
app.use(passport.initialize());
app.use(passport.session());

// Rotte
const authRoutes = require('./routes/authRoutes');
app.use('/auth', authRoutes);

// Avvio server
app.listen(port, () => {
  console.log(`Server in ascolto su http://localhost:${port}`);
});
