"""
=============================================================
  MARKET RISK ANALYSIS — Indicatori Macroeconomici Globali
  Dati: simulati con parametri realistici (World Bank)
  basati su dati reali 2015-2023

    Installazione:
        pip install pandas numpy matplotlib scipy scikit-learn requests

    Esecuzione:
        python MarketRiskAnalysis.py

  ► Per dati reali: usa fetch_worldbank() nel main()
    API: https://api.worldbank.org/v2/ (nessuna API key)
=============================================================
"""
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.colors as mcolors
import matplotlib.patches as mpatches
from pathlib import Path
import argparse
from scipy import stats
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler
from sklearn.decomposition import PCA
from sklearn.ensemble import IsolationForest
import warnings
warnings.filterwarnings("ignore")

REGION_EAST_ASIA = "East Asia"
REGION_LATIN_AMERICA = "Latin America"
REGION_SOUTH_ASIA = "South Asia"
REGION_SE_ASIA = "SE Asia"
REGION_MIDDLE_EAST = "Middle East"
LABEL_RISK_SCORE = "Risk Score"

# ══════════════════════════════════════════════════════════════
# COORDINATE GEOGRAFICHE — 50 paesi (centroidi)
# ══════════════════════════════════════════════════════════════
COUNTRIES = {
    "USA":{"lat":37.1,"lon":-95.7,"region":"North America","income":"High"},
    "CHN":{"lat":35.8,"lon":104.2,"region":REGION_EAST_ASIA,"income":"Upper-Mid"},
    "DEU":{"lat":51.2,"lon":10.5,"region":"Europe","income":"High"},
    "JPN":{"lat":36.2,"lon":138.3,"region":REGION_EAST_ASIA,"income":"High"},
    "GBR":{"lat":55.4,"lon":-3.4,"region":"Europe","income":"High"},
    "FRA":{"lat":46.2,"lon":2.2,"region":"Europe","income":"High"},
    "ITA":{"lat":41.9,"lon":12.6,"region":"Europe","income":"High"},
    "BRA":{"lat":-10.0,"lon":-53.0,"region":REGION_LATIN_AMERICA,"income":"Upper-Mid"},
    "IND":{"lat":20.6,"lon":79.1,"region":REGION_SOUTH_ASIA,"income":"Lower-Mid"},
    "CAN":{"lat":56.1,"lon":-106.3,"region":"North America","income":"High"},
    "KOR":{"lat":35.9,"lon":127.8,"region":REGION_EAST_ASIA,"income":"High"},
    "AUS":{"lat":-25.3,"lon":133.8,"region":"Oceania","income":"High"},
    "MEX":{"lat":23.6,"lon":-102.6,"region":REGION_LATIN_AMERICA,"income":"Upper-Mid"},
    "IDN":{"lat":-2.5,"lon":118.0,"region":REGION_SE_ASIA,"income":"Lower-Mid"},
    "TUR":{"lat":38.9,"lon":35.2,"region":"Europe/ME","income":"Upper-Mid"},
    "SAU":{"lat":23.9,"lon":45.1,"region":REGION_MIDDLE_EAST,"income":"High"},
    "NLD":{"lat":52.1,"lon":5.3,"region":"Europe","income":"High"},
    "CHE":{"lat":46.8,"lon":8.2,"region":"Europe","income":"High"},
    "ARG":{"lat":-34.0,"lon":-64.0,"region":REGION_LATIN_AMERICA,"income":"Upper-Mid"},
    "POL":{"lat":51.9,"lon":19.1,"region":"Europe","income":"High"},
    "SWE":{"lat":60.1,"lon":18.6,"region":"Europe","income":"High"},
    "BEL":{"lat":50.5,"lon":4.5,"region":"Europe","income":"High"},
    "NOR":{"lat":60.5,"lon":8.5,"region":"Europe","income":"High"},
    "AUT":{"lat":47.5,"lon":14.6,"region":"Europe","income":"High"},
    "ARE":{"lat":23.4,"lon":53.8,"region":REGION_MIDDLE_EAST,"income":"High"},
    "ZAF":{"lat":-29.0,"lon":25.1,"region":"Africa","income":"Upper-Mid"},
    "NGA":{"lat":8.6,"lon":8.1,"region":"Africa","income":"Lower-Mid"},
    "EGY":{"lat":26.8,"lon":30.8,"region":"Africa/ME","income":"Lower-Mid"},
    "COL":{"lat":4.6,"lon":-74.3,"region":REGION_LATIN_AMERICA,"income":"Upper-Mid"},
    "CHL":{"lat":-35.7,"lon":-71.5,"region":REGION_LATIN_AMERICA,"income":"High"},
    "PHL":{"lat":12.9,"lon":121.8,"region":REGION_SE_ASIA,"income":"Lower-Mid"},
    "MYS":{"lat":4.2,"lon":108.0,"region":REGION_SE_ASIA,"income":"Upper-Mid"},
    "THA":{"lat":15.9,"lon":100.9,"region":REGION_SE_ASIA,"income":"Upper-Mid"},
    "VNM":{"lat":14.1,"lon":108.3,"region":REGION_SE_ASIA,"income":"Lower-Mid"},
    "BGD":{"lat":23.7,"lon":90.4,"region":REGION_SOUTH_ASIA,"income":"Lower-Mid"},
    "PAK":{"lat":30.4,"lon":69.3,"region":REGION_SOUTH_ASIA,"income":"Lower-Mid"},
    "IRN":{"lat":32.4,"lon":53.7,"region":REGION_MIDDLE_EAST,"income":"Lower-Mid"},
    "IRQ":{"lat":33.2,"lon":43.7,"region":REGION_MIDDLE_EAST,"income":"Upper-Mid"},
    "KAZ":{"lat":47.2,"lon":67.0,"region":"Central Asia","income":"Upper-Mid"},
    "UKR":{"lat":49.0,"lon":31.4,"region":"Europe","income":"Lower-Mid"},
    "ROM":{"lat":45.9,"lon":25.0,"region":"Europe","income":"Upper-Mid"},
    "CZE":{"lat":49.8,"lon":15.5,"region":"Europe","income":"High"},
    "HUN":{"lat":47.2,"lon":19.5,"region":"Europe","income":"High"},
    "GRC":{"lat":39.1,"lon":21.8,"region":"Europe","income":"High"},
    "PRT":{"lat":39.4,"lon":-8.2,"region":"Europe","income":"High"},
    "DNK":{"lat":56.3,"lon":9.5,"region":"Europe","income":"High"},
    "FIN":{"lat":61.9,"lon":25.7,"region":"Europe","income":"High"},
    "NZL":{"lat":-40.9,"lon":174.9,"region":"Oceania","income":"High"},
    "PER":{"lat":-9.2,"lon":-75.0,"region":REGION_LATIN_AMERICA,"income":"Upper-Mid"},
    "ETH":{"lat":9.1,"lon":40.5,"region":"Africa","income":"Low"},
}

