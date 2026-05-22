# cameriere_rimborsi.py

# Dati per i mesi estivi 2026
# Struttura: locale con tarifa fissa e numero di turni/servizi per mese
# etichette come costanti (possono essere usate nelle tuple senza virgolette)
VP = "VP"
MudJ = "MudJ"

MESI = {
    "MAGGIO": {
        "VP": {"tarifa": 120, "turni": 1},
        "MudJ": {"tarifa": 40, "turni": 6},
        "aricoScarico": {"tarifa": 0, "turni": 0},
        "pagamenti": [],
        "mance": ["20 (VP)", (15,VP)]
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

RISERVATI = {"pagamenti", "mance"}

def parse_importo(entry):
    """Estrae importo e descrizione da dict, numero o stringa."""
    if isinstance(entry, dict):
        return entry.get("importo", 0), entry.get("desc", "")
    # supporto tuple/lista del tipo (importo, descrizione)
    if isinstance(entry, (list, tuple)) and len(entry) >= 1:
        importo = entry[0] if isinstance(entry[0], (int, float)) else 0
        desc = str(entry[1]) if len(entry) > 1 else ""
        return importo, desc
    if isinstance(entry, (int, float)):
        return entry, ""
    if isinstance(entry, str):
        try:
            valore = float(entry.split()[0])
            desc = entry[entry.find("(")+1:entry.find(")")] if "(" in entry else ""
            return valore, desc
        except ValueError:
            return 0, ""
    return 0, ""

def parse_mance(mance):
    """Normalizza le voci di mancia in lista di dict {importo, fonte}."""
    risultato = []
    for voce in mance:
        # supporto tuple/lista (importo, descrizione) e riusa parse_importo
        if isinstance(voce, (list, tuple)):
            importo, fonte = parse_importo(voce)
            if importo:
                risultato.append({"importo": importo, "fonte": fonte or "Mancia"})
            continue
        if isinstance(voce, str):
            importo, fonte = parse_importo(voce)
            if importo:
                risultato.append({"importo": importo, "fonte": fonte or "Mancia"})
        elif isinstance(voce, dict):
            risultato.append({
                "importo": voce.get("importo", 0),
                "fonte": voce.get("fonte", voce.get("desc", "Mancia"))
            })
        elif isinstance(voce, (int, float)):
            risultato.append({"importo": voce, "fonte": "Mancia"})
    return risultato

def format_euro(valore):
    """Formatta un numero in euro con due decimali."""
    try:
        return f"€{valore:.2f}"
    except (TypeError, ValueError):
        return f"€{valore}"

def calcola_locale(nome, dati_locale):
    """Calcola compenso per un singolo locale."""
    tariffa = dati_locale.get("tarifa", 0)
    turni = dati_locale.get("turni", 0)
    totale = tariffa * turni
    descrizione = f"{nome}: {tariffa}€ x {turni} = {format_euro(totale)}" if turni else ""
    return totale, descrizione

def calcola_mese(mese):
    """Calcola compensi, pagamenti e mance per un mese."""
    dati = MESI[mese]
    pagamenti = dati.get("pagamenti", [])
    mance = parse_mance(dati.get("mance", []))

    compensi_totali = 0
    dettagli = []
    for nome, dati_locale in dati.items():
        if nome in RISERVATI:
            continue
        totale_locale, descrizione = calcola_locale(nome, dati_locale)
        compensi_totali += totale_locale
        if descrizione:
            dettagli.append(descrizione)

    somma_pagato = sum(parse_importo(p)[0] for p in pagamenti)
    somma_mance = sum(v["importo"] for v in mance)
    differenza = compensi_totali - somma_pagato - somma_mance

    print(f"\n{mese} 2026")
    print("-" * 60)
    for dettaglio in dettagli:
        print(f"  {dettaglio}")
    print(f"Compensi totali: {format_euro(compensi_totali)}")

    if pagamenti:
        for pagamento in pagamenti:
            importo, desc = parse_importo(pagamento)
            print(f"  Pagato ({desc or 'Pagamento'}): {format_euro(importo)}")
    else:
        print("  Nessun pagamento ricevuto")

    print(f"Totale pagato: {format_euro(somma_pagato)}")

    if mance:
        for mancia in mance:
            print(f"  Mancia ({mancia['fonte']}): {format_euro(mancia['importo'])}")
    else:
        print("  Nessuna mancia ricevuta")

    print(f"Totale mance: {format_euro(somma_mance)}")
    print(f"Da ricevere: {format_euro(differenza)}")

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
    print(f"Totale compensi maturati: {format_euro(totale_compensi)}")
    print(f"Totale pagato: {format_euro(totale_pagato)}")
    print(f"Totale mance ricevute: {format_euro(totale_mance)}")
    print(f"Totale da ricevere: {format_euro(totale_da_ricevere)}")
    print("=" * 60)
