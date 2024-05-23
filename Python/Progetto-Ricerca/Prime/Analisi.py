import os
import numpy as np
import pandas as pd

filenames = ['events.csv', 'fixations.csv', 'blinks.csv', 'saccades.csv', '3d_eye_states.csv', 'imu.csv']
cartelle = ['ag11', 'cs2', 'fp7', 'mg6', 'am13', 'gm9', 'so15', 'as10', 'df_2', 'mf16', 'lc', 'tb12', 'vn4', 'so15_3', 'so15_4', 'ic1', 'tb12', 'fm8', 'so15_2', 'so15_1', 'as3']
StartPath = '/home/kenobi/Documenti/Dati-Progetti/dati da analizzare/'
DestinationPath: str = '/home/kenobi/Documenti/GitHub/Progetto-Ricerca'

def SaveDFtoExcel(dataframe: pd.DataFrame, destination_path: str, filename: str):
    """
    Salva un dataframe in un file Excel nel percorso specificato.

    Args:
        dataframe (pd.DataFrame): Il dataframe da salvare.
        destination_path (str): Il percorso completo della cartella in cui salvare il file Excel.
        filename (str): Il nome del file Excel.
    """
    file_path = os.path.join(destination_path, filename)
    # Crea un writer Excel
    with pd.ExcelWriter(file_path, engine='xlsxwriter') as writer:
        # Scrivi il DataFrame in un foglio Excel
        dataframe.to_excel(writer, index=False, sheet_name='Sheet1')
        # Ottieni l'oggetto workbook dal writer
        workbook = writer.book
        # Ottieni l'oggetto worksheet
        worksheet = writer.sheets['Sheet1']
        # Imposta la larghezza delle colonne in base alla lunghezza massima delle stringhe in ogni colonna
        for i, col in enumerate(dataframe.columns):
            max_length = max(dataframe[col].astype(str).map(len).max(), len(col))
            worksheet.set_column(i, i, max_length)

