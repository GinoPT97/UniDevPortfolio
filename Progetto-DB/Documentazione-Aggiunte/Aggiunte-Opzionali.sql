CREATE VIEW infoClienti (CodCliente, Nome, Cognome, CodiceFiscale, ViaResidenza, ComuneResidenza, CAP, Provincia, CodTessera, PuntiTessera, )

-- Query per la ricerca dei clienti (o dipendenti) in base alla loro residenza

SELECT C.Nome, C.Cognome
FROM CLIENTE AS C
WHERE SPLIT_PART(Indirizzo, '-', <campoInteressato>) <condizione>
