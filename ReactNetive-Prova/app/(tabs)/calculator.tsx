import React from 'react';
import { View } from 'react-native';
import Calculator from '../../components/Calculator';

export default function CalculatorScreen() {
  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Calculator />
    </View>
  );
}