YEARS = list(range(2015, 2024))

# ══════════════════════════════════════════════════════════════
# 1. GENERAZIONE DATI REALISTICI
# ══════════════════════════════════════════════════════════════
def _apply_year_shocks(rng, year, gdp, inf, uem):
    if year == 2020:
        shock = rng.uniform(6, 14)
        gdp -= shock
        uem += rng.uniform(1, 4)
        inf -= rng.uniform(0, 1.5)
    elif year == 2021:
        gdp += rng.uniform(2, 6)
    elif year == 2022:
        inf += rng.uniform(2, 8)
    return gdp, inf, uem


def _apply_country_adjustments(rng, iso, year, gdp, inf):
    if iso == "ARG":
        inf += rng.uniform(20, 40) if year >= 2022 else rng.uniform(5, 15)
    elif iso == "TUR":
        inf += rng.uniform(15, 50) if year >= 2021 else rng.uniform(5, 10)
    elif iso == "VNM":
        gdp += rng.uniform(1, 3)
    elif iso == "CHN":
        gdp += rng.uniform(0.5, 2)
    return gdp, inf


def generate_data(seed=42):
    """
    Simula indicatori macroeconomici per 50 paesi (2015–2023).
    Parametri calibrati su dati World Bank reali.

    Per dati reali, usa fetch_worldbank() nel main().
    """
    rng = np.random.default_rng(seed)
    print("[+] Generazione dati macroeconomici (parametri World Bank 2015–2023)...")

    # Profili per gruppo di reddito
    profiles = {
        "High": {"gdp_mu": 1.8, "gdp_sig": 1.8, "inf_mu": 1.8, "inf_sig": 1.2, "uem_mu": 5.5, "uem_sig": 2.5},
        "Upper-Mid": {"gdp_mu": 3.5, "gdp_sig": 2.5, "inf_mu": 5.0, "inf_sig": 3.5, "uem_mu": 7.5, "uem_sig": 3.5},
        "Lower-Mid": {"gdp_mu": 5.0, "gdp_sig": 3.0, "inf_mu": 8.5, "inf_sig": 5.0, "uem_mu": 6.0, "uem_sig": 3.0},
        "Low": {"gdp_mu": 6.5, "gdp_sig": 3.5, "inf_mu": 12.0, "inf_sig": 6.0, "uem_mu": 5.0, "uem_sig": 2.5},
    }

    records = []
    for iso, meta in COUNTRIES.items():
        p = profiles[meta["income"]]
        for yr in YEARS:
            gdp = rng.normal(p["gdp_mu"], p["gdp_sig"])
            inf = max(rng.normal(p["inf_mu"], p["inf_sig"]), 0.1)
            uem = max(rng.normal(p["uem_mu"], p["uem_sig"]), 0.5)

            gdp, inf, uem = _apply_year_shocks(rng, yr, gdp, inf, uem)
            gdp, inf = _apply_country_adjustments(rng, iso, yr, gdp, inf)

            records.append({
                "iso": iso, "year": yr,
                "country": iso,
                "region": meta["region"],
                "income": meta["income"],
                "lat": meta["lat"], "lon": meta["lon"],
                "gdp_growth": round(gdp, 2),
                "inflation":  round(inf, 2),
                "unemployment": round(uem, 2),
            })

    df = pd.DataFrame(records)
    print(f"    → {len(df):,} osservazioni ({len(COUNTRIES)} paesi × {len(YEARS)} anni)\n")
    return df


