import { AppNotification, Film, UserRow } from './types';

export function toLines(response: string): string[] {
  return response
    .split('\n')
    .map((line) => line.trim())
    .filter((line) => line.length > 0 && line !== 'END');
}

export function parseFilms(raw: string): Film[] {
  return toLines(raw)
    .map((line) => line.split('\t'))
    .filter((tokens) => tokens.length === 14)
    .map((t) => ({
      idFilm: Number(t[0]),
      titolo: t[1],
      genere: t[2],
      descrizione: t[3],
      linguaOriginale: t[4],
      dataRilascio: t[5],
      locandina: t[6],
      votoMedio: Number(t[7]),
      numeroVoti: Number(t[8]),
      popolarita: Number(t[9]),
      prezzo: Number(t[10]),
      numeroCopieDisponibili: Number(t[11]),
      numeroCopieInPrestito: Number(t[12]),
      stato: t[13],
    }));
}

export function parseUsers(raw: string): UserRow[] {
  return toLines(raw)
    .map((line) => {
      const splitIndex = line.indexOf(' - ');
      if (splitIndex < 0) {
        return null;
      }
      return {
        id: Number(line.slice(0, splitIndex)),
        username: line.slice(splitIndex + 3),
      };
    })
    .filter((u): u is UserRow => Boolean(u));
}

export function parseNotifications(raw: string): AppNotification[] {
  return toLines(raw)
    .map((line) => {
      const parts = line.split(' | ');
      if (parts.length < 3) {
        return null;
      }
      const id = Number(parts[0].replace('ID: ', '').trim());
      return {
        id,
        message: parts[1],
        date: parts[2].replace('Data: ', '').trim(),
      };
    })
    .filter((n): n is AppNotification => Boolean(n));
}
