#!/bin/bash

# Modifica le autorizzazioni sul file chiave SSH privata
sudo chmod 600 ~/.ssh/id_rsa

# Aggiungi la chiave SSH privata all'agente SSH
ssh-add ~/.ssh/id_rsa

# Verifica che la chiave SSH privata sia stata aggiunta all'agente SSH
ssh-add -l
