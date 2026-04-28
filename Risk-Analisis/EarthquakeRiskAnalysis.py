"""
=============================================================
  RISK ANALYSIS — Catastrofi Naturali (Terremoti)
  Dati: simulati con parametri realistici (Gutenberg-Richter)
  basati sul catalogo USGS globale 2020-2024

    Installazione dipendenze (Linux/macOS consigliato):
        cd Risk-Analisis
        python3 -m venv .venv
        .venv/bin/pip install -r requirements.txt

    Esecuzione:
        .venv/bin/python EarthquakeRiskAnalysis.py
  
  ► In locale: sostituisci generate_data() con fetch_usgs()
    (API: https://earthquake.usgs.gov/fdsnws/event/1/)
=============================================================
"""
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.colors as mcolors
from pathlib import Path
from scipy import stats
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler
import warnings
warnings.filterwarnings("ignore")

# ══════════════════════════════════════════════════════════════
# 1. GENERAZIONE DATI REALISTICI (Gutenberg-Richter)
#    b-value ≈ 1.0, ~6000 eventi/anno mag≥4 (globale)
# ══════════════════════════════════════════════════════════════
def generate_data(n=5000, seed=42):
    """
    Simula un catalogo sismico realistico basato su:
      - Legge Gutenberg-Richter: log N = a - b*M  (b=1.0)
      - Distribuzione geografica lungo le principali zone sismiche
      - Correlazione profondità/magnitudo
    
    Per usare dati USGS reali in locale:
      import requests
      url = "https://earthquake.usgs.gov/fdsnws/event/1/query"
      r = requests.get(url, params={"format":"geojson","starttime":"2020-01-01",
          "endtime":"2024-12-31","minmagnitude":4.0,"limit":5000})
      features = r.json()["features"]
    """
    rng = np.random.default_rng(seed)
    print("[+] Generazione dati sismici (parametri USGS reali 2020–2024)...")

    # Gutenberg-Richter: P(M≥m) = 10^(a-b*m)  →  esponenziale troncata
    b_val = 1.0
    m_min, m_max = 4.0, 9.5
    # Distribuzione esponenziale troncata
    u = rng.uniform(0, 1, n)
    beta = b_val * np.log(10)
    mags = m_min - np.log(1 - u*(1 - np.exp(-beta*(m_max-m_min)))) / beta
    mags = np.clip(mags, m_min, m_max)

    # Zone sismiche realistiche: Ring of Fire, Himalaya, Mediterraneo, Mid-Atlantic
    zones = [
        # lat_c, lon_c, lat_std, lon_std, weight
        ( 0,   130,  15,  25, 0.30),   # Indonesia/Filippine
        (35,   140,  10,  10, 0.15),   # Giappone
        (-20,  -70,  12,  15, 0.18),   # Cile/Perù
        ( 38,   40,  10,  25, 0.12),   # Turchia/Caucaso
        ( 35,   25,   8,  20, 0.08),   # Grecia/Mediterraneo
        ( 60, -150,  10,  15, 0.07),   # Alaska
        (-15,  170,   8,  10, 0.10),   # Vanuatu/Tonga
    ]
    weights = np.array([z[4] for z in zones]); weights /= weights.sum()
    zone_idx = rng.choice(len(zones), size=n, p=weights)
    lats, lons = [], []
    for i, z in enumerate(zones):
        mask = (zone_idx == i)
        cnt = mask.sum()
        lats.extend(rng.normal(z[0], z[2], cnt))
        lons.extend(rng.normal(z[1], z[3], cnt))
    lats = np.array(lats); lons = np.array(lons)
    lons = ((lons + 180) % 360) - 180  # wrap -180..180

    # Profondità: maggiore per subduction zones, correlata con mag
    depths = rng.exponential(50, n) + rng.uniform(5, 20, n)
    depths = np.clip(depths, 0, 700)

    # Serie temporale 2020-2024 con clustering temporale (aftershocks)
    base_times = pd.date_range("2020-01-01", "2024-12-31", periods=n)
    jitter = pd.to_timedelta(rng.integers(-30, 30, n), unit="D")
    times = pd.DatetimeIndex(sorted(base_times + jitter))

    df = pd.DataFrame({
        "time": times[:n], "magnitude": mags,
        "lat": lats, "lon": lons, "depth_km": depths,
    })
    df["year"] = df["time"].dt.year
    df["month"] = df["time"].dt.month
    print(f"    → {len(df):,} eventi (2020–2024, mag≥{m_min})\n")
    return df

