import React from 'react';
import { Image } from 'expo-image';
import { Pressable, ScrollView, Text, TextInput, View } from 'react-native';
import { styles } from './styles';
import { AppNotification, Film, UserRow } from './types';

type AuthSectionProps = {
  authMode: 'login' | 'register';
  username: string;
  password: string;
  confirmPassword: string;
  setUsername: (v: string) => void;
  setPassword: (v: string) => void;
  setConfirmPassword: (v: string) => void;
  onSubmit: () => void;
  onToggleMode: () => void;
};

export function AuthSection(props: Readonly<AuthSectionProps>) {
  return (
    <View style={styles.card}>
      <Text style={styles.title}>CiNEBOX Login</Text>
      <TextInput
        style={styles.input}
        placeholder="Username"
        value={props.username}
        onChangeText={props.setUsername}
        autoCapitalize="none"
      />
      <TextInput
        style={styles.input}
        placeholder="Password"
        value={props.password}
        onChangeText={props.setPassword}
        secureTextEntry
      />
      {props.authMode === 'register' && (
        <TextInput
          style={styles.input}
          placeholder="Conferma password"
          value={props.confirmPassword}
          onChangeText={props.setConfirmPassword}
          secureTextEntry
        />
      )}

      <Pressable style={styles.primaryButton} onPress={props.onSubmit}>
        <Text style={styles.primaryButtonText}>{props.authMode === 'login' ? 'Accedi' : 'Registrati'}</Text>
      </Pressable>

      <Pressable style={styles.secondaryButton} onPress={props.onToggleMode}>
        <Text style={styles.secondaryButtonText}>
          {props.authMode === 'login' ? 'Vai a Registrazione' : 'Torna a Login'}
        </Text>
      </Pressable>
    </View>
  );
}

type DashboardSectionProps = {
  isAdmin: boolean;
  allRentalsOverview: string[];
  last5Rentals: string[];
  top5Films: string[];
  activeRentals: string[];
  onMarkReturned: (line: string) => void;
};

export function DashboardSection(props: Readonly<DashboardSectionProps>) {
  if (props.isAdmin) {
    return (
      <View style={styles.card}>
        <Text style={styles.sectionTitle}>Panoramica Noleggi Attivi</Text>
        {props.allRentalsOverview.length === 0 ? (
          <Text style={styles.muted}>Nessun noleggio attivo.</Text>
        ) : (
          props.allRentalsOverview.map((line) => (
            <Pressable key={line} style={styles.listItem} onPress={() => props.onMarkReturned(line)}>
              <Text style={styles.listText}>{line}</Text>
              <Text style={styles.badge}>Segna restituito</Text>
            </Pressable>
          ))
        )}
      </View>
    );
  }

  return (
    <>
      <View style={styles.card}>
        <Text style={styles.sectionTitle}>Ultimi 5 Noleggi</Text>
        {props.last5Rentals.length === 0 ? (
          <Text style={styles.muted}>Nessun dato.</Text>
        ) : (
          props.last5Rentals.map((item) => (
            <Text key={`l5-${item}`} style={styles.listText}>
              • {item}
            </Text>
          ))
        )}
      </View>
      <View style={styles.card}>
        <Text style={styles.sectionTitle}>Top 5 Film Piu Noleggiati</Text>
        {props.top5Films.length === 0 ? (
          <Text style={styles.muted}>Nessun dato.</Text>
        ) : (
          props.top5Films.map((item) => (
            <Text key={`t5-${item}`} style={styles.listText}>
              • {item}
            </Text>
          ))
        )}
      </View>
      <View style={styles.card}>
        <Text style={styles.sectionTitle}>Noleggi Attivi</Text>
        {props.activeRentals.length === 0 ? (
          <Text style={styles.muted}>Nessun noleggio attivo.</Text>
        ) : (
          props.activeRentals.map((item) => (
            <Text key={`a-${item}`} style={styles.listText}>
              • {item}
            </Text>
          ))
        )}
      </View>
    </>
  );
}

type CatalogSectionProps = {
  filteredFilms: Film[];
  search: string;
  setSearch: (v: string) => void;
  genres: string[];
  genreFilter: string;
  setGenreFilter: (v: string) => void;
  sortMode: 'Nessuno' | 'PopolaritaAsc' | 'PopolaritaDesc';
  setSortMode: (v: 'Nessuno' | 'PopolaritaAsc' | 'PopolaritaDesc') => void;
  cart: Film[];
  isAdmin: boolean;
  onToggleCartFilm: (film: Film) => void;
};