# ══════════════════════════════════════════════════════════════
# 1b. FETCH DATI REALI — World Bank API
# ══════════════════════════════════════════════════════════════
def fetch_worldbank(indicator="NY.GDP.MKTP.KD.ZG", start=2015, end=2023):
    """
    Scarica un indicatore World Bank per tutti i paesi.
    Indicatori utili:
      NY.GDP.MKTP.KD.ZG  = PIL crescita annua (%)
      FP.CPI.TOTL.ZG     = Inflazione CPI (%)
      SL.UEM.TOTL.ZS     = Disoccupazione (% forza lavoro)
    """
    import requests
    url = (f"https://api.worldbank.org/v2/country/all/indicator/{indicator}"
           f"?format=json&date={start}:{end}&per_page=10000")
    r = requests.get(url, timeout=60); r.raise_for_status()
    data = r.json()[1]
    rows = [{"iso": d["countryiso3code"], "year": int(d["date"]),
             "value": d["value"]} for d in data if d["value"] is not None]
    return pd.DataFrame(rows)


# ══════════════════════════════════════════════════════════════
# 2. EDA
# ══════════════════════════════════════════════════════════════
def eda(df):
    print("═"*58)
    print("  EDA — Statistiche descrittive")
    print("═"*58)
    cols = ["gdp_growth","inflation","unemployment"]
    print(df[cols].describe().round(2).to_string())
    print(f"\n  Paesi:    {df['iso'].nunique()}")
    print(f"  Periodo:  {df['year'].min()} – {df['year'].max()}")
    print(f"\n  GDP Growth negativo (recessione): {(df['gdp_growth']<0).sum()} casi "
          f"({(df['gdp_growth']<0).mean()*100:.1f}%)")
    print(f"  Inflazione >10%:                  {(df['inflation']>10).sum()} casi "
          f"({(df['inflation']>10).mean()*100:.1f}%)")
    return df


# ══════════════════════════════════════════════════════════════
# 3. VOLATILITÀ GDP (proxy del rischio di mercato)
# ══════════════════════════════════════════════════════════════
def gdp_volatility(df, ax):
    vol = (df.groupby("iso")["gdp_growth"]
             .std()
             .reset_index(name="volatility")
             .merge(df[["iso","income","region"]].drop_duplicates(), on="iso"))
    vol = vol.sort_values("volatility", ascending=False)

    colors_map = {"High":"#2980b9","Upper-Mid":"#27ae60","Lower-Mid":"#e67e22","Low":"#e74c3c"}
    cols = [colors_map.get(i,"#95a5a6") for i in vol["income"]]
    ax.barh(vol["iso"].iloc[:20], vol["volatility"].iloc[:20], color=cols[:20], edgecolor="white", lw=0.4)
    ax.set(xlabel="Std Dev PIL (%)", title="Top 20 Paesi per Volatilità PIL\n(Rischio di Mercato)")
    ax.grid(True, axis="x", alpha=0.3)
    patches = [mpatches.Patch(color=v,label=k) for k,v in colors_map.items()]
    ax.legend(handles=patches, fontsize=7, loc="lower right")

    print("\n[Volatilità PIL — Top 10]")
    for _,r in vol.head(10).iterrows():
        print(f"  {r['iso']:>4}  σ={r['volatility']:.2f}%  ({r['income']})")
    return vol


# ══════════════════════════════════════════════════════════════
# 4. CORRELAZIONE MACRO — Heatmap
# ══════════════════════════════════════════════════════════════
def correlation_heatmap(df, ax):
    corr = df[["gdp_growth","inflation","unemployment"]].corr()
    labels = ["GDP Growth","Inflazione","Disoccupazione"]
    im = ax.imshow(corr.values, cmap="RdYlGn", vmin=-1, vmax=1, aspect="auto")
    ax.set_xticks(range(3)); ax.set_xticklabels(labels, fontsize=8, rotation=20)
    ax.set_yticks(range(3)); ax.set_yticklabels(labels, fontsize=8)
    ax.set_title("Correlazione Indicatori Macro", fontweight="bold")
    plt.colorbar(im, ax=ax)
    for i in range(3):
        for j in range(3):
            ax.text(j, i, f"{corr.values[i,j]:.2f}", ha="center", va="center",
                    fontsize=11, fontweight="bold",
                    color="white" if abs(corr.values[i,j]) > 0.5 else "black")
    print(f"\n[Correlazioni]\n{corr.round(3).to_string()}")


