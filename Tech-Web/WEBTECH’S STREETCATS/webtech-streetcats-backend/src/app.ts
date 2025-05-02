import express from 'express';
import cors from 'cors';
import { json } from 'body-parser';
import { healthRouter } from './routes/health.routes';
// ...importa altri router...

const app = express();
app.use(cors());
app.use(json());

// Monta un semplice health check
app.use('/health', healthRouter);

// Monta qui tutti gli altri router
// app.use('/api/auth', authRouter);
// app.use('/api/sightings', sightingsRouter);
// ...

export default app;