export function CatalogSection(props: Readonly<CatalogSectionProps>) {
  const getPosterUri = (locandina: string) => {
    const value = locandina.trim();
    if (!value) {
      return null;
    }
    if (value.startsWith('http://') || value.startsWith('https://')) {
      return value;
    }
    if (value.startsWith('/')) {
      return `https://image.tmdb.org/t/p/w500${value}`;
    }
    return null;
  };

  const formatDate = (value: string) => {
    if (!value) {
      return '-';
    }
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
      return value;
    }
    return date.toLocaleDateString('it-IT');
  };

  return (
    <>
      <View style={styles.card}>
        <Text style={styles.sectionTitle}>Catalogo Film ({props.filteredFilms.length})</Text>
        <TextInput
          style={styles.input}
          placeholder="Cerca per titolo/genere"
          value={props.search}
          onChangeText={props.setSearch}
        />
        <Text style={styles.muted}>Genere</Text>
        <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.chipRow}>
          {props.genres.map((genre) => (
            <Pressable
              key={genre}
              style={[styles.chip, props.genreFilter === genre && styles.chipActive]}
              onPress={() => props.setGenreFilter(genre)}
            >
              <Text style={props.genreFilter === genre ? styles.chipTextActive : styles.chipText}>{genre}</Text>
            </Pressable>
          ))}
        </ScrollView>

        <Text style={styles.muted}>Ordina</Text>
        <View style={styles.row}>
          <Pressable
            style={[styles.chip, props.sortMode === 'Nessuno' && styles.chipActive]}
            onPress={() => props.setSortMode('Nessuno')}
          >
            <Text style={props.sortMode === 'Nessuno' ? styles.chipTextActive : styles.chipText}>Nessuno</Text>
          </Pressable>
          <Pressable
            style={[styles.chip, props.sortMode === 'PopolaritaAsc' && styles.chipActive]}
            onPress={() => props.setSortMode('PopolaritaAsc')}
          >
            <Text style={props.sortMode === 'PopolaritaAsc' ? styles.chipTextActive : styles.chipText}>Popolarita ↑</Text>
          </Pressable>
          <Pressable
            style={[styles.chip, props.sortMode === 'PopolaritaDesc' && styles.chipActive]}
            onPress={() => props.setSortMode('PopolaritaDesc')}
          >
            <Text style={props.sortMode === 'PopolaritaDesc' ? styles.chipTextActive : styles.chipText}>Popolarita ↓</Text>
          </Pressable>
        </View>
      </View>

      {props.filteredFilms.map((film) => {
        const inCart = props.cart.some((f) => f.idFilm === film.idFilm);
        const posterUri = getPosterUri(film.locandina);
        return (
          <View key={film.idFilm} style={styles.filmCard}>
            {posterUri ? (
              <Image
                source={{ uri: posterUri }}
                style={styles.filmPoster}
                contentFit="cover"
                transition={160}
              />
            ) : (
              <View style={styles.filmPosterFallback}>
                <Text style={styles.filmPosterFallbackText}>Locandina non disponibile</Text>
              </View>
            )}

            <View style={styles.filmContent}>
              <Text style={styles.filmTitle}>{film.titolo}</Text>
              <Text style={styles.muted}>{film.genere}</Text>
              <Text style={styles.small}>{film.descrizione || 'Descrizione non disponibile'}</Text>

              <View style={styles.filmMetaGrid}>
                <Text style={styles.filmMetaItem}>Lingua: {film.linguaOriginale || '-'}</Text>
                <Text style={styles.filmMetaItem}>Data rilascio: {formatDate(film.dataRilascio)}</Text>
                <Text style={styles.filmMetaItem}>Voto medio: {film.votoMedio.toFixed(1)}</Text>
                <Text style={styles.filmMetaItem}>Numero voti: {film.numeroVoti}</Text>
                <Text style={styles.filmMetaItem}>Popolarita: {film.popolarita.toFixed(1)}</Text>
                <Text style={styles.filmMetaItem}>Prezzo: € {film.prezzo.toFixed(2)}</Text>
                <Text style={styles.filmMetaItem}>Copie disponibili: {film.numeroCopieDisponibili}</Text>
                <Text style={styles.filmMetaItem}>In prestito: {film.numeroCopieInPrestito}</Text>
              </View>

              <View style={styles.filmStateRow}>
                <Text style={styles.badge}>Stato: {film.stato || '-'}</Text>
              </View>
            </View>

            {!props.isAdmin && (
              <Pressable
                style={[styles.primaryButton, inCart && styles.removeButton]}
                onPress={() => props.onToggleCartFilm(film)}
              >
                <Text style={styles.primaryButtonText}>{inCart ? 'Rimuovi dal carrello' : 'Aggiungi al carrello'}</Text>
              </Pressable>
            )}
          </View>
        );
      })}
    </>
  );
}

