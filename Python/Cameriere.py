# Cameriere.py
#
# Uso rapido:
#   python Cameriere.py                   →  apre la finestra con tutte le analisi
#   python Cameriere.py --export          →  salva riepilogo.csv (senza GUI)
#   python Cameriere.py --mese MAGGIO     →  solo maggio (senza GUI, stampa a schermo)
#
# Per aggiungere un nuovo locale: aggiungilo a "locali" nel JSON con la tariffa,
# poi comparirà automaticamente nei nuovi mesi creati dal programma.

import csv
import argparse
import json
from datetime import date
from pathlib import Path

DATA_FILE = Path(__file__).with_name("cameriere_data.json")

MESE_ORDINE = [
    "GENNAIO", "FEBBRAIO", "MARZO", "APRILE", "MAGGIO", "GIUGNO",
    "LUGLIO", "AGOSTO", "SETTEMBRE", "OTTOBRE", "NOVEMBRE", "DICEMBRE",
]

MESE_NUMERI = {mese: index + 1 for index, mese in enumerate(MESE_ORDINE)}

# ---------------------------------------------------------------------------
# Helper
# ---------------------------------------------------------------------------

def mancia(importo: float, fonte: str = "Mancia") -> dict:
    return {"importo": importo, "fonte": fonte}

def pagamento(importo: float, desc: str = "Pagamento") -> dict:
    return {"importo": importo, "desc": desc}

def arbitraggio(importo: float, desc: str = "Arbitraggio") -> dict:
    return {"importo": importo, "desc": desc}

# ---------------------------------------------------------------------------
# Caricamento / salvataggio
# ---------------------------------------------------------------------------

def load_dati() -> dict:
    try:
        with DATA_FILE.open("r", encoding="utf-8") as f:
            return json.load(f)
    except FileNotFoundError:
        raise FileNotFoundError(
            f"File dati non trovato: {DATA_FILE}. Crea il file o ripristinalo dal repository."
        )

def save_dati() -> None:
    data["locali"] = LOCALI
    data["straordinario"] = STRAORDINARIO_TARIFFE
    data["MESI_COMPLETATI"] = MESI_COMPLETATI
    data["MESI"] = MESI

    with DATA_FILE.open("w", encoding="utf-8") as f:
        json.dump(data, f, indent=2, ensure_ascii=False)

def get_mese_corrente() -> str:
    return MESE_ORDINE[date.today().month - 1]

def mese_vuoto() -> dict:
    """Restituisce un mese con tutti i campi azzerati, usando i locali dal JSON."""
    return {
        "turni":      {nome: 0 for nome in LOCALI},
        "pagamenti":  [],
        "mance":      [],
        "arbitraggi": [],
    }

def sincronizza_mesi() -> None:
    """
    All'avvio:
    - sposta in MESI_COMPLETATI tutti i mesi di MESI precedenti al mese corrente
    - aggiunge il mese corrente a MESI se non è già presente né in MESI_COMPLETATI
    """
    mese_corrente = get_mese_corrente()
    num_corrente  = MESE_NUMERI[mese_corrente]
    modificato    = False

    # 1. sposta i mesi passati
    da_spostare = [
        m for m in list(MESI.keys())
        if MESE_NUMERI.get(m, 99) < num_corrente
    ]
    for mese in da_spostare:
        MESI_COMPLETATI[mese] = MESI.pop(mese)
        modificato = True

    # 2. aggiungi il mese corrente se manca
    if mese_corrente not in MESI and mese_corrente not in MESI_COMPLETATI:
        MESI[mese_corrente] = mese_vuoto()
        modificato = True

    if modificato:
        save_dati()

data = load_dati()
LOCALI:           dict[str, dict] = data.get("locali", {})
STRAORDINARIO_TARIFFE: dict[str, float] = data.get("straordinario", {})
MESI_COMPLETATI:  dict[str, dict] = data.get("MESI_COMPLETATI", {})
MESI:             dict[str, dict] = data.get("MESI", {})

# ---------------------------------------------------------------------------
# Validazione
# ---------------------------------------------------------------------------

def get_mesi() -> dict[str, dict]:
    return MESI_COMPLETATI | MESI

def mese_stato(mese: str) -> str:
    return "COMPLETATO" if mese in MESI_COMPLETATI else "IN CORSO"

