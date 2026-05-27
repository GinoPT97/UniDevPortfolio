# Dashboard.py
# GUI unificata per Cameriere e Arbitraggio.
#
# Uso:
#   python Dashboard.py
#
# Importa i dati da Cameriere.py e Arbitraggio.py,
# entrambi devono trovarsi nella stessa cartella.

import tkinter as tk
from tkinter import ttk
import matplotlib
matplotlib.use("TkAgg")
import matplotlib.pyplot as plt
from matplotlib.axes import Axes
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import numpy as np

# ---------------------------------------------------------------------------
# Palette condivisa
# ---------------------------------------------------------------------------

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
FONT_UI   = "Segoe UI"

# ---------------------------------------------------------------------------
# Utility grafiche condivise
# ---------------------------------------------------------------------------

def style_ax(ax: Axes) -> None:
    ax.set_facecolor(C_BG)
    ax.tick_params(colors=C_SUBTEXT, labelsize=9)
    for sp in ax.spines.values():
        sp.set_edgecolor(C_GRID)
    ax.yaxis.grid(True, color=C_GRID, linewidth=0.6)
    ax.set_axisbelow(True)


def embed(fig: plt.Figure, parent: tk.Widget) -> None:
    canvas = FigureCanvasTkAgg(fig, master=parent)
    canvas.draw()
    canvas.get_tk_widget().pack(fill="both", expand=True, padx=12, pady=8)


def card(parent: tk.Widget, title: str, value: str,
         color: str = C_TEXT, col: int = 0, row: int = 0) -> None:
    f = tk.Frame(parent, bg=C_PANEL, padx=16, pady=12)
    f.grid(row=row, column=col, padx=8, pady=6, sticky="ew")
    tk.Label(f, text=title, bg=C_PANEL, fg=C_SUBTEXT,
             font=(FONT_UI, 9)).pack(anchor="w")
    tk.Label(f, text=value, bg=C_PANEL, fg=color,
             font=(FONT_UI, 16, "bold")).pack(anchor="w", pady=(2, 0))


def apply_treeview_style(style: ttk.Style) -> None:
    style.theme_use("default")
    style.configure("TNotebook",        background=C_BG,    borderwidth=0)
    style.configure("TNotebook.Tab",    background=C_PANEL, foreground=C_SUBTEXT,
                    padding=[12, 5],    font=(FONT_UI, 10))
    style.map("TNotebook.Tab",
              background=[("selected", C_ACCENT)],
              foreground=[("selected", "#ffffff")])
    style.configure("TFrame",           background=C_BG)
    style.configure("TLabel",           background=C_BG,    foreground=C_TEXT,
                    font=(FONT_UI, 10))
    style.configure("Treeview",         background=C_PANEL, foreground=C_TEXT,
                    fieldbackground=C_PANEL, rowheight=26,   font=(FONT_UI, 10))
    style.configure("Treeview.Heading", background=C_GRID,  foreground=C_SUBTEXT,
                    font=(FONT_UI, 10, "bold"))
    style.map("Treeview", background=[("selected", C_ACCENT)])


# ---------------------------------------------------------------------------
# Sezione Cameriere
# ---------------------------------------------------------------------------

