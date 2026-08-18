# Sommatoria.py
# Modulo dati e calcolo rimborsi arbitraggio per stagione.
# La GUI è gestita da Dashboard.py.
#
# Uso standalone (output testo):
#   python Sommatoria.py
# Oppure apri la GUI:
#   python Dashboard.py

import numpy as np

# ---------------------------------------------------------------------------
# Dati
# ---------------------------------------------------------------------------

STAGIONI: dict[str, dict] = {
    "2024/2025": {
        "rimborso": [37, 57, 79, 63, 61, 57, 61, 70, 63, 61, 79, 63, 63, 63, 63, 61, 63, 70, 63, 79,
                     61, 63, 57, 61, 63, 70, 70, 57, 61, 61, 63, 61, 63, 37, 61, 89, 63, 63, 61, 57,
                     63, 57, 61, 57, 61, 63, 70, 63, 70, 63, 70, 89, 63, 63, 63, 57, 61, 63],
        "pagato":   [573, 173, 118, 63, 131, 205, 124, 372, 142, 57, 127, 61, 63, 98, 89, 183, 114, 63,
                     194, 203, 152, 120, 250],
    },
    "2025/2026": {
        "rimborso": [63, 79, 70, 61, 63, 63, 63, 70, 70, 61, 70, 79, 63, 70, 63, 63, 70, 61, 61, 70,
                     63, 63, 70, 61, 70, 70, 63, 61, 70, 54, 37, 63, 70, 63, 79, 63, 63, 61, 61, 37,
                     63, 63, 63, 63],
        "pagato":   [63, 140, 259, 350, 133, 131, 257, 133, 107, 196, 142, 61, 37, 63, 516, {"importo": 57.21, "detrazioni_fiscali": 5.79}, 122],
    },
    "2026/2027": {
        "rimborso": [],
        "pagato":   [],
    },
}

# ---------------------------------------------------------------------------
# Calcolo
# ---------------------------------------------------------------------------

def _somma_pagamenti(pagamenti: list) -> tuple[float, float]:
    """Somma importi netti e detrazioni fiscali da una lista di pagamenti."""
    tot_pagato = 0.0
    tot_detrazioni = 0.0
    for voce in pagamenti:
        if isinstance(voce, dict):
            tot_pagato += voce.get("importo", 0)
            tot_detrazioni += voce.get("detrazioni_fiscali", 0)
        else:
            tot_pagato += float(voce)
    return tot_pagato, tot_detrazioni


def _normalize_amount(valore: float):
    """Arrotonda a 2 decimali e ritorna intero se il risultato è un intero."""
    valore = round(valore, 2)
    return int(valore) if float(valore).is_integer() else valore


def calcola(stagione: str) -> dict:
    """Restituisce il dizionario dei dati calcolati per una stagione."""
    d        = STAGIONI[stagione]
    rimborso = d["rimborso"]
    pagato   = d["pagato"]
    tot_r    = sum(rimborso)
    tot_p, tot_detrazioni = _somma_pagamenti(pagato)
    return {
        "stagione":             stagione,
        "rimborso":             rimborso,
        "pagato":               pagato,
        "tot_rimborso":         _normalize_amount(tot_r),
        "tot_pagato":           _normalize_amount(tot_p),
        "tot_detrazioni_fiscali": _normalize_amount(tot_detrazioni),
        "da_ricevere":          _normalize_amount(tot_r - tot_p - tot_detrazioni),
        "n_gare":               len(rimborso),
        "n_pagamenti":          len(pagato),
        "media_gara":           _normalize_amount(tot_r / len(rimborso)) if rimborso else 0,
        "max_gara":             _normalize_amount(max(rimborso)) if rimborso else 0,
        "min_gara":             _normalize_amount(min(rimborso)) if rimborso else 0,
        "cumulativo":           list(np.cumsum(rimborso)),
    }

def calcola_tutte() -> list[dict]:
    """Restituisce la lista dei risultati per tutte le stagioni."""
    return [calcola(s) for s in STAGIONI]

# ---------------------------------------------------------------------------
# Entry point (output testo)
# ---------------------------------------------------------------------------

if __name__ == "__main__":
    risultati = calcola_tutte()
    print("=" * 60)
    print("CALCOLO RIMBORSI PER STAGIONE")
    print("=" * 60)
    for r in risultati:
        print(f"\n STAGIONE {r['stagione']}")
        print("-" * 60)
        print(f"Rimborsi totali: {r['n_gare']} gare = €{r['tot_rimborso']}")
        print(f"Pagato:          {r['n_pagamenti']} pagamenti = €{r['tot_pagato']}")
        print(f"Detrazioni fiscali: €{r['tot_detrazioni_fiscali']}")
        print(f"Da ricevere:     €{r['da_ricevere']}")
    tot_r = sum(r["tot_rimborso"] for r in risultati)
    tot_p = sum(r["tot_pagato"]   for r in risultati)
    tot_detrazioni = sum(r["tot_detrazioni_fiscali"] for r in risultati)
    print("\n" + "=" * 60)
    print(" RIEPILOGO GENERALE")
    print("=" * 60)
    print(f"Totale rimborsi maturati: €{tot_r}")
    print(f"Totale pagato:            €{tot_p}")
    print(f"Totale detrazioni fiscali: €{tot_detrazioni}")
    print(f"Totale da ricevere:       €{tot_r - tot_p - tot_detrazioni}")
    print("=" * 60)