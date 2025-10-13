def somma_lista(numeri: list) -> float:
    """
    Calcola la somma di una lista di numeri.
    :param numeri: lista di numeri (int o float)
    :return: somma dei numeri
    """
    return sum(numeri)

if __name__ == "__main__":
    # Prima lista di rimborsi
    RImborso = [37, 57, 79, 63, 61, 57, 61, 70, 63, 61, 79, 63, 63, 63, 63, 61, 63, 70, 63, 79, 
                61, 63, 57, 61, 63, 70, 70, 57, 61, 61, 63, 61, 63, 37, 61, 89, 63, 63, 61, 57, 
                63, 57, 61, 57, 61, 63, 70, 63, 70, 63, 70, 89, 63, 63, 63, 57, 61, 63]
    
    # Seconda lista di rimborsi (inserisci qui i tuoi dati)
    Pagato = [573, 173, 118, 63, 131, 205, 124, 372, 142, 57, 127, 61, 63, 98, 89, 183, 114, 63,
              194, 203, 152, 120]
    
    # Calcolo somme
    somma_1 = somma_lista(RImborso)
    somma_2 = somma_lista(Pagato)
    differenza = somma_1 - somma_2

    print(f"Prima sommatoria: {len(RImborso)} rimborsi = €{somma_1}")
    print(f"Seconda sommatoria: {len(Pagato)} rimborsi = €{somma_2}")
    print(f"Differenza: €{differenza}")