def build_cameriere(notebook: ttk.Notebook, risultati: list[dict], avvisi: list[str]) -> None:
    from Cameriere import stato_pagamento, format_euro

    tab_root = ttk.Frame(notebook)
    notebook.add(tab_root, text="  🍽️  Cameriere  ")

    sub = ttk.Notebook(tab_root)
    sub.pack(fill="both", expand=True, padx=4, pady=4)

    COLOR_MAP = {
        "✓ Saldato":             C_GREEN,
        "⚠ Parzialmente pagato": C_YELLOW,
        "✗ Non pagato":          C_RED,
        "— nessun lavoro":       C_SUBTEXT,
    }

    # ---- TAB C1: Panoramica ----
    c1 = ttk.Frame(sub)
    sub.add(c1, text="  📊 Panoramica  ")

    tot_compensi   = sum(r["compensi_totali"]  for r in risultati)
    tot_pagato     = sum(r["somma_pagato"]      for r in risultati)
    tot_mance      = sum(r["somma_mance"]       for r in risultati)
    tot_arbitraggi = sum(r["somma_arbitraggi"]  for r in risultati)
    tot_ricevere   = sum(r["da_ricevere"]       for r in risultati)
    tot_entrate    = tot_compensi + tot_mance + tot_arbitraggi

    cf = tk.Frame(c1, bg=C_BG)
    cf.pack(fill="x", padx=8, pady=(10, 4))
    for i in range(5): cf.columnconfigure(i, weight=1)
    card(cf, "Entrate totali",     format_euro(tot_entrate),    C_CYAN,   col=0)
    card(cf, "Compensi cameriere", format_euro(tot_compensi),   C_ACCENT, col=1)
    card(cf, "Arbitraggi (cam.)",  format_euro(tot_arbitraggi), C_ORANGE, col=2)
    card(cf, "Mance",              format_euro(tot_mance),      C_GREEN,  col=3)
    card(cf, "Da ricevere",        format_euro(tot_ricevere),
         C_RED if tot_ricevere > 0 else C_GREEN, col=4)

    cols = ("Mese", "Stato", "Compensi", "Arbitraggi", "Mance", "Pagato", "Da ricevere", "Saldo")
    tree = ttk.Treeview(c1, columns=cols, show="headings", height=9)
    for c in cols:
        tree.heading(c, text=c)
        tree.column(c, anchor="center", width=105)
    tree.column("Mese", width=85)
    tree.column("Saldo", width=145)
    for tag, color in COLOR_MAP.items():
        tree.tag_configure(tag, foreground=color)
    for r in risultati:
        sp = stato_pagamento(r["compensi_totali"], r["somma_pagato"])
        tree.insert("", "end", values=(
            r["mese"].capitalize(), r["stato"],
            format_euro(r["compensi_totali"]),
            format_euro(r["somma_arbitraggi"]),
            format_euro(r["somma_mance"]),
            format_euro(r["somma_pagato"]),
            format_euro(r["da_ricevere"]), sp,
        ), tags=(sp,))
    sb = ttk.Scrollbar(c1, orient="vertical", command=tree.yview)
    tree.configure(yscrollcommand=sb.set)
    tree.pack(side="left", fill="both", expand=True, padx=(16, 0), pady=4)
    sb.pack(side="left", fill="y", pady=4)

    if avvisi:
        wf = tk.Frame(c1, bg="#3a2a2a", padx=10, pady=6)
        wf.pack(fill="x", padx=16, pady=4, side="bottom")
        tk.Label(wf, text="⚠️  Avvisi nei dati:", bg="#3a2a2a",
                 fg=C_YELLOW, font=(FONT_UI, 9, "bold")).pack(anchor="w")
        for a in avvisi:
            tk.Label(wf, text=f"  • {a}", bg="#3a2a2a",
                     fg=C_YELLOW, font=(FONT_UI, 9)).pack(anchor="w")

    # ---- TAB C2: Guadagni mensili ----
    c2 = ttk.Frame(sub)
    sub.add(c2, text="  📅 Guadagni mensili  ")

    xs = np.arange(len(risultati))
    w  = 0.22
    fig_c2, ax_c2 = plt.subplots(figsize=(10, 4.8), facecolor=C_BG)
    style_ax(ax_c2)
    ax_c2.bar(xs - w*1.5, [r["compensi_totali"]  for r in risultati], w, label="Compensi",   color=C_ACCENT, alpha=0.9)
    ax_c2.bar(xs - w*0.5, [r["somma_arbitraggi"] for r in risultati], w, label="Arbitraggi", color=C_ORANGE, alpha=0.9)
    ax_c2.bar(xs + w*0.5, [r["somma_mance"]      for r in risultati], w, label="Mance",      color=C_GREEN,  alpha=0.9)
    ax_c2.bar(xs + w*1.5, [r["somma_pagato"]     for r in risultati], w, label="Pagato",     color=C_CYAN,   alpha=0.9)
    ax_c2.set_xticks(xs)
    ax_c2.set_xticklabels([r["mese"].capitalize()[:3] for r in risultati], color=C_SUBTEXT)
    ax_c2.set_ylabel("€", color=C_SUBTEXT)
    ax_c2.set_title("Guadagni per mese", color=C_TEXT, pad=10)
    ax_c2.legend(facecolor=C_PANEL, edgecolor=C_GRID, labelcolor=C_TEXT, fontsize=9)
    for container in ax_c2.containers:
        for bar in container:
            h = bar.get_height()
            if h > 0:
                ax_c2.text(bar.get_x() + bar.get_width()/2, h + 2, f"{h:.0f}",
                           ha="center", va="bottom", fontsize=7.5, color=C_TEXT)
    fig_c2.tight_layout()
    embed(fig_c2, c2)

    # ---- TAB C3: Per locale ----
    c3 = ttk.Frame(sub)
    sub.add(c3, text="  🏢 Per locale  ")

    from Cameriere import LOCALI
    riepilogo_locali: dict[str, dict] = {}
    for r in risultati:
        for nome, d in r["per_locale"].items():
            if nome not in riepilogo_locali:
                riepilogo_locali[nome] = {"turni": 0, "compensi": 0.0}
            riepilogo_locali[nome]["turni"]    += d["turni"]
            riepilogo_locali[nome]["compensi"] += d["compensi"]
    locali_attivi = {k: v for k, v in riepilogo_locali.items() if v["turni"] > 0}

    if locali_attivi:
        palette3 = [C_ACCENT, C_ORANGE, C_CYAN, C_GREEN, C_YELLOW]
        nomi3  = list(locali_attivi.keys())
        turni3 = [locali_attivi[n]["turni"]    for n in nomi3]
        comps3 = [locali_attivi[n]["compensi"] for n in nomi3]
        cols3  = palette3[:len(nomi3)]
        xs3    = np.arange(len(nomi3))

        fig_c3, (ax3a, ax3b) = plt.subplots(1, 2, figsize=(10, 4.5), facecolor=C_BG)
        style_ax(ax3a); style_ax(ax3b)

        ax3a.bar(xs3, turni3, color=cols3, alpha=0.9, width=0.5)
        ax3a.set_xticks(xs3); ax3a.set_xticklabels(nomi3, color=C_SUBTEXT)
        ax3a.set_ylabel("Turni", color=C_SUBTEXT)
        ax3a.set_title("Turni per locale", color=C_TEXT, pad=8)
        for i, v in enumerate(turni3):
            ax3a.text(i, v + 0.1, str(v), ha="center", va="bottom",
                      color=C_TEXT, fontsize=10, fontweight="bold")

        ax3b.bar(xs3, comps3, color=cols3, alpha=0.9, width=0.5)
        ax3b.set_xticks(xs3); ax3b.set_xticklabels(nomi3, color=C_SUBTEXT)
        ax3b.set_ylabel("€", color=C_SUBTEXT)
        ax3b.set_title("Compensi per locale", color=C_TEXT, pad=8)
        for i, v in enumerate(comps3):
            ax3b.text(i, v + 1, f"{v:.0f}€", ha="center", va="bottom",
                      color=C_TEXT, fontsize=10, fontweight="bold")

        fig_c3.tight_layout()
        embed(fig_c3, c3)
    else:
        tk.Label(c3, text="Nessun turno registrato.", fg=C_SUBTEXT,
                 font=(FONT_UI, 12)).pack(expand=True)

    # ---- TAB C4: Pagamenti ----
    c4 = ttk.Frame(sub)
    sub.add(c4, text="  💰 Pagamenti  ")

    saldati    = sum(1 for r in risultati if r["compensi_totali"] > 0 and r["somma_pagato"] >= r["compensi_totali"])
    parziali   = sum(1 for r in risultati if 0 < r["somma_pagato"] < r["compensi_totali"])
    non_pagati = sum(1 for r in risultati if r["compensi_totali"] > 0 and r["somma_pagato"] <= 0)

    fig_c4, (ax4a, ax4b) = plt.subplots(1, 2, figsize=(10, 4.5), facecolor=C_BG)
    style_ax(ax4b)
    ax4a.set_facecolor(C_BG)

    slices = [(l, v, c) for l, v, c in [("Saldato", saldati, C_GREEN),
                                          ("Parziale", parziali, C_YELLOW),
                                          ("Non pagato", non_pagati, C_RED)] if v > 0]
    if slices:
        _, _, autotexts = ax4a.pie(
            [s[1] for s in slices], labels=[s[0] for s in slices],
            colors=[s[2] for s in slices], autopct="%1.0f%%", startangle=90,
            textprops={"color": C_TEXT, "fontsize": 10},
            wedgeprops={"linewidth": 2, "edgecolor": C_BG},
        )
        for at in autotexts: at.set_color(C_BG); at.set_fontsize(9)
    ax4a.set_title("Stato pagamenti", color=C_TEXT, pad=10)

    mr = [r for r in risultati if r["compensi_totali"] > 0]
    if mr:
        xm = np.arange(len(mr))
        ax4b.bar(xm - 0.2, [r["compensi_totali"] for r in mr], 0.4, label="Maturato", color=C_ACCENT, alpha=0.9)
        ax4b.bar(xm + 0.2, [r["somma_pagato"]    for r in mr], 0.4, label="Pagato",   color=C_GREEN,  alpha=0.9)
        ax4b.set_xticks(xm)
        ax4b.set_xticklabels([r["mese"].capitalize()[:3] for r in mr], color=C_SUBTEXT)
        ax4b.set_ylabel("€", color=C_SUBTEXT)
        ax4b.set_title("Maturato vs Pagato", color=C_TEXT, pad=8)
        ax4b.legend(facecolor=C_PANEL, edgecolor=C_GRID, labelcolor=C_TEXT, fontsize=9)
    fig_c4.tight_layout()
    embed(fig_c4, c4)

    # ---- TAB C5: Dettaglio ----
    c5 = ttk.Frame(sub)
    sub.add(c5, text="  🔍 Dettaglio  ")

    top5 = tk.Frame(c5, bg=C_BG)
    top5.pack(fill="x", padx=16, pady=(10, 4))
    tk.Label(top5, text="Seleziona mese:", fg=C_SUBTEXT,
             font=(FONT_UI, 10), bg=C_BG).pack(side="left")
    mese_var = tk.StringVar(value=risultati[0]["mese"])
    combo5 = ttk.Combobox(top5, textvariable=mese_var, state="readonly",
                          values=[r["mese"] for r in risultati], width=14)
    combo5.pack(side="left", padx=8)

    detail5 = tk.Frame(c5, bg=C_BG)
    detail5.pack(fill="both", expand=True, padx=16, pady=4)

    def aggiorna_cam(*_: object) -> None:
        for w in detail5.winfo_children(): w.destroy()
        r = next(x for x in risultati if x["mese"] == mese_var.get())
        sp = stato_pagamento(r["compensi_totali"], r["somma_pagato"])

        hf = tk.Frame(detail5, bg=C_BG)
        hf.pack(fill="x", pady=(0, 8))
        tk.Label(hf, text=f"{r['mese'].capitalize()} 2026",
                 bg=C_BG, fg=C_TEXT, font=(FONT_UI, 14, "bold")).pack(side="left")
        tk.Label(hf, text=f"  [{r['stato']}]  [{sp}]",
                 bg=C_BG, fg=COLOR_MAP.get(sp, C_TEXT), font=(FONT_UI, 10)).pack(side="left")

        def sezione(titolo: str, voci: list[str], totale: float,
                    col_tot: str = C_TEXT, vuoto: str = "Nessuno") -> None:
            sf = tk.Frame(detail5, bg=C_PANEL, padx=12, pady=8)
            sf.pack(fill="x", pady=3)
            tk.Label(sf, text=titolo, bg=C_PANEL, fg=C_SUBTEXT,
                     font=(FONT_UI, 9, "bold")).pack(anchor="w")
            for v in voci:
                tk.Label(sf, text=f"  • {v}", bg=C_PANEL, fg=C_TEXT,
                         font=(FONT_UI, 10)).pack(anchor="w")
            if not voci:
                tk.Label(sf, text=f"  {vuoto}", bg=C_PANEL, fg=C_SUBTEXT,
                         font=(FONT_UI, 10, "italic")).pack(anchor="w")
            tk.Label(sf, text=f"  Totale: {format_euro(totale)}",
                     bg=C_PANEL, fg=col_tot,
                     font=(FONT_UI, 10, "bold")).pack(anchor="w", pady=(4, 0))

        sezione("🏠  Turni",       r["dettagli_locali"],  r["compensi_totali"], C_ACCENT, "Nessun turno")
        sezione("💳  Pagamenti",   [f"{p['desc']}: {format_euro(p['importo'])}" for p in r["pagamenti"]],
                r["somma_pagato"],   C_GREEN,  "Nessun pagamento")
        sezione("🤑  Mance",       [f"{m['fonte']}: {format_euro(m['importo'])}" for m in r["mance"]],
                r["somma_mance"],    C_GREEN,  "Nessuna mancia")
        sezione("🏆  Arbitraggi",  [f"{a['desc']}: {format_euro(a['importo'])}" for a in r["arbitraggi"]],
                r["somma_arbitraggi"], C_ORANGE, "Nessun arbitraggio")

        dr_col = C_RED if r["da_ricevere"] > 0 else C_GREEN
        df = tk.Frame(detail5, bg=C_BG)
        df.pack(fill="x", pady=(8, 0))
        tk.Label(df, text="Da ricevere: ", bg=C_BG, fg=C_SUBTEXT,
                 font=(FONT_UI, 12)).pack(side="left")
        tk.Label(df, text=format_euro(r["da_ricevere"]), bg=C_BG, fg=dr_col,
                 font=(FONT_UI, 14, "bold")).pack(side="left")

    combo5.bind("<<ComboboxSelected>>", aggiorna_cam)
    aggiorna_cam()