def valida_dati() -> list[str]:
    avvisi = []
    for mese, dati in get_mesi().items():
        for nome, n_turni in dati.get("turni", {}).items():
            if nome not in LOCALI:
                avvisi.append(f"[{mese}] '{nome}' non è in LOCALI")
                continue
            if n_turni < 0:
                avvisi.append(f"[{mese}] {nome}: turni negativi")
        for m in dati.get("mance", []):
            if isinstance(m, dict) and m.get("importo", 0) <= 0:
                avvisi.append(f"[{mese}] mancia non valida: {m}")
        for a in dati.get("arbitraggi", []):
            importo, _ = parse_amount_entry(a, "Arbitraggio", "desc")
            if importo <= 0:
                avvisi.append(f"[{mese}] arbitraggio non valido: {a}")
        for p in dati.get("pagamenti", []):
            importo, desc = parse_pagamento(p)
            if importo < 0:
                avvisi.append(f"[{mese}] pagamento negativo ({desc}: {importo}€)")
        straordinario = dati.get("straordinario")
        if straordinario is not None:
            if (not isinstance(straordinario, (list, tuple)) or
                    len(straordinario) != 2):
                avvisi.append(f"[{mese}] straordinario non valido: {straordinario}")
            else:
                locale, ore = straordinario
                if locale not in LOCALI:
                    avvisi.append(f"[{mese}] straordinario: locale sconosciuto '{locale}'")
                if not isinstance(ore, (int, float)) or ore <= 0:
                    avvisi.append(f"[{mese}] straordinario: ore non valide {ore}")
    return avvisi

# ---------------------------------------------------------------------------
# Parsing
# ---------------------------------------------------------------------------

def parse_amount_entry(entry, default_label: str, label_key: str) -> tuple[float, str]:
    if isinstance(entry, dict):
        return entry.get("importo", 0), entry.get(label_key, default_label)
    if isinstance(entry, (list, tuple)) and len(entry) >= 1:
        importo = entry[0] if isinstance(entry[0], (int, float)) else 0
        label   = str(entry[1]) if len(entry) > 1 else default_label
        return importo, label
    if isinstance(entry, (int, float)):
        return entry, default_label
    if isinstance(entry, str):
        try:
            importo = float(entry.split()[0])
            label   = entry[entry.find("(")+1:entry.find(")")] if "(" in entry else default_label
            return importo, label
        except ValueError:
            return 0, default_label
    return 0, default_label

def parse_pagamento(entry) -> tuple[float, str]:
    return parse_amount_entry(entry, "Pagamento", "desc")

def parse_mance(mance: list) -> list[dict]:
    valid = []
    for entry in mance:
        importo, fonte = parse_amount_entry(entry, "Mancia", "fonte")
        if importo > 0:
            valid.append({"importo": importo, "fonte": fonte})
    return valid

# ---------------------------------------------------------------------------
# Formattazione
# ---------------------------------------------------------------------------

def format_euro(valore: float) -> str:
    try:
        return f"€{valore:.2f}"
    except (TypeError, ValueError):
        return f"€{valore}"



def stato_pagamento(compensi: float, pagato: float) -> str:
    if compensi == 0:
        return "— nessun lavoro"
    if pagato <= 0:
        return "✗ Non pagato"
    if pagato >= compensi:
        return "✓ Saldato"
    return "⚠ Parzialmente pagato"

# ---------------------------------------------------------------------------
# Calcolo
# ---------------------------------------------------------------------------

def calcola_locale(nome: str, n_turni: int) -> tuple[float, int, str]:
    tariffa = LOCALI.get(nome, {}).get("tariffa", 0)
    totale  = tariffa * n_turni
    desc    = f"{nome}: {tariffa}€ x {n_turni} = {format_euro(totale)}" if n_turni else ""
    return totale, n_turni, desc

def calcola_mese(mese: str) -> dict:
    dati      = get_mesi()[mese]
    pagamenti = dati.get("pagamenti", [])
    mance     = parse_mance(dati.get("mance", []))

    compensi_totali    = 0
    dettagli_locali    = []
    dettagli_pagamenti = []
    per_locale         = {}

    for nome, n_turni in dati.get("turni", {}).items():
        totale_locale, turni, descrizione = calcola_locale(nome, n_turni)
        compensi_totali += totale_locale
        per_locale[nome] = {"compensi": totale_locale, "turni": turni}
        if descrizione:
            dettagli_locali.append(descrizione)

    straordinario_dati = {}
    straordinario_item = dati.get("straordinario")
    if isinstance(straordinario_item, (list, tuple)) and len(straordinario_item) == 2:
        locale, ore = straordinario_item
        if isinstance(locale, str) and isinstance(ore, (int, float)) and ore > 0:
            tariffa = STRAORDINARIO_TARIFFE.get(locale, 0)
            importo_straord = tariffa * ore
            compensi_totali += importo_straord
            straordinario_dati = {
                "locale": locale,
                "ore": ore,
                "tariffa": tariffa,
                "importo": importo_straord,
            }
            dettagli_locali.append(
                f"Straordinario {locale}: {ore} ore x {format_euro(tariffa)} = {format_euro(importo_straord)}"
            )

    for p in pagamenti:
        importo, desc = parse_pagamento(p)
        dettagli_pagamenti.append({"importo": importo, "desc": desc})

    somma_pagato = sum(v["importo"] for v in dettagli_pagamenti)
    somma_mance  = sum(v["importo"] for v in mance)
    da_ricevere  = compensi_totali - somma_pagato

    arbitraggi_raw  = dati.get("arbitraggi", [])
    arbitraggi      = []
    for entry in arbitraggi_raw:
        importo, desc = parse_amount_entry(entry, "Arbitraggio", "desc")
        if importo > 0:
            arbitraggi.append({"importo": importo, "desc": desc})
    somma_arbitraggi = sum(v["importo"] for v in arbitraggi)

    return {
        "mese":             mese,
        "stato":            mese_stato(mese),
        "compensi_totali":  compensi_totali,
        "dettagli_locali":  dettagli_locali,
        "per_locale":       per_locale,
        "pagamenti":        dettagli_pagamenti,
        "somma_pagato":     somma_pagato,
        "mance":            mance,
        "somma_mance":      somma_mance,
        "arbitraggi":       arbitraggi,
        "somma_arbitraggi": somma_arbitraggi,
        "straordinario":    straordinario_dati,
        "da_ricevere":      da_ricevere,
    }

