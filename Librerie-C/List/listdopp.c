#include "listdopp.h"

int CalcolaDim(ListDopp L) {
    if (L == NULL) return 0;
    else return 1 + CalcolaDim(L->next);
}

ListDopp inCoda(ListDopp lista, int el) {
    ListDopp nodo = malloc(sizeof(struct TDList));
    nodo->info = el;
    nodo->prev = NULL;
    nodo->next = lista;
    if (lista != NULL) {
        lista->prev = nodo;
    }
    return nodo;
}

ListDopp RimuoviNodo(ListDopp l1) {
  if (l1->prev != NULL) {
    l1->prev->next = l1->next;
  }

  if (l1->next != NULL) {
    l1->next->prev = l1->prev;
  }

  l1->next = NULL;
  l1->prev = NULL;

  return l1->next;
}

ListDopp inTesta(ListDopp lista, int el) {
    ListDopp nodo = malloc(sizeof(struct TDList));
    nodo->info = el;
    nodo->prev = NULL;
    nodo->next = lista;
    if (lista != NULL) {
        lista->prev = nodo;
    }
    return nodo;
}

ListDopp EliminaDoppia(ListDopp l, int x) {
    if (l) {
        l->next = EliminaDoppia(l->next,x);
        if (l->info == x) {
            if (l->next)
                l->next->prev = l->prev;
            if (l->prev)
                l->prev->next = l->next;
            ListDopp tmp = l->next;
            free(l);
            l = tmp;
        }
    }
    return l;
}

ListDopp initNodeListD(int info) {
    ListDopp L = malloc(sizeof(struct TDList));
    L->info = info;
    L->prev = NULL;
    L->next = NULL;
    return L;
}

ListDopp newListD(int index) {
    ListDopp L = NULL;
    int i;
    for (i = 0; i < index; i++) {
        int el;
        printf("\nMancano %d elementi: ", index - i);
        scanf("%d", &el);
        L = inCoda(L, el); // inserimento in coda
    }
    return L;
}

void printListD(ListDopp L) {
    if (L != NULL && L->next != NULL) {
        printf("->[ %d ]<- ", L->info);
        printListD(L->next);
    } else {
    	printf("->[ %d ]", L->info);
	}
}

void freeListD(ListDopp L) {
    if (L != NULL) {
        freeListD(L->next);
        free(L);
    }
}

void PopolaLista(ListDopp* l1, ListDopp* l2) {
    int dim1, dim2;
    *l1 = *l2 = NULL;
    do {
        printf("Inserisci dimensione Lista L1:\n");
    } while (!getPositive(&dim1));
    *l1 = newListD(dim1);
    printf("\nStampa Lista L1 doppiamente concatenata iniziale, L:");
    printListD(*l1);
    do {
        printf("\nInserisci dimensione Lista L2:\n");
    } while (!getPositive(&dim2));
    *l2 = newListD(dim2);
    printf("\nStampa Lista L2 doppiamente concatenata iniziale, L:");
    printListD(*l2);
}
