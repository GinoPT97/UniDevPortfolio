import React from 'react';
import { Pressable, ScrollView, Text, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import {
  AuthSection,
  CartSection,
  CatalogSection,
  DashboardSection,
  NotificationsSection,
} from './cinebox/sections';
import { styles } from './cinebox/styles';
import { TabKey } from './cinebox/types';
import { useCineboxController } from './cinebox/use-cinebox-controller';

export default function CineboxScreen() {
  const controller = useCineboxController();

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.content}>
        {controller.session ? (
          <>
            <View style={styles.topBar}>
              <Text style={styles.title}>CiNEBOX</Text>
              <Text style={styles.muted}>
                {controller.session.username} ({controller.isAdmin ? 'admin' : `id ${controller.session.userId}`})
              </Text>
              <Pressable style={styles.secondaryButton} onPress={controller.logout}>
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
                  style={[styles.tabBtn, controller.activeTab === key && styles.tabBtnActive]}
                  onPress={() => {
                    controller.setActiveTab(key);
                    void controller.refreshTab(key);
                  }}
                >
                  <Text style={controller.activeTab === key ? styles.tabTextActive : styles.tabText}>{label}</Text>
                </Pressable>
              ))}
            </View>

            {controller.activeTab === 'dashboard' && (
              <DashboardSection
                isAdmin={controller.isAdmin}
                allRentalsOverview={controller.allRentalsOverview}
                last5Rentals={controller.last5Rentals}
                top5Films={controller.top5Films}
                activeRentals={controller.activeRentals}
                onMarkReturned={controller.markRentalReturned}
              />
            )}
            {controller.activeTab === 'catalogo' && (
              <CatalogSection
                filteredFilms={controller.filteredFilms}
                search={controller.search}
                setSearch={controller.setSearch}
                genres={controller.genres}
                genreFilter={controller.genreFilter}
                setGenreFilter={controller.setGenreFilter}
                sortMode={controller.sortMode}
                setSortMode={controller.setSortMode}
                cart={controller.cart}
                isAdmin={controller.isAdmin}
                onToggleCartFilm={controller.toggleCartFilm}
              />
            )}
            {controller.activeTab === 'carrello' && (
              <CartSection
                isAdmin={controller.isAdmin}
                currentMaxRentalsHint={controller.currentMaxRentalsHint}
                maxRentals={controller.maxRentals}
                setMaxRentals={controller.setMaxRentals}
                onSetAdminMaxRentals={controller.setAdminMaxRentals}
                cart={controller.cart}
                cartTotal={controller.cartTotal}
                onToggleCartFilm={controller.toggleCartFilm}
                onCheckout={controller.checkout}
                onClearCart={controller.clearCart}
              />
            )}
            {controller.activeTab === 'notifiche' && (
              <NotificationsSection
                isAdmin={controller.isAdmin}
                users={controller.users}
                selectedUserId={controller.selectedUserId}
                setSelectedUserId={controller.setSelectedUserId}
                notificationMessage={controller.notificationMessage}
                setNotificationMessage={controller.setNotificationMessage}
                onSendAdminNotification={controller.sendAdminNotification}
                notifications={controller.notifications}
                onDeleteNotification={controller.deleteNotification}
              />
            )}
          </>
        ) : (
          <AuthSection
            authMode={controller.authMode}
            username={controller.username}
            password={controller.password}
            confirmPassword={controller.confirmPassword}
            setUsername={controller.setUsername}
            setPassword={controller.setPassword}
            setConfirmPassword={controller.setConfirmPassword}
            onSubmit={controller.authMode === 'login' ? controller.login : controller.register}
            onToggleMode={() =>
              controller.setAuthMode((prev: 'login' | 'register') =>
                prev === 'login' ? 'register' : 'login'
              )
            }
          />
        )}

        {controller.loading && <Text style={styles.loading}>Caricamento in corso...</Text>}
        {controller.status ? <Text style={styles.status}>{controller.status}</Text> : null}
      </ScrollView>
    </SafeAreaView>
  );
}
