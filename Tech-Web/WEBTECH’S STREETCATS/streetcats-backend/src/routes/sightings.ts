import express from 'express';
import { getSightings, createSighting, getSightingById, updateSighting, deleteSighting } from '../controllers/sightingsController';
import upload from '../middleware/upload';

const router = express.Router();

router.get('/', getSightings);
router.get('/:id', getSightingById);
router.post('/', upload.single('image'), createSighting);
router.put('/:id', updateSighting);
router.delete('/:id', deleteSighting);

export default router;