def StatisticheGiri(EventsDF, FixationsDF, BlinksDF, SaccadesDF, EyeStatesDF, cartella):
    """
    Calcola il numero e la durata media delle fissazioni, dei blink, delle saccadi e del diametro medio delle pupille
    raccolti tra ogni coppia di giri consecutivi.

    Args:
        EventsDF (pd.DataFrame): DataFrame contenente i dati degli eventi.
        FixationsDF (pd.DataFrame): DataFrame contenente i dati delle fissazioni.
        BlinksDF (pd.DataFrame): DataFrame contenente i dati dei blink.
        SaccadesDF (pd.DataFrame): DataFrame contenente i dati dei saccadi.
        EyeStatesDF (pd.DataFrame): DataFrame contenente i dati dello stato dell'occhio.
        cartella (str): Nome della cartella.

    Returns:
        pd.DataFrame: DataFrame contenente il numero e la durata media delle fissazioni, dei blink, delle saccadi e del diametro medio delle pupille
                      raccolti tra ogni coppia di giri consecutivi.
    """

    # Inizializza il DataFrame finale e la lista delle variabili
    final_df = pd.DataFrame(columns=['ID: ' + cartella])
    variabili = ['Tempo Giro (s)', 'Velocità Media per Giro (Km/h)', 'Numero Fissazioni', 'Durata media Fissazioni', 'Numero Blinks', 
                 'Media durata Blinks', 'Numero Saccadi', 'Media durata Saccadi', 'Diametro Medio Pupilla']

    # Filtra i giri che includono le parole "giro" o "exit"
    giri = EventsDF[EventsDF['name'].str.contains('giro|exit', case=False)]['name'].unique()

    # Aggiungi le variabili al DataFrame finale
    final_df['ID: ' + cartella] = variabili

    # Itera su tutti i giri
    for i in range(len(giri) - 1):
        # Trova il nome del giro corrente e il successivo
        current_giro = giri[i]
        next_giro = giri[i + 1]

        # Trova il timestamp per il giro corrente
        start_giro = EventsDF.loc[EventsDF['name'] == current_giro, 'timestamp [ns]'].values
        start_giro = start_giro[0] if len(start_giro) > 0 else None

        # Trova il timestamp per il giro successivo
        end_giro = EventsDF.loc[EventsDF['name'] == next_giro, 'timestamp [ns]'].values
        end_giro = end_giro[0] if len(end_giro) > 0 else None

        if start_giro is not None and end_giro is not None:
            # Filtra i dati degli eventi, delle fissazioni, dei blink, delle saccadi e delle pupille per il giro corrente
            fixations_filtrate = FixationsDF[(FixationsDF['start timestamp [ns]'].astype('int64') >= start_giro.astype('int64')) & (FixationsDF['end timestamp [ns]'].astype('int64') <= end_giro.astype('int64'))]
            blinks_filtrati = BlinksDF[(BlinksDF['start timestamp [ns]'].astype('int64') >= start_giro.astype('int64')) & (BlinksDF['end timestamp [ns]'].astype('int64') <= end_giro.astype('int64'))]
            saccades_filtrati = SaccadesDF[(SaccadesDF['start timestamp [ns]'].astype('int64') >= start_giro.astype('int64')) & (SaccadesDF['end timestamp [ns]'].astype('int64') <= end_giro.astype('int64'))]
            eye_states_filtrati = EyeStatesDF[(EyeStatesDF['timestamp [ns]'].astype('int64') >= start_giro.astype('int64')) & (EyeStatesDF['timestamp [ns]'].astype('int64') <= end_giro.astype('int64'))]

            # Calcola le statistiche per il giro corrente
            num_fixations = len(fixations_filtrate)
            avg_duration_fixations = fixations_filtrate['duration [ms]'].mean().round(2) if not fixations_filtrate.empty else np.nan

            num_blinks = len(blinks_filtrati)
            avg_duration_blinks = blinks_filtrati['duration [ms]'].mean().round(2) if not blinks_filtrati.empty else np.nan

            num_saccades = len(saccades_filtrati)
            avg_duration_saccades = saccades_filtrati['duration [ms]'].mean().round(2) if not saccades_filtrati.empty else np.nan

            giro_duration = (end_giro - start_giro) / 1e9 if start_giro is not None and end_giro is not None else np.nan
            giro_duration_hours = giro_duration / 3600
            distance_km = 1.2
            avg_speed_kmh = (distance_km / giro_duration_hours).round(2)

            # Calcola il diametro medio della pupilla per il giro corrente
            avg_pupil_diameter = eye_states_filtrati['pupil diameter [mm]'].mean().round(2) if not eye_states_filtrati.empty else 0

            # Aggiungi le informazioni al DataFrame finale
            final_df['Giro ' + current_giro.split()[-1]] = [giro_duration, avg_speed_kmh, num_fixations, avg_duration_fixations, num_blinks, avg_duration_blinks, num_saccades, avg_duration_saccades, avg_pupil_diameter]
    
    return final_df

