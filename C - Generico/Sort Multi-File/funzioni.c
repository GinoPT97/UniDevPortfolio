#include "libreria.h"

int *creavettore(int *v,int n){
	
	int i;
	v = NULL;
	
	v = (int *) malloc(n*sizeof(int));
	if(v==NULL) printf("Errore creazione vettore!");
	
	for(i=0; i<n; i++){
		printf("Inserire l'elemento alla posizione %d del vettore : ", i);
	    scanf("%d", &v[i]);
	}
	
	printf("\nVettore iniziale : ");
	stampavettore(v,n);
	
	return v;
}

void stampavettore(int *v,int n){
	int i;
	for(i=0; i<n; i++)
	  printf("\nElemento alla posizione %d del vettore : %d", i,v[i]);
	printf("\n");
}

void freevettore(int *v){
  free((void *) v);
}

void selsort(int *v, int n){
	int x;
	
	printf("\nSeleziona il sort che vuoi utilizzare\n1)Selection Sort\n2)Insertion Sort\n3)Buble Sort\n4)Merge Sort\n");
	scanf("%d", &x);
	
	switch(x){
		case 1:
		  SelectionSort(v,n);
		  break;
		case 2:
		 InsertionSort(v,n);
		 break;
		case 3:
		 BubbleSort(v,n);
		 break;
		case 4:
			MergeSort(v, 0, n-1);
		 break;
		default:
		  printf("Selezione non valida");
	}
}

void Swap(int *a, int *b){
   int temp;
   
   temp = *a;
   *a = *b;
   *b = temp;
}


void SelectionSort(int *v, int n){
    int i, j, min, t;
     
	for(i=0; i<n; i++){
      min = i;
      for (j = i; j < n; j++)
       if (v[j] < v[min])
         min = j;
     Swap(&v[min],&v[i]);
    }
    
    printf("\nVettore ordinato con Selection Sort : ");
	stampavettore(v,n);
	
}

void InsertionSort(int *v, int n){
	int i,j,k;
    int tmp;
    
	for(i=1;i<n;i++){
      tmp=v[i]; 
       j=i-1; 
       
	   while(j>=0 && (v[j] > tmp)){
         v[j+1] = v[j];
         j -= 1;
        }
      v[j+1]=tmp;
    }
	
	printf("\nVettore ordinato con Insertion Sort : ");
	stampavettore(v,n);
	
}

void merge(int *v, int p, int q, int r) {
  int i, j, k=0, b[r+1];
  i = p;
  j = q+1;

  while (i<=q && j<=r) {
    if (v[i]<v[j]) {
      b[k] = v[i];
      i++;
    } else {
      b[k] = v[j];
      j++;
    }
    k++;
  }
  while (i <= q) {
    b[k] = v[i];
    i++;
    k++;
  }
  while (j <= r) {
    b[k] = v[j];
    j++;
    k++;
  }
  for (k=p; k<=r; k++)
    v[k] = b[k-p];
}

void MergeSort(int *v, int p, int r){
	int q;
    if (p < r) {
       q = (p+r)/2;
       MergeSort(v, p, q);
       MergeSort(v, q+1, r);
       merge(v, p, q, r);
  }
   printf("\nVettore ordinato con Merge Sort : \n");
   stampavettore(v,r+1);
}

void BubbleSort(int *V, int n) { 
    int i,j; 
    int nswaps=0; // numero di scambi 
    int ncycles=0; // numero di iterazioni 
    bool scambi = true; // TRUE se è stato effettuato almeno
    i=1; // uno scambio

    while((i<=n-1) && (scambi==true)) { 
     scambi= false;
     ncycles++;
     for(j=0;j<n-i;j++){ // Analizza il sottoinsieme V[0]..V[n-i]
      if(V[j]>V[j+1]) { 
       nswaps++; 
       Swap(&V[j],&V[j+1]); 
       scambi= true;
       }      
      } 
    i++;
    }
   
   printf("\nVettore ordinato con Buble Sort : \n");
   printf("Sono stati eseguiti %d scambi\n", nswaps);
   printf("Sono stati eseguiti %d cicli\n", ncycles);
   stampavettore(V,n);
   
}
