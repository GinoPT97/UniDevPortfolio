# somma_rimborsi_stagioni.py

def somma_lista(numeri: list) -> float:
    """
    Calcola la somma di una lista di numeri.
    :param numeri: lista di numeri (int o float)
    :return: somma dei numeri
    """
    return sum(numeri)

def calcola_stagione_2024_25():
    """
    Calcola i rimborsi per la stagione 2024/2025.
    :return: tuple (rimborsi_totali, pagato_totale, da_ricevere)
    """
    Rimborso = [37, 57, 79, 63, 61, 57, 61, 70, 63, 61, 79, 63, 63, 63, 63, 61, 63, 70, 63, 79, 
                61, 63, 57, 61, 63, 70, 70, 57, 61, 61, 63, 61, 63, 37, 61, 89, 63, 63, 61, 57, 
                63, 57, 61, 57, 61, 63, 70, 63, 70, 63, 70, 89, 63, 63, 63, 57, 61, 63]
    
    Pagato = [573, 173, 118, 63, 131, 205, 124, 372, 142, 57, 127, 61, 63, 98, 89, 183, 114, 63,
              194, 203, 152, 120, 250]
    
    somma_rimborso = somma_lista(Rimborso)
    somma_pagato = somma_lista(Pagato)
    differenza = somma_rimborso - somma_pagato
    
    print("\n STAGIONE 2024/2025")
    print("-" * 60)
    print(f"Rimborsi totali: {len(Rimborso)} gare = €{somma_rimborso}")
    print(f"Pagato: {len(Pagato)} pagamenti = €{somma_pagato}")
    print(f"Da ricevere: €{differenza}")
    
    return somma_rimborso, somma_pagato, differenza


def calcola_stagione_2025_26():
    """
    Calcola i rimborsi per la stagione 2025/2026.
    :return: tuple (rimborsi_totali, pagato_totale, da_ricevere)
    """
    Rimborso = [63, 79, 70, 61, 63, 63, 63, 70, 70, 61, 70, 79, 63, 70, 63, 63, 70, 61, 61, 70]
    
    Pagato = [63, 140, 259, 350, 133]
    
    somma_rimborso = somma_lista(Rimborso)
    somma_pagato = somma_lista(Pagato)
    differenza = somma_rimborso - somma_pagato
    
    print("\n STAGIONE 2025/2026")
    print("-" * 60)
    print(f"Rimborsi totali: {len(Rimborso)} gare = €{somma_rimborso}")
    print(f"Pagato: {len(Pagato)} pagamenti = €{somma_pagato}")
    print(f"Da ricevere: €{differenza}")
    
    return somma_rimborso, somma_pagato, differenza


def stampa_riepilogo(totale_rimborsi, totale_pagato, totale_da_ricevere):
    """
    Stampa il riepilogo generale di tutte le stagioni.
    """
    print("\n" + "=" * 60)
    print(" RIEPILOGO GENERALE")
    print("=" * 60)
    print(f"Totale rimborsi maturati: €{totale_rimborsi}")
    print(f"Totale pagato: €{totale_pagato}")
    print(f"Totale da ricevere: €{totale_da_ricevere}")
    print("=" * 60)


if __name__ == "__main__":
    print("=" * 60)
    print("CALCOLO RIMBORSI PER STAGIONE")
    print("=" * 60)
    
    # Calcola stagione 2024/2025
    rimborsi_24_25, pagato_24_25, da_ricevere_24_25 = calcola_stagione_2024_25()
    
    # Calcola stagione 2025/2026
    rimborsi_25_26, pagato_25_26, da_ricevere_25_26 = calcola_stagione_2025_26()
    
    # Calcola totali
    totale_rimborsi = rimborsi_24_25 + rimborsi_25_26
    totale_pagato = pagato_24_25 + pagato_25_26
    totale_da_ricevere = da_ricevere_24_25 + da_ricevere_25_26
    
    # Stampa riepilogo generale
    stampa_riepilogo(totale_rimborsi, totale_pagato, totale_da_ricevere)