# ══════════════════════════════════════════════════════════════
# 5. VALUE AT RISK sul PIL (GDP Growth)
# ══════════════════════════════════════════════════════════════
def compute_var(df, ax):
    data = df["gdp_growth"].values
    ax.hist(data, bins=50, density=True, color="#3498db", alpha=0.6,
            edgecolor="white", lw=0.2, label="Distribuzione")
    # Fit normale
    mu, sigma = data.mean(), data.std()
    x = np.linspace(data.min(), data.max(), 300)
    ax.plot(x, stats.norm.pdf(x, mu, sigma), color="#2c3e50", lw=2, label=f"Normale(μ={mu:.1f}, σ={sigma:.1f})")

    print("\n[VaR sul PIL — Rischio di Crescita Negativa]")
    print(f"  {'Conf':>6}  {'VaR (%)':>8}  {'CVaR (%)':>10}  Interpretazione")
    interp = {90:"1 anno su 10 sotto questa soglia",
              95:"1 anno su 20",
              99:"1 anno su 100 (tail risk)"}
    for alpha, col in [(0.90,"#f39c12"),(0.95,"#e67e22"),(0.99,"#e74c3c")]:
        # VaR downside: worst α% dei casi
        var  = np.percentile(data, (1-alpha)*100)
        cvar = data[data <= var].mean()
        ax.axvline(var, color=col, lw=2, label=f"VaR {int(alpha*100)}% = {var:.1f}%")
        ax.axvspan(data.min(), var, alpha=0.07, color=col)
        print(f"  {int(alpha*100):>5}%  {var:>8.2f}%  {cvar:>10.2f}%  {interp[int(alpha*100)]}")

    ax.set(xlabel="PIL Crescita (%)", ylabel="Densità", title="Distribuzione PIL & VaR Downside")
    ax.legend(fontsize=7); ax.grid(True, alpha=0.3)
    ax.axvline(0, color="black", lw=1.2, ls="--", alpha=0.5)


# ══════════════════════════════════════════════════════════════
# 6. RISK MATRIX — Rischio Crescita × Rischio Inflazione
# ══════════════════════════════════════════════════════════════
def risk_matrix(df, ax):
    """
    Likelihood = frequenza di crescita negativa per cluster
    Severity   = livello medio di inflazione
    """
    latest = df[df["year"] == df["year"].max()].copy()
    # Buckets inflazione (severity 1-5)
    latest["inf_bucket"] = pd.cut(latest["inflation"],
        bins=[-np.inf, 2, 5, 10, 20, np.inf],
        labels=[1, 2, 3, 4, 5]).astype(int)
    # Buckets GDP (likelihood di rischio: più bassa la crescita, più alto il rischio)
    latest["gdp_bucket"] = pd.cut(latest["gdp_growth"],
        bins=[-np.inf, 0, 1.5, 3, 5, np.inf],
        labels=[5, 4, 3, 2, 1]).astype(int)

    mat = np.zeros((5,5))
    for _, r in latest.iterrows():
        i = 5 - r["gdp_bucket"]
        j = r["inf_bucket"] - 1
        mat[i, j] += 1

    mat_norm = mat / mat.sum() * 25  # normalizza per colori

    cmap = mcolors.LinearSegmentedColormap.from_list(
        "r", ["#2ecc71","#f1c40f","#e67e22","#e74c3c","#8e1a1a"])
    im = ax.imshow(mat_norm, cmap=cmap, vmin=0, vmax=8, aspect="auto")
    ax.set_xticks(range(5))
    ax.set_xticklabels(["<2%","2-5%","5-10%","10-20%",">20%"], fontsize=8)
    ax.set_yticks(range(5))
    ax.set_yticklabels(["Recessione\n(<0%)","Stag.\n(0-1.5%)","Bassa\n(1.5-3%)","Media\n(3-5%)","Alta\n(>5%)"],
                       fontsize=7)
    ax.set(xlabel="Inflazione →", ylabel="← Crescita PIL", title="Risk Matrix\nCrescita vs Inflazione")
    plt.colorbar(im, ax=ax, label="N paesi")
    for i in range(5):
        for j in range(5):
            v = int(mat[i,j])
            if v > 0:
                ax.text(j, i, str(v), ha="center", va="center",
                        fontweight="bold", fontsize=9,
                        color="white" if mat_norm[i,j]>5 else "black")

    print(f"\n[Risk Matrix {df['year'].max()} — distribuzione paesi]")
    n_rec = (latest["gdp_growth"] < 0).sum()
    n_hif = (latest["inflation"] > 10).sum()
    n_stag = ((latest["gdp_growth"] < 2) & (latest["inflation"] > 5)).sum()
    print(f"  Paesi in recessione (PIL<0):       {n_rec}")
    print(f"  Paesi alta inflazione (>10%):      {n_hif}")
    print(f"  Stagflazione (PIL<2% & Inf>5%):    {n_stag}")


