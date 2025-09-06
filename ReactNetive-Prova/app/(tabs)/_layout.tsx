
import { Tabs } from 'expo-router';
import React from 'react';
import { Platform } from 'react-native';

import { HapticTab } from '@/components/HapticTab';
import TabBarBackground from '@/components/ui/TabBarBackground';
import { Colors } from '@/constants/Colors';
import { useColorScheme } from '@/hooks/useColorScheme';

import { Ionicons } from '@expo/vector-icons';
import { Image } from 'expo-image';

const HomeIcon = ({ color }: { color: string }) => (
  <Image
    source={require('@/assets/images/react-logo.png')}
    style={{ width: 28, height: 28 }}
    contentFit="contain"
  />
);

const CalculatorIcon = ({ color }: { color: string }) => (
  <Ionicons name="calculator-outline" size={28} color={color} />
);

export default function TabLayout() {
  const colorScheme = useColorScheme();

  return (
    <Tabs
      screenOptions={{
        tabBarActiveTintColor: Colors[colorScheme ?? 'light'].tint,
        headerShown: false,
        tabBarButton: HapticTab,
        tabBarBackground: TabBarBackground,
        tabBarPosition: 'top',
        tabBarStyle: Platform.select({
          ios: {
            position: 'absolute',
          },
          default: {},
        }),
      }}
    >
      <Tabs.Screen
        name="index"
        options={{
          title: 'Home',
          tabBarIcon: HomeIcon,
        }}
      />
      <Tabs.Screen
        name="calculator"
        options={{
          title: 'Calculator',
          tabBarIcon: CalculatorIcon,
        }}
      />
    </Tabs>
  );
}
