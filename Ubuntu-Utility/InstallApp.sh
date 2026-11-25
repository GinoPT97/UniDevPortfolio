#!/bin/bash
set -e

echo "Aggiornamento del sistema..."

sudo apt update && sudo apt upgrade -y
sudo apt install -y \
  ca-certificates curl gnupg lsb-release wget apt-transport-https net-tools \
  zram-config \
  build-essential git git-lfs git-filter-repo cmake gdebi dos2unix zenity linux-tools-common \
  openjdk-21-jdk \
  postgresql redis tor \
  vlc gparted \
  graphviz latexmk \
  texlive-latex-base texlive-latex-extra texlive-latex-recommended texlive-fonts-recommended texlive-pictures texlive-science \
  hplip hplip-gui \
  gnome-tweaks ptyxis stacer bleachbit \
  language-pack-it language-pack-it-base language-pack-gnome-it language-pack-gnome-it-base \
  mythes-it hyphen-it libreoffice-l10n-it libreoffice-help-it ripgrep scrcpy

echo "Installazione di VirtualBox e strumenti multimediali..."
sudo apt install virtualbox -y
sudo apt install mpv curl ffmpeg -y

git lfs install

echo "Configurazione della password PostgreSQL..."
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'postgres';"

# Avvio e abilitazione servizi Tor e Redis
sudo systemctl start tor && sudo systemctl enable tor
sudo systemctl start redis-server && sudo systemctl enable redis-server

install_deb() {
  local url=$1
  local file="/tmp/$(basename "$url")"
  echo ">>> Installazione $(basename "$url")..."
  wget -q --show-progress "$url" -O "$file" || { echo "ERRORE: download fallito"; return 1; }
  sudo dpkg -i "$file" || sudo apt-get install -f -y
  rm -f "$file"
}

install_deb "https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb"
install_deb "https://github.com/shiftkey/desktop/releases/download/release-2.8.1-linux2/GitHubDesktop-linux-2.8.1-linux2.deb"

git config --global user.email "g.pandozzitrani@studenti.unina.it"                                  
git config --global user.name "kenobi1797"

#Installazione di code tramite apt (formato deb822, coerente con quello che apt genera di default)
wget -qO- https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > microsoft.gpg
sudo install -o root -g root -m 644 microsoft.gpg /usr/share/keyrings/
rm -f microsoft.gpg

sudo tee /etc/apt/sources.list.d/vscode.sources > /dev/null << 'EOF'
Types: deb
URIs: https://packages.microsoft.com/repos/code
Suites: stable
Components: main
Architectures: amd64
Signed-By: /usr/share/keyrings/microsoft.gpg
EOF

sudo apt update
sudo apt install code -y

echo "Installazione estensioni VS Code..."
VSCODE_EXTENSIONS=(
  abians.prisma-generate-uml
  alefragnani.project-manager
  anthropic.claude-code
  christian-kohler.npm-intellisense
  christian-kohler.path-intellisense
  codezombiech.gitignore
  cschlosser.doxdocgen
  databricks.neon-local-connect
  dbaeumer.vscode-eslint
  dsznajder.es7-react-js-snippets
  eamodio.gitlens
  formulahendry.docker-explorer
  formulahendry.docker-extension-pack
  github.vscode-github-actions
  github.vscode-pull-request-github
  james-yu.latex-workshop
  jasonnutter.search-node-modules
  jawandarajbir.react-vscode-extension-pack
  jebbs.plantuml
  mathiasfrohlich.kotlin
  mhutchie.git-graph
  ms-azuretools.vscode-bicep
  ms-azuretools.vscode-containers
  ms-azuretools.vscode-docker
  ms-ceintl.vscode-language-pack-it
  ms-dotnettools.csharp
  ms-dotnettools.vscode-dotnet-runtime
  ms-playwright.playwright
  ms-python.debugpy
  ms-python.python
  ms-python.vscode-pylance
  ms-python.vscode-python-envs
  ms-vscode-remote.remote-containers
  ms-vscode.cmake-tools
  ms-vscode.cpp-devtools
  ms-vscode.cpptools
  ms-vscode.cpptools-extension-pack
  ms-vscode.cpptools-themes
  ms-vscode.makefile-tools
  ms-vscode.remote-repositories
  ms-vscode.vscode-typescript-next
  naco-siren.gradle-language
  redhat.java
  redhat.vscode-xml
  richardwillis.vscode-gradle-extension-pack
  rust-lang.rust-analyzer
  segerdekort.angular-cli
  sohibe.java-generate-setters-getters
  sonarsource.sonarlint-vscode
  sondermx.java-kits
  steoates.autoimport
  tomoki1207.pdf
  tomoyukim.vscode-mermaid-editor
  typescriptteam.native-preview
  vmware.vscode-spring-boot
  vscjava.vscode-gradle
  vscjava.vscode-java-debug
  vscjava.vscode-java-dependency
  vscjava.vscode-java-pack
  vscjava.vscode-java-test
  vscjava.vscode-maven
  vscjava.vscode-spring-boot-dashboard
  vscjava.vscode-spring-initializr
  xabikos.javascriptsnippets
  xabikos.reactsnippets
  yangbaopan.vscode-java-ibatisx
)
for ext in "${VSCODE_EXTENSIONS[@]}"; do
  code --install-extension "$ext" --force
done

#Installazione di Rust
echo "Installazione di Rust..."
curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y
source $HOME/.cargo/env

echo "Installazione applicazioni Snap..."
sudo snap install --classic openjdk
#sudo snap install --classic code
sudo snap install --classic android-studio
sudo snap install --classic eclipse
sudo snap install intellij-idea-ultimate --classic
sudo snap install node --classic
sudo snap install telegram-desktop teams-for-linux pgadmin4 perplexity-desktop docker
sudo snap install firefox thunderbird steam

echo "Installazione di Docker..."
sudo snap start docker
sudo groupadd docker || true
sudo usermod -aG docker "$USER"

echo "Installazione completata!"
