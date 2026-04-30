export type TabKey = 'dashboard' | 'catalogo' | 'carrello' | 'notifiche';

export type Session = {
  userId: number;
  username: string;
};

export type Film = {
  idFilm: number;
  titolo: string;
  genere: string;
  descrizione: string;
  linguaOriginale: string;
  dataRilascio: string;
  locandina: string;
  votoMedio: number;
  numeroVoti: number;
  popolarita: number;
  prezzo: number;
  numeroCopieDisponibili: number;
  numeroCopieInPrestito: number;
  stato: string;
};

export type AppNotification = {
  id: number;
  message: string;
  date: string;
};

export type UserRow = {
  id: number;
  username: string;
};
