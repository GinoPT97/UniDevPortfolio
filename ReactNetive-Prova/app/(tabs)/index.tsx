import { Image } from 'expo-image';

import { HelloWave } from '@/components/HelloWave';
import ParallaxScrollView from '@/components/ParallaxScrollView';
import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';

export default function HomeScreen() {
  return (
    <ParallaxScrollView
      headerBackgroundColor={{ light: '#A1CEDC', dark: '#1D3D47' }}
      headerImage={
        <Image
          source={require('@/assets/images/react-logo.png')}
          style={{ width: 120, height: 120, alignSelf: 'center', marginTop: 30 }}
        />
      }>
      <ThemedView style={{ alignItems: 'center', marginTop: 20 }}>
        <ThemedText type="title">UniDev Portfolio</ThemedText>
        <HelloWave />
        <ThemedText style={{ marginTop: 10 }}>
          Benvenuto! Qui puoi esplorare le funzionalità principali dell&apos;app.
        </ThemedText>
      </ThemedView>
      <ThemedView style={{ marginTop: 30, padding: 16, borderRadius: 12, backgroundColor: '#e0eafc' }}>
        <ThemedText type="subtitle">Calcolatrice</ThemedText>
        </ThemedView>
        <ThemedView style={{ marginTop: 30, padding: 16, borderRadius: 12, backgroundColor: '#e0eafc' }}>
          <ThemedText type="subtitle">Calcolatrice</ThemedText>
          <ThemedText>
            Prova la calcolatrice per eseguire operazioni matematiche di base.
          </ThemedText>
        </ThemedView>
        <ThemedView style={{ marginTop: 30, padding: 16, borderRadius: 12, backgroundColor: '#e0eafc' }}>
          <ThemedText type="subtitle">Personalizza il tuo portfolio</ThemedText>
          <ThemedText>
            Modifica i file nella cartella <ThemedText type="defaultSemiBold">app/</ThemedText> per aggiungere nuove funzionalità.
          </ThemedText>
        </ThemedView>
      </ParallaxScrollView>
    );
}