# ---------------------------------------------------------------------------
# Sezione Arbitraggio
# ---------------------------------------------------------------------------

def build_arbitraggio(notebook: ttk.Notebook, risultati: list[dict]) -> None:
    PALETTE = [C_ACCENT, C_ORANGE]
    LBL_DA_RIC = "Da ricevere"

    tab_root = ttk.Frame(notebook)
    notebook.add(tab_root, text="  🏆  Arbitraggio  ")

    sub = ttk.Notebook(tab_root)
    sub.pack(fill="both", expand=True, padx=4, pady=4)

    tot_rimborsi = sum(r["tot_rimborso"] for r in risultati)
    tot_pagato   = sum(r["tot_pagato"]   for r in risultati)
    tot_ricevere = sum(r["da_ricevere"]  for r in risultati)
    tot_gare     = sum(r["n_gare"]       for r in risultati)

    # ---- TAB A1: Panoramica ----
    a1 = ttk.Frame(sub)
    sub.add(a1, text="  📊 Panoramica  ")

    cf = tk.Frame(a1, bg=C_BG)
    cf.pack(fill="x", padx=8, pady=(10, 4))
    for i in range(4): cf.columnconfigure(i, weight=1)
    card(cf, "Gare totali",       str(tot_gare),      C_CYAN,   col=0)
    card(cf, "Rimborsi maturati", f"€{tot_rimborsi}", C_ACCENT, col=1)
    card(cf, "Totale pagato",     f"€{tot_pagato}",   C_GREEN,  col=2)
    card(cf, LBL_DA_RIC,          f"€{tot_ricevere}",
         C_RED if tot_ricevere > 0 else C_GREEN, col=3)

    cols = ("Stagione", "Gare", "Rimborsi", "Pagamenti", "Totale pagato", LBL_DA_RIC, "Media/gara")
    tree = ttk.Treeview(a1, columns=cols, show="headings", height=4)
    for c in cols:
        tree.heading(c, text=c); tree.column(c, anchor="center", width=130)
    tree.column("Stagione", width=100)
    for r in risultati:
        tag = "rosso" if r["da_ricevere"] > 0 else "verde"
        tree.insert("", "end", values=(
            r["stagione"], r["n_gare"], f"€{r['tot_rimborso']}",
            r["n_pagamenti"], f"€{r['tot_pagato']}",
            f"€{r['da_ricevere']}", f"€{r['media_gara']:.1f}",
        ), tags=(tag,))
    tree.tag_configure("rosso", foreground=C_RED)
    tree.tag_configure("verde", foreground=C_GREEN)
    tree.pack(fill="x", padx=20, pady=4)

    sf = tk.Frame(a1, bg=C_BG)
    sf.pack(fill="x", padx=20, pady=(8, 4))
    for i, r in enumerate(risultati):
        pad_r = 0 if i == len(risultati) - 1 else 8
        pf = tk.Frame(sf, bg=C_PANEL, padx=14, pady=10)
        pf.pack(side="left", fill="x", expand=True, padx=(0, pad_r))
        tk.Label(pf, text=f"Stagione {r['stagione']}", bg=C_PANEL, fg=C_ACCENT,
                 font=(FONT_UI, 10, "bold")).pack(anchor="w")
        for label, val in [
            ("Gara più alta",  f"€{r['max_gara']}"),
            ("Gara più bassa", f"€{r['min_gara']}"),
            ("Media per gara", f"€{r['media_gara']:.1f}"),
            ("% pagata",       f"{r['tot_pagato']/r['tot_rimborso']*100:.1f}%" if r["tot_rimborso"] else "—"),
        ]:
            row = tk.Frame(pf, bg=C_PANEL); row.pack(fill="x", pady=1)
            tk.Label(row, text=label + ":", bg=C_PANEL, fg=C_SUBTEXT,
                     font=(FONT_UI, 9), width=16, anchor="w").pack(side="left")
            tk.Label(row, text=val, bg=C_PANEL, fg=C_TEXT,
                     font=(FONT_UI, 9, "bold")).pack(side="left")

    # ---- TAB A2: Maturato vs Pagato ----
    a2 = ttk.Frame(sub)
    sub.add(a2, text="  💳 Maturato vs Pagato  ")

    fig_a2, (ax2a, ax2b) = plt.subplots(1, 2, figsize=(10, 4.5), facecolor=C_BG)
    style_ax(ax2a); style_ax(ax2b)
    nomi = [r["stagione"] for r in risultati]
    xs2  = np.arange(len(nomi))
    ax2a.bar(xs2 - 0.2, [r["tot_rimborso"] for r in risultati], 0.38, label="Maturato", color=C_ACCENT, alpha=0.9)
    ax2a.bar(xs2 + 0.2, [r["tot_pagato"]   for r in risultati], 0.38, label="Pagato",   color=C_GREEN,  alpha=0.9)
    ax2a.set_xticks(xs2); ax2a.set_xticklabels(nomi, color=C_SUBTEXT)
    ax2a.set_ylabel("€", color=C_SUBTEXT)
    ax2a.set_title("Maturato vs Pagato per stagione", color=C_TEXT, pad=8)
    ax2a.legend(facecolor=C_PANEL, edgecolor=C_GRID, labelcolor=C_TEXT, fontsize=9)
    for bars in ax2a.containers:
        for bar in bars:
            h = bar.get_height()
            ax2a.text(bar.get_x() + bar.get_width()/2, h + 5, f"{h:.0f}",
                      ha="center", va="bottom", fontsize=8.5, color=C_TEXT)
    diffs = [r["da_ricevere"] for r in risultati]
    ax2b.bar(xs2, diffs, 0.45, color=[C_RED if d > 0 else C_GREEN for d in diffs], alpha=0.9)
    ax2b.set_xticks(xs2); ax2b.set_xticklabels(nomi, color=C_SUBTEXT)
    ax2b.set_ylabel("€", color=C_SUBTEXT)
    ax2b.set_title(LBL_DA_RIC + " per stagione", color=C_TEXT, pad=8)
    ax2b.axhline(0, color=C_GRID, linewidth=1)
    for i, d in enumerate(diffs):
        ax2b.text(i, d + 3, f"€{d}", ha="center", va="bottom",
                  fontsize=10, color=C_TEXT, fontweight="bold")
    fig_a2.tight_layout(); embed(fig_a2, a2)

    # ---- TAB A3: Distribuzione gare ----
    a3 = ttk.Frame(sub)
    sub.add(a3, text="  📈 Distribuzione gare  ")

    fig_a3, (ax3a, ax3b) = plt.subplots(1, 2, figsize=(10, 4.5), facecolor=C_BG)
    style_ax(ax3a); style_ax(ax3b)
    for ax, r, color in zip((ax3a, ax3b), risultati, PALETTE):
        vals   = r["rimborso"]
        unique = sorted(set(vals))
        counts = [vals.count(v) for v in unique]
        ax.bar([str(v) for v in unique], counts, color=color, alpha=0.88, width=0.6)
        ax.set_title(f"Distribuzione {r['stagione']}", color=C_TEXT, pad=8)
        ax.set_xlabel("€ per gara", color=C_SUBTEXT); ax.set_ylabel("N° gare", color=C_SUBTEXT)
        for j, cnt in enumerate(counts):
            ax.text(j, cnt + 0.1, str(cnt), ha="center", va="bottom", fontsize=8.5, color=C_TEXT)
    fig_a3.tight_layout(); embed(fig_a3, a3)

    # ---- TAB A4: Andamento ----
    a4 = ttk.Frame(sub)
    sub.add(a4, text="  📉 Andamento  ")

    fig_a4, ax4 = plt.subplots(figsize=(10, 4.5), facecolor=C_BG)
    style_ax(ax4)
    for r, color in zip(risultati, PALETTE):
        cum = r["cumulativo"]
        xs4 = range(1, len(cum) + 1)
        ax4.plot(xs4, cum, color=color, linewidth=2, label=r["stagione"])
        ax4.fill_between(xs4, cum, alpha=0.12, color=color)
        ax4.axhline(r["tot_pagato"], color=color, linewidth=1, linestyle="--", alpha=0.6)
        ax4.text(len(cum), r["tot_pagato"] + 4, f"Pagato {r['stagione']}",
                 color=color, fontsize=8, ha="right")
    ax4.set_xlabel("Gara n°", color=C_SUBTEXT); ax4.set_ylabel("€ cumulativi", color=C_SUBTEXT)
    ax4.set_title("Rimborsi cumulativi  (tratteggio = pagato)", color=C_TEXT, pad=8)
    ax4.legend(facecolor=C_PANEL, edgecolor=C_GRID, labelcolor=C_TEXT, fontsize=9)
    fig_a4.tight_layout(); embed(fig_a4, a4)

    # ---- TAB A5: Dettaglio stagione ----
    a5 = ttk.Frame(sub)
    sub.add(a5, text="  🔍 Dettaglio  ")

    top_a5 = tk.Frame(a5, bg=C_BG)
    top_a5.pack(fill="x", padx=16, pady=(10, 4))
    tk.Label(top_a5, text="Seleziona stagione:", fg=C_SUBTEXT,
             font=(FONT_UI, 10), bg=C_BG).pack(side="left")
    stag_var = tk.StringVar(value=risultati[0]["stagione"])
    combo_a5 = ttk.Combobox(top_a5, textvariable=stag_var, state="readonly",
                             values=[r["stagione"] for r in risultati], width=12)
    combo_a5.pack(side="left", padx=8)

    detail_a5 = tk.Frame(a5, bg=C_BG)
    detail_a5.pack(fill="both", expand=True, padx=16, pady=4)

    def aggiorna_arb(*_: object) -> None:
        for w in detail_a5.winfo_children(): w.destroy()
        r = next(x for x in risultati if x["stagione"] == stag_var.get())

        cf5 = tk.Frame(detail_a5, bg=C_BG)
        cf5.pack(fill="x", pady=(0, 8))
        for j in range(5): cf5.columnconfigure(j, weight=1)
        for j, (t, v, c) in enumerate([
            ("Gare",        str(r["n_gare"]),          C_CYAN),
            ("Rimborsi",    f"€{r['tot_rimborso']}",   C_ACCENT),
            ("Pagato",      f"€{r['tot_pagato']}",     C_GREEN),
            (LBL_DA_RIC,    f"€{r['da_ricevere']}",    C_RED if r["da_ricevere"] > 0 else C_GREEN),
            ("Media/gara",  f"€{r['media_gara']:.1f}", C_YELLOW),
        ]):
            card(cf5, t, v, c, col=j)

        lf = tk.Frame(detail_a5, bg=C_BG)
        lf.pack(fill="both", expand=True)
        for side_data, label_txt, fg in [
            (list(enumerate(r["rimborso"], 1)),  f"Rimborsi ({r['n_gare']} gare)",          C_TEXT),
            (list(enumerate(r["pagato"],   1)),  f"Pagamenti ricevuti ({r['n_pagamenti']})", C_GREEN),
        ]:
            pane = tk.Frame(lf, bg=C_BG)
            pane.pack(side="left", fill="both", expand=True, padx=(0, 6))
            tk.Label(pane, text=label_txt, bg=C_BG, fg=C_SUBTEXT,
                     font=(FONT_UI, 9, "bold")).pack(anchor="w")
            lb = tk.Listbox(pane, bg=C_PANEL, fg=fg, selectbackground=C_ACCENT,
                            font=(FONT_UI, 10), relief="flat", borderwidth=0)
            sb_lb = ttk.Scrollbar(pane, orient="vertical", command=lb.yview)
            lb.configure(yscrollcommand=sb_lb.set)
            lb.pack(side="left", fill="both", expand=True)
            sb_lb.pack(side="left", fill="y")
            prefix = "Gara" if fg == C_TEXT else "Pagamento"
            for idx, val in side_data:
                lb.insert("end", f"  {prefix} {idx:>3}:  €{val}")

    combo_a5.bind("<<ComboboxSelected>>", aggiorna_arb)
    aggiorna_arb()


