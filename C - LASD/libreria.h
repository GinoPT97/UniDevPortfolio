#ifndef Library_libreria_h
#define Library_libreria_h
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <limits.h>
#include "libreria.h"
#include "../Librerie-C/Input/inputReader.h"
#include "../Librerie-C/Code/queue.h"
#include "../Librerie-C/BST/tree.h"
#include "../Librerie-C/Grafi/list.h"
#include "../Librerie-C/Grafi/graph.h"
#include "../Librerie-C/MatrixGraph/matrixgraph.h"
#include "../Librerie-C/Stack/stack.h"
#include "../Librerie-C/List/listdopp.h"
#include "../Librerie-C/Heap/heap.h"

struct Pree {
    int info;
    struct Pree *sx, *dx, *cx;
};

typedef struct Pree* tern;

//HEAP
void main090209();

//STACK
void main181208();
void main090714();

//CODE
void main10619();
void main1090209();
void main11018();
void main130616();

//LISTE
void mainesameinterleaving();
void main062019();
void main200606();
void main1181208();

//ALBERI
void main180217();
void main215615();
void main210612();

//GRAFI
void main30619();
void menu30120();
void main4130616();
void main300106();

#endif
