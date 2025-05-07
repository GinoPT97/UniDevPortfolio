import { Request, Response } from 'express';
import Sighting, { ISighting } from '../models/Sighting';

export const getSightings = async (req: Request, res: Response): Promise<void> => {
  try {
    const sightings: ISighting[] = await Sighting.find();
    res.status(200).json(sightings);
  } catch (error) {
    res.status(500).json({ message: error instanceof Error ? error.message : 'Unknown error' });
  }
};

export const getSightingById = async (req: Request, res: Response): Promise<void> => {
  try {
    const { id } = req.params;
    const sighting = await Sighting.findById(id);
    if (!sighting) {
      res.status(404).json({ message: 'Sighting not found' });
      return;
    }
    res.status(200).json(sighting);
  } catch (error) {
    res.status(500).json({ message: error instanceof Error ? error.message : 'Unknown error' });
  }
};

export const createSighting = async (req: Request, res: Response): Promise<void> => {
  const { description, location } = req.body;
  const imageUrl = req.file ? `/uploads/${req.file.filename}` : undefined;

  const newSighting = new Sighting({ description, location, imageUrl });

  try {
    await newSighting.save();
    res.status(201).json(newSighting);
  } catch (error) {
    res.status(500).json({ message: error instanceof Error ? error.message : 'Unknown error' });
  }
};

export const updateSighting = async (req: Request, res: Response): Promise<void> => {
  try {
    const { id } = req.params;
    const updatedSighting = await Sighting.findByIdAndUpdate(id, req.body, { new: true });
    if (!updatedSighting) {
      res.status(404).json({ message: 'Avvistamento non trovato' });
      return;
    }
    res.json(updatedSighting);
  } catch (error) {
    res.status(500).json({ message: 'Errore durante l\'aggiornamento dell\'avvistamento', error });
  }
};

export const deleteSighting = async (req: Request, res: Response): Promise<void> => {
  try {
    const { id } = req.params;
    const deletedSighting = await Sighting.findByIdAndDelete(id);
    if (!deletedSighting) {
      res.status(404).json({ message: 'Avvistamento non trovato' });
      return;
    }
    res.json({ message: 'Avvistamento eliminato con successo' });
  } catch (error) {
    res.status(500).json({ message: 'Errore durante l\'eliminazione dell\'avvistamento', error });
  }
};
