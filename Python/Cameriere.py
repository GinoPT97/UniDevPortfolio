# cameriere_rimborsi.py

# Dati per i mesi estivi 2026
# Struttura: location con tarifa fissa e numero di turni/servizi per mese
MESI = {
    "MAGGIO": {
        "VP": {"tarifa": 120, "turni": 1},
        "MudJ": {"tarifa": 40, "turni": 6},
        "pagamenti": []
    },
    "GIUGNO": {
        "VP": {"tarifa": 120, "turni": 0},
        "MudJ": {"tarifa": 40, "turni": 0},
        "pagamenti": []
    },
    "LUGLIO": {
        "VP": {"tarifa": 120, "turni": 0},
        "MudJ": {"tarifa": 40, "turni": 0},
        "pagamenti": []
    },
    "AGOSTO": {
        "VP": {"tarifa": 120, "turni": 0},
        "MudJ": {"tarifa": 40, "turni": 0},
        "pagamenti": []
    },
}

def calcola_mese(mese: str) -> tuple:
    """
    Calcola i compensi per un mese.
    :param mese: nome del mese
    :return: tuple (compensi_totali, pagato_totale, da_ricevere)
    """
    dati = MESI[mese]
    pagamenti = dati["pagamenti"]
    
    # Calcola compensi per ogni location
    compensi_totali = 0
    dettagli = []
    
    for location, info in dati.items():
        if location != "pagamenti":
            compenso = info["tarifa"] * info["turni"]
            compensi_totali += compenso
            if info["turni"] > 0:
                dettagli.append(f"{location}: {info['tarifa']}€ x {info['turni']} = {compenso}€")
    
    somma_pagato = sum(p.get("importo", 0) for p in pagamenti) if pagamenti else 0
    differenza = compensi_totali - somma_pagato
    
    print(f"\n {mese} 2026")
    print("-" * 60)
    for dettaglio in dettagli:
        print(f"  {dettaglio}")
    print(f"Compensi totali: €{compensi_totali}")
    
    if pagamenti:
        for pagamento in pagamenti:
            print(f"  Pagato ({pagamento.get('desc', 'Pagamento')}): €{pagamento['importo']}")
    else:
        print(f"  Nessun pagamento ricevuto")
    
    print(f"Totale pagato: €{somma_pagato}")
    print(f"Da ricevere: €{differenza}")
    
    return compensi_totali, somma_pagato, differenza


if __name__ == "__main__":
    print("=" * 60)
    print("CALCOLO COMPENSI CAMERIERE - ESTATE 2026")
    print("=" * 60)
    
    totale_compensi = totale_pagato = totale_da_ricevere = 0
    
    for mese in MESI.keys():
        compensi, pagato, da_ricevere = calcola_mese(mese)
        totale_compensi += compensi
        totale_pagato += pagato
        totale_da_ricevere += da_ricevere
    
    print("\n" + "=" * 60)
    print(" RIEPILOGO GENERALE - ESTATE 2026")
    print("=" * 60)
    print(f"Totale compensi maturati: €{totale_compensi}")
    print(f"Totale pagato: €{totale_pagato}")
    print(f"Totale da ricevere: €{totale_da_ricevere}")
    print("=" * 60)
