#include <stdio.h>
#include <stdlib.h>
#include "../Input/inputReader.h"

struct TDList {
    int info;
    struct TDList* next;
    struct TDList* prev;
};

typedef struct TDList* ListDopp;

int CalcolaDim(ListDopp L);
void PopolaLista(ListDopp *l1,ListDopp *l2);
void printListD(ListDopp L);
void freeListD(ListDopp L);
ListDopp EliminaDoppia(ListDopp l, int x);
ListDopp inCoda (ListDopp lista, int el);
ListDopp inTesta (ListDopp lista, int el);
ListDopp newListD(int index);
ListDopp initNodeListD(int info);