# ══════════════════════════════════════════════════════════════
# 7. TREND TEMPORALE PIL per gruppo di reddito
# ══════════════════════════════════════════════════════════════
def gdp_trend(df, ax):
    trend = df.groupby(["year","income"])["gdp_growth"].mean().reset_index()
    colors = {"High":"#2980b9","Upper-Mid":"#27ae60","Lower-Mid":"#e67e22","Low":"#e74c3c"}
    for inc, grp in trend.groupby("income"):
        ax.plot(grp["year"], grp["gdp_growth"], marker="o", ms=5,
                lw=2, color=colors.get(inc,"#95a5a6"), label=inc)
    ax.axhline(0, color="black", lw=1, ls="--", alpha=0.4)
    ax.axvspan(2019.5, 2020.5, alpha=0.12, color="#e74c3c", label="COVID-19 shock")
    ax.set(xlabel="Anno", ylabel="PIL Crescita media (%)", title="Trend PIL per Gruppo di Reddito")
    ax.legend(fontsize=8); ax.grid(True, alpha=0.3)


# ══════════════════════════════════════════════════════════════
# 8. K-MEANS CLUSTERING + MAPPA GEOGRAFICA
# ══════════════════════════════════════════════════════════════
def geo_risk_map(df, ax, k=5):
    latest = df[df["year"] == df["year"].max()].copy()
    feats = ["gdp_growth","inflation","unemployment"]
    X = latest[feats].fillna(latest[feats].mean())
    scaled_features = StandardScaler().fit_transform(X)
    km = KMeans(n_clusters=k, random_state=42, n_init=15)
    latest["cluster"] = km.fit_predict(scaled_features)

    # Risk score per cluster: bassa crescita + alta inflazione + alta disoccupazione = rischio alto
    cs = latest.groupby("cluster").agg(
        n=("iso","count"),
        gdp=("gdp_growth","mean"),
        inf=("inflation","mean"),
        uem=("unemployment","mean")
    ).reset_index()
    # Score: normalizzato — crescita negativa aumenta rischio
    cs["rs"] = (
        (cs["inf"] / cs["inf"].max()) * 0.4 +
        ((cs["uem"] / cs["uem"].max()) * 0.3) +
        ((cs["gdp"].max() - cs["gdp"]) / (cs["gdp"].max() - cs["gdp"].min() + 1e-6)) * 0.3
    ).round(3)

    latest = latest.merge(cs[["cluster","rs"]], on="cluster")

    sc = ax.scatter(latest["lon"], latest["lat"],
                    c=latest["rs"], cmap="RdYlGn_r",
                    s=latest["inflation"].clip(1,40)*8,
                    alpha=0.75, linewidths=0.5, edgecolors="white",
                    vmin=0, vmax=1)
    plt.colorbar(sc, ax=ax, label=LABEL_RISK_SCORE, shrink=0.8)

    # Label paesi ad alto rischio
    top_risk = latest.nlargest(8,"rs")
    for _, r in top_risk.iterrows():
        ax.annotate(r["iso"], (r["lon"], r["lat"]),
                    fontsize=6.5, fontweight="bold", color="#fff",
                    xytext=(3,3), textcoords="offset points")

    ax.set_facecolor("#0d1117")
    ax.set(xlabel="Longitudine", ylabel="Latitudine",
           title=f"Mappa Rischio di Mercato (K={k} cluster)\n"
                 f"Dimensione bolla = inflazione  |  Colore = {LABEL_RISK_SCORE}")
    for sp in ax.spines.values(): sp.set_color("#444")
    ax.tick_params(colors="#ccc")
    ax.xaxis.label.set_color("#ccc"); ax.yaxis.label.set_color("#ccc")
    ax.title.set_color("#eee"); ax.grid(True, alpha=0.12, color="#555")
    ax.set_xlim(-180, 180); ax.set_ylim(-60, 80)

    print(f"\n[Geo Clustering — K={k} profili di rischio]")
    print(f"  {'Cluster':>8}  {'N':>4}  {'GDP%':>6}  {'Inf%':>6}  {'Uem%':>6}  {'Score':>7}  Profilo")
    for _,r in cs.sort_values("rs",ascending=False).iterrows():
        bar = "█" * int(r["rs"]*20)
        print(f"  {int(r['cluster']):>8}  {r['n']:>4}  {r['gdp']:>6.1f}  {r['inf']:>6.1f}"
              f"  {r['uem']:>6.1f}  {r['rs']:>7.3f}  {bar}")


