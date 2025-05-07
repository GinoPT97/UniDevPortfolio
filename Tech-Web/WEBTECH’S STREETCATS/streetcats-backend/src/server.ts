import express from 'express';
import mongoose from 'mongoose';
import dotenv from 'dotenv';
import helmet from 'helmet';
import cors from 'cors';
import sightingsRoutes from './routes/sightings';

dotenv.config();

const app = express();
const PORT = process.env.PORT || 5000;
const MONGODB_URI = process.env.MONGODB_URI || 'mongodb://localhost:27017/streetcats';

// Middleware
app.use(helmet());
app.use(cors());
app.use(express.json());

// Routes
app.use('/api/sightings', sightingsRoutes);

// Database connection and server start
mongoose
  .connect(MONGODB_URI)
  .then(() => {
    console.log('Connesso a MongoDB');
    app.listen(PORT, () => {
      console.log(`Server in ascolto su http://localhost:${PORT}`);
    });
  })
  .catch((error) => {
    console.error('Errore di connessione a MongoDB:', error);
  });
