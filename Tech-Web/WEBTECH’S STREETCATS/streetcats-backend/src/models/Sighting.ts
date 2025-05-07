import mongoose, { Document, Schema } from 'mongoose';

export interface ISighting extends Document {
  description: string;
  location: string;
  date: Date;
  imageUrl?: string;
}

const SightingSchema: Schema = new Schema({
  description: { type: String, required: true },
  location: { type: String, required: true },
  date: { type: Date, default: Date.now },
  imageUrl: { type: String },
});

export default mongoose.model<ISighting>('Sighting', SightingSchema);
