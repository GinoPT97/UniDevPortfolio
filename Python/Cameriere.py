# cameriere_rimborsi.py
#
# Uso rapido:
#   python cameriere_rimborsi.py                  →  tutti i mesi
#   python cameriere_rimborsi.py --mese GIUGNO    →  solo giugno
#   python cameriere_rimborsi.py --export         →  salva riepilogo.csv
#   python cameriere_rimborsi.py --mese LUGLIO --export
#
# Helper disponibili:
#   mancia(20, "VP")            →  mancia da VP
#   mancia(5)                   →  mancia generica
#   pagamento(120, "VP")        →  pagamento da VP

import csv
import argparse
import sys

RESERVED_KEYS = {"pagamenti", "mance"}


# ---------------------------------------------------------------------------
# Helper per la creazione di voci
# ---------------------------------------------------------------------------

def mancia(importo: float, fonte: str = "Mancia") -> dict:
    """Crea una voce mancia. Uso: mancia(20, 'VP')"""
    return {"importo": importo, "fonte": fonte}


def pagamento(importo: float, desc: str = "Pagamento") -> dict:
    """Crea una voce pagamento. Uso: pagamento(120, 'VP')"""
    return {"importo": importo, "desc": desc}



# ---------------------------------------------------------------------------
# Dati
# ---------------------------------------------------------------------------

MESI = {
    "MAGGIO": {
        "VP":            {"tariffa": 120, "turni": 1},
        "MudJ":          {"tariffa": 40,  "turni": 9},
        "CaricoScarico": {"tariffa": 0,   "turni": 0},
        "pagamenti": [],
        "mance": [
            mancia(20, "VP"),
        ],
    },
    "GIUGNO": {
        "VP":            {"tariffa": 120, "turni": 0},
        "MudJ":          {"tariffa": 40,  "turni": 0},
        "CaricoScarico": {"tariffa": 0,   "turni": 0},
        "pagamenti": [],
        "mance": [],
    },
    "LUGLIO": {
        "VP":            {"tariffa": 120, "turni": 0},
        "MudJ":          {"tariffa": 40,  "turni": 0},
        "CaricoScarico": {"tariffa": 0,   "turni": 0},
        "pagamenti": [],
        "mance": [],
    },
    "AGOSTO": {
        "VP":            {"tariffa": 120, "turni": 0},
        "MudJ":          {"tariffa": 40,  "turni": 0},
        "CaricoScarico": {"tariffa": 0,   "turni": 0},
        "pagamenti": [],
        "mance": [],
    },
}


# ---------------------------------------------------------------------------
# Validazione
# ---------------------------------------------------------------------------

def valida_dati() -> list[str]:
    """Controlla i dati e restituisce una lista di avvisi. Lista vuota = tutto ok."""
    avvisi = []
    for mese, dati in MESI.items():
        for nome, dati_locale in dati.items():
            if nome in RESERVED_KEYS:
                continue
            tariffa = dati_locale.get("tariffa", 0)
            turni   = dati_locale.get("turni", 0)
            if tariffa < 0:
                avvisi.append(f"[{mese}] {nome}: tariffa negativa ({tariffa}€)")
            if turni < 0:
                avvisi.append(f"[{mese}] {nome}: turni negativi ({turni})")

        for m in dati.get("mance", []):
            if isinstance(m, dict) and m.get("importo", 0) <= 0:
                avvisi.append(f"[{mese}] mancia con importo non valido: {m}")

        for p in dati.get("pagamenti", []):
            importo, desc = parse_pagamento(p)
            if importo < 0:
                avvisi.append(f"[{mese}] pagamento negativo ({desc}: {importo}€)")

    return avvisi


# ---------------------------------------------------------------------------
# Parsing
# ---------------------------------------------------------------------------

def parse_pagamento(entry) -> tuple[float, str]:
    """Estrae importo e descrizione da una voce pagamento."""
    if isinstance(entry, dict):
        return entry.get("importo", 0), entry.get("desc", "Pagamento")
    if isinstance(entry, (list, tuple)) and len(entry) >= 1:
        importo = entry[0] if isinstance(entry[0], (int, float)) else 0
        desc    = str(entry[1]) if len(entry) > 1 else "Pagamento"
        return importo, desc
    if isinstance(entry, (int, float)):
        return entry, "Pagamento"
    if isinstance(entry, str):
        try:
            valore = float(entry.split()[0])
            desc   = entry[entry.find("(")+1:entry.find(")")] if "(" in entry else "Pagamento"
            return valore, desc
        except ValueError:
            return 0, ""
    return 0, ""


def parse_mance(mance: list[dict]) -> list[dict]:
    """Restituisce le mance valide (importo > 0)."""
    return [v for v in mance if isinstance(v, dict) and v.get("importo", 0) > 0]


# ---------------------------------------------------------------------------
# Formattazione
# ---------------------------------------------------------------------------

def format_euro(valore: float) -> str:
    try:
        return f"€{valore:.2f}"
    except (TypeError, ValueError):
        return f"€{valore}"


# ---------------------------------------------------------------------------
# Calcolo (puro, senza stampa)
# ---------------------------------------------------------------------------

def calcola_locale(nome: str, dati_locale: dict) -> tuple[float, int, str]:
    """Calcola il compenso per un singolo locale. Restituisce (totale, n_turni, descrizione)."""
    tariffa = dati_locale.get("tariffa", 0)
    turni   = dati_locale.get("turni", 0)
    totale  = tariffa * turni
    desc    = f"{nome}: {tariffa}€ x {turni} = {format_euro(totale)}" if turni else ""
    return totale, turni, desc


