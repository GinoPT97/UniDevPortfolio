#!/bin/bash

# Repository specifiche
repo1="$HOME/Documenti/GitHub/UniDevPortfolio"
repo2="$HOME/Documenti/GitHub/RealDevPortfolio"

# Funzione per verificare che l'URL remoto sia configurato per SSH
check_ssh_url() {
  local repo_path=$1
  local remote_url
  remote_url=$(git -C "$repo_path" remote get-url origin)

  if [[ "$remote_url" =~ ^git@github.com: ]]; then
    echo "La repository $repo_path è configurata correttamente con SSH: $remote_url"
  else
    echo "Attenzione: La repository $repo_path non è configurata per SSH. L'URL remoto è $remote_url."
    echo "Modificando l'URL remoto per usare SSH..."
    git -C "$repo_path" remote set-url origin git@github.com:$(basename "$repo_path" .git)/$(basename "$repo_path").git
  fi
}

# Funzione per eseguire pull
pull_repo() {
  local repo_path=$1
  check_ssh_url "$repo_path"
  echo "Eseguendo pull nella repository: $repo_path"
  pushd "$repo_path" || exit
  git pull origin main
  popd || exit
}

# Funzione per eseguire push
push_repo() {
  local repo_path=$1
  check_ssh_url "$repo_path"
  echo "Eseguendo push nella repository: $repo_path"
  pushd "$repo_path" || exit
  git push origin main
  popd || exit
}

# Esegui il pull per le repository specifiche
pull_repo "$repo1"
pull_repo "$repo2"

# Esegui il push per le repository specifiche
push_repo "$repo1"
push_repo "$repo2"