# ══════════════════════════════════════════════════════════════
# 9. COMPOSITE RISK SCORE + ANALISI AVANZATE
# ══════════════════════════════════════════════════════════════
def add_composite_risk_score(df):
    out = df.copy()
    out["risk_score"] = (
        (-out["gdp_growth"]).clip(lower=0) * 0.4 +
        out["inflation"].clip(upper=50) / 50 * 0.35 +
        out["unemployment"].clip(upper=40) / 40 * 0.25
    ).round(3)
    return out


def regional_risk_heatmap(df, ax):
    risk = add_composite_risk_score(df)
    top_regions = risk["region"].value_counts().head(10).index
    risk = risk[risk["region"].isin(top_regions)]
    pv = risk.pivot_table(index="region", columns="year", values="risk_score", aggfunc="mean")
    im = ax.imshow(pv.values, cmap="YlOrRd", aspect="auto")
    ax.set_title("Risk Score Medio per Regione/Anno", fontweight="bold")
    ax.set_xticks(range(len(pv.columns))); ax.set_xticklabels(pv.columns, fontsize=8, rotation=25)
    ax.set_yticks(range(len(pv.index))); ax.set_yticklabels(pv.index, fontsize=7)
    plt.colorbar(im, ax=ax, fraction=0.046, pad=0.04)


def income_risk_boxplot(df, ax):
    latest = add_composite_risk_score(df)
    latest = latest[latest["year"] == latest["year"].max()]
    order = ["High", "Upper-Mid", "Lower-Mid", "Low"]
    vals = [latest.loc[latest["income"] == k, "risk_score"].values for k in order if k in latest["income"].values]
    labels = [k for k in order if k in latest["income"].values]
    bp = ax.boxplot(vals, labels=labels, patch_artist=True)
    palette = ["#2980b9", "#27ae60", "#e67e22", "#e74c3c"]
    for i, patch in enumerate(bp["boxes"]):
        patch.set_facecolor(palette[i % len(palette)])
        patch.set_alpha(0.55)
    ax.set_title(f"Distribuzione Risk Score per Income Group ({latest['year'].max()})", fontweight="bold")
    ax.set_ylabel("Risk Score")
    ax.grid(True, axis="y", alpha=0.3)


def monte_carlo_global_risk(df, ax, n_sims=8000):
    X = df[["gdp_growth", "inflation", "unemployment"]].dropna().values
    mu = X.mean(axis=0)
    cov = np.cov(X, rowvar=False)
    sims = np.random.default_rng(42).multivariate_normal(mu, cov, size=n_sims)

    gdp_sim = sims[:, 0]
    inf_sim = np.clip(sims[:, 1], 0, 80)
    uem_sim = np.clip(sims[:, 2], 0, 60)
    risk_sim = ((-gdp_sim).clip(min=0) * 0.4 + (inf_sim / 50) * 0.35 + (uem_sim / 40) * 0.25)

    ax.hist(risk_sim, bins=45, color="#8e44ad", alpha=0.65, edgecolor="white", lw=0.3)
    q95 = np.percentile(risk_sim, 95)
    q99 = np.percentile(risk_sim, 99)
    ax.axvline(q95, color="#e67e22", lw=2, label=f"P95={q95:.2f}")
    ax.axvline(q99, color="#e74c3c", lw=2, label=f"P99={q99:.2f}")
    ax.set_title("Monte Carlo — Distribuzione Global Risk Score", fontweight="bold")
    ax.set_xlabel("Risk Score simulato")
    ax.set_ylabel("Frequenza")
    ax.legend(fontsize=8)
    ax.grid(True, alpha=0.3)

    print("\n[Monte Carlo Global Risk]")
    print(f"  Media: {risk_sim.mean():.3f}  |  P95: {q95:.3f}  |  P99: {q99:.3f}")