def calcola_mese(mese: str) -> dict:
    """Calcola compensi, pagamenti e mance per un mese. Restituisce un dict con tutti i dati."""
    dati      = MESI[mese]
    pagamenti = dati.get("pagamenti", [])
    mance     = parse_mance(dati.get("mance", []))

    compensi_totali  = 0
    dettagli_locali  = []
    dettagli_pagamenti = []
    for nome, dati_locale in dati.items():
        if nome in RESERVED_KEYS:
            continue
        totale_locale, n_turni, descrizione = calcola_locale(nome, dati_locale)
        compensi_totali += totale_locale
        if descrizione:
            dettagli_locali.append(descrizione)

    for p in pagamenti:
        importo, desc = parse_pagamento(p)
        dettagli_pagamenti.append({"importo": importo, "desc": desc})

    somma_pagato = sum(v["importo"] for v in dettagli_pagamenti)
    somma_mance  = sum(v["importo"] for v in mance)
    differenza   = compensi_totali - somma_pagato

    return {
        "mese":               mese,
        "compensi_totali":    compensi_totali,
        "dettagli_locali":    dettagli_locali,
        "pagamenti":          dettagli_pagamenti,
        "somma_pagato":       somma_pagato,
        "mance":              mance,
        "somma_mance":        somma_mance,
        "da_ricevere":        differenza,
    }


# ---------------------------------------------------------------------------
# Stampa
# ---------------------------------------------------------------------------

def stampa_mese(r: dict) -> None:
    """Stampa il riepilogo di un mese dato il risultato di calcola_mese."""
    print(f"\n{r['mese']} 2026")
    print("-" * 60)
    for dettaglio in r["dettagli_locali"]:
        print(f"  {dettaglio}")
    print(f"Compensi totali:  {format_euro(r['compensi_totali'])}")

    if r["pagamenti"]:
        for p in r["pagamenti"]:
            print(f"  Pagato ({p['desc']}): {format_euro(p['importo'])}")
    else:
        print("  Nessun pagamento ricevuto")
    print(f"Totale pagato:    {format_euro(r['somma_pagato'])}")

    if r["mance"]:
        for m in r["mance"]:
            print(f"  Mancia ({m['fonte']}): {format_euro(m['importo'])}")
    else:
        print("  Nessuna mancia ricevuta")
    print(f"Totale mance:     {format_euro(r['somma_mance'])}")
    print(f"Da ricevere:      {format_euro(r['da_ricevere'])}")


def stampa_riepilogo(risultati: list[dict]) -> None:
    """Stampa il riepilogo generale di tutti i mesi."""
    tot_compensi  = sum(r["compensi_totali"] for r in risultati)
    tot_pagato    = sum(r["somma_pagato"]     for r in risultati)
    tot_mance     = sum(r["somma_mance"]      for r in risultati)
    tot_ricevere  = sum(r["da_ricevere"]      for r in risultati)

    print("\n" + "=" * 60)
    print(" RIEPILOGO GENERALE - ESTATE 2026")
    print("=" * 60)
    print(f"Totale compensi maturati: {format_euro(tot_compensi)}")
    print(f"Totale pagato:            {format_euro(tot_pagato)}")
    print(f"Totale mance ricevute:    {format_euro(tot_mance)}")
    print(f"Totale da ricevere:       {format_euro(tot_ricevere)}")
    print("=" * 60)


# ---------------------------------------------------------------------------
# Export CSV
# ---------------------------------------------------------------------------

def esporta_csv(risultati: list[dict], filepath: str = "riepilogo.csv") -> None:
    """Salva il riepilogo mensile in un file CSV."""
    with open(filepath, "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(["Mese", "Compensi", "Pagato", "Mance", "Da ricevere"])
        for r in risultati:
            writer.writerow([
                r["mese"],
                f"{r['compensi_totali']:.2f}",
                f"{r['somma_pagato']:.2f}",
                f"{r['somma_mance']:.2f}",
                f"{r['da_ricevere']:.2f}",
            ])
        # Riga totali
        writer.writerow([
            "TOTALE",
            f"{sum(r['compensi_totali'] for r in risultati):.2f}",
            f"{sum(r['somma_pagato']    for r in risultati):.2f}",
            f"{sum(r['somma_mance']     for r in risultati):.2f}",
            f"{sum(r['da_ricevere']     for r in risultati):.2f}",
        ])
    print(f"\nExport salvato in: {filepath}")


# ---------------------------------------------------------------------------
# Entry point
# ---------------------------------------------------------------------------

def main() -> None:
    parser = argparse.ArgumentParser(description="Calcolo compensi cameriere - Estate 2026")
    parser.add_argument(
        "--mese",
        type=str,
        choices=list(MESI.keys()),
        metavar="MESE",
        help=f"Filtra per un mese specifico: {', '.join(MESI.keys())}",
    )
    parser.add_argument(
        "--export",
        action="store_true",
        help="Salva il riepilogo in riepilogo.csv",
    )
    args = parser.parse_args()

    # Validazione dati
    avvisi = valida_dati()
    if avvisi:
        print("⚠️  AVVISI NEI DATI:")
        for a in avvisi:
            print(f"   {a}")
        print()

    print("=" * 60)
    print("CALCOLO COMPENSI CAMERIERE - ESTATE 2026")
    print("=" * 60)

    mesi_da_calcolare = [args.mese] if args.mese else list(MESI.keys())
    risultati = []
    for mese in mesi_da_calcolare:
        r = calcola_mese(mese)
        stampa_mese(r)
        risultati.append(r)

    if len(risultati) > 1:
        stampa_riepilogo(risultati)

    if args.export:
        esporta_csv(risultati)


if __name__ == "__main__":
    main()