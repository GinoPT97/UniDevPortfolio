#include <stdlib.h>
#include <stdio.h>
#define N 10

int main(int argc, char *argv[]) {
	
	int v[N]={3,2,4,5,1,5,3,2,5,6};
    int i,j,max=0,maxi,x=1;
    
    for(i=0;i < N-1;i++){
     for(j=i+1;j < N;j++) 
	   if(v[j]==v[i]) x++;
       if(x > max) {
       	 max=x;
		 maxi=i;
	   }
     x=1;
    }
    
    printf("Gli elementi dell'array sono :");
    
    for(i=0;i < N;i++) 
      printf("\nElemento in posizione n.%d : %d", i, v[i]);
    
    printf("\n%d volte il %d", max,v[maxi]);
	
	return 0;
}