# ---------------------------------------------------------------------------
# Export CSV (usato da --export senza GUI)
# ---------------------------------------------------------------------------

def esporta_csv(risultati: list[dict], filepath: str = "riepilogo.csv") -> None:
    with open(filepath, "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(["Mese", "Compensi", "Pagato", "Mance", "Arbitraggi", "Da ricevere", "Stato"])
        for r in risultati:
            writer.writerow([
                r["mese"],
                f"{r['compensi_totali']:.2f}",
                f"{r['somma_pagato']:.2f}",
                f"{r['somma_mance']:.2f}",
                f"{r['somma_arbitraggi']:.2f}",
                f"{r['da_ricevere']:.2f}",
                stato_pagamento(r["compensi_totali"], r["somma_pagato"]),
            ])
        writer.writerow([
            "TOTALE",
            f"{sum(r['compensi_totali']  for r in risultati):.2f}",
            f"{sum(r['somma_pagato']     for r in risultati):.2f}",
            f"{sum(r['somma_mance']      for r in risultati):.2f}",
            f"{sum(r['somma_arbitraggi'] for r in risultati):.2f}",
            f"{sum(r['da_ricevere']      for r in risultati):.2f}",
            "",
        ])
    print(f"Export salvato in: {filepath}")

# GUI moved to Dashboard.py. This module provides data, calculation and
# textual output when run standalone. Use Dashboard.py for the graphical UI.

# ---------------------------------------------------------------------------
# Entry point
# ---------------------------------------------------------------------------

def main() -> None:
    sincronizza_mesi()

    parser = argparse.ArgumentParser(description="Compensi cameriere - Estate 2026")
    parser.add_argument("--mese", type=str, choices=list(get_mesi().keys()),
                        metavar="MESE", help="Filtra per un mese (modalità testo)")
    parser.add_argument("--export", action="store_true",
                        help="Salva riepilogo.csv (modalità testo)")
    args = parser.parse_args()

    avvisi = valida_dati()

    mesi_da_calcolare = [args.mese] if args.mese else list(get_mesi().keys())
    risultati = [calcola_mese(m) for m in mesi_da_calcolare]

    def print_risultati(risultati: list[dict], avvisi: list[str]) -> None:
        if avvisi:
            print("⚠️  AVVISI:")
            for a in avvisi:
                print(f"  - {a}")
        for r in risultati:
            sp = stato_pagamento(r["compensi_totali"], r["somma_pagato"])
            print(f"\n{r['mese']} [{r['stato']}] [{sp}]")
            print("-" * 50)
            if r["dettagli_locali"]:
                print("Turni:")
                for d in r["dettagli_locali"]:
                    print(f"  {d}")
            else:
                print("Nessun turno registrato.")
            if r.get("straordinario"):
                s = r["straordinario"]
                print(f"Straordinario: {s['locale']} - {s['ore']} ore = {format_euro(s['importo'])}")
            print(f"Compensi:   {format_euro(r['compensi_totali'])}")
            print(f"Pagato:     {format_euro(r['somma_pagato'])}")
            print(f"Mance:      {format_euro(r['somma_mance'])}")
            print(f"Arbitraggi: {format_euro(r['somma_arbitraggi'])}")
            print(f"Da ricevere:{format_euro(r['da_ricevere'])}")

    # Sempre modalità testuale in questo modulo; la GUI è in Dashboard.py
    print_risultati(risultati, avvisi)
    if args.export:
        esporta_csv(risultati)


if __name__ == "__main__":
    main()
