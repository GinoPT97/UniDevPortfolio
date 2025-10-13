# somma_rimborsi_stagioni.py

def somma_lista(numeri: list) -> float:
    """
    Calcola la somma di una lista di numeri.
    :param numeri: lista di numeri (int o float)
    :return: somma dei numeri
    """
    return sum(numeri)

if __name__ == "__main__":
    print("=" * 60)
    print("CALCOLO RIMBORSI PER STAGIONE")
    print("=" * 60)
    
    # ==================== STAGIONE 2024/2025 ====================
    print("\n📅 STAGIONE 2024/2025")
    print("-" * 60)
    
    Rimborso_2024_25 = [37, 57, 79, 63, 61, 57, 61, 70, 63, 61, 79, 63, 63, 63, 63, 61, 63, 70, 63, 79, 
                        61, 63, 57, 61, 63, 70, 70, 57, 61, 61, 63, 61, 63, 37, 61, 89, 63, 63, 61, 57, 
                        63, 57, 61, 57, 61, 63, 70, 63, 70, 63, 70, 89, 63, 63, 63, 57, 61, 63]
    
    Pagato_2024_25 = [573, 173, 118, 63, 131, 205, 124, 372, 142, 57, 127, 61, 63, 98, 89, 183, 114, 63,
                      194, 203, 152, 120]
    
    somma_rimborso_24_25 = somma_lista(Rimborso_2024_25)
    somma_pagato_24_25 = somma_lista(Pagato_2024_25)
    differenza_24_25 = somma_rimborso_24_25 - somma_pagato_24_25
    
    print(f"Rimborsi totali: {len(Rimborso_2024_25)} gare = €{somma_rimborso_24_25}")
    print(f"Pagato: {len(Pagato_2024_25)} pagamenti = €{somma_pagato_24_25}")
    print(f"Da ricevere: €{differenza_24_25}")
    
    # ==================== STAGIONE 2025/2026 ====================
    print("\n📅 STAGIONE 2025/2026")
    print("-" * 60)
    
    Rimborso_2025_26 = [70, 70, 63, 63, 63, 61, 70, 79, 63]
    
    Pagato_2025_26 = [63]
    
    somma_rimborso_25_26 = somma_lista(Rimborso_2025_26)
    somma_pagato_25_26 = somma_lista(Pagato_2025_26)
    differenza_25_26 = somma_rimborso_25_26 - somma_pagato_25_26
    
    print(f"Rimborsi totali: {len(Rimborso_2025_26)} gare = €{somma_rimborso_25_26}")
    print(f"Pagato: {len(Pagato_2025_26)} pagamenti = €{somma_pagato_25_26}")
    print(f"Da ricevere: €{differenza_25_26}")
    
    # ==================== TOTALE GENERALE ====================
    print("\n" + "=" * 60)
    print("💰 RIEPILOGO GENERALE")
    print("=" * 60)
    
    totale_rimborsi = somma_rimborso_24_25 + somma_rimborso_25_26
    totale_pagato = somma_pagato_24_25 + somma_pagato_25_26
    totale_da_ricevere = differenza_24_25 + differenza_25_26
    
    print(f"Totale rimborsi maturati: €{totale_rimborsi}")
    print(f"Totale pagato: €{totale_pagato}")
    print(f"Totale da ricevere: €{totale_da_ricevere}")
    print("=" * 60)