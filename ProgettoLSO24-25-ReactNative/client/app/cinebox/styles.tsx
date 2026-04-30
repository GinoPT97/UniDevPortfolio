import { StyleSheet } from 'react-native';

export const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f4f6fb',
  },
  content: {
    padding: 16,
    gap: 12,
  },
  topBar: {
    gap: 8,
    marginBottom: 6,
  },
  title: {
    fontSize: 28,
    fontWeight: '800',
    color: '#0e2f5a',
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '700',
    color: '#19355d',
    marginBottom: 8,
  },
  muted: {
    color: '#5d6c85',
  },
  small: {
    color: '#465269',
    fontSize: 12,
  },
  card: {
    backgroundColor: '#ffffff',
    borderRadius: 12,
    padding: 14,
    borderWidth: 1,
    borderColor: '#d8dfec',
    gap: 8,
  },
  input: {
    borderWidth: 1,
    borderColor: '#bac6dc',
    backgroundColor: '#fff',
    borderRadius: 10,
    paddingHorizontal: 12,
    paddingVertical: 10,
  },
  multiInput: {
    minHeight: 96,
    textAlignVertical: 'top',
  },
  row: {
    flexDirection: 'row',
    gap: 8,
    alignItems: 'center',
  },
  chipRow: {
    flexDirection: 'row',
    gap: 8,
    paddingVertical: 4,
  },
  chip: {
    borderWidth: 1,
    borderColor: '#a8b8d6',
    borderRadius: 20,
    paddingHorizontal: 12,
    paddingVertical: 6,
    backgroundColor: '#fff',
  },
  chipActive: {
    backgroundColor: '#174ea8',
    borderColor: '#174ea8',
  },
  chipText: {
    color: '#174ea8',
  },
  chipTextActive: {
    color: '#fff',
  },
  tabs: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
    marginBottom: 6,
  },
  tabBtn: {
    borderWidth: 1,
    borderColor: '#adc0df',
    borderRadius: 999,
    paddingHorizontal: 12,
    paddingVertical: 8,
    backgroundColor: '#fff',
  },
  tabBtnActive: {
    backgroundColor: '#0f4cb5',
    borderColor: '#0f4cb5',
  },
  tabText: {
    color: '#0f4cb5',
    fontWeight: '600',
  },
  tabTextActive: {
    color: '#fff',
    fontWeight: '600',
  },
  primaryButton: {
    backgroundColor: '#0f4cb5',
    borderRadius: 10,
    paddingHorizontal: 14,
    paddingVertical: 10,
    alignItems: 'center',
  },
  removeButton: {
    backgroundColor: '#8a2330',
  },
  primaryButtonText: {
    color: '#fff',
    fontWeight: '700',
  },
  secondaryButton: {
    backgroundColor: '#e8edf7',
    borderRadius: 10,
    paddingHorizontal: 14,
    paddingVertical: 10,
    alignItems: 'center',
  },
  secondaryButtonText: {
    color: '#234b86',
    fontWeight: '700',
  },
  flexGrow: {
    flexGrow: 1,
  },
  filmTitle: {
    fontSize: 17,
    fontWeight: '700',
    color: '#1d3557',
  },
  listItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    gap: 10,
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#edf1f7',
  },
  listItemColumn: {
    gap: 4,
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#edf1f7',
  },
  listText: {
    color: '#1f2b3e',
    flexShrink: 1,
  },
  link: {
    color: '#0f4cb5',
    fontWeight: '700',
  },
  badge: {
    color: '#fff',
    backgroundColor: '#335ea8',
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 14,
    overflow: 'hidden',
  },
  loading: {
    textAlign: 'center',
    color: '#0f4cb5',
    fontWeight: '700',
  },
  status: {
    textAlign: 'center',
    color: '#1f2b3e',
  },
});
