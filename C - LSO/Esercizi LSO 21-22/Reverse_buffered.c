#include<stdio.h>
#include<errno.h>
#include<unistd.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<fcntl.h>
#include<time.h>
#include<stdlib.h>

int main(int argc , char *argv[] ){

/*-------------------------------------------------------------------------------------------------*/

    /*LOGICA DI ESECUZIONE
    - 1) Ottenere il file e controllare se ha dimensione = 0 byte (nel caso non ha senso procedere)
    - 2) Memorizzare il contenuto del file nel buffer di dimensione data
    - 3) Invertire l'ordine del contenuto del file e riportarlo sullo stdout
    */

/*-------------------------------------------------------------------------------------------------*/

/* 0) Breve check di correttezza dei parametri passati al programma */
(argv[1] == NULL || argc != 2) ? printf("Errore: file mancante\n") : printf("Passato come parametro il file: %s\n",argv[1]);

/* 1) Ottenere il file e controllare se ha dimensione = 0 byte (nel caso non ha senso procedere) */
int File_descriptor;
struct stat Informazioni;
ssize_t File_size;

/* 1.1) Chiamata alla funzione lstat per ottenere le informazioni sul file (in particolare la dimensione totale del file)*/
if(lstat(argv[1],&Informazioni) == -1){
    perror("Lstat error\n");
    exit(EXIT_FAILURE);
}
else{
    File_size = Informazioni.st_size;
    printf("Dimensione del file '%s': %ld\n",argv[1],File_size);
}

/* 1.2) Check sulla dimensione del file & rispettiva apertura */
if(File_size == 0){
    printf("File vuoto.\n");
    exit(EXIT_FAILURE);
}
else
    (File_descriptor = open(argv[1], O_RDONLY)) < 0 ? printf("Errore aprtura file\n") : printf("file aperto con successo!\n");




/* 2) Memorizzare il contenuto del file nel buffer di dimensione data */
char Buffer[File_size]; //buffer di dimensione del file 

/* 2.1) Chiamata alla funzione read per leggere il contenuto del file */
if(read(File_descriptor, Buffer , File_size) == -1 ){
    perror("Errore di lettura\n");
    exit(EXIT_FAILURE);
}
else   
    printf("Contenuto del file letto con successo !.\n");



/* 3) Invertire l'ordine del contenuto del file */

/* 3.1) Spostare l'offset alla fine del file*/
if(lseek(File_descriptor, 0 , SEEK_END) == -1){
    perror("Errore di seek\n");
    exit(EXIT_FAILURE);
}
else{
    printf("Offset spostato con successo alla posizione finale , cioè: %ld\n",lseek(File_descriptor , 0 , SEEK_END));
}

/* 3.2) Lettura al contrario dal buffer parallela alla scrittura su stdout */

/* Essendo il buffer un array di caratteri , al fine di visualizzare il contenuto in reverse è possibile scorrerlo al contrario con
un ciclo for e memorizare man mano l'elemento corrente in una variabile temp, il cui indirizzo viene passato alla funzione write che
ne visualizzerà il contenuto sullo stdout, al termine si avrà il contenuto del buffer e quindi del file in reverse. */

printf("\n\nCONTENUTO DEL FILE AL CONTRARIO\n\n");
for (int i = File_size - 1 ; i >= 0 ; i--){
    char temp = Buffer[i];
    if(write(STDOUT_FILENO , &temp , 1) == -1) {perror("Errore scrittura\n") ; exit(EXIT_FAILURE); }
}

printf("\n");
}


