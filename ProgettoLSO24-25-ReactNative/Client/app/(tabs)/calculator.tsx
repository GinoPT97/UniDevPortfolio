import { ThemedText } from '@/components/ThemedText';
import { Ionicons } from '@expo/vector-icons';
import React from 'react';
import { View } from 'react-native';
import Calculator from '../../components/Calculator';

export default function CalculatorScreen() {
  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: '#f5f6fa' }}>
      <View style={{ alignItems: 'center', marginBottom: 24, padding: 18, borderRadius: 24, backgroundColor: '#eaf6ff', shadowColor: '#0984e3', shadowOpacity: 0.13, shadowRadius: 8, elevation: 3 }}>
        <View style={{ backgroundColor: '#0984e3', borderRadius: 50, padding: 16, marginBottom: 10, shadowColor: '#0984e3', shadowOpacity: 0.18, shadowRadius: 10, elevation: 4 }}>
          <Ionicons name="calculator-outline" size={54} color="#fff" />
        </View>
        <ThemedText type="title" style={{ fontSize: 28, color: '#0984e3', fontWeight: 'bold' }}>Calcolatrice</ThemedText>
        <ThemedText style={{ marginTop: 8, color: '#636e72', fontSize: 16 }}>
          Esegui operazioni matematiche di base in modo semplice e veloce.
        </ThemedText>
      </View>
      <Calculator />
    </View>
  );
}
