#!/bin/bash

# Elenco delle repository
repos=(
  "/home/kenobi/Documenti/GitHub/RealDevPortfolio"
  "/home/kenobi/Documenti/GitHub/UniDevPortfolio"
)

# Funzione per eseguire pull
pull_repo() {
  local repo_path=$1
  echo "Eseguendo pull nella repository: $repo_path"
  cd "$repo_path" || exit
  git pull origin main
}

# Funzione per eseguire push
push_repo() {
  local repo_path=$1
  echo "Eseguendo push nella repository: $repo_path"
  cd "$repo_path" || exit
  git push origin main
}

# Esegui il pull per entrambe le repository
for repo in "${repos[@]}"; do
  pull_repo "$repo"
done

# Esegui il push per entrambe le repository
for repo in "${repos[@]}"; do
  push_repo "$repo"
done
