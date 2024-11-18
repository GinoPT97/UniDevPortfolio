import os
import json
import shutil
import logging
import zipfile
import tempfile
from flask import Flask, request, jsonify
import threading
import tkinter as tk
from tkinter import filedialog, messagebox
from tkinter.ttk import Progressbar

LOG_FORMAT = "%(asctime)s - %(levelname)s - %(message)s"
logging.basicConfig(level=logging.INFO, format=LOG_FORMAT)

app = Flask(__name__)

def create_folder(folder_path: str) -> None:
    """Crea una cartella nel percorso specificato, se non esiste già."""
    try:
        os.makedirs(folder_path, exist_ok=True)
        logging.info(f"Creata cartella: {folder_path}")
    except OSError as e:
        logging.error(f"Errore durante la creazione della cartella {folder_path}: {e}")

def extract_title_and_local_folder_name(json_data: dict) -> tuple:
    """Estrae il titolo e il nome della cartella locale dai dati JSON forniti."""
    title = json_data.get("title", "")
    local_folder_name = json_data.get("googlePhotosOrigin", {}).get("mobileUpload", {}).get("deviceFolder", {}).get("localFolderName", "Camera").strip()
    logging.info(f"Estratto titolo: {title}, nome cartella locale: {local_folder_name}")
    return title, local_folder_name

def move_file(src: str, dest: str) -> None:
    """Sposta un file dalla sorgente alla destinazione specificata."""
    try:
        shutil.move(src, dest)
        logging.info(f"Spostato {os.path.basename(src)} in {dest}")
    except FileNotFoundError:
        logging.error(f"Errore: File di origine non trovato: {src}")
    except Exception as e:
        logging.error(f"Errore durante lo spostamento di {os.path.basename(src)}: {e}")

def process_json_file(file_path: str, data: dict, json_files_directory: str) -> None:
    """Elabora un file JSON, estrae il titolo e il nome della cartella locale e sposta il file in una directory specificata."""
    if not file_path.endswith('.json'):
        return  # Ignora file non JSON

    try:
        with open(file_path, "r") as f:
            json_data = json.load(f)
            title, local_folder_name = extract_title_and_local_folder_name(json_data)
            data[title] = local_folder_name
            move_file(file_path, os.path.join(json_files_directory, "Json", os.path.basename(file_path)))
            logging.info(f"File elaborato: {file_path}")
    except (json.JSONDecodeError, OSError) as e:
        logging.error(f"Errore durante l'elaborazione del file {file_path}: {e}")

def unify_json_files(json_files_directory: str, output_json_path: str) -> str:
    """Unifica più file JSON in un singolo file JSON."""
    data = {}

    for file_name in os.listdir(json_files_directory):
        file_path = os.path.join(json_files_directory, file_name)
        process_json_file(file_path, data, json_files_directory)

    with open(output_json_path, "w") as f:
        json.dump(data, f, indent=2)
        logging.info(f"Creato file JSON unificato: {output_json_path}")

    return output_json_path

@app.route('/unify', methods=['POST'])
def unify_files():
    """API per unificare i file JSON inviati via POST."""
    json_files_directory = request.json.get('json_files_directory')
    output_json_path = request.json.get('output_json_path')

    if not json_files_directory or not output_json_path:
        return jsonify({"error": "Missing parameters"}), 400

    output_json_path = unify_json_files(json_files_directory, output_json_path)
    return jsonify({"status": "success", "output_json_path": output_json_path}), 200

def extract_zip(zip_path: str, extract_to: str) -> None:
    """Estrae un file ZIP nella directory specificata."""
    try:
        with zipfile.ZipFile(zip_path, 'r') as zip_ref:
            zip_ref.extractall(extract_to)
            logging.info(f"File ZIP estratto in: {extract_to}")
    except zipfile.BadZipFile as e:
        logging.error(f"Errore durante l'estrazione del file ZIP {zip_path}: {e}")
    except Exception as e:
        logging.error(f"Errore durante l'estrazione del file ZIP {zip_path}: {e}")