type CartSectionProps = {
  isAdmin: boolean;
  currentMaxRentalsHint: string;
  maxRentals: string;
  setMaxRentals: (v: string) => void;
  onSetAdminMaxRentals: () => void;
  cart: Film[];
  cartTotal: number;
  onToggleCartFilm: (film: Film) => void;
  onCheckout: () => void;
  onClearCart: () => void;
};

export function CartSection(props: Readonly<CartSectionProps>) {
  if (props.isAdmin) {
    return (
      <View style={styles.card}>
        <Text style={styles.sectionTitle}>Controllo Admin</Text>
        <Text style={styles.muted}>Max noleggi corrente: {props.currentMaxRentalsHint || '-'}</Text>
        <TextInput
          style={styles.input}
          placeholder="Nuovo max noleggi"
          value={props.maxRentals}
          onChangeText={props.setMaxRentals}
          keyboardType="numeric"
        />
        <Pressable style={styles.primaryButton} onPress={props.onSetAdminMaxRentals}>
          <Text style={styles.primaryButtonText}>Aggiorna limite</Text>
        </Pressable>
      </View>
    );
  }

  return (
    <View style={styles.card}>
      <Text style={styles.sectionTitle}>Carrello</Text>
      {props.cart.length === 0 ? (
        <Text style={styles.muted}>Il carrello e vuoto.</Text>
      ) : (
        props.cart.map((film) => (
          <View key={`c-${film.idFilm}`} style={styles.listItem}>
            <Text style={styles.listText}>
              {film.titolo} - € {film.prezzo.toFixed(2)}
            </Text>
            <Pressable onPress={() => props.onToggleCartFilm(film)}>
              <Text style={styles.link}>Rimuovi</Text>
            </Pressable>
          </View>
        ))
      )}
      <Text style={styles.sectionTitle}>Totale: € {props.cartTotal.toFixed(2)}</Text>
      <View style={styles.row}>
        <Pressable style={styles.primaryButton} onPress={props.onCheckout}>
          <Text style={styles.primaryButtonText}>Checkout</Text>
        </Pressable>
        <Pressable style={[styles.secondaryButton, styles.flexGrow]} onPress={props.onClearCart}>
          <Text style={styles.secondaryButtonText}>Svuota</Text>
        </Pressable>
      </View>
    </View>
  );
}

type NotificationsSectionProps = {
  isAdmin: boolean;
  users: UserRow[];
  selectedUserId: number | null;
  setSelectedUserId: (id: number) => void;
  notificationMessage: string;
  setNotificationMessage: (v: string) => void;
  onSendAdminNotification: () => void;
  notifications: AppNotification[];
  onDeleteNotification: (id: number) => void;
};

export function NotificationsSection(props: Readonly<NotificationsSectionProps>) {
  if (props.isAdmin) {
    return (
      <View style={styles.card}>
        <Text style={styles.sectionTitle}>Invia Notifica</Text>
        <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.chipRow}>
          {props.users.map((u) => (
            <Pressable
              key={u.id}
              style={[styles.chip, props.selectedUserId === u.id && styles.chipActive]}
              onPress={() => props.setSelectedUserId(u.id)}
            >
              <Text style={props.selectedUserId === u.id ? styles.chipTextActive : styles.chipText}>
                {u.id} - {u.username}
              </Text>
            </Pressable>
          ))}
        </ScrollView>
        <TextInput
          style={[styles.input, styles.multiInput]}
          multiline
          value={props.notificationMessage}
          onChangeText={props.setNotificationMessage}
          placeholder="Messaggio notifica"
        />
        <Pressable style={styles.primaryButton} onPress={props.onSendAdminNotification}>
          <Text style={styles.primaryButtonText}>Invia</Text>
        </Pressable>
      </View>
    );
  }

  return (
    <View style={styles.card}>
      <Text style={styles.sectionTitle}>Le Tue Notifiche</Text>
      {props.notifications.length === 0 ? (
        <Text style={styles.muted}>Nessuna notifica.</Text>
      ) : (
        props.notifications.map((n) => (
          <View key={n.id} style={styles.listItemColumn}>
            <Text style={styles.listText}>{n.message}</Text>
            <Text style={styles.small}>{n.date}</Text>
            <Pressable onPress={() => props.onDeleteNotification(n.id)}>
              <Text style={styles.link}>Elimina</Text>
            </Pressable>
          </View>
        ))
      )}
    </View>
  );
}