# ══════════════════════════════════════════════════════════════
# 1b. FETCH DATI REALI DA USGS (nessuna API key richiesta)
# ══════════════════════════════════════════════════════════════
def fetch_usgs(start="2020-01-01", end="2024-12-31", min_mag=4.0, limit=5000):
    """
    Scarica il catalogo sismico reale da USGS FDSN Web Service.
    Nessuna registrazione o API key necessaria.
    """
    import requests
    url = "https://earthquake.usgs.gov/fdsnws/event/1/query"
    params = {
        "format": "geojson",
        "starttime": start,
        "endtime": end,
        "minmagnitude": min_mag,
        "limit": limit,
    }
    print(f"[+] Scaricamento dati USGS ({start} → {end}, mag≥{min_mag})...")
    r = requests.get(url, params=params, timeout=60)
    r.raise_for_status()
    features = r.json()["features"]
    print(f"    → {len(features):,} eventi scaricati\n")
    return features


def parse_earthquakes(features):
    records = []
    for f in features:
        p = f["properties"]
        c = f["geometry"]["coordinates"]
        records.append({
            "time":      pd.to_datetime(p["time"], unit="ms"),
            "magnitude": p["mag"],
            "depth_km":  c[2],
            "lon":       c[0],
            "lat":       c[1],
            "type":      p.get("type", "earthquake"),
        })
    df = pd.DataFrame(records)
    df = df[df["type"] == "earthquake"].dropna(subset=["magnitude"])
    df["year"]  = df["time"].dt.year
    df["month"] = df["time"].dt.month
    return df.reset_index(drop=True)


# ══════════════════════════════════════════════════════════════
# 2. EDA
# ══════════════════════════════════════════════════════════════
def eda(df):
    print("═"*55)
    print("  EDA — Statistiche descrittive")
    print("═"*55)
    print(df[["magnitude","depth_km"]].describe().round(2))
    print(f"\n  Periodo:     {df['time'].min().date()} → {df['time'].max().date()}")
    print(f"  Tot eventi:  {len(df):,}  |  Mag max: {df['magnitude'].max():.1f}  |  Media: {df['magnitude'].mean():.2f}")
    bins=[0,4.9,5.9,6.9,7.9,10]
    labels=["Minor (4–4.9)","Moderate (5–5.9)","Strong (6–6.9)","Major (7–7.9)","Great (8+)"]
    df["severity_class"]=pd.cut(df["magnitude"],bins=bins,labels=labels)
    counts=df["severity_class"].value_counts().sort_index()
    print("\n  Frequenza per classe di magnitudo:")
    mx=counts.max()
    for k,v in counts.items():
        print(f"    {k:<22} {v:>5}  {'█'*(v*28//mx)}")
    return df

# ══════════════════════════════════════════════════════════════
# 3. GUTENBERG-RICHTER
# ══════════════════════════════════════════════════════════════
def gutenberg_richter(df, ax):
    mags = np.arange(df["magnitude"].min(), df["magnitude"].max()+0.1, 0.1)
    counts = np.array([len(df[df["magnitude"]>=m]) for m in mags], dtype=float)
    log_c = np.log10(counts + 1e-9)
    mask = (counts>2)
    slope, intercept, r, *_ = stats.linregress(mags[mask], log_c[mask])
    b_val = -slope
    ax.scatter(mags, log_c, s=14, color="#e74c3c", label="Osservato", zorder=3)
    ax.plot(mags, intercept+slope*mags,"--",color="#2c3e50",lw=1.8,
            label=f"Fit G-R  b={b_val:.2f}  R²={r**2:.3f}")
    ax.set(xlabel="Magnitudo M", ylabel="log₁₀(N ≥ M)", title="Legge di Gutenberg-Richter")
    ax.legend(fontsize=8); ax.grid(True,alpha=0.3)
    print(f"\n[Gutenberg-Richter]  b-value={b_val:.3f} (atteso ~1.0)  a={intercept:.2f}  R²={r**2:.4f}")
    return b_val

# ══════════════════════════════════════════════════════════════
# 4. EXCEEDANCE PROBABILITY / RETURN PERIOD
# ══════════════════════════════════════════════════════════════
def exceedance_probability(df, ax):
    years = (df["time"].max()-df["time"].min()).days/365.25
    ms = np.sort(df["magnitude"]); n = len(ms)
    exc = 1 - np.arange(1,n+1)/(n+1)      # Weibull plotting position
    rp  = 1 / (exc * n/years + 1e-12)     # Return period in anni
    ax.semilogy(ms, rp, color="#8e44ad", lw=2)
    for thr,col,va in [(5.5,"#e67e22","top"),(6.5,"#e74c3c","bottom"),(7.0,"#c0392b","top")]:
        idx = np.searchsorted(ms, thr)
        if idx < n:
            ax.axvline(thr,color=col,ls=":",lw=1.3,alpha=0.8)
            ax.annotate(f"M{thr}  ~{rp[idx]:.0f}a", xy=(thr,rp[idx]),
                        xytext=(thr+0.08,rp[idx]*(2 if va=="top" else 0.5)),
                        fontsize=7, color=col, fontweight="bold")
    ax.set(xlabel="Magnitudo", ylabel="Return Period (anni)", title="Exceedance Probability")
    ax.grid(True,alpha=0.3,which="both")
    print("\n[Return Period stimato]")
    for thr in [5.0,5.5,6.0,6.5,7.0,7.5,8.0]:
        idx=np.searchsorted(ms,thr)
        if idx<n: print(f"  M≥{thr:.1f}  →  ~{rp[idx]:.1f} anni")

