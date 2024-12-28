import os
import json
import shutil
import zipfile
import logging
import argparse

# Configura il logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def extract_zip(zip_path, extract_to):
    """Estrae il contenuto di un file ZIP."""
    with zipfile.ZipFile(zip_path, 'r') as zip_ref:
        zip_ref.extractall(extract_to)
    logging.info(f"File ZIP estratti in {extract_to}")

def get_folder_from_json(json_file):
    """Estrai il nome della cartella originale dal file JSON."""
    try:
        with open(json_file, "r", encoding='utf-8') as f:
            data = json.load(f)
        return data.get("googlePhotosOrigin", {}).get(
            "mobileUpload", {}).get("deviceFolder", {}).get("localFolderName", None)
    except (json.JSONDecodeError, FileNotFoundError) as e:
        logging.warning(f"Errore nel decodificare o leggere il file JSON: {json_file} - {e}")
        return None

def process_takeout(temp_dir, output_dir):
    """Processa i file JSON di Google Takeout per ricreare la struttura delle cartelle."""
    json_processed = 0
    media_processed = 0

    for root, _, files in os.walk(temp_dir):
        for file in files:
            file_path = os.path.join(root, file)

            if file.endswith(".json"):
                folder_name = get_folder_from_json(file_path)

                if folder_name:
                    target_folder = os.path.join(output_dir, folder_name)
                    os.makedirs(target_folder, exist_ok=True)

                    media_file = file.replace(".json", "")
                    media_path = os.path.join(root, media_file)

                    if os.path.exists(media_path):
                        shutil.move(media_path, os.path.join(target_folder, media_file))
                        media_processed += 1
                        logging.info(f"Spostato: {media_file} in {target_folder}")

                    shutil.move(file_path, os.path.join(target_folder, file))
                    json_processed += 1
                else:
                    logging.warning(f"Cartella originale non trovata per il JSON: {file}")

    logging.info(f"Processati {json_processed} file JSON e {media_processed} file multimediali.")

def main(zip_file_path, output_dir):
    """Funzione principale che gestisce l'estrazione e il processo."""
    os.makedirs(output_dir, exist_ok=True)

    if not os.path.exists(zip_file_path):
        logging.error(f"Il file ZIP non esiste: {zip_file_path}")
        return

    temp_dir = os.path.join(output_dir, 'takeout')
    extract_zip(zip_file_path, temp_dir)
    process_takeout(temp_dir, output_dir)
    shutil.rmtree(temp_dir)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Processa un file ZIP di Google Takeout per ricreare la struttura delle cartelle.")
    parser.add_argument("zip_file_path", help="Percorso al file ZIP di Google Foto Takeout")
    parser.add_argument("output_dir", help="Cartella di destinazione per i file processati")

    args = parser.parse_args()
    main(args.zip_file_path, args.output_dir)