def StatisticheCurva1(EventsDF, FixationsDF, BlinksDF, SaccadesDF, EyeStatesDF,cartella):
    """
    Calcola il numero e la durata media delle fissazioni, dei blink e delle saccadi all'ingresso e all'uscita dalla curva 1.

    Args:
        EventsDF (pd.DataFrame): DataFrame contenente i dati degli eventi.
        FixationsDF (pd.DataFrame): DataFrame contenente i dati delle fissazioni.
        BlinksDF (pd.DataFrame): DataFrame contenente i dati dei blink.
        SaccadesDF (pd.DataFrame): DataFrame contenente i dati dei saccadi.
        EyeStatesDF (pd.DataFrame): DataFrame contenente i dati dello stato dell'occhio.
        cartella (str): Nome della cartella.

    Returns:
        pd.DataFrame: DataFrame contenente il numero e la durata media delle fissazioni, dei blink e delle saccadi
                      all'ingresso e all'uscita dalla curva 1.
    """

    # Inizializza il DataFrame finale e la lista delle variabili
    final_df = pd.DataFrame(columns=['ID: ' + cartella])
    variabili = ['Tempo Ingresso Curva 1 (s)', 'Tempo Uscita Curva 1 (s)', 'Numero Fissazioni Ingresso Curva 1', 'Durata Media Fissazioni Ingresso Curva 1', 
                 'Fissazione Più Lunga Ingresso Curva 1', 'Numero Fissazioni Uscita Curva 1', 'Durata Media Fissazioni Uscita Curva 1', 
                 'Fissazione Più Lunga Uscita Curva 1', 'Numero Blinks Ingresso Curva 1', 'Durata Media Blinks Ingresso Curva 1', 
                 'Numero Blinks Uscita Curva 1', 'Durata Media Blinks Uscita Curva 1', 'Numero Saccadi Ingresso Curva 1', 
                 'Durata Media Saccadi Ingresso Curva 1', 'Numero Saccadi Uscita Curva 1', 'Durata Media Saccadi Uscita Curva 1', 
                 'Diametro Medio Pupilla Ingresso Curva 1 (mm)', 'Diametro Medio Pupilla Uscita Curva 1 (mm)']

    # Aggiungi le variabili al DataFrame finale
    final_df['ID: ' + cartella] = variabili

    # Trova tutte le terne di curva 1 nel DataFrame degli eventi
    curve_terna_indices = EventsDF[EventsDF['name'].str.contains('SC1_|PC1_|EC1_', case=False)].index

    # Itera su tutte le terne di curva 1
    for i in range(len(curve_terna_indices) // 3):
        # Trova gli indici per l'ingresso e l'uscita dalla curva 1 nella terna corrente
        ingresso_index = curve_terna_indices[i * 3]
        uscita_index = curve_terna_indices[i * 3 + 2]

        # Trova i timestamp per l'ingresso e l'uscita dalla curva 1
        SC1 = EventsDF.loc[ingresso_index, 'timestamp [ns]']
        PC1 = EventsDF.loc[ingresso_index + 1, 'timestamp [ns]']
        EC1 = EventsDF.loc[uscita_index, 'timestamp [ns]']

        # Calcola il numero e la durata media delle fissazioni, dei blink e delle saccadi all'ingresso e all'uscita dalla curva 1
        if not pd.isnull(SC1) and not pd.isnull(PC1) and not pd.isnull(EC1):
            fixations_ingresso = FixationsDF[(FixationsDF['start timestamp [ns]'] >= SC1) & (FixationsDF['end timestamp [ns]'] <= PC1)]
            num_fixations_ingresso = len(fixations_ingresso)
            avg_duration_fixations_ingresso = fixations_ingresso['duration [ms]'].mean().round(2) if not fixations_ingresso.empty else 0
            longest_fixation_id_ingresso = fixations_ingresso['fixation id'].max() if not fixations_ingresso.empty else 0

            fixations_uscita = FixationsDF[(FixationsDF['start timestamp [ns]'] >= PC1) & (FixationsDF['end timestamp [ns]'] <= EC1)]
            num_fixations_uscita = len(fixations_uscita)
            avg_duration_fixations_uscita = fixations_uscita['duration [ms]'].mean().round(2) if not fixations_uscita.empty else 0
            longest_fixation_id_uscita = fixations_uscita['fixation id'].max() if not fixations_uscita.empty else 0

            # Filtra i blink per i timestamp di ingresso e uscita dalla curva 1
            blinks_ingresso = BlinksDF[(BlinksDF['start timestamp [ns]'] >= SC1) & (BlinksDF['end timestamp [ns]'] <= PC1)]
            num_blinks_ingresso = len(blinks_ingresso)
            avg_duration_blinks_ingresso = blinks_ingresso['duration [ms]'].mean().round(2) if not blinks_ingresso.empty else 0

            blinks_uscita = BlinksDF[(BlinksDF['start timestamp [ns]'] >= PC1) & (BlinksDF['end timestamp [ns]'] <= EC1)]
            num_blinks_uscita = len(blinks_uscita)
            avg_duration_blinks_uscita = blinks_uscita['duration [ms]'].mean().round(2) if not blinks_uscita.empty else 0

            saccades_ingresso = SaccadesDF[(SaccadesDF['start timestamp [ns]'] >= SC1) & (SaccadesDF['end timestamp [ns]'] <= PC1)]
            num_saccades_ingresso = len(saccades_ingresso)
            avg_duration_saccades_ingresso = saccades_ingresso['duration [ms]'].mean().round(2) if not saccades_ingresso.empty else 0

            saccades_uscita = SaccadesDF[(SaccadesDF['start timestamp [ns]'] >= PC1) & (SaccadesDF['end timestamp [ns]'] <= EC1)]
            num_saccades_uscita = len(saccades_uscita)
            avg_duration_saccades_uscita = saccades_uscita['duration [ms]'].mean().round(2) if not saccades_uscita.empty else 0

            eye_states_ingresso = EyeStatesDF[(EyeStatesDF['timestamp [ns]'] >= SC1) & (EyeStatesDF['timestamp [ns]'] <= PC1)]
            avg_pupil_diameter_ingresso = eye_states_ingresso['pupil diameter [mm]'].mean().round(2) if not eye_states_ingresso.empty else 0

            eye_states_uscita = EyeStatesDF[(EyeStatesDF['timestamp [ns]'] >= PC1) & (EyeStatesDF['timestamp [ns]'] <= EC1)]
            avg_pupil_diameter_uscita = eye_states_uscita['pupil diameter [mm]'].mean().round(2) if not eye_states_uscita.empty else 0

            # Tempo in secondi
            tempo_ingresso = (PC1 - SC1) / 1e9
            tempo_uscita = (EC1 - PC1) / 1e9

            # Aggiungi i risultati al DataFrame finale
            final_df['Giro ' + str(i)] = [round(tempo_ingresso, 2), round(tempo_uscita, 2),
                                            num_fixations_ingresso, avg_duration_fixations_ingresso,
                                            longest_fixation_id_ingresso,
                                            num_fixations_uscita, avg_duration_fixations_uscita,
                                            longest_fixation_id_uscita,
                                            num_blinks_ingresso, avg_duration_blinks_ingresso,
                                            num_blinks_uscita, avg_duration_blinks_uscita,
                                            num_saccades_ingresso, avg_duration_saccades_ingresso,
                                            num_saccades_uscita, avg_duration_saccades_uscita,
                                            avg_pupil_diameter_ingresso, avg_pupil_diameter_uscita]
    
    return final_df

def StatisticheCurva9(EventsDF, FixationsDF, BlinksDF, SaccadesDF, EyeStatesDF,cartella):
    """
    Calcola il numero e la durata media delle fissazioni, dei blink e delle saccadi all'ingresso e all'uscita dalla curva 9.

    Args:
        EventsDF (pd.DataFrame): DataFrame contenente i dati degli eventi.
        FixationsDF (pd.DataFrame): DataFrame contenente i dati delle fissazioni.
        BlinksDF (pd.DataFrame): DataFrame contenente i dati dei blink.
        SaccadesDF (pd.DataFrame): DataFrame contenente i dati dei saccadi.
        EyeStatesDF (pd.DataFrame): DataFrame contenente i dati dello stato dell'occhio.
        cartella (str): Nome della cartella.

    Returns:
        pd.DataFrame: DataFrame contenente il numero e la durata media delle fissazioni, dei blink e delle saccadi
                      all'ingresso e all'uscita dalla curva 9.
    """

    # Inizializza il DataFrame finale e la lista delle variabili
    final_df = pd.DataFrame(columns=['ID: ' + cartella])
    variabili = ['Tempo Ingresso Curva 9 (s)', 'Tempo Uscita Curva 9 (s)', 'Numero Fissazioni Ingresso Curva 9', 'Durata Media Fissazioni Ingresso Curva 9', 
                 'Fissazione Più Lunga Ingresso Curva 9', 'Numero Fissazioni Uscita Curva 9', 'Durata Media Fissazioni Uscita Curva 9', 
                 'Fissazione Più Lunga Uscita Curva 9', 'Numero Blinks Ingresso Curva 9', 'Durata Media Blinks Ingresso Curva 9', 
                 'Numero Blinks Uscita Curva 9', 'Durata Media Blinks Uscita Curva 9', 'Numero Saccadi Ingresso Curva 9', 
                 'Durata Media Saccadi Ingresso Curva 9', 'Numero Saccadi Uscita Curva 9', 'Durata Media Saccadi Uscita Curva 9', 
                 'Diametro Medio Pupilla Ingresso Curva 9 (mm)', 'Diametro Medio Pupilla Uscita Curva 9 (mm)']

    # Aggiungi le variabili al DataFrame finale
    final_df['ID: ' + cartella] = variabili

    # Trova tutte le terne di curva 9 nel DataFrame degli eventi
    curve_terna_indices = EventsDF[EventsDF['name'].str.contains('SC9_|PC9_|EC9_', case=False)].index

    # Itera su tutte le terne di curva 9
    for i in range(len(curve_terna_indices) // 3):
        # Trova gli indici per l'ingresso e l'uscita dalla curva 9 nella terna corrente
        ingresso_index = curve_terna_indices[i * 3]
        uscita_index = curve_terna_indices[i * 3 + 2]

        # Trova i timestamp per l'ingresso e l'uscita dalla curva 9
        SC9 = EventsDF.loc[ingresso_index, 'timestamp [ns]']
        PC9 = EventsDF.loc[ingresso_index + 1, 'timestamp [ns]']
        EC9 = EventsDF.loc[uscita_index, 'timestamp [ns]']

        # Calcola il numero e la durata media delle fissazioni, dei blink e delle saccadi all'ingresso e all'uscita dalla curva 9
        if not pd.isnull(SC9) and not pd.isnull(PC9) and not pd.isnull(EC9):
            fixations_ingresso = FixationsDF[(FixationsDF['start timestamp [ns]'] >= SC9) & (FixationsDF['end timestamp [ns]'] <= PC9)]
            num_fixations_ingresso = len(fixations_ingresso)
            avg_duration_fixations_ingresso = fixations_ingresso['duration [ms]'].mean().round(2) if not fixations_ingresso.empty else 0
            longest_fixation_id_ingresso = fixations_ingresso['fixation id'].max() if not fixations_ingresso.empty else 0

            fixations_uscita = FixationsDF[(FixationsDF['start timestamp [ns]'] >= PC9) & (FixationsDF['end timestamp [ns]'] <= EC9)]
            num_fixations_uscita = len(fixations_uscita)
            avg_duration_fixations_uscita = fixations_uscita['duration [ms]'].mean().round(2) if not fixations_uscita.empty else 0
            longest_fixation_id_uscita = fixations_uscita['fixation id'].max() if not fixations_uscita.empty else 0

            # Filtra i blink per i timestamp di ingresso e uscita dalla curva 9
            blinks_ingresso = BlinksDF[(BlinksDF['start timestamp [ns]'] >= SC9) & (BlinksDF['end timestamp [ns]'] <= PC9)]
            num_blinks_ingresso = len(blinks_ingresso)
            avg_duration_blinks_ingresso = blinks_ingresso['duration [ms]'].mean().round(2) if not blinks_ingresso.empty else 0

            blinks_uscita = BlinksDF[(BlinksDF['start timestamp [ns]'] >= PC9) & (BlinksDF['end timestamp [ns]'] <= EC9)]
            num_blinks_uscita = len(blinks_uscita)
            avg_duration_blinks_uscita = blinks_uscita['duration [ms]'].mean().round(2) if not blinks_uscita.empty else 0

            saccades_ingresso = SaccadesDF[(SaccadesDF['start timestamp [ns]'] >= SC9) & (SaccadesDF['end timestamp [ns]'] <= PC9)]
            num_saccades_ingresso = len(saccades_ingresso)
            avg_duration_saccades_ingresso = saccades_ingresso['duration [ms]'].mean().round(2) if not saccades_ingresso.empty else 0

            saccades_uscita = SaccadesDF[(SaccadesDF['start timestamp [ns]'] >= PC9) & (SaccadesDF['end timestamp [ns]'] <= EC9)]
            num_saccades_uscita = len(saccades_uscita)
            avg_duration_saccades_uscita = saccades_uscita['duration [ms]'].mean().round(2) if not saccades_uscita.empty else 0

            eye_states_ingresso = EyeStatesDF[(EyeStatesDF['timestamp [ns]'] >= SC9) & (EyeStatesDF['timestamp [ns]'] <= PC9)]
            avg_pupil_diameter_ingresso = eye_states_ingresso['pupil diameter [mm]'].mean().round(2) if not eye_states_ingresso.empty else 0

            eye_states_uscita = EyeStatesDF[(EyeStatesDF['timestamp [ns]'] >= PC9) & (EyeStatesDF['timestamp [ns]'] <= EC9)]
            avg_pupil_diameter_uscita = eye_states_uscita['pupil diameter [mm]'].mean().round(2) if not eye_states_uscita.empty else 0

            # Tempo in secondi
            tempo_ingresso = (PC9 - SC9) / 1e9
            tempo_uscita = (EC9 - PC9) / 1e9

            # Aggiungi i risultati al DataFrame finale
            final_df['Giro ' + str(i)] = [round(tempo_ingresso, 2), round(tempo_uscita, 2),
                                            num_fixations_ingresso, avg_duration_fixations_ingresso,
                                            longest_fixation_id_ingresso,
                                            num_fixations_uscita, avg_duration_fixations_uscita,
                                            longest_fixation_id_uscita,
                                            num_blinks_ingresso, avg_duration_blinks_ingresso,
                                            num_blinks_uscita, avg_duration_blinks_uscita,
                                            num_saccades_ingresso, avg_duration_saccades_ingresso,
                                            num_saccades_uscita, avg_duration_saccades_uscita,
                                            avg_pupil_diameter_ingresso, avg_pupil_diameter_uscita]
    
    return final_df

def StatisticheSettore1(EventsDF, FixationsDF, BlinksDF, SaccadesDF, EyeStatesDF,ImuDF,cartella):
    """
    Calcola il numero e la durata media delle fissazioni, dei blink e delle saccadi raccolti tra EC1 e SC9.

    Args:
        EventsDF (pd.DataFrame): DataFrame contenente i dati degli eventi.
        FixationsDF (pd.DataFrame): DataFrame contenente i dati delle fissazioni.
        BlinksDF (pd.DataFrame): DataFrame contenente i dati dei blink.
        SaccadesDF (pd.DataFrame): DataFrame contenente i dati dei saccadi.
        EyeStatesDF (pd.DataFrame): DataFrame contenente i dati dello stato dell'occhio.
        ImuDF (pd.DataFrame): DataFrame contenente i dati dell'IMU.
        cartella (str): Nome della cartella.

    Returns:
        pd.DataFrame: DataFrame contenente il numero e la durata media delle fissazioni, dei blink e delle saccadi
                      raccolti tra EC1 e SC9, con le variabili lungo la prima colonna e il numero del giro come indice delle righe.
    """

    # Inizializza il DataFrame finale e la lista delle variabili
    final_df = pd.DataFrame(columns=['ID: ' + cartella])
    variabili = ['Tempo Settore 1 (s)', 'Numero Fissazioni Settore 1', 'Durata Media Fissazioni Settore 1 (ms)',
                 'Numero Blinks Settore 1', 'Durata Media Blinks Settore 1 (ms)',
                 'Numero Saccadi Settore 1', 'Durata Media Saccadi Settore 1 (ms)',
                 'Diametro Medio Pupilla Settore 1 (mm)',
                 'Gyro X Medio Settore 1 (deg/s)', 'Gyro Y Medio Settore 1 (deg/s)', 'Gyro Z Medio Settore 1 (deg/s)',
                 'Accelerazione X Media Settore 1 (G)', 'Accelerazione Y Media Settore 1 (G)', 'Accelerazione Z Media Settore 1 (G)']

    # Aggiungi le variabili al DataFrame finale
    final_df['ID: ' + cartella] = variabili

    # Creiamo un dizionario per associare ogni giro alla coppia di timestamp EC1 e SC9
    giri_timestamps = {}
    for giro in range(len(EventsDF)):
        # Trova il timestamp per EC1 e SC9 del giro corrente
        EC1 = EventsDF.loc[(EventsDF['name'] == f'EC1_{giro}'), 'timestamp [ns]'].values
        SC9 = EventsDF.loc[(EventsDF['name'] == f'SC9_{giro}'), 'timestamp [ns]'].values

        # Assicurati che entrambi i timestamp siano stati trovati
        if EC1.size > 0 and SC9.size > 0:
            giri_timestamps[giro] = (EC1[0], SC9[0])

    # Iteriamo su ogni coppia di timestamp trovata
    for giro, (EC1_ts, SC9_ts) in giri_timestamps.items():
        # Filtra i dati delle fissazioni, dei blink e delle saccadi per il timestamp di EC1 e SC9
        fixations_filtrate = FixationsDF[(FixationsDF['start timestamp [ns]'] >= EC1_ts) & (FixationsDF['end timestamp [ns]'] <= SC9_ts)]
        blinks_filtrati = BlinksDF[(BlinksDF['start timestamp [ns]'] >= EC1_ts) & (BlinksDF['end timestamp [ns]'] <= SC9_ts)]
        saccades_filtrati = SaccadesDF[(SaccadesDF['start timestamp [ns]'] >= EC1_ts) & (SaccadesDF['end timestamp [ns]'] <= SC9_ts)]
        eye_states_filtrati = EyeStatesDF[(EyeStatesDF['timestamp [ns]'] >= EC1_ts) & (EyeStatesDF['timestamp [ns]'] <= SC9_ts)]
        imu_filtrato = ImuDF[(ImuDF['timestamp [ns]'] >= EC1_ts) & (ImuDF['timestamp [ns]'] <= SC9_ts)]

        # Calcola le statistiche per il giro corrente
        num_fixations = len(fixations_filtrate)
        avg_duration_fixations = fixations_filtrate['duration [ms]'].mean().round(2) if not fixations_filtrate.empty else np.nan

        num_blinks = len(blinks_filtrati)
        avg_duration_blinks = blinks_filtrati['duration [ms]'].mean().round(2) if not blinks_filtrati.empty else np.nan

        num_saccades = len(saccades_filtrati)
        avg_duration_saccades = saccades_filtrati['duration [ms]'].mean().round(2) if not saccades_filtrati.empty else np.nan

        avg_pupil_diameter_mm = eye_states_filtrati['pupil diameter [mm]'].mean().round(2) if not eye_states_filtrati.empty else np.nan

        avg_gyro_x = imu_filtrato['gyro x [deg/s]'].mean().round(2) if not imu_filtrato.empty else np.nan
        avg_gyro_y = imu_filtrato['gyro y [deg/s]'].mean().round(2) if not imu_filtrato.empty else np.nan
        avg_gyro_z = imu_filtrato['gyro z [deg/s]'].mean().round(2) if not imu_filtrato.empty else np.nan
        avg_accel_x = imu_filtrato['acceleration x [G]'].mean().round(2) if not imu_filtrato.empty else np.nan
        avg_accel_y = imu_filtrato['acceleration y [G]'].mean().round(2) if not imu_filtrato.empty else np.nan
        avg_accel_z = imu_filtrato['acceleration z [G]'].mean().round(2) if not imu_filtrato.empty else np.nan

        # Calcola il tempo in secondi
        time_seconds = (SC9_ts - EC1_ts) / 1e9

        # Aggiungi le statistiche al DataFrame finale
        final_df[f'Giro {giro}'] = [round(time_seconds, 2), num_fixations, round(avg_duration_fixations, 2),
                                   num_blinks, round(avg_duration_blinks, 2),
                                   num_saccades, round(avg_duration_saccades, 2),
                                   round(avg_pupil_diameter_mm, 2),
                                   round(avg_gyro_x, 2), round(avg_gyro_y, 2), round(avg_gyro_z, 2),
                                   round(avg_accel_x, 2), round(avg_accel_y, 2), round(avg_accel_z, 2)]

    return final_df

def CalcoloTotale(EventsDF, FixationsDF, BlinksDF, SaccadesDF, EyeStatesDF, ImuDF, cartella):
    # Utilizza la funzione per calcolare le statistiche dei giri e creare un DataFrame
    GiroDF = StatisticheGiri(EventsDF, FixationsDF, BlinksDF, SaccadesDF, EyeStatesDF, cartella)
    
    # Utilizza la funzione per calcolare le statistiche della curva 1 e creare un DataFrame
    Curva1DF = StatisticheCurva1(EventsDF, FixationsDF, BlinksDF, SaccadesDF, EyeStatesDF, cartella)
    
    # Utilizza la funzione per calcolare le statistiche della curva 9 e creare un DataFrame
    Curva9DF = StatisticheCurva9(EventsDF, FixationsDF, BlinksDF, SaccadesDF, EyeStatesDF, cartella)
    
    # Utilizza la funzione per calcolare le statistiche del settore 1 e creare un DataFrame
    Settore1DF = StatisticheSettore1(EventsDF, FixationsDF, BlinksDF, SaccadesDF, EyeStatesDF, ImuDF, cartella)
    
    # Concatena i DataFrame di GiroDF, Curva1DF, Curva9DF e Settore1DF
    FinalDF = pd.concat([GiroDF, Curva1DF, Curva9DF, Settore1DF], ignore_index=True)
    
    return FinalDF

def Principal():
    for cartella in cartelle:
        # Imposta la path per questa cartella
        current_folder_path = os.path.join(StartPath, cartella)
        
        # Carica i file CSV per questa cartella
        EventsDF = pd.read_csv(os.path.join(current_folder_path, filenames[0]), header=0)
        FixationsDF = pd.read_csv(os.path.join(current_folder_path, filenames[1]), header=0)
        BlinksDF = pd.read_csv(os.path.join(current_folder_path, filenames[2]), header=0)
        SaccadesDF = pd.read_csv(os.path.join(current_folder_path, filenames[3]), header=0)
        EyeStatesDF = pd.read_csv(os.path.join(current_folder_path, filenames[4]), header=0)
        ImuDF = pd.read_csv(os.path.join(current_folder_path, filenames[5]), header=0)
        
        # Ottiene il nome dell'ultima cartella dalla path iniziale
        folder_name = os.path.basename(os.path.normpath(current_folder_path))
        
        # Crea il nome del file Excel utilizzando il nome della cartella
        excel_file_name = folder_name + ".xlsx"
        
        # Calcola il DataFrame finale per questa cartella
        FinalDF = CalcoloTotale(EventsDF, FixationsDF, BlinksDF, SaccadesDF, EyeStatesDF,ImuDF,cartella)
        
        # Azzeramento dei DataFrame
        EventsDF = FixationsDF = BlinksDF = SaccadesDF = EyeStatesDF = ImuDF = None
        
        # Salva il DataFrame nel file Excel utilizzando il percorso di destinazione
        SaveDFtoExcel(FinalDF, DestinationPath, excel_file_name)

        print("File Excel salvato con successo per la cartella:", folder_name)

Principal()