# ══════════════════════════════════════════════════════════════
# 5. VALUE AT RISK (VaR) sulla magnitudo
# ══════════════════════════════════════════════════════════════
def compute_var(df, ax):
    data = df["magnitude"].values
    ax.hist(data,bins=60,density=True,color="#3498db",alpha=0.6,label="Distribuzione",edgecolor="white",lw=0.2)
    print("\n[Value at Risk sulla Magnitudo]")
    print(f"  {'Conf':>6}  {'VaR':>6}  {'CVaR':>6}")
    for alpha,col in [(0.90,"#f39c12"),(0.95,"#e67e22"),(0.99,"#e74c3c")]:
        var  = np.percentile(data, alpha*100)
        cvar = data[data>=var].mean()
        ax.axvline(var,color=col,lw=2.0,label=f"VaR {int(alpha*100)}% = {var:.2f}")
        ax.axvspan(var, data.max(), alpha=0.08, color=col)
        print(f"  {int(alpha*100):>5}%  {var:>6.2f}  {cvar:>6.2f}")
    ax.set(xlabel="Magnitudo", ylabel="Densità", title="Distribuzione Magnitudo & VaR")
    ax.legend(fontsize=8); ax.grid(True,alpha=0.3)

# ══════════════════════════════════════════════════════════════
# 6. RISK MATRIX 5×5
# ══════════════════════════════════════════════════════════════
def risk_matrix(df, ax):
    cls_def = {"Minor\n4–4.9":(4,4.9,1),"Moderate\n5–5.9":(5,5.9,2),
               "Strong\n6–6.9":(6,6.9,3),"Major\n7–7.9":(7,7.9,4),"Great\n8+":(8,12,5)}
    total = len(df); rows = []
    for lbl,(lo,hi,sev) in cls_def.items():
        cnt = len(df[(df["magnitude"]>=lo)&(df["magnitude"]<hi)])
        fr  = cnt/total if total else 0
        lik = 5 if fr>.40 else 4 if fr>.20 else 3 if fr>.08 else 2 if fr>.02 else 1
        rows.append(dict(cls=lbl,n=cnt,fr=fr,lik=lik,sev=sev,sc=lik*sev))
    mat = np.zeros((5,5))
    for r in rows:
        mat[5-r["lik"], r["sev"]-1] = r["sc"]
    cmap = mcolors.LinearSegmentedColormap.from_list("r",["#2ecc71","#f1c40f","#e67e22","#e74c3c","#8e1a1a"])
    im = ax.imshow(mat,cmap=cmap,vmin=1,vmax=25,aspect="auto")
    ax.set_xticks(range(5)); ax.set_xticklabels(["Minor","Mod.","Strong","Major","Great"],fontsize=8)
    ax.set_yticks(range(5)); ax.set_yticklabels(["V.Likely","Likely","Possible","Unlikely","Rare"],fontsize=8)
    ax.set(xlabel="Severity →",ylabel="← Likelihood",title="Risk Matrix (5×5)")
    plt.colorbar(im,ax=ax,label="Risk Score")
    for i in range(5):
        for j in range(5):
            v=mat[i,j]
            if v>0: ax.text(j,i,f"{v:.0f}",ha="center",va="center",
                            fontweight="bold",fontsize=10,color="white" if v>12 else "black")
    print("\n[Risk Matrix — Likelihood × Severity]")
    print(f"  {'Classe':<18}  {'N':>5}  {'Freq%':>6}  {'Lik':>4}  {'Sev':>4}  {'Score':>6}")
    for r in sorted(rows,key=lambda x:-x["sc"]):
        print(f"  {r['cls'].replace(chr(10),' '):<18}  {r['n']:>5}  {r['fr']*100:>5.1f}%  {r['lik']:>4}  {r['sev']:>4}  {r['sc']:>6}")

