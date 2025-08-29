import React, { useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet } from 'react-native';

const Calculator: React.FC = () => {
  const [num1, setNum1] = useState('');
  const [num2, setNum2] = useState('');
  const [result, setResult] = useState<string | null>(null);

  const handleClear = () => {
    setNum1('');
    setNum2('');
    setResult(null);
  };

  const handleOperation = (op: string) => {
    const a = parseFloat(num1);
    const b = parseFloat(num2);
    if (op === '√') {
      if (isNaN(a)) {
        setResult('Inserisci un numero valido');
        return;
      }
      setResult(Math.sqrt(a).toString());
      return;
    }
    if (isNaN(a) || isNaN(b)) {
      setResult('Inserisci numeri validi');
      return;
    }
    let res: number;
    switch (op) {
      case '+': res = a + b; break;
      case '-': res = a - b; break;
      case '*': res = a * b; break;
      case '/':
        if (b === 0) {
          setResult('Divisione per zero!');
          return;
        }
        res = a / b; break;
      case '^':
        res = Math.pow(a, b); break;
      default: return;
    }
    setResult(res.toString());
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Calcolatrice Avanzata</Text>
      <TextInput
        style={styles.input}
        keyboardType="numeric"
        placeholder="Numero 1"
        placeholderTextColor="#636e72"
        value={num1}
        onChangeText={setNum1}
      />
      <TextInput
        style={styles.input}
        keyboardType="numeric"
        placeholder="Numero 2 (opzionale per radice)"
        placeholderTextColor="#636e72"
        value={num2}
        onChangeText={setNum2}
      />
      <View style={styles.buttonGrid}>
        <View style={styles.buttonRow}>
          <View style={styles.button}><Button color="#00b894" title="+" onPress={() => handleOperation('+')} /></View>
          <View style={styles.button}><Button color="#fdcb6e" title="-" onPress={() => handleOperation('-')} /></View>
          <View style={styles.button}><Button color="#0984e3" title="×" onPress={() => handleOperation('*')} /></View>
        </View>
        <View style={styles.buttonRow}>
          <View style={styles.button}><Button color="#d63031" title="÷" onPress={() => handleOperation('/')} /></View>
          <View style={styles.button}><Button color="#6C63FF" title="^" onPress={() => handleOperation('^')} /></View>
          <View style={styles.button}><Button color="#FF6F61" title="√" onPress={() => handleOperation('√')} /></View>
        </View>
        <View style={[styles.buttonRow, { justifyContent: 'center' }] }>
          <View style={[styles.button, { maxWidth: 120 }]}><Button color="#b2bec3" title="Pulisci" onPress={handleClear} /></View>
        </View>
      </View>
      {result !== null && <Text style={styles.result}>Risultato: {result}</Text>}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 20,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#f5f6fa',
    borderRadius: 10,
    margin: 10,
  },
  title: {
    fontSize: 22,
    fontWeight: 'bold',
    marginBottom: 18,
    color: '#2d3436',
  },
  input: {
    width: 120,
    borderWidth: 1,
    borderColor: '#00b894',
    borderRadius: 6,
    padding: 8,
    marginBottom: 10,
    textAlign: 'center',
    fontSize: 16,
    backgroundColor: '#fff',
    color: '#2d3436',
  },
  buttonGrid: {
    marginTop: 10,
    marginBottom: 18,
    width: 240,
    alignSelf: 'center',
  },
  buttonRow: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 10,
    width: '100%',
  },
  button: {
    flex: 1,
    minWidth: 70,
    maxWidth: 80,
    marginHorizontal: 2,
    borderRadius: 6,
    overflow: 'hidden',
  },
  result: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#0984e3',
    backgroundColor: '#dfe6e9',
    padding: 8,
    borderRadius: 6,
    marginTop: 6,
    minWidth: 120,
    textAlign: 'center',
  },
});

export default Calculator;
