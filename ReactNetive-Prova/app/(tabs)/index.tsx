import { HelloWave } from '@/components/HelloWave';
import ParallaxScrollView from '@/components/ParallaxScrollView';
import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import { Ionicons } from '@expo/vector-icons';
import { Image } from 'expo-image';
import { useRouter } from 'expo-router';
import { TouchableOpacity } from 'react-native';

export default function HomeScreen() {
  const router = useRouter();
  return (
    <ParallaxScrollView
      headerBackgroundColor={{ light: '#F8FAFC', dark: '#A1CEDC' }}
      headerImage={
        <Image
          source={require('@/assets/images/react-logo.png')}
          style={{ width: 120, height: 120, alignSelf: 'center', marginTop: 30 }}
        />
      }>
      <ThemedView style={{ alignItems: 'center', marginTop: 20 }}>
        <ThemedText type="title" style={{ color: '#6C63FF', fontSize: 32 }}>UniDev Portfolio</ThemedText>
        <HelloWave />
        <ThemedText style={{ marginTop: 10, color: '#444', fontSize: 16 }}>
          Benvenuto! Qui puoi esplorare le funzionalità principali dell&apos;app.
        </ThemedText>
        <ThemedText style={{ marginTop: 16, color: '#888', fontStyle: 'italic', fontSize: 15 }}>
          Un progetto open-source per mostrare competenze, idee e strumenti utili per sviluppatori.
        </ThemedText>
      </ThemedView>

      {/* Sezione Bio */}
      <ThemedView style={{ marginTop: 30, padding: 16, borderRadius: 16, backgroundColor: '#F3F3F3', alignItems: 'center', flexDirection: 'row', gap: 12 }}>
        <Ionicons name="person-circle-outline" size={40} color="#6C63FF" style={{ marginRight: 10 }} />
        <ThemedText style={{ color: '#444', fontSize: 15 }}>
          <ThemedText type="defaultSemiBold">Kenobi1797</ThemedText> — Studente e appassionato di sviluppo mobile e web. Sempre alla ricerca di nuove tecnologie!
        </ThemedText>
      </ThemedView>

      {/* Card Calcolatrice con pulsante */}
      <ThemedView style={{ marginTop: 30, padding: 16, borderRadius: 16, backgroundColor: '#B6E0FE', shadowColor: '#185a9d', shadowOpacity: 0.1, shadowRadius: 6 }}>
        <ThemedText type="subtitle" style={{ color: '#185a9d', fontSize: 18 }}>
          <Ionicons name="calculator-outline" size={20} color="#185a9d" /> Calcolatrice
        </ThemedText>
        <ThemedText style={{ color: '#185a9d', marginBottom: 10 }}>
          Prova la calcolatrice per eseguire operazioni matematiche di base.
        </ThemedText>
        <TouchableOpacity
          style={{ backgroundColor: '#185a9d', paddingVertical: 8, paddingHorizontal: 18, borderRadius: 8, alignSelf: 'flex-start', flexDirection: 'row', alignItems: 'center', gap: 8 }}
          onPress={() => {
            router.push('/calculator');
          }}
        >
          <Ionicons name="calculator-outline" size={20} color="#fff" style={{ marginRight: 6 }} />
          <ThemedText style={{ color: '#fff', fontWeight: 'bold' }}>Vai alla Calcolatrice</ThemedText>
        </TouchableOpacity>
      </ThemedView>

    </ParallaxScrollView>
  );
}

