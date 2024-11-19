#!/bin/bash

# Lista delle repository (una per riga per maggiore chiarezza)
repos=(
  "$HOME/Documenti/GitHub/UniDevPortfolio"
  "$HOME/Documenti/GitHub/RealDevPortfolio"
)

# Funzione per eseguire pull
pull_repo() {
  local repo_path=$1
  echo "Eseguendo pull in: $repo_path"
  pushd "$repo_path" > /dev/null || exit
  git pull -q origin main  # Opzione -q per output ridotto
  popd > /dev/null || exit
}

# Funzione per eseguire push
push_repo() {
  local repo_path=$1
  echo "Eseguendo push in: $repo_path"
  pushd "$repo_path" > /dev/null || exit
  git push -q origin main  # Opzione -q per output ridotto
  popd > /dev/null || exit
}

# Esegui il pull e il push per ogni repository nella lista
for repo in "${repos[@]}"; do
  pull_repo "$repo"
  push_repo "$repo"
done
