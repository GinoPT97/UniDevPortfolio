"""Script per calcolo compensi cameriere - versione con dataclass e dati esterni.

Usi principali:
  - `python Cameriere.py` stampa il riepilogo per tutti i mesi nel file `data_2026.json`
  - `python Cameriere.py --mese MAGGIO` stampa solo maggio
  - `python Cameriere.py --export riepilogo.csv` esporta il riepilogo in CSV
"""

from dataclasses import dataclass, field
from typing import Dict, List
import json
import argparse
import csv
from pathlib import Path


def mancia(importo: float, fonte: str = "Mancia") -> dict:
    if not isinstance(importo, (int, float)):
        raise TypeError("importo deve essere un numero")
    return {"importo": float(importo), "fonte": fonte}


# Dati integrati (era in data_2026.py / data_2026.json)
DATA = {
    "MAGGIO": {
        "VP": {"tariffa": 120, "turni": 1},
        "MudJ": {"tariffa": 40, "turni": 9},
        "CaricoScarico": {"tariffa": 0, "turni": 0},
        "pagamenti": [],
        "mance": [
            mancia(20, "VP"),
        ],
    },
    "GIUGNO": {
        "VP": {"tariffa": 120, "turni": 0},
        "MudJ": {"tariffa": 40, "turni": 0},
        "CaricoScarico": {"tariffa": 0, "turni": 0},
        "pagamenti": [],
        "mance": [],
    },
    "LUGLIO": {
        "VP": {"tariffa": 120, "turni": 0},
        "MudJ": {"tariffa": 40, "turni": 0},
        "CaricoScarico": {"tariffa": 0, "turni": 0},
        "pagamenti": [],
        "mance": [],
    },
    "AGOSTO": {
        "VP": {"tariffa": 120, "turni": 0},
        "MudJ": {"tariffa": 40, "turni": 0},
        "CaricoScarico": {"tariffa": 0, "turni": 0},
        "pagamenti": [],
        "mance": [],
    },
}


def parse_locali(dati: dict) -> Dict[str, Locale]:
    locali: Dict[str, Locale] = {}
    for nome, v in dati.items():
        if nome in RESERVED_KEYS:
            continue
        tariffa = float(v.get("tariffa", 0))
        turni = int(v.get("turni", 0))
        locali[nome] = Locale(tariffa=tariffa, turni=turni)
    return locali


def parse_pagamenti(dati: dict) -> List[Pagamento]:
    pagamenti: List[Pagamento] = []
    for p in dati.get("pagamenti", []):
        if isinstance(p, dict):
            pagamenti.append(Pagamento(importo=float(p.get("importo", 0)), desc=p.get("desc", "")))
        else:
            pagamenti.append(Pagamento(importo=float(p), desc=""))
    return pagamenti


def parse_mance(dati: dict, mese: str) -> List[Mancia]:
    mance: List[Mancia] = []
    for m in dati.get("mance", []):
        if not isinstance(m, dict) or "importo" not in m:
            raise TypeError(f"Formato mancia non valido per mese {mese}: {m}")
        mance.append(Mancia(importo=float(m["importo"]), fonte=m.get("fonte", "Mancia")))
    return mance


@dataclass
class Locale:
    tariffa: float
    turni: int = 0


@dataclass
class Mancia:
    importo: float
    fonte: str = "Mancia"


@dataclass
class Pagamento:
    importo: float
    desc: str = ""


@dataclass
class DatiMese:
    locali: Dict[str, Locale] = field(default_factory=dict)
    pagamenti: List[Pagamento] = field(default_factory=list)
    mance: List[Mancia] = field(default_factory=list)


DATA_FILE = Path(__file__).with_name("data_2026.json")
RESERVED_KEYS = {"pagamenti", "mance"}


def load_data() -> Dict[str, DatiMese]:
    raw = DATA
    result: Dict[str, DatiMese] = {}
    for mese, dati in raw.items():
        locali = parse_locali(dati)
        pagamenti = parse_pagamenti(dati)
        mance = parse_mance(dati, mese)
        result[mese] = DatiMese(locali=locali, pagamenti=pagamenti, mance=mance)
    return result