# ---------------------------------------------------------------------------
# Riepilogo Generale (tab di primo livello)
# ---------------------------------------------------------------------------

def build_riepilogo(notebook: ttk.Notebook,
                    cam_risultati: list[dict],
                    arb_risultati: list[dict]) -> None:
    from Cameriere import format_euro

    tab = ttk.Frame(notebook)
    notebook.add(tab, text="  📈  Riepilogo Generale  ")

    # totali cameriere
    cam_comp  = sum(r["compensi_totali"]  for r in cam_risultati)
    cam_mance = sum(r["somma_mance"]      for r in cam_risultati)
    cam_arb   = sum(r["somma_arbitraggi"] for r in cam_risultati)
    cam_rec   = sum(r["da_ricevere"]      for r in cam_risultati)

    # totali arbitraggio stagioni
    arb_rim   = sum(r["tot_rimborso"] for r in arb_risultati)
    arb_pag   = sum(r["tot_pagato"]   for r in arb_risultati)
    arb_rec   = sum(r["da_ricevere"]  for r in arb_risultati)

    totale_entrate  = cam_comp + cam_mance + cam_arb + arb_rim
    totale_ricevere = cam_rec + arb_rec

    # cards generali
    cf = tk.Frame(tab, bg=C_BG)
    cf.pack(fill="x", padx=8, pady=(14, 6))
    for i in range(4): cf.columnconfigure(i, weight=1)
    card(cf, "Totale entrate",       format_euro(totale_entrate),  C_CYAN,   col=0)
    card(cf, "Cameriere (compensi)", format_euro(cam_comp),        C_ACCENT, col=1)
    card(cf, "Arbitraggio (rimborsi)", f"€{arb_rim}",              C_ORANGE, col=2)
    card(cf, "Da ricevere (totale)", format_euro(totale_ricevere),
         C_RED if totale_ricevere > 0 else C_GREEN, col=3)

    # grafico torta: composizione entrate
    categorie = []
    valori    = []
    colori    = []
    for label, val, col in [
        ("Compensi cameriere", cam_comp,  C_ACCENT),
        ("Mance",              cam_mance, C_GREEN),
        ("Arb. cameriere",     cam_arb,   C_YELLOW),
        ("Rimborsi arbitraggio", arb_rim, C_ORANGE),
    ]:
        if val > 0:
            categorie.append(label); valori.append(val); colori.append(col)

    fig_r, (ax_pie, ax_bar) = plt.subplots(1, 2, figsize=(10, 4.2), facecolor=C_BG)
    ax_pie.set_facecolor(C_BG)
    if valori:
        _, _, autotexts = ax_pie.pie(
            valori, labels=categorie, colors=colori,
            autopct="%1.1f%%", startangle=90,
            textprops={"color": C_TEXT, "fontsize": 9},
            wedgeprops={"linewidth": 2, "edgecolor": C_BG},
        )
        for at in autotexts: at.set_color(C_BG); at.set_fontsize(8)
    ax_pie.set_title("Composizione entrate totali", color=C_TEXT, pad=10)

    # grafico barre: da ricevere per fonte
    style_ax(ax_bar)
    fonti = ["Cameriere", "Arbitraggio"]
    da_ric = [cam_rec, arb_rec]
    bar_c  = [C_RED if v > 0 else C_GREEN for v in da_ric]
    ax_bar.bar(fonti, da_ric, color=bar_c, alpha=0.9, width=0.45)
    ax_bar.set_ylabel("€", color=C_SUBTEXT)
    ax_bar.set_title("Da ricevere per fonte", color=C_TEXT, pad=8)
    ax_bar.axhline(0, color=C_GRID, linewidth=1)
    for i, v in enumerate(da_ric):
        ax_bar.text(i, v + max(da_ric) * 0.02, format_euro(v),
                    ha="center", va="bottom", color=C_TEXT,
                    fontsize=11, fontweight="bold")
    ax_bar.set_xticklabels(fonti, color=C_SUBTEXT)

    fig_r.tight_layout()
    embed(fig_r, tab)


# ---------------------------------------------------------------------------
# Punto di ingresso
# ---------------------------------------------------------------------------

def avvia() -> None:
    import Cameriere
    import Arbitraggio

    # prepara dati cameriere
    Cameriere.aggiungi_mese_corrente_se_manca()
    Cameriere.sposta_mesi_completati()
    avvisi    = Cameriere.valida_dati()
    cam_ris   = [Cameriere.calcola_mese(m) for m in Cameriere.get_mesi()]

    # prepara dati arbitraggio
    arb_ris   = [Arbitraggio.calcola(s) for s in Arbitraggio.STAGIONI]

    root = tk.Tk()
    root.title("UniDevPortfolio — Dashboard Lavori")
    root.configure(bg=C_BG)
    root.geometry("1150x740")
    root.minsize(950, 620)

    apply_treeview_style(ttk.Style())

    notebook = ttk.Notebook(root)
    notebook.pack(fill="both", expand=True, padx=10, pady=10)

    build_riepilogo(notebook, cam_ris, arb_ris)
    build_cameriere(notebook, cam_ris, avvisi)
    build_arbitraggio(notebook, arb_ris)

    root.mainloop()


if __name__ == "__main__":
    avvia()