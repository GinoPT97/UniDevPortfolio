import easyocr
import cv2
import re
import pandas as pd
import os

# Funzione per caricare e pre-processare l'immagine
def load_and_process_image(image_path):
    """Carica e pre-processa l'immagine (conversione a grigio e sfocatura)."""
    image = cv2.imread(image_path)
    if image is None:
        raise FileNotFoundError(f"Immagine non trovata al percorso: {image_path}")
    gray_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    processed_image = cv2.GaussianBlur(gray_image, (5, 5), 0)
    return processed_image

# Funzione per estrarre dati con regex
def extract_data_from_text(text):
    """Estrae data, rimborso e stato da un testo usando espressioni regolari."""
    date_match = re.search(r'(\d{2}/\d{2}/\d{4})', text)
    rimborso_match = re.search(r'(\d+,\d{2})\s*€', text)
    stato_match = re.search(r'(Omologata|Non Omologata)', text)

    if date_match and rimborso_match and stato_match:
        return {
            'Data': date_match.group(1),
            'Rimborso': float(rimborso_match.group(1).replace(',', '.')),
            'Stato': stato_match.group(1)
        }
    return None

# Funzione per eseguire OCR
def perform_ocr(image):
    """Esegue il riconoscimento del testo sull'immagine."""
    reader = easyocr.Reader(['it'])
    result = reader.readtext(image)
    return result

# Funzione per salvare i dati in un file di testo
def save_to_file(data, total, filename='rimborsi_estratti.txt'):
    """Salva i dati estratti su un file di testo."""
    with open(filename, 'w') as file:
        for entry in data:
            file.write(f"{entry['Data']} - {entry['Rimborso']}€ - {entry['Stato']}\n")
        file.write(f"\nTotale rimborsi: {total}€")
    print(f"\nDati salvati in '{filename}'.")

# Funzione principale
def main():
    # 1. Chiedi il percorso dell'immagine
    image_path = input("Inserisci il percorso dell'immagine: ")

    # 2. Caricamento e pre-elaborazione dell'immagine
    try:
        processed_image = load_and_process_image(image_path)
    except FileNotFoundError as e:
        print(e)
        return

    # 3. Esecuzione di EasyOCR per estrarre il testo
    result = perform_ocr(processed_image)

    # 4. Estrazione dei dati da ogni testo trovato
    data = []
    for detection in result:
        text = detection[1]
        extracted = extract_data_from_text(text)
        if extracted:
            data.append(extracted)

    # 5. Creazione di un DataFrame per organizzare i dati
    if not data:
        print("Nessun dato valido trovato.")
        return

    df = pd.DataFrame(data)

    # 6. Calcolo del totale dei rimborsi
    total = df['Rimborso'].sum()

    # 7. Output dei dati
    print("\nDati estratti:")
    print(df)
    print(f'\nTotale rimborsi: {total}€')

    # 8. Salvataggio su file di testo
    save_to_file(data, total)

if __name__ == "__main__":
    main()


