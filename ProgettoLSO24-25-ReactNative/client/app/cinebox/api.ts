import { Platform } from 'react-native';

export const BRIDGE_URL =
  process.env.EXPO_PUBLIC_BRIDGE_URL ||
  (Platform.OS === 'android' ? 'http://10.0.2.2:8090' : 'http://localhost:8090');

export async function sendCommand(command: string, expectEnd = false): Promise<string> {
  const res = await fetch(`${BRIDGE_URL}/command`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ command, expectEnd }),
  });

  const data = await res.json();
  if (!res.ok) {
    throw new Error(data?.error || `HTTP ${res.status}`);
  }
  return (data?.response || '').toString();
}
