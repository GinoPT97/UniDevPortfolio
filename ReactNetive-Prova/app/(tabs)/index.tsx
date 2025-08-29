import { Image } from 'expo-image';

import { HelloWave } from '@/components/HelloWave';
import ParallaxScrollView from '@/components/ParallaxScrollView';
import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';

export default function HomeScreen() {
  return (
    <ParallaxScrollView
      headerBackgroundColor={{ light: '#E3F6FF', dark: '#A1CEDC' }}
      headerImage={
        <Image
          source={require('@/assets/images/react-logo.png')}
          style={{ width: 120, height: 120, alignSelf: 'center', marginTop: 30 }}
        />
      }>
      <ThemedView style={{ alignItems: 'center', marginTop: 20 }}>
        <ThemedText type="title" style={{ color: '#185a9d' }}>UniDev Portfolio</ThemedText>
        <HelloWave />
        <ThemedText style={{ marginTop: 10, color: '#3a3a3a' }}>
          Benvenuto! Qui puoi esplorare le funzionalità principali dell&apos;app.
        </ThemedText>
      </ThemedView>
      <ThemedView style={{ marginTop: 30, padding: 16, borderRadius: 16, backgroundColor: '#B6E0FE', shadowColor: '#185a9d', shadowOpacity: 0.1, shadowRadius: 6 }}>
        <ThemedText type="subtitle" style={{ color: '#185a9d' }}>Calcolatrice</ThemedText>
        <ThemedText style={{ color: '#185a9d' }}>
          Prova la calcolatrice per eseguire operazioni matematiche di base.
        </ThemedText>
      </ThemedView>
      <ThemedView style={{ marginTop: 30, padding: 16, borderRadius: 16, backgroundColor: '#FFD6E0', shadowColor: '#d76d77', shadowOpacity: 0.1, shadowRadius: 6 }}>
        <ThemedText type="subtitle" style={{ color: '#d76d77' }}>Personalizza il tuo portfolio</ThemedText>
        <ThemedText style={{ color: '#d76d77' }}>
          Modifica i file nella cartella <ThemedText type="defaultSemiBold" style={{ color: '#d76d77' }}>app/</ThemedText> per aggiungere nuove funzionalità.
        </ThemedText>
      </ThemedView>
    </ParallaxScrollView>
  );
}