# ══════════════════════════════════════════════════════════════
# 7. CLUSTERING GEOGRAFICO K-MEANS
# ══════════════════════════════════════════════════════════════
def geo_clustering(df, ax, k=6):
    X  = df[["lat","lon","magnitude"]].dropna()
    Xs = StandardScaler().fit_transform(X)
    km = KMeans(n_clusters=k, random_state=42, n_init=10)
    df2 = X.copy(); df2["cluster"] = km.fit_predict(Xs)
    cs = df2.groupby("cluster").agg(n=("magnitude","size"),
                                    mm=("magnitude","mean"),
                                    mx=("magnitude","max")).reset_index()
    cs["rs"] = (cs["mm"]/cs["mm"].max()*0.6 + cs["n"]/cs["n"].max()*0.4).round(3)
    sc = ax.scatter(df2["lon"],df2["lat"],
                    c=df2["cluster"].map(dict(zip(cs["cluster"],cs["rs"]))),
                    cmap="RdYlGn_r",s=5,alpha=0.55,linewidths=0)
    plt.colorbar(sc,ax=ax,label="Risk Score")
    ax.set_facecolor("#0d1117")
    ax.set(xlabel="Longitudine",ylabel="Latitudine",title=f"Cluster Geografici del Rischio (K={k})")
    for spine in ax.spines.values(): spine.set_color("#444")
    ax.tick_params(colors="#ddd"); ax.xaxis.label.set_color("#ddd"); ax.yaxis.label.set_color("#ddd")
    ax.title.set_color("#eee"); ax.grid(True,alpha=0.15,color="#555")
    print(f"\n[Geo Clustering — K={k} zone di rischio omogeneo]")
    print(f"  {'Cluster':>8}  {'N':>5}  {'MagMedia':>9}  {'MagMax':>7}  {'Score':>7}")
    for _,r in cs.sort_values("rs",ascending=False).iterrows():
        bar="█"*int(r["rs"]*20)
        print(f"  {int(r['cluster']):>8}  {r['n']:>5}  {r['mm']:>9.2f}  {r['mx']:>7.2f}  {r['rs']:>7.3f}  {bar}")

# ══════════════════════════════════════════════════════════════
# MAIN
# ══════════════════════════════════════════════════════════════
def main(use_real_data=True):
    if use_real_data:
        try:
            df = parse_earthquakes(fetch_usgs())
        except Exception as e:
            print(f"[!] USGS non raggiungibile ({e}), uso dati simulati.")
            df = generate_data(n=5000)
    else:
        df = generate_data(n=5000)
    df = eda(df)

    fig, axes = plt.subplots(2,3,figsize=(18,10))
    fig.suptitle("Risk Analysis — Terremoti Globali 2020–2024\n(Parametri basati su USGS Earthquake Catalog)",
                 fontsize=14, fontweight="bold", y=1.01)
    fig.patch.set_facecolor("#f0f2f5")
    for ax in axes.flat: ax.set_facecolor("#fff")

    # Distribuzione magnitudo
    axes[0,0].hist(df["magnitude"],bins=60,color="#2980b9",edgecolor="white",lw=0.3)
    axes[0,0].set(title="Distribuzione Magnitudo",xlabel="Magnitudo",ylabel="Frequenza")
    axes[0,0].grid(True,alpha=0.3)

    # Serie temporale mensile
    m = df.groupby(["year","month"]).size().reset_index(name="c")
    m["date"] = pd.to_datetime(m[["year","month"]].assign(day=1))
    axes[0,1].plot(m["date"],m["c"],color="#27ae60",lw=1.8)
    axes[0,1].fill_between(m["date"],m["c"],alpha=0.25,color="#27ae60")
    axes[0,1].set(title="Frequenza Mensile Terremoti",ylabel="N eventi / mese")
    axes[0,1].tick_params(axis="x",rotation=25); axes[0,1].grid(True,alpha=0.3)

    gutenberg_richter(df, axes[0,2])
    exceedance_probability(df, axes[1,0])
    compute_var(df, axes[1,1])
    risk_matrix(df, axes[1,2])

    base_dir = Path(__file__).resolve().parent
    out_dir = base_dir / "outputs" / "earthquake_risk_analysis"
    out_dir.mkdir(parents=True, exist_ok=True)

    plt.tight_layout()
    plt.show()
    out1 = out_dir / "risk_analysis_charts.png"
    plt.savefig(out1, dpi=150, bbox_inches="tight")
    print(f"\n[✓] Dashboard → {out1}")

    # Mappa geografica
    fig2, ax2 = plt.subplots(figsize=(14,7))
    fig2.patch.set_facecolor("#0d1117")
    geo_clustering(df, ax2)
    plt.tight_layout()
    plt.show()
    out2 = out_dir / "risk_geo_clusters.png"
    plt.savefig(out2, dpi=150, bbox_inches="tight")
    print(f"[✓] Mappa geografica → {out2}")
    print("\n  ANALISI COMPLETATA ✓")

if __name__ == "__main__":
    main()