def pca_anomaly_diagnostics(df, ax):
    latest = df[df["year"] == df["year"].max()].copy()
    X = latest[["gdp_growth", "inflation", "unemployment"]].ffill().bfill()
    scaled_features = StandardScaler().fit_transform(X)

    pca = PCA(n_components=2, random_state=42)
    z = pca.fit_transform(scaled_features)

    iso = IsolationForest(contamination=0.14, random_state=42)
    pred = iso.fit_predict(scaled_features)
    latest["is_anomaly"] = pred == -1

    colors = np.where(latest["is_anomaly"], "#e74c3c", "#3498db")
    ax.scatter(z[:, 0], z[:, 1], c=colors, alpha=0.8, s=45, edgecolors="white", linewidths=0.4)
    ax.set_title("PCA + Anomaly Detection (ultimo anno)", fontweight="bold")
    ax.set_xlabel(f"PC1 ({pca.explained_variance_ratio_[0]*100:.1f}%)")
    ax.set_ylabel(f"PC2 ({pca.explained_variance_ratio_[1]*100:.1f}%)")
    ax.grid(True, alpha=0.25)

    outliers = latest[latest["is_anomaly"]].nlargest(8, "inflation")
    for _, r in outliers.iterrows():
        idx = latest.index.get_loc(r.name)
        ax.annotate(r["iso"], (z[idx, 0], z[idx, 1]), fontsize=7,
                    xytext=(3, 3), textcoords="offset points")

    print("\n[PCA + Anomaly Detection]")
    print(f"  Varianza spiegata PC1+PC2: {(pca.explained_variance_ratio_.sum()*100):.2f}%")
    print("  Paesi anomali (top per inflazione):")
    for _, r in outliers.iterrows():
        print(f"    {r['iso']}: PIL {r['gdp_growth']:+.1f}% | Inf {r['inflation']:.1f}% | Uem {r['unemployment']:.1f}%")


def stress_test_summary(df):
    latest = add_composite_risk_score(df)
    latest = latest[latest["year"] == latest["year"].max()].copy()

    scenarios = {
        "Base": {"gdp": 0.0, "inf": 0.0, "uem": 0.0},
        "Recessione": {"gdp": -2.5, "inf": 1.0, "uem": 1.5},
        "Inflazione": {"gdp": -0.5, "inf": 5.0, "uem": 0.5},
        "Shock Severo": {"gdp": -4.0, "inf": 7.0, "uem": 2.5},
    }

    print("\n[Stress Test — Risk Score medio per scenario]")
    print(f"  {'Scenario':<14} {'Score Medio':>11}  {'Top 3 paesi più rischiosi':>30}")
    for name, s in scenarios.items():
        tmp = latest.copy()
        tmp["gdp_growth"] += s["gdp"]
        tmp["inflation"] = (tmp["inflation"] + s["inf"]).clip(lower=0)
        tmp["unemployment"] = (tmp["unemployment"] + s["uem"]).clip(lower=0)
        tmp = add_composite_risk_score(tmp)
        top3 = ", ".join(tmp.nlargest(3, "risk_score")["iso"].tolist())
        print(f"  {name:<14} {tmp['risk_score'].mean():>11.3f}  {top3:>30}")


def risk_change_analysis(df):
    r = add_composite_risk_score(df)
    first_y, last_y = r["year"].min(), r["year"].max()
    first = r[r["year"] == first_y][["iso", "risk_score"]].rename(columns={"risk_score": "risk_first"})
    last = r[r["year"] == last_y][["iso", "risk_score"]].rename(columns={"risk_score": "risk_last"})
    d = first.merge(last, on="iso", how="inner")
    d["delta"] = (d["risk_last"] - d["risk_first"]).round(3)

    print(f"\n[Variazione rischio {first_y} → {last_y}]")
    print("  Peggioramenti top 5:")
    for _, row in d.sort_values("delta", ascending=False).head(5).iterrows():
        print(f"    {row['iso']}: +{row['delta']:.3f}")
    print("  Miglioramenti top 5:")
    for _, row in d.sort_values("delta", ascending=True).head(5).iterrows():
        print(f"    {row['iso']}: {row['delta']:.3f}")


def _load_real_or_simulated_data(use_real_data):
    if use_real_data:
        try:
            print("[+] Fetch World Bank API...")
            gdp = fetch_worldbank("NY.GDP.MKTP.KD.ZG")
            inf = fetch_worldbank("FP.CPI.TOTL.ZG")
            uem = fetch_worldbank("SL.UEM.TOTL.ZS")
            df = (gdp.rename(columns={"value":"gdp_growth"})
                     .merge(inf.rename(columns={"value":"inflation"}), on=["iso","year"])
                     .merge(uem.rename(columns={"value":"unemployment"}), on=["iso","year"]))
            # Aggiungi metadati geografici
            meta_df = pd.DataFrame([
                {"iso":k,"lat":v["lat"],"lon":v["lon"],"region":v["region"],"income":v["income"]}
                for k,v in COUNTRIES.items()
            ])
            df = df.merge(meta_df, on="iso", how="left").dropna(subset=["lat"])
        except Exception as e:
            print(f"[!] World Bank non raggiungibile ({e}), uso dati simulati.")
            return generate_data()
        return df
    return generate_data()


def _save_and_optionally_show_plot(fig, output_path, label, show_plots, save_png):
    if save_png and output_path is not None:
        fig.savefig(output_path, dpi=150, bbox_inches="tight")
        print(f"[✓] {label} → {output_path}")
    if show_plots:
        plt.show()


