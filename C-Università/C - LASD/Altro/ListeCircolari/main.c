#include <stdio.h>
#include <stdlib.h>
#include "listeCircolari.h"

/* run this program using the console pauser or add your own getch, system("pause") or input loop */

int main(int argc, char *argv[]) {
	
    Node *prova = newList(5);
    printList(prova, prova, prova->prev);
    prova = insHead(prova, newNode());
    printList(prova, prova, prova->prev);
    prova = removeHead(prova);
    printList(prova, prova, prova->prev);
    insTail(prova, newNode());
    printList(prova, prova, prova->prev);
    printf("La coda e': %d\n", getTail(prova)->val);
    removeTail(prova);
    printList(prova, prova, prova->prev);

	return 0;
}
