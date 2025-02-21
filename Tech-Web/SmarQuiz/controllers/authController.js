const User = require('../models/User');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
require('dotenv').config();

// Registrazione
const register = async (req, res) => {
  const { name, email, password } = req.body;

  try {
    const existingUser = await User.getUserByEmail(email);
    if (existingUser) {
      return res.status(400).json({ message: 'Email già registrata' });
    }

    const user = await User.createUser(name, email, password);
    res.status(201).json({ message: 'Registrazione completata', user });
  } catch (error) {
    console.error('Errore nella registrazione:', error);
    res.status(500).json({ message: 'Errore nel server' });
  }
};

// Login
const login = async (req, res) => {
  const { email, password } = req.body;

  try {
    const user = await User.getUserByEmail(email);
    if (!user) {
      return res.status(400).json({ message: 'Email o password non valide' });
    }

    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch) {
      return res.status(400).json({ message: 'Email o password non valide' });
    }

    const token = jwt.sign({ userId: user.id, name: user.nome }, process.env.JWT_SECRET, { expiresIn: '1h' });
    res.json({ message: 'Login effettuato', token });
  } catch (error) {
    console.error('Errore nel login:', error);
    res.status(500).json({ message: 'Errore nel server' });
  }
};

module.exports = { register, login };
