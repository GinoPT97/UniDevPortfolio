import os
import json
import shutil
import logging
import zipfile
import tempfile
import tkinter as tk
from tkinter import filedialog, messagebox
from tkinter.ttk import Progressbar

LOG_FORMAT = "%(asctime)s - %(levelname)s - %(message)s"
logging.basicConfig(level=logging.INFO, format=LOG_FORMAT)

def create_folder(folder_path):
    """Crea una cartella nel percorso specificato."""
    try:
        os.makedirs(folder_path, exist_ok=True)
        logging.info(f"Creata cartella: {folder_path}")
    except Exception as e:
        logging.error(f"Errore durante la creazione della cartella {folder_path}: {e}")

def extract_title_and_local_folder_name(json_data):
    """Estrae il titolo e il nome della cartella locale dai dati JSON forniti."""
    title = json_data.get("title", "")
    google_photos_origin = json_data.get("googlePhotosOrigin", {})
    mobile_upload = google_photos_origin.get("mobileUpload", {})
    device_folder = mobile_upload.get("deviceFolder", {})
    local_folder_name = device_folder.get("localFolderName", "").strip()

    logging.info(f"Estratto titolo: {title}, nome cartella locale: {local_folder_name}")
    return title, local_folder_name if local_folder_name else "Camera"

def move_file(src, dest):
    """Sposta un file dalla sorgente alla destinazione specificata."""
    try:
        shutil.move(src, dest)
        logging.info(f"Spostato {os.path.basename(src)} in {dest}")
    except FileNotFoundError:
        logging.error(f"Errore: File di origine non trovato: {src}")
    except Exception as e:
        logging.error(f"Errore durante lo spostamento di {os.path.basename(src)}: {e}")

def process_json_file(file_path, data, json_files_directory):
    """Elabora un file JSON, estrae il titolo e il nome della cartella locale e sposta il file in una directory specificata."""
    if file_path.endswith('.json'):
        try:
            with open(file_path, "r") as f:
                json_data = json.load(f)
                title, local_folder_name = extract_title_and_local_folder_name(json_data)
                data[title] = local_folder_name
                logging.info(f"File elaborato: {file_path}")
                move_file(file_path, os.path.join(json_files_directory, "Json", os.path.basename(file_path)))
        except json.JSONDecodeError as e:
            logging.error(f"Errore durante la decodifica JSON in {file_path}: {e}")
        except Exception as e:
            logging.error(f"Errore durante l'elaborazione del file {file_path}: {e}")

def unify_json_files(json_files_directory, output_json_path):
    """Unifica più file JSON in un singolo file JSON."""
    data = {}
    json_files = [f for f in os.listdir(json_files_directory) if f.endswith('.json')]

    for file_name in json_files:
        file_path = os.path.join(json_files_directory, file_name)
        process_json_file(file_path, data, json_files_directory)

    with open(output_json_path, "w") as f:
        json.dump(data, f, indent=2)
        logging.info(f"Creato file JSON unificato: {output_json_path}")

    return output_json_path

def organize_json_files(json_files_directory):
    """Sposta i file JSON in una directory specificata."""
    json_destination_directory = os.path.join(json_files_directory, "Json")
    create_folder(json_destination_directory)

    json_files = [f for f in os.listdir(json_files_directory) if f.endswith('.json')]
    for file_name in json_files:
        source_path = os.path.join(json_files_directory, file_name)
        move_file(source_path, os.path.join(json_destination_directory, file_name))

def copy_files_to_destination(source_directory, destination_directory, title, copied_files):
    """Copia i file da una directory di origine a una directory di destinazione in base al titolo e all'estensione del file."""
    valid_extensions = {'.jpeg', '.jpg', '.png', '.mp4'}
    files_to_copy = [f for f in os.listdir(source_directory) if title in f and os.path.splitext(f)[1].lower() in valid_extensions]

    for file_name in files_to_copy:
        source_path = os.path.join(source_directory, file_name)
        destination_path = os.path.join(destination_directory, file_name)
        move_file(source_path, destination_path)
        copied_files.add(file_name)

def move_screenshot_files(source_directory, destination_directory):
    """Sposta i file di screenshot da una directory di origine a una directory di destinazione."""
    create_folder(destination_directory)
    screenshot_files = [f for f in os.listdir(source_directory) if "Screenshot" in f]

    for file_name in screenshot_files:
        source_path = os.path.join(source_directory, file_name)
        destination_path = os.path.join(destination_directory, file_name)
        move_file(source_path, destination_path)