def create_folders_from_json(json_path: str, base_path: str) -> None:
    """Crea cartelle in base ai dati JSON."""
    try:
        with open(json_path, "r") as f:
            data = json.load(f)
            for title, folder_name in data.items():
                folder_path = os.path.join(base_path, folder_name)
                create_folder(folder_path)
                logging.info(f"Creata cartella: {folder_path}")
    except (json.JSONDecodeError, OSError) as e:
        logging.error(f"Errore durante la creazione delle cartelle dal file JSON {json_path}: {e}")

def process_unified_data(json_path: str, google_photos_directory: str, base_path: str) -> None:
    """Elabora i dati unificati e sposta i file nelle cartelle appropriate."""
    try:
        with open(json_path, "r") as f:
            data = json.load(f)
            for title, folder_name in data.items():
                folder_path = os.path.join(base_path, folder_name)
                for root, _, files in os.walk(google_photos_directory):
                    for file in files:
                        if file.startswith(title):
                            move_file(os.path.join(root, file), folder_path)
    except (json.JSONDecodeError, OSError) as e:
        logging.error(f"Errore durante l'elaborazione dei dati unificati dal file JSON {json_path}: {e}")

def start_process(base_path_folders: str, google_photos_path: str, is_zip: bool, progress: Progressbar, root: tk.Tk) -> None:
    """Avvia il processo di unificazione ed elaborazione dei file JSON."""
    create_folder(base_path_folders)

    if is_zip:
        with tempfile.TemporaryDirectory() as temp_dir:
            extract_zip(google_photos_path, temp_dir)
            google_photos_directory = temp_dir
            output_json_path = unify_json_files(google_photos_directory, os.path.join(base_path_folders, "JsonUnify.json"))
    else:
        google_photos_directory = google_photos_path
        output_json_path = unify_json_files(google_photos_directory, os.path.join(base_path_folders, "JsonUnify.json"))

    create_folders_from_json(output_json_path, base_path_folders)
    process_unified_data(output_json_path, google_photos_directory, base_path_folders)

    progress.stop()
    messagebox.showinfo("Completato", "Processo completato con successo!")
    root.quit()

def select_folder(var: tk.StringVar) -> None:
    """Permette all'utente di selezionare o creare una cartella."""
    selected_path = filedialog.askdirectory()
    if selected_path:
        var.set(selected_path)

def main() -> None:
    """Funzione principale che coordina l'esecuzione dello script."""
    root = tk.Tk()
    root.title("Organizzazione File JSON")
    root.geometry("500x400")

    tk.Label(root, text="Seleziona il percorso di partenza:").pack(pady=10)
    base_path_folders = tk.StringVar()
    tk.Entry(root, textvariable=base_path_folders, width=50).pack(pady=5)
    tk.Button(root, text="Sfoglia", command=lambda: select_folder(base_path_folders)).pack(pady=5)

    tk.Label(root, text="Seleziona il percorso di Google Foto (cartella o ZIP):").pack(pady=10)
    google_photos_path = tk.StringVar()
    tk.Entry(root, textvariable=google_photos_path, width=50).pack(pady=5)
    tk.Button(root, text="Sfoglia", command=lambda: select_folder(google_photos_path)).pack(pady=5)

    is_zip = tk.BooleanVar()
    tk.Checkbutton(root, text="È un file ZIP", variable=is_zip).pack(pady=5)

    progress = Progressbar(root, mode='indeterminate')

    def on_start() -> None:
        """Funzione per avviare il processo e mostrare la barra di progressione."""
        progress.pack(pady=20)
        progress.start()
        start_process(base_path_folders.get(), google_photos_path.get(), is_zip.get(), progress, root)

    tk.Button(root, text="Inizia", command=on_start).pack(pady=10)

    # Avvia l'API in un thread separato
    threading.Thread(target=lambda: app.run(debug=False, use_reloader=False)).start()

    root.mainloop()

if __name__ == "__main__":
    main()