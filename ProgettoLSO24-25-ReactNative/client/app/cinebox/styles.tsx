import { StyleSheet } from 'react-native';

export const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#eef3f9',
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
    color: '#17375f',
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '700',
    color: '#1f426f',
    marginBottom: 8,
  },
  muted: {
    color: '#5a6f8e',
  },
  small: {
    color: '#425976',
    fontSize: 12,
  },
  card: {
    backgroundColor: '#ffffff',
    borderRadius: 14,
    padding: 14,
    borderWidth: 1,
    borderColor: '#cfd9e8',
    gap: 8,
  },
  input: {
    borderWidth: 1,
    borderColor: '#b8c8e0',
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
    borderColor: '#8da4cb',
    borderRadius: 20,
    paddingHorizontal: 12,
    paddingVertical: 6,
    backgroundColor: '#f7faff',
  },
  chipActive: {
    backgroundColor: '#1f5fb8',
    borderColor: '#1f5fb8',
  },
  chipText: {
    color: '#1d4f9c',
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
    borderColor: '#9eb6d8',
    borderRadius: 999,
    paddingHorizontal: 12,
    paddingVertical: 8,
    backgroundColor: '#f7faff',
  },
  tabBtnActive: {
    backgroundColor: '#1c5ab3',
    borderColor: '#1c5ab3',
  },
  tabText: {
    color: '#1c5ab3',
    fontWeight: '600',
  },
  tabTextActive: {
    color: '#fff',
    fontWeight: '600',
  },
  primaryButton: {
    backgroundColor: '#1f5fb8',
    borderRadius: 10,
    paddingHorizontal: 14,
    paddingVertical: 10,
    alignItems: 'center',
  },
  removeButton: {
    backgroundColor: '#a23345',
  },
  primaryButtonText: {
    color: '#fff',
    fontWeight: '700',
  },
  secondaryButton: {
    backgroundColor: '#e6eefb',
    borderRadius: 10,
    paddingHorizontal: 14,
    paddingVertical: 10,
    alignItems: 'center',
  },
  secondaryButtonText: {
    color: '#234f91',
    fontWeight: '700',
  },
  flexGrow: {
    flexGrow: 1,
  },
  filmTitle: {
    fontSize: 20,
    fontWeight: '700',
    color: '#1d3f68',
  },
  filmCard: {
    backgroundColor: '#ffffff',
    borderRadius: 14,
    borderWidth: 1,
    borderColor: '#cfd9e8',
    overflow: 'hidden',
    marginBottom: 2,
  },
  filmPoster: {
    width: '100%',
    height: 220,
    backgroundColor: '#d9e3f2',
  },
  filmPosterFallback: {
    width: '100%',
    height: 180,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#d9e3f2',
    paddingHorizontal: 12,
  },
  filmPosterFallbackText: {
    color: '#37567f',
    fontWeight: '600',
    textAlign: 'center',
  },
  filmContent: {
    paddingHorizontal: 14,
    paddingVertical: 12,
    gap: 6,
  },
  filmMetaGrid: {
    marginTop: 4,
    gap: 3,
  },
  filmMetaItem: {
    color: '#2f4768',
    fontSize: 13,
  },
  filmStateRow: {
    flexDirection: 'row',
    marginTop: 6,
  },
  listItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    gap: 10,
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#e5ecf7',
  },
  listItemColumn: {
    gap: 4,
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#e5ecf7',
  },
  listText: {
    color: '#253752',
    flexShrink: 1,
  },
  link: {
    color: '#1f5fb8',
    fontWeight: '700',
  },
  badge: {
    color: '#fff',
    backgroundColor: '#2f67b9',
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 14,
    overflow: 'hidden',
  },
  loading: {
    textAlign: 'center',
    color: '#1f5fb8',
    fontWeight: '700',
  },
  status: {
    textAlign: 'center',
    color: '#253752',
  },
});
