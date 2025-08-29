import React from 'react';
import { View } from 'react-native';
import { Image } from 'expo-image';
import { ThemedText } from '@/components/ThemedText';
import Calculator from '../../components/Calculator';

export default function CalculatorScreen() {
  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: '#f5f6fa' }}>
      <View style={{ alignItems: 'center', marginBottom: 18 }}>
        <View style={{ backgroundColor: '#0984e3', borderRadius: 50, padding: 12, marginBottom: 8 }}>
          <Image
            source={require('@/assets/images/icon.png')}
            style={{ width: 48, height: 48 }}
          />
        </View>
        <ThemedText type="title">Calcolatrice</ThemedText>
        <ThemedText style={{ marginTop: 6, color: '#636e72' }}>
          Esegui operazioni matematiche di base in modo semplice e veloce.
        </ThemedText>
      </View>
      <Calculator />
    </View>
  );
}
