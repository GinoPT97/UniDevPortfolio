import os
import json
import shutil
import logging

LOG_FORMAT = "%(asctime)s - %(levelname)s - %(message)s"
logging.basicConfig(level=logging.INFO, format=LOG_FORMAT)

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

def process_json_file(file_path: str, data: dict, json_files_directory: str) -> None:
    """Elabora un file JSON, estrae il titolo e il nome della cartella locale e sposta il file in una directory specificata."""
    if not file_path.endswith('.json'):
        return  # Ignora file non JSON

    try:
        with open(file_path, "r") as f:
            json_data = json.load(f)
            title, local_folder_name = extract_title_and_local_folder_name(json_data)
            data[title] = local_folder_name
            shutil.move(file_path, os.path.join(json_files_directory, "Json", os.path.basename(file_path)))
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

def organize_json_files(json_files_directory: str) -> None:
    """Sposta i file JSON in una directory specificata."""
    json_destination_directory = os.path.join(json_files_directory, "Json")
    create_folder(json_destination_directory)

    for file_name in os.listdir(json_files_directory):
        if file_name.endswith('.json'):
            source_path = os.path.join(json_files_directory, file_name)
            destination_path = os.path.join(json_destination_directory, file_name)
            try:
                shutil.move(source_path, destination_path)
                logging.info(f"Spostato {file_name} in {json_destination_directory}")
            except FileNotFoundError:
                logging.error(f"Errore: File di origine non trovato: {source_path}")

def copy_files_to_destination(source_directory: str, destination_directory: str, title: str) -> None:
    """Copia i file da una directory di origine a una directory di destinazione in base al titolo."""
    for file_name in os.listdir(source_directory):
        if title in file_name and file_name.lower().endswith(('.jpeg', '.jpg', '.png', '.mp4')):
            source_path = os.path.join(source_directory, file_name)
            destination_path = os.path.join(destination_directory, file_name)
            try:
                shutil.move(source_path, destination_path)
                logging.info(f"Spostato {file_name} in {destination_directory}")
            except FileNotFoundError:
                logging.error(f"Errore: File di origine non trovato: {source_path}")

def move_screenshot_files(source_directory: str, destination_directory: str) -> None:
    """Sposta i file di screenshot da una directory di origine a una directory di destinazione."""
    create_folder(destination_directory)

    for file_name in os.listdir(source_directory):
        if "Screenshot" in file_name:
            source_path = os.path.join(source_directory, file_name)
            destination_path = os.path.join(destination_directory, file_name)
            try:
                shutil.move(source_path, destination_path)
                logging.info(f"Spostato {file_name} in {destination_directory}")
            except FileNotFoundError:
                logging.error(f"Errore: File di origine non trovato: {source_path}")

def process_unified_data(json_file_path: str, source_directory: str, base_path_destination: str) -> None:
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

    for title, local_folder_name in unified_data.items():
        destination_directory = os.path.join(base_path_destination, local_folder_name)
        create_folder(destination_directory)
        copy_files_to_destination(source_directory, destination_directory, title)

    screenshot_source_path = os.path.join(base_path_destination, "Camera")
    screenshot_destination_path = os.path.join(base_path_destination, "Screenshot")
    move_screenshot_files(screenshot_source_path, screenshot_destination_path)

def create_folders_from_json(json_file_path: str, base_path_destination: str) -> None:
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

    for folder_name in set(data.values()):  # Usa set per evitare cartelle duplicate
        folder_path = os.path.join(base_path_destination, folder_name)
        create_folder(folder_path)

def main() -> None:
    """Funzione principale che coordina l'esecuzione dello script."""
    base_path_folders = os.path.join("/home", "kenobi", "Immagini", "Percorsi-originali")
    google_photos_directory = os.path.join("/media", "kenobi", "Google Photos")

    json_files_directory = os.path.join(base_path_folders, "Json")
    output_json_path = os.path.join(json_files_directory, "unified_data.json")

    create_folder(json_files_directory)
    unified_json_file_path = unify_json_files(json_files_directory, output_json_path)

    base_path_destination = os.path.join(base_path_folders, "Destinazione")
    create_folders_from_json(unified_json_file_path, base_path_destination)
    process_unified_data(unified_json_file_path, google_photos_directory, base_path_destination)

if __name__ == "__main__":
    main()
