# cameriere_rimborsi.py

# Dati per i mesi estivi 2026
# Struttura: location con tarifa fissa e numero di turni/servizi per mese
MESI = {
    "MAGGIO": {
        "VP": {"tarifa": 120, "turni": 1},
        "MudJ": {"tarifa": 40, "turni": 6},
        "CaricoScarico": {"tarifa": 0, "turni": 0},
        "pagamenti": [],
        "mance": []
    },
    "GIUGNO": {
        "VP": {"tarifa": 120, "turni": 0},
        "MudJ": {"tarifa": 40, "turni": 0},
        "CaricoScarico": {"tarifa": 0, "turni": 0},
        "pagamenti": [],
        "mance": []
    },
    "LUGLIO": {
        "VP": {"tarifa": 120, "turni": 0},
        "MudJ": {"tarifa": 40, "turni": 0},
        "CaricoScarico": {"tarifa": 0, "turni": 0},
        "pagamenti": [],
        "mance": []
    },
    "AGOSTO": {
        "VP": {"tarifa": 120, "turni": 0},
        "MudJ": {"tarifa": 40, "turni": 0},
        "CaricoScarico": {"tarifa": 0, "turni": 0},
        "pagamenti": [],
        "mance": []
    },
}

def calcola_mese(mese: str) -> tuple:
    """
    Calcola i compensi per un mese.
    :param mese: nome del mese
    :return: tuple (compensi_totali, pagato_totale, mance_totali, da_ricevere)
    """
    dati = MESI[mese]
    pagamenti = dati["pagamenti"]
    mance = dati["mance"]
    
    # Calcola compensi per ogni location e attività
    compensi_totali = 0
    dettagli = []
    
    for location, info in dati.items():
        if location not in ["pagamenti", "mance"]:
            compenso = info.get("tarifa", 0) * info.get("turni", 0)
            compensi_totali += compenso
            if info.get("turni", 0) > 0:
                dettagli.append(f"{location}: {info['tarifa']}€ x {info['turni']} = {compenso}€")

    somma_pagato = sum(p.get("importo", 0) for p in pagamenti) if pagamenti else 0
    
    # Estrai importo da stringhe come "20 (VP)"
    mance_parsate = []
    for m in mance:
        if isinstance(m, str):
            try:
                importo = float(m.split()[0])
                fonte = m[m.find("(")+1:m.find(")")] if "(" in m else "Mancia"
                mance_parsate.append({"importo": importo, "fonte": fonte})
            except:
                pass
        else:
            mance_parsate.append(m)
    
    somma_mance = sum(m.get("importo", 0) for m in mance_parsate) if mance_parsate else 0
    differenza = compensi_totali - somma_pagato - somma_mance
    
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
    
    if mance_parsate:
        for mancia in mance_parsate:
            print(f"  Mancia ({mancia['fonte']}): €{mancia['importo']}")
    else:
        print(f"  Nessuna mancia ricevuta")
    
    print(f"Totale mance: €{somma_mance}")
    print(f"Da ricevere: €{differenza}")
    
    return compensi_totali, somma_pagato, somma_mance, differenza


if __name__ == "__main__":
    print("=" * 60)
    print("CALCOLO COMPENSI CAMERIERE - ESTATE 2026")
    print("=" * 60)
    
    totale_compensi = totale_pagato = totale_mance = totale_da_ricevere = 0
    
    for mese in MESI.keys():
        compensi, pagato, mance, da_ricevere = calcola_mese(mese)
        totale_compensi += compensi
        totale_pagato += pagato
        totale_mance += mance
        totale_da_ricevere += da_ricevere
    
    print("\n" + "=" * 60)
    print(" RIEPILOGO GENERALE - ESTATE 2026")
    print("=" * 60)
    print(f"Totale compensi maturati: €{totale_compensi}")
    print(f"Totale pagato: €{totale_pagato}")
    print(f"Totale mance ricevute: €{totale_mance}")
    print(f"Totale da ricevere: €{totale_da_ricevere}")
    print("=" * 60)