def format_euro(valore: float) -> str:
    return f"€{valore:.2f}"


def calcola_locale(locale: Locale) -> float:
    return locale.tariffa * locale.turni


def calcola_mese(dati_mese: DatiMese):
    compensi_totali = sum(calcola_locale(l) for l in dati_mese.locali.values())
    somma_pagato = sum(p.importo for p in dati_mese.pagamenti)
    somma_mance = sum(m.importo for m in dati_mese.mance)
    differenza = compensi_totali - somma_pagato - somma_mance
    dettagli = [f"{nome}: {loc.tariffa}€ x {loc.turni} = {format_euro(calcola_locale(loc))}" for nome, loc in dati_mese.locali.items() if loc.turni]
    return {
        "compensi_totali": compensi_totali,
        "somma_pagato": somma_pagato,
        "somma_mance": somma_mance,
        "differenza": differenza,
        "dettagli": dettagli,
        "mance": dati_mese.mance,
        "pagamenti": dati_mese.pagamenti,
    }


def stampa_riepilogo(mese: str, res: dict):
    print(f"\n{mese} 2026")
    print("-" * 60)
    for d in res["dettagli"]:
        print(f"  {d}")
    print(f"Compensi totali: {format_euro(res['compensi_totali'])}")

    if res["pagamenti"]:
        for p in res["pagamenti"]:
            print(f"  Pagato ({p.desc or 'Pagamento'}): {format_euro(p.importo)}")
    else:
        print("  Nessun pagamento ricevuto")

    print(f"Totale pagato: {format_euro(res['somma_pagato'])}")

    if res["mance"]:
        for m in res["mance"]:
            print(f"  Mancia ({m.fonte}): {format_euro(m.importo)}")
    else:
        print("  Nessuna mancia ricevuta")

    print(f"Totale mance: {format_euro(res['somma_mance'])}")
    print(f"Da ricevere: {format_euro(res['differenza'])}")


def export_csv(path: Path, riepilogo: Dict[str, dict]):
    with path.open("w", newline="") as f:
        writer = csv.writer(f)
        writer.writerow(["mese", "compensi_totali", "totale_pagato", "totale_mance", "differenza"])
        for mese, r in riepilogo.items():
            writer.writerow([mese, f"{r['compensi_totali']:.2f}", f"{r['somma_pagato']:.2f}", f"{r['somma_mance']:.2f}", f"{r['differenza']:.2f}"])


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--mese", help="nome del mese da calcolare (es. MAGGIO)")
    parser.add_argument("--export", help="percorso file CSV per esportare il riepilogo")
    parser.add_argument("--data", help="file dati JSON", default=str(DATA_FILE))
    args = parser.parse_args()

    dati = load_data()

    mesi_da_elaborare = [args.mese] if args.mese else list(dati.keys())

    riepilogo = {}
    totale_compensi = totale_pagato = totale_mance = totale_da_ricevere = 0

    for mese in mesi_da_elaborare:
        dm = dati.get(mese)
        if dm is None:
            print(f"Mese non trovato: {mese}")
            continue
        res = calcola_mese(dm)
        riepilogo[mese] = res
        stampa_riepilogo(mese, res)
        totale_compensi += res["compensi_totali"]
        totale_pagato += res["somma_pagato"]
        totale_mance += res["somma_mance"]
        totale_da_ricevere += res["differenza"]

    print("\n" + "=" * 60)
    print(" RIEPILOGO GENERALE - ESTATE 2026")
    print("=" * 60)
    print(f"Totale compensi maturati: {format_euro(totale_compensi)}")
    print(f"Totale pagato: {format_euro(totale_pagato)}")
    print(f"Totale mance ricevute: {format_euro(totale_mance)}")
    print(f"Totale da ricevere: {format_euro(totale_da_ricevere)}")
    print("=" * 60)

    if args.export:
        export_csv(Path(args.export), riepilogo)


if __name__ == "__main__":
    main()

