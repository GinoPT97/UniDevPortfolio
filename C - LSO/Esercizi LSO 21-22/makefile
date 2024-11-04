CC = gcc # Compilatore da usare
CFLAGS = -Wall -Werror -O2 -g -fstack-clash-protection -fstack-protector-all
# Per una lista più ampia https://developers.redhat.com/blog/2018/03/21/compiler-and-linker-flags-gcc
LDLIBS = -lm # Flag da usare per il linker
OBJECTFILES =
TARGET = result # nome dell'eseguibile prodotto
#SRC = $(wildcard *.c) # Compiliamo solo i file *.c
SRC = ReverseBuffered.c
FILE = $(shell echo -e "CIAO CIAO\nciao ciao\nCiao Ciao" > file)

all: $(TARGET) $(FILE)
	@printf "\e[32m%b\e[0m" "Esecuzione del file completata\n"
	@printf "\e[32m%b\e[0m" "Creazione del file completata\n"


$(TARGET) : $(OBJECTFILESS)
	$(CC) $(CFLAGS) -o $(TARGET) $(OBJECTFILES) $(LDLIBS) $(SRC)

.PHONY: depend clean # Controlliamo che non sia presente realmente un file chiamato clean

clean:
	rm -f $(OBJECTFILES) $(TARGET) $(FILE) ~*
