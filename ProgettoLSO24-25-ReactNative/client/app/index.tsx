import React, { useMemo, useState } from 'react';
import { Alert, Pressable, ScrollView, Text, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { sendCommand } from './cinebox/api';
import { parseFilms, parseNotifications, parseUsers, toLines } from './cinebox/parsers';
import {
  AuthSection,
  CartSection,
  CatalogSection,
  DashboardSection,
  NotificationsSection,
} from './cinebox/sections';
import { styles } from './cinebox/styles.tsx';
import { AppNotification, Film, Session, TabKey, UserRow } from './cinebox/types';

export default function CineboxScreen() {
  const [authMode, setAuthMode] = useState<'login' | 'register'>('login');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [session, setSession] = useState<Session | null>(null);
  const [status, setStatus] = useState('');
  const [loading, setLoading] = useState(false);

  const [activeTab, setActiveTab] = useState<TabKey>('dashboard');

  const [films, setFilms] = useState<Film[]>([]);
  const [cart, setCart] = useState<Film[]>([]);
  const [search, setSearch] = useState('');
  const [genreFilter, setGenreFilter] = useState('Tutti');
  const [sortMode, setSortMode] = useState<'Nessuno' | 'PopolaritaAsc' | 'PopolaritaDesc'>('Nessuno');

  const [last5Rentals, setLast5Rentals] = useState<string[]>([]);
  const [top5Films, setTop5Films] = useState<string[]>([]);
  const [activeRentals, setActiveRentals] = useState<string[]>([]);
  const [allRentalsOverview, setAllRentalsOverview] = useState<string[]>([]);

  const [users, setUsers] = useState<UserRow[]>([]);
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
  const [notificationMessage, setNotificationMessage] = useState(
    'Superata la scadenza per la restituzione, consegnare il film'
  );
  const [notifications, setNotifications] = useState<AppNotification[]>([]);

  const [maxRentals, setMaxRentals] = useState('');
  const [currentMaxRentalsHint, setCurrentMaxRentalsHint] = useState('');

  const isAdmin = session?.userId === 0;

  const genres = useMemo(() => {
    const set = new Set<string>();
    films.forEach((film) => {
      film.genere.split(',').forEach((g) => set.add(g.trim()));
    });
    return ['Tutti', ...Array.from(set).sort((a, b) => a.localeCompare(b))];
  }, [films]);

  const filteredFilms = useMemo(() => {
    let result = films.filter((film) => {
      const q = search.trim().toLowerCase();
      const matchesQuery =
        q.length === 0 ||
        film.titolo.toLowerCase().includes(q) ||
        film.genere.toLowerCase().includes(q);

      const matchesGenre =
        genreFilter === 'Tutti' ||
        film.genere
          .split(',')
          .map((g) => g.trim().toLowerCase())
          .includes(genreFilter.toLowerCase());

      return matchesQuery && matchesGenre;
    });

    if (sortMode === 'PopolaritaAsc') {
      result = [...result].sort((a, b) => a.numeroVoti - b.numeroVoti);
    }
    if (sortMode === 'PopolaritaDesc') {
      result = [...result].sort((a, b) => b.numeroVoti - a.numeroVoti);
    }

    return result;
  }, [films, genreFilter, search, sortMode]);

  const cartTotal = useMemo(() => cart.reduce((acc, film) => acc + film.prezzo, 0), [cart]);

  const loadFilms = async () => {
    const response = await sendCommand('GET_FILMS', true);
    setFilms(parseFilms(response));
  };

  const loadDashboard = async () => {
    if (!session) {
      return;
    }

    if (session.userId === 0) {
      const overview = await sendCommand('GET_ALL_RENTALS_OVERVIEW', true);
      setAllRentalsOverview(toLines(overview));
      return;
    }

    const [last5, top5, active] = await Promise.all([
      sendCommand(`GET_LAST_5_RENTALS_BY_USER ${session.userId}`, true),
      sendCommand('GET_TOP_5_RENTED_FILMS', true),
      sendCommand(`GET_ACTIVE_RENTALS_BY_USER ${session.userId}`, true),
    ]);

    setLast5Rentals(toLines(last5));
    setTop5Films(toLines(top5));
    setActiveRentals(toLines(active));
  };

  const loadNotifications = async () => {
    if (!session) {
      return;
    }

    if (session.userId === 0) {
      const usersRaw = await sendCommand('LIST_USERS', true);
      const parsedUsers = parseUsers(usersRaw).filter((u) => u.id !== 0);
      setUsers(parsedUsers);
      if (parsedUsers.length > 0 && selectedUserId == null) {
        setSelectedUserId(parsedUsers[0].id);
      }
      return;
    }

    const notificationsRaw = await sendCommand(`GET_NOTIFICATIONS ${session.userId}`, true);
    setNotifications(parseNotifications(notificationsRaw));
  };

  const loadAdminLimits = async () => {
    const response = await sendCommand('GET_MAX_RENTALS');
    const value = response.replace('MAX_RENTALS', '').trim();
    setCurrentMaxRentalsHint(value);
  };

  const refreshTab = async (tab: TabKey) => {
    if (!session) {
      return;
    }

    if (tab === 'catalogo') {
      await loadFilms();
      return;
    }

    if (tab === 'dashboard') {
      await loadDashboard();
      return;
    }

    if (tab === 'notifiche') {
      await loadNotifications();
      return;
    }

    if (tab === 'carrello' && isAdmin) {
      await loadAdminLimits();
    }
  };

  const withBusy = async (action: () => Promise<void>) => {
    try {
      setLoading(true);
      await action();
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Errore sconosciuto';
      setStatus(message);
      Alert.alert('Errore', message);
    } finally {
      setLoading(false);
    }
  };

  const login = () =>
    withBusy(async () => {
      const response = await sendCommand(`LOGIN ${username.trim()} ${password}`);
      if (!response.startsWith('SUCCESS')) {
        throw new Error(response || 'Login fallito');
      }
      const userId = Number(response.split(' ')[1]);
      setSession({ userId, username: username.trim() });
      setStatus(`Login effettuato: ${username.trim()}`);
      setActiveTab('dashboard');
      await Promise.all([loadFilms(), loadDashboard(), loadNotifications()]);
    });

  const register = () =>
    withBusy(async () => {
      if (password !== confirmPassword) {
        throw new Error('Le password non coincidono');
      }
      const response = await sendCommand(`REGISTER ${username.trim()} ${password}`);
      if (response !== 'SUCCESS') {
        throw new Error(response || 'Registrazione fallita');
      }
      setStatus('Registrazione completata, ora fai login');
      setAuthMode('login');
      setPassword('');
      setConfirmPassword('');
    });

  const logout = () => {
    setSession(null);
    setCart([]);
    setStatus('Logout effettuato');
    setActiveTab('dashboard');
    setLast5Rentals([]);
    setTop5Films([]);
    setActiveRentals([]);
    setAllRentalsOverview([]);
    setNotifications([]);
  };

  const toggleCartFilm = (film: Film) => {
    const exists = cart.some((f) => f.idFilm === film.idFilm);
    if (exists) {
      setCart((prev) => prev.filter((f) => f.idFilm !== film.idFilm));
      return;
    }
    setCart((prev) => [...prev, film]);
  };

  const checkout = () =>
    withBusy(async () => {
      if (!session) {
        return;
      }

      if (cart.length === 0) {
        throw new Error('Il carrello e vuoto');
      }

      const unavailable = new Set<number>();
      for (const film of cart) {
        const availability = await sendCommand(`CHECK_AVAILABILITY ${film.idFilm}`);
        if (availability !== 'AVAILABLE') {
          unavailable.add(film.idFilm);
        }
      }

      if (unavailable.size > 0) {
        const unavailableTitles = cart
          .filter((f) => unavailable.has(f.idFilm))
          .map((f) => f.titolo)
          .join(', ');

        const remainingCart = cart.filter((f) => !unavailable.has(f.idFilm));
        setCart(remainingCart);
        throw new Error(`Film non disponibili rimossi dal carrello: ${unavailableTitles}`);
      }

      const payload = cart.map((film) => `${session.userId},${film.idFilm},1;`).join('');
      const response = await sendCommand(`CHECKOUT ${payload}`);

      if (!response.startsWith('SUCCESS')) {
        throw new Error(response || 'Checkout fallito');
      }

      setCart([]);
      setStatus('Checkout completato');
      await Promise.all([loadFilms(), loadDashboard()]);
    });

  const sendAdminNotification = () =>
    withBusy(async () => {
      if (selectedUserId == null) {
        throw new Error('Nessun utente selezionato');
      }
      if (!notificationMessage.trim()) {
        throw new Error('Messaggio vuoto');
      }

      const response = await sendCommand(
        `SEND_NOTIFICATION ${selectedUserId} ${notificationMessage.trim()}`
      );

      if (!response.toLowerCase().includes('notifica') && !response.toLowerCase().includes('inviata')) {
        setStatus(`Risposta server: ${response}`);
      } else {
        setStatus('Messaggio inviato');
      }
    });

  const deleteNotification = (id: number) =>
    withBusy(async () => {
      await sendCommand(`DELETE_NOTIFICATION ${id}`);
      await loadNotifications();
    });

  const setAdminMaxRentals = () =>
    withBusy(async () => {
      if (!maxRentals.trim()) {
        throw new Error('Inserisci il valore max noleggi');
      }
      await sendCommand(`SET_MAX_RENTALS ${Number(maxRentals)}`);
      setStatus('Limite noleggi aggiornato');
      setMaxRentals('');
      await loadAdminLimits();
    });

  const markRentalReturned = (line: string) =>
    withBusy(async () => {
      const rentalId = line.split(' - ')[0];
      if (!rentalId) {
        throw new Error('ID noleggio non valido');
      }
      await sendCommand(`RETURN_RENTAL ${rentalId}`);
      await loadDashboard();
    });

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.content}>
        {session ? (
          <>
            <View style={styles.topBar}>
              <Text style={styles.title}>CiNEBOX</Text>
              <Text style={styles.muted}>
                {session.username} ({isAdmin ? 'admin' : `id ${session.userId}`})
              </Text>
              <Pressable style={styles.secondaryButton} onPress={logout}>
                <Text style={styles.secondaryButtonText}>Logout</Text>
              </Pressable>
            </View>

            <View style={styles.tabs}>
              {([
                ['dashboard', 'Dashboard'],
                ['catalogo', 'Catalogo'],
                ['carrello', 'Carrello'],
                ['notifiche', 'Notifiche'],
              ] as [TabKey, string][]).map(([key, label]) => (
                <Pressable
                  key={key}
                  style={[styles.tabBtn, activeTab === key && styles.tabBtnActive]}
                  onPress={() => {
                    setActiveTab(key);
                    void withBusy(async () => {
                      await refreshTab(key);
                    });
                  }}
                >
                  <Text style={activeTab === key ? styles.tabTextActive : styles.tabText}>{label}</Text>
                </Pressable>
              ))}
            </View>

            {activeTab === 'dashboard' && (
              <DashboardSection
                isAdmin={isAdmin}
                allRentalsOverview={allRentalsOverview}
                last5Rentals={last5Rentals}
                top5Films={top5Films}
                activeRentals={activeRentals}
                onMarkReturned={markRentalReturned}
              />
            )}
            {activeTab === 'catalogo' && (
              <CatalogSection
                filteredFilms={filteredFilms}
                search={search}
                setSearch={setSearch}
                genres={genres}
                genreFilter={genreFilter}
                setGenreFilter={setGenreFilter}
                sortMode={sortMode}
                setSortMode={setSortMode}
                cart={cart}
                isAdmin={isAdmin}
                onToggleCartFilm={toggleCartFilm}
              />
            )}
            {activeTab === 'carrello' && (
              <CartSection
                isAdmin={isAdmin}
                currentMaxRentalsHint={currentMaxRentalsHint}
                maxRentals={maxRentals}
                setMaxRentals={setMaxRentals}
                onSetAdminMaxRentals={setAdminMaxRentals}
                cart={cart}
                cartTotal={cartTotal}
                onToggleCartFilm={toggleCartFilm}
                onCheckout={checkout}
                onClearCart={() => setCart([])}
              />
            )}
            {activeTab === 'notifiche' && (
              <NotificationsSection
                isAdmin={isAdmin}
                users={users}
                selectedUserId={selectedUserId}
                setSelectedUserId={setSelectedUserId}
                notificationMessage={notificationMessage}
                setNotificationMessage={setNotificationMessage}
                onSendAdminNotification={sendAdminNotification}
                notifications={notifications}
                onDeleteNotification={deleteNotification}
              />
            )}
          </>
        ) : (
          <AuthSection
            authMode={authMode}
            username={username}
            password={password}
            confirmPassword={confirmPassword}
            setUsername={setUsername}
            setPassword={setPassword}
            setConfirmPassword={setConfirmPassword}
            onSubmit={authMode === 'login' ? login : register}
            onToggleMode={() => setAuthMode((prev) => (prev === 'login' ? 'register' : 'login'))}
          />
        )}

        {loading && <Text style={styles.loading}>Caricamento in corso...</Text>}
        {status ? <Text style={styles.status}>{status}</Text> : null}
      </ScrollView>
    </SafeAreaView>
  );
}
