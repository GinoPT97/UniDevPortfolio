#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#define N 5

int main(int argc, char *argv[]) {
	
	int i,j,v[N];
    int d;
    
    srand(time(0));
    for(i=0;i < N;i++)
    if(!i) v[i]=rand()%10;
    else do{
      d=0;
      v[i]=rand()%10;
      for(j=0;j < i;j++)
        if(v[i]==v[j])d=1;
    }while(d);
    
    printf("Gli elementi dell'array sono :");
    
    for(i=0;i < N;i++) 
      printf("\nElemento in posizione n.%d : %d", i, v[i]);
	
	return 0;
}