def main(use_real_data=False, show_plots=True, save_png=True, k_clusters=5):
    df = _load_real_or_simulated_data(use_real_data)
    df = eda(df)

    # ── Dashboard 3×3 ─────────────────────────────────────────
    fig, axes = plt.subplots(3, 3, figsize=(19, 14))
    fig.suptitle("Market Risk Analysis — Indicatori Macroeconomici Globali 2015–2023\n"
                 "(Parametri calibrati su World Bank Open Data)",
                 fontsize=13, fontweight="bold", y=1.01)
    fig.patch.set_facecolor("#f0f2f5")
    for ax in axes.flat:
        ax.set_facecolor("#fff")

    gdp_trend(df, axes[0,0])
    gdp_volatility(df, axes[0,1])
    correlation_heatmap(df, axes[0,2])
    compute_var(df, axes[1,0])
    risk_matrix(df, axes[1,1])

    # Top 10 paesi per risk score complessivo (ultimo anno)
    latest = df[df["year"] == df["year"].max()].copy()
    latest["risk_score"] = (
        (-latest["gdp_growth"]).clip(lower=0) * 0.4 +
        latest["inflation"].clip(upper=50) / 50 * 0.35 +
        latest["unemployment"] / latest["unemployment"].max() * 0.25
    ).round(3)
    top10 = latest.nlargest(10,"risk_score")[["iso","gdp_growth","inflation","unemployment","risk_score"]]
    ax6 = axes[1,2]
    colors_bar = ["#e74c3c" if v > 0.5 else "#e67e22" if v > 0.3 else "#f1c40f"
                  for v in top10["risk_score"]]
    ax6.barh(top10["iso"], top10["risk_score"], color=colors_bar, edgecolor="white", lw=0.4)
    ax6.set(xlabel=LABEL_RISK_SCORE, title=f"Top 10 Paesi ad Alto Rischio ({df['year'].max()})")
    ax6.grid(True, axis="x", alpha=0.3)
    for i, (_, r) in enumerate(top10.iterrows()):
        ax6.text(r["risk_score"]+0.005, i,
                 f"PIL:{r['gdp_growth']:+.1f}%  Inf:{r['inflation']:.1f}%",
                 va="center", fontsize=7, color="#555")

    print("\n[Top 10 Paesi — Risk Score complessivo]")
    print(f"  {'ISO':>4}  {'PIL%':>6}  {'Inf%':>6}  {'Uem%':>6}  {'Score':>7}")
    for _, r in top10.iterrows():
        print(f"  {r['iso']:>4}  {r['gdp_growth']:>6.1f}  {r['inflation']:>6.1f}"
              f"  {r['unemployment']:>6.1f}  {r['risk_score']:>7.3f}")

    regional_risk_heatmap(df, axes[2,0])
    pca_anomaly_diagnostics(df, axes[2,1])
    monte_carlo_global_risk(df, axes[2,2])
    stress_test_summary(df)
    risk_change_analysis(df)

    plt.tight_layout()
    out_dir = None
    if save_png:
        base_dir = Path(__file__).resolve().parent
        out_dir = base_dir / "outputs" / "market_risk_analysis"
        out_dir.mkdir(parents=True, exist_ok=True)
    out1 = (out_dir / "market_risk_dashboard.png") if out_dir is not None else None
    _save_and_optionally_show_plot(fig, out1, "Dashboard", show_plots, save_png)

    # ── Mappa geografica ──────────────────────────────────────
    fig2, ax2 = plt.subplots(figsize=(16, 8))
    fig2.patch.set_facecolor("#0d1117")
    geo_risk_map(df, ax2, k=k_clusters)
    plt.tight_layout()
    out2 = (out_dir / "market_risk_map.png") if out_dir is not None else None
    _save_and_optionally_show_plot(fig2, out2, "Mappa", show_plots, save_png)

    # ── Figura avanzata aggiuntiva ───────────────────────────
    fig3, ax3 = plt.subplots(figsize=(10, 6))
    fig3.patch.set_facecolor("#f0f2f5")
    ax3.set_facecolor("#fff")
    income_risk_boxplot(df, ax3)
    plt.tight_layout()
    out3 = (out_dir / "market_risk_income_boxplot.png") if out_dir is not None else None
    _save_and_optionally_show_plot(fig3, out3, "Figura avanzata", show_plots, save_png)
    print("\n  ANALISI COMPLETATA ✓")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Market Risk Analysis")
    parser.add_argument("--real-data", action="store_true", help="Usa dati reali World Bank")
    parser.add_argument("--no-show", action="store_true", help="Non mostrare le figure")
    parser.add_argument("--no-save-png", action="store_true", help="Non salvare PNG in outputs")
    parser.add_argument("--clusters", type=int, default=5, help="Numero cluster KMeans per mappa")
    args = parser.parse_args()
    main(use_real_data=args.real_data,
         show_plots=not args.no_show,
         save_png=not args.no_save_png,
         k_clusters=max(2, args.clusters))
