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
    with DATA_FILE.open("w", encoding="utf-8") as f:
        json.dump(
            {"locali": LOCALI, "MESI_COMPLETATI": MESI_COMPLETATI, "MESI": MESI},
            f, indent=2, ensure_ascii=False,
        )

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

# ---------------------------------------------------------------------------
# GUI
# ---------------------------------------------------------------------------

def mostra_gui(risultati: list[dict], avvisi: list[str]) -> None:
    import tkinter as tk
    from tkinter import ttk
    import matplotlib
    matplotlib.use("TkAgg")
    import matplotlib.pyplot as plt
    from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
    import numpy as np

    # ---- palette colori ----
    C_BG      = "#1e1e2e"
    C_PANEL   = "#2a2a3e"
    C_ACCENT  = "#7c6af7"
    C_GREEN   = "#50fa7b"
    C_YELLOW  = "#f1fa8c"
    C_RED     = "#ff5555"
    C_ORANGE  = "#ffb86c"
    C_CYAN    = "#8be9fd"
    C_TEXT    = "#cdd6f4"
    C_SUBTEXT = "#a6adc8"
    C_GRID    = "#313244"

    mesi_attivi = [r for r in risultati if r["compensi_totali"] > 0 or r["somma_arbitraggi"] > 0]

    root = tk.Tk()
    root.title("Compensi Estate 2026")
    root.configure(bg=C_BG)
    root.geometry("1100x720")
    root.minsize(900, 600)

    style = ttk.Style()
    style.theme_use("default")
    style.configure("TNotebook",        background=C_BG,    borderwidth=0)
    style.configure("TNotebook.Tab",    background=C_PANEL, foreground=C_SUBTEXT,
                    padding=[14, 6],    font=("Segoe UI", 10))
    style.map("TNotebook.Tab",
              background=[("selected", C_ACCENT)],
              foreground=[("selected", "#ffffff")])
    style.configure("TFrame",           background=C_BG)
    style.configure("TLabel",           background=C_BG,    foreground=C_TEXT,
                    font=("Segoe UI", 10))
    style.configure("Header.TLabel",    background=C_BG,    foreground=C_TEXT,
                    font=("Segoe UI", 13, "bold"))
    style.configure("Card.TFrame",      background=C_PANEL, relief="flat")
    style.configure("Treeview",         background=C_PANEL, foreground=C_TEXT,
                    fieldbackground=C_PANEL, rowheight=26,
                    font=("Segoe UI", 10))
    style.configure("Treeview.Heading", background=C_GRID,  foreground=C_SUBTEXT,
                    font=("Segoe UI", 10, "bold"))
    style.map("Treeview", background=[("selected", C_ACCENT)])

    def mpl_fig(figsize=(10, 4.5)):
        fig, ax = plt.subplots(figsize=figsize, facecolor=C_BG)
        ax.set_facecolor(C_BG)
        ax.tick_params(colors=C_SUBTEXT, labelsize=9)
        for spine in ax.spines.values():
            spine.set_edgecolor(C_GRID)
        ax.yaxis.grid(True, color=C_GRID, linewidth=0.6)
        ax.set_axisbelow(True)
        return fig, ax

    def embed(fig, parent):
        canvas = FigureCanvasTkAgg(fig, master=parent)
        canvas.draw()
        canvas.get_tk_widget().pack(fill="both", expand=True, padx=12, pady=8)
        return canvas

    def card(parent, title, value, color=C_TEXT, col=0, row=0):
        f = tk.Frame(parent, bg=C_PANEL, padx=16, pady=12)
        f.grid(row=row, column=col, padx=8, pady=6, sticky="ew")
        tk.Label(f, text=title, bg=C_PANEL, fg=C_SUBTEXT,
                 font=("Segoe UI", 9)).pack(anchor="w")
        tk.Label(f, text=value, bg=C_PANEL, fg=color,
                 font=("Segoe UI", 16, "bold")).pack(anchor="w", pady=(2, 0))

    notebook = ttk.Notebook(root)
    notebook.pack(fill="both", expand=True, padx=10, pady=10)

    # ================================================================
    # TAB 1 — PANORAMICA
    # ================================================================
    tab1 = ttk.Frame(notebook)
    notebook.add(tab1, text="  📊 Panoramica  ")

    tot_compensi   = sum(r["compensi_totali"]  for r in risultati)
    tot_pagato     = sum(r["somma_pagato"]      for r in risultati)
    tot_mance      = sum(r["somma_mance"]       for r in risultati)
    tot_arbitraggi = sum(r["somma_arbitraggi"]  for r in risultati)
    tot_ricevere   = sum(r["da_ricevere"]       for r in risultati)
    tot_entrate    = tot_compensi + tot_mance + tot_arbitraggi

    cards_frame = tk.Frame(tab1, bg=C_BG)
    cards_frame.pack(fill="x", padx=8, pady=(10, 4))
    for i in range(5):
        cards_frame.columnconfigure(i, weight=1)

    card(cards_frame, "Entrate totali",     format_euro(tot_entrate),    C_CYAN,   col=0)
    card(cards_frame, "Compensi cameriere", format_euro(tot_compensi),   C_ACCENT, col=1)
    card(cards_frame, "Arbitraggi",         format_euro(tot_arbitraggi), C_ORANGE, col=2)
    card(cards_frame, "Mance",              format_euro(tot_mance),      C_GREEN,  col=3)
    card(cards_frame, "Da ricevere",        format_euro(tot_ricevere),
         C_RED if tot_ricevere > 0 else C_GREEN, col=4)

    ttk.Label(tab1, text="Dettaglio mensile", style="Header.TLabel").pack(
        anchor="w", padx=20, pady=(10, 4))

    cols = ("Mese", "Stato", "Compensi", "Arbitraggi", "Mance", "Pagato", "Da ricevere", "Pagamento")
    tree = ttk.Treeview(tab1, columns=cols, show="headings", height=10)
    for c in cols:
        tree.heading(c, text=c)
        tree.column(c, anchor="center", width=110)
    tree.column("Mese",      width=90)
    tree.column("Pagamento", width=150)

    COLOR_MAP = {
        "✓ Saldato":             C_GREEN,
        "⚠ Parzialmente pagato": C_YELLOW,
        "✗ Non pagato":          C_RED,
        "— nessun lavoro":       C_SUBTEXT,
    }
    for tag, color in COLOR_MAP.items():
        tree.tag_configure(tag, foreground=color)

    for r in risultati:
        sp = stato_pagamento(r["compensi_totali"], r["somma_pagato"])
        tree.insert("", "end", values=(
            r["mese"].capitalize(),
            r["stato"],
            format_euro(r["compensi_totali"]),
            format_euro(r["somma_arbitraggi"]),
            format_euro(r["somma_mance"]),
            format_euro(r["somma_pagato"]),
            format_euro(r["da_ricevere"]),
            sp,
        ), tags=(sp,))

    sb = ttk.Scrollbar(tab1, orient="vertical", command=tree.yview)
    tree.configure(yscrollcommand=sb.set)
    tree.pack(side="left", fill="both", expand=True, padx=(20, 0), pady=4)
    sb.pack(side="left", fill="y", pady=4)

    if avvisi:
        warn_frame = tk.Frame(tab1, bg="#3a2a2a", padx=10, pady=6)
        warn_frame.pack(fill="x", padx=20, pady=6, side="bottom")
        tk.Label(warn_frame, text="⚠️  Avvisi nei dati:", bg="#3a2a2a",
                 fg=C_YELLOW, font=("Segoe UI", 9, "bold")).pack(anchor="w")
        for a in avvisi:
            tk.Label(warn_frame, text=f"  • {a}", bg="#3a2a2a",
                     fg=C_YELLOW, font=("Segoe UI", 9)).pack(anchor="w")

    # ================================================================
    # TAB 2 — GUADAGNI PER MESE
    # ================================================================
    tab2 = ttk.Frame(notebook)
    notebook.add(tab2, text="  📅 Guadagni mensili  ")

    etichette = [r["mese"].capitalize()[:3] for r in risultati]
    v_comp    = [r["compensi_totali"]  for r in risultati]
    v_arb     = [r["somma_arbitraggi"] for r in risultati]
    v_man     = [r["somma_mance"]      for r in risultati]
    v_pag     = [r["somma_pagato"]     for r in risultati]

    fig2, ax2 = mpl_fig(figsize=(10, 4.8))
    xs = np.arange(len(etichette))
    w  = 0.22

    ax2.bar(xs - w*1.5, v_comp, w, label="Compensi",   color=C_ACCENT, alpha=0.9)
    ax2.bar(xs - w*0.5, v_arb,  w, label="Arbitraggi", color=C_ORANGE, alpha=0.9)
    ax2.bar(xs + w*0.5, v_man,  w, label="Mance",      color=C_GREEN,  alpha=0.9)
    ax2.bar(xs + w*1.5, v_pag,  w, label="Pagato",     color=C_CYAN,   alpha=0.9)

    ax2.set_xticks(xs)
    ax2.set_xticklabels(etichette, color=C_SUBTEXT)
    ax2.set_ylabel("€", color=C_SUBTEXT)
    ax2.set_title("Guadagni per mese", color=C_TEXT, pad=10)
    ax2.legend(facecolor=C_PANEL, edgecolor=C_GRID, labelcolor=C_TEXT, fontsize=9)

    for bars in ax2.containers:
        for bar in bars:
            h = bar.get_height()
            if h > 0:
                ax2.text(bar.get_x() + bar.get_width()/2, h + 2, f"{h:.0f}",
                         ha="center", va="bottom", fontsize=7.5, color=C_TEXT)

    fig2.tight_layout()
    embed(fig2, tab2)

    # ================================================================
    # TAB 3 — PER LOCALE
    # ================================================================
    tab3 = ttk.Frame(notebook)
    notebook.add(tab3, text="  🏢 Per locale  ")

    riepilogo_locali: dict[str, dict] = {}
    for r in risultati:
        for nome, d in r["per_locale"].items():
            if nome not in riepilogo_locali:
                riepilogo_locali[nome] = {"turni": 0, "compensi": 0.0}
            riepilogo_locali[nome]["turni"]    += d["turni"]
            riepilogo_locali[nome]["compensi"] += d["compensi"]

    locali_attivi = {k: v for k, v in riepilogo_locali.items() if v["turni"] > 0}

    if locali_attivi:
        fig3, (ax3a, ax3b) = plt.subplots(1, 2, figsize=(10, 4.5), facecolor=C_BG)
        palette = [C_ACCENT, C_ORANGE, C_CYAN, C_GREEN, C_YELLOW]

        nomi   = list(locali_attivi.keys())
        turni  = [locali_attivi[n]["turni"]    for n in nomi]
        comps  = [locali_attivi[n]["compensi"] for n in nomi]
        colors = palette[:len(nomi)]

        for ax in (ax3a, ax3b):
            ax.set_facecolor(C_BG)
            ax.tick_params(colors=C_SUBTEXT, labelsize=9)
            for spine in ax.spines.values():
                spine.set_edgecolor(C_GRID)
            ax.yaxis.grid(True, color=C_GRID, linewidth=0.6)
            ax.set_axisbelow(True)

        xs3 = np.arange(len(nomi))
        ax3a.bar(xs3, turni, color=colors, alpha=0.9, width=0.5)
        ax3a.set_xticks(xs3); ax3a.set_xticklabels(nomi, color=C_SUBTEXT)
        ax3a.set_ylabel("Turni", color=C_SUBTEXT)
        ax3a.set_title("Turni per locale", color=C_TEXT, pad=8)
        for i, v in enumerate(turni):
            ax3a.text(i, v + 0.1, str(v), ha="center", va="bottom",
                      color=C_TEXT, fontsize=10, fontweight="bold")

        ax3b.bar(xs3, comps, color=colors, alpha=0.9, width=0.5)
        ax3b.set_xticks(xs3); ax3b.set_xticklabels(nomi, color=C_SUBTEXT)
        ax3b.set_ylabel("€", color=C_SUBTEXT)
        ax3b.set_title("Compensi per locale", color=C_TEXT, pad=8)
        for i, v in enumerate(comps):
            ax3b.text(i, v + 1, f"{v:.0f}€", ha="center", va="bottom",
                      color=C_TEXT, fontsize=10, fontweight="bold")

        fig3.tight_layout()
        embed(fig3, tab3)
    else:
        tk.Label(tab3, text="Nessun turno registrato.", fg=C_SUBTEXT,
                 font=("Segoe UI", 12)).pack(expand=True)

    # ================================================================
    # TAB 4 — STATO PAGAMENTI (torta)
    # ================================================================
    tab4 = ttk.Frame(notebook)
    notebook.add(tab4, text="  💰 Pagamenti  ")

    saldati    = sum(1 for r in risultati if r["compensi_totali"] > 0 and r["somma_pagato"] >= r["compensi_totali"])
    parziali   = sum(1 for r in risultati if 0 < r["somma_pagato"] < r["compensi_totali"])
    non_pagati = sum(1 for r in risultati if r["compensi_totali"] > 0 and r["somma_pagato"] <= 0)

    fig4, (ax4a, ax4b) = plt.subplots(1, 2, figsize=(10, 4.5), facecolor=C_BG)

    slice_labels, slice_values, slice_colors = [], [], []
    for label, val, col in [("Saldato", saldati, C_GREEN),
                              ("Parziale", parziali, C_YELLOW),
                              ("Non pagato", non_pagati, C_RED)]:
        if val > 0:
            slice_labels.append(label); slice_values.append(val); slice_colors.append(col)

    ax4a.set_facecolor(C_BG)
    if slice_values:
        wedges, texts, autotexts = ax4a.pie(
            slice_values, labels=slice_labels, colors=slice_colors,
            autopct="%1.0f%%", startangle=90,
            textprops={"color": C_TEXT, "fontsize": 10},
            wedgeprops={"linewidth": 2, "edgecolor": C_BG},
        )
        for at in autotexts:
            at.set_fontsize(9)
            at.set_color(C_BG)
    ax4a.set_title("Stato pagamenti (mesi con lavoro)", color=C_TEXT, pad=10)

    ax4b.set_facecolor(C_BG)
    ax4b.tick_params(colors=C_SUBTEXT, labelsize=8)
    for spine in ax4b.spines.values():
        spine.set_edgecolor(C_GRID)
    ax4b.yaxis.grid(True, color=C_GRID, linewidth=0.6)
    ax4b.set_axisbelow(True)

    mr = [r for r in risultati if r["compensi_totali"] > 0]
    if mr:
        xm = np.arange(len(mr))
        ax4b.bar(xm - 0.2, [r["compensi_totali"] for r in mr], 0.4,
                 label="Maturato", color=C_ACCENT, alpha=0.9)
        ax4b.bar(xm + 0.2, [r["somma_pagato"]    for r in mr], 0.4,
                 label="Pagato",   color=C_GREEN,  alpha=0.9)
        ax4b.set_xticks(xm)
        ax4b.set_xticklabels([r["mese"].capitalize()[:3] for r in mr], color=C_SUBTEXT)
        ax4b.set_ylabel("€", color=C_SUBTEXT)
        ax4b.set_title("Maturato vs Pagato", color=C_TEXT, pad=8)
        ax4b.legend(facecolor=C_PANEL, edgecolor=C_GRID, labelcolor=C_TEXT, fontsize=9)

    fig4.tight_layout()
    embed(fig4, tab4)

    # ================================================================
    # TAB 5 — DETTAGLIO MESE (selezionabile)
    # ================================================================
    tab5 = ttk.Frame(notebook)
    notebook.add(tab5, text="  🔍 Dettaglio  ")

    top5 = tk.Frame(tab5, bg=C_BG)
    top5.pack(fill="x", padx=16, pady=(10, 4))
    tk.Label(top5, text="Seleziona mese:", fg=C_SUBTEXT,
             font=("Segoe UI", 10)).pack(side="left")

    mese_var = tk.StringVar(value=risultati[0]["mese"])
    combo = ttk.Combobox(top5, textvariable=mese_var, state="readonly",
                         values=[r["mese"] for r in risultati], width=14)
    combo.pack(side="left", padx=8)

    detail_frame = tk.Frame(tab5, bg=C_BG)
    detail_frame.pack(fill="both", expand=True, padx=16, pady=4)

    def aggiorna_dettaglio(*_):
        for w in detail_frame.winfo_children():
            w.destroy()

        r      = next(x for x in risultati if x["mese"] == mese_var.get())
        sp     = stato_pagamento(r["compensi_totali"], r["somma_pagato"])
        col_sp = COLOR_MAP.get(sp, C_TEXT)

        hf = tk.Frame(detail_frame, bg=C_BG)
        hf.pack(fill="x", pady=(0, 8))
        tk.Label(hf, text=f"{r['mese'].capitalize()} 2026",
                 bg=C_BG, fg=C_TEXT, font=("Segoe UI", 14, "bold")).pack(side="left")
        tk.Label(hf, text=f"  [{r['stato']}]  [{sp}]",
                 bg=C_BG, fg=col_sp, font=("Segoe UI", 10)).pack(side="left")

        def sezione(titolo, voci, totale, col_tot=C_TEXT, vuoto="Nessuno"):
            sf = tk.Frame(detail_frame, bg=C_PANEL, padx=12, pady=8)
            sf.pack(fill="x", pady=3)
            tk.Label(sf, text=titolo, bg=C_PANEL, fg=C_SUBTEXT,
                     font=("Segoe UI", 9, "bold")).pack(anchor="w")
            if voci:
                for v in voci:
                    tk.Label(sf, text=f"  • {v}", bg=C_PANEL, fg=C_TEXT,
                             font=("Segoe UI", 10)).pack(anchor="w")
            else:
                tk.Label(sf, text=f"  {vuoto}", bg=C_PANEL, fg=C_SUBTEXT,
                         font=("Segoe UI", 10, "italic")).pack(anchor="w")
            tk.Label(sf, text=f"  Totale: {format_euro(totale)}",
                     bg=C_PANEL, fg=col_tot,
                     font=("Segoe UI", 10, "bold")).pack(anchor="w", pady=(4, 0))

        sezione("🏠  Turni",
                r["dettagli_locali"],
                r["compensi_totali"],
                col_tot=C_ACCENT, vuoto="Nessun turno")

        sezione("💳  Pagamenti ricevuti",
                [f"{p['desc']}: {format_euro(p['importo'])}" for p in r["pagamenti"]],
                r["somma_pagato"],
                col_tot=C_GREEN, vuoto="Nessun pagamento")

        sezione("🤑  Mance",
                [f"{m['fonte']}: {format_euro(m['importo'])}" for m in r["mance"]],
                r["somma_mance"],
                col_tot=C_GREEN, vuoto="Nessuna mancia")

        sezione("🏆  Arbitraggi",
                [f"{a['desc']}: {format_euro(a['importo'])}" for a in r["arbitraggi"]],
                r["somma_arbitraggi"],
                col_tot=C_ORANGE, vuoto="Nessun arbitraggio")

        dr_col = C_RED if r["da_ricevere"] > 0 else C_GREEN
        df = tk.Frame(detail_frame, bg=C_BG)
        df.pack(fill="x", pady=(8, 0))
        tk.Label(df, text="Da ricevere: ", bg=C_BG, fg=C_SUBTEXT,
                 font=("Segoe UI", 12)).pack(side="left")
        tk.Label(df, text=format_euro(r["da_ricevere"]), bg=C_BG, fg=dr_col,
                 font=("Segoe UI", 14, "bold")).pack(side="left")

    combo.bind("<<ComboboxSelected>>", aggiorna_dettaglio)
    aggiorna_dettaglio()

    root.mainloop()

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

    # modalità testo: --mese o --export
    if args.mese or args.export:
        if avvisi:
            print("⚠️  AVVISI:", *avvisi, sep="\n   ")
        for r in risultati:
            sp = stato_pagamento(r["compensi_totali"], r["somma_pagato"])
            print(f"\n{r['mese']} [{r['stato']}] [{sp}]")
            print("-" * 50)
            for d in r["dettagli_locali"]:
                print(f"  {d}")
            print(f"  Compensi:   {format_euro(r['compensi_totali'])}")
            print(f"  Pagato:     {format_euro(r['somma_pagato'])}")
            print(f"  Mance:      {format_euro(r['somma_mance'])}")
            print(f"  Arbitraggi: {format_euro(r['somma_arbitraggi'])}")
            print(f"  Da ricevere:{format_euro(r['da_ricevere'])}")
        if args.export:
            esporta_csv(risultati)
        return

    # modalità default: finestra GUI
    mostra_gui(risultati, avvisi)


if __name__ == "__main__":
    main()
