#include <stdio.h>
#include <stdlib.h>

#define MAX 10

int acquireHeap(int * H);
int sinistro(int i);
int destro(int i);
int padre(int i);
void scambia(int * H, int i , int j);

void costruisciHeap(int heapsize, int *H);
void heapify(int *H, int i, int heapsize);
void printHeap(int *H, int i, int heapsize, int indent);

int checkHeap(int * H, int i, int heapsize);

void modificaHeap(int * H, int heapsize);
