import { HelloWave } from '@/components/HelloWave';
import ParallaxScrollView from '@/components/ParallaxScrollView';
import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import { FontAwesome5, Ionicons, MaterialIcons } from '@expo/vector-icons';
import { Image } from 'expo-image';
import { useRouter } from 'expo-router';
import { Pressable } from 'react-native';

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
  <ThemedView style={{ marginTop: 30, padding: 22, borderRadius: 24, backgroundColor: '#F3F3F3', alignItems: 'center', flexDirection: 'row', shadowColor: '#6C63FF', shadowOpacity: 0.12, shadowRadius: 8, elevation: 2 }}>
        <Ionicons name="person-circle-outline" size={40} color="#6C63FF" style={{ marginRight: 14 }} />
        <ThemedText style={{ color: '#444', fontSize: 18, lineHeight: 26, fontWeight: '500' }}>
          <ThemedText type="defaultSemiBold" style={{ fontSize: 19, color: '#6C63FF' }}>Kenobi1797</ThemedText>
          {'  '}<Ionicons name="code-slash" size={18} color="#6C63FF" />
          {'\n'}Studente e appassionato di sviluppo mobile e web
          <ThemedText style={{ color: '#6C63FF' }}> 🚀</ThemedText>
          {'\n'}Sempre alla ricerca di nuove tecnologie!
        </ThemedText>
      </ThemedView>

      {/* Card Calcolatrice con pulsante */}
      <ThemedView style={{
        marginTop: 30,
        padding: 22,
        borderRadius: 24,
        backgroundColor: '#B6E0FE',
        shadowColor: '#185a9d',
        shadowOpacity: 0.15,
        shadowRadius: 10,
        elevation: 3,
      }}>
        <ThemedText type="subtitle" style={{ color: '#185a9d', fontSize: 18 }}>
          <Ionicons name="calculator-outline" size={20} color="#185a9d" /> Calcolatrice
        </ThemedText>
        <ThemedText style={{ color: '#185a9d', marginBottom: 10 }}>
          Prova la calcolatrice per eseguire operazioni matematiche di base.
        </ThemedText>
        <Pressable
          style={({ pressed }) => [{
            backgroundColor: pressed ? '#274b7a' : '#185a9d',
            paddingVertical: 14,
            paddingHorizontal: 26,
            borderRadius: 14,
            alignSelf: 'flex-start',
            flexDirection: 'row',
            alignItems: 'center',
            shadowColor: '#185a9d',
            shadowOpacity: pressed ? 0.2 : 0.15,
            shadowRadius: pressed ? 12 : 10,
            elevation: pressed ? 4 : 3,
          }]}
          android_ripple={{ color: '#fff' }}
          onPress={() => {
            router.push('/calculator');
          }}
        >
          <Ionicons name="calculator-outline" size={24} color="#fff" style={{ marginRight: 14 }} />
          <ThemedText style={{ color: '#fff', fontWeight: 'bold', fontSize: 18 }}>Vai alla Calcolatrice</ThemedText>
        </Pressable>
      </ThemedView>

      {/* Sezione Skills */}
      <ThemedView style={{
        marginTop: 32,
        padding: 20,
        borderRadius: 24,
        backgroundColor: '#fff',
        shadowColor: '#6C63FF',
        shadowOpacity: 0.10,
        shadowRadius: 8,
        elevation: 2,
        alignItems: 'center',
      }}>
        <ThemedText type="subtitle" style={{ color: '#6C63FF', fontSize: 18, marginBottom: 10 }}>
          <Ionicons name="rocket-outline" size={20} color="#6C63FF" style={{ marginRight: 8 }} /> Skills principali
        </ThemedText>
        <ThemedView style={{ flexDirection: 'row', justifyContent: 'center', alignItems: 'center' }}>
          <FontAwesome5 name="react" size={32} color="#61dafb" style={{ marginHorizontal: 10 }} />
          <FontAwesome5 name="js" size={32} color="#f7df1e" style={{ marginHorizontal: 10 }} />
          <FontAwesome5 name="node" size={32} color="#8cc84b" style={{ marginHorizontal: 10 }} />
          <MaterialIcons name="mobile-friendly" size={32} color="#6C63FF" style={{ marginHorizontal: 10 }} />
        </ThemedView>
        <ThemedText style={{ color: '#444', marginTop: 12, fontSize: 15, textAlign: 'center' }}>
          React Native, JavaScript, Node.js, sviluppo mobile e web.
        </ThemedText>
      </ThemedView>
    </ParallaxScrollView>
  );
}