def process_unified_data(json_file_path, source_directory, base_path_destination):
    """Elabora i dati unificati da un file JSON e copia i file nelle directory di destinazione in base ai nomi delle cartelle locali."""
    if not os.path.exists(json_file_path):
        logging.error(f"Errore: File non trovato: {json_file_path}")
        return

    with open(json_file_path, "r") as f:
        try:
            unified_data = json.load(f)
        except json.JSONDecodeError as e:
            logging.error(f"Errore: Impossibile decodificare JSON in {json_file_path}: {e}")
            return

    copied_files = set()

    for title, local_folder_name in unified_data.items():
        destination_directory = os.path.join(base_path_destination, local_folder_name)
        create_folder(destination_directory)
        copy_files_to_destination(source_directory, destination_directory, title, copied_files)

    screenshot_source_path = os.path.join(base_path_destination, "Camera")
    screenshot_destination_path = os.path.join(base_path_destination, "Screenshot")
    move_screenshot_files(screenshot_source_path, screenshot_destination_path)

def create_folders_from_json(json_file_path, base_path_destination):
    """Crea cartelle in base ai nomi delle cartelle locali specificati in un file JSON."""
    if not os.path.exists(json_file_path):
        logging.error(f"Errore: File non trovato: {json_file_path}")
        return

    with open(json_file_path, "r") as f:
        try:
            data = json.load(f)
        except json.JSONDecodeError as e:
            logging.error(f"Errore: Impossibile decodificare JSON in {json_file_path}: {e}")
            return

    for folder_name in data.values():
        folder_path = os.path.join(base_path_destination, folder_name)
        create_folder(folder_path)

def extract_zip(zip_path, extract_to):
    """Estrae un file ZIP nella directory specificata."""
    try:
        with zipfile.ZipFile(zip_path, 'r') as zip_ref:
            zip_ref.extractall(extract_to)
            logging.info(f"File ZIP estratto in: {extract_to}")
    except zipfile.BadZipFile as e:
        logging.error(f"Errore durante l'estrazione del file ZIP {zip_path}: {e}")
    except Exception as e:
        logging.error(f"Errore durante l'estrazione del file ZIP {zip_path}: {e}")

def start_process(base_path_folders, google_photos_path, is_zip, progress, root):
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

def select_folder(var):
    """Permette all'utente di selezionare o creare una cartella."""
    selected_path = filedialog.askdirectory()
    if selected_path:
        var.set(selected_path)

def create_new_folder(var):
    """Permette all'utente di creare una nuova cartella."""
    new_folder = filedialog.asksaveasfilename(confirmoverwrite=False)
    if new_folder:
        os.makedirs(new_folder, exist_ok=True)
        var.set(new_folder)

def select_file(var):
    """Permette all'utente di selezionare un file ZIP o una cartella."""
    selected_path = filedialog.askopenfilename(filetypes=[("Tutti i file", "*.*"), ("File ZIP", "*.zip"), ("Cartelle", "")])
    if selected_path:
        var.set(selected_path)

def main():
    """Funzione principale che coordina l'esecuzione dello script."""
    root = tk.Tk()
    root.title("Organizzazione File JSON")
    root.geometry("500x400")

    tk.Label(root, text="Seleziona il percorso di partenza:").pack(pady=10)
    base_path_folders = tk.StringVar()
    tk.Entry(root, textvariable=base_path_folders, width=50).pack(pady=5)
    tk.Button(root, text="Sfoglia", command=lambda: select_folder(base_path_folders)).pack(pady=5)
    tk.Button(root, text="Crea Nuova Cartella", command=lambda: create_new_folder(base_path_folders)).pack(pady=5)

    tk.Label(root, text="Seleziona il percorso di Google Foto (cartella o ZIP):").pack(pady=10)
    google_photos_path = tk.StringVar()
    tk.Entry(root, textvariable=google_photos_path, width=50).pack(pady=5)
    tk.Button(root, text="Sfoglia", command=lambda: select_file(google_photos_path)).pack(pady=5)

    is_zip = tk.BooleanVar()
    tk.Checkbutton(root, text="È un file ZIP", variable=is_zip).pack(pady=5)

    progress = Progressbar(root, mode='indeterminate')

    def on_start():
        """Funzione per avviare il processo e mostrare la barra di progressione."""
        progress.pack(pady=20)
        progress.start()
        start_process(base_path_folders.get(), google_photos_path.get(), is_zip.get(), progress, root)

    tk.Button(root, text="Inizia", command=on_start).pack(pady=10)

    root.mainloop()

if __name__ == "__main__":
    main()
