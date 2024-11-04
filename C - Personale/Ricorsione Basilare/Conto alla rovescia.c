#include <stdio.h>
#include <stdlib.h>

void  contoallarovescia(int n){
	if(n<0) printf("fine");
	else{
		printf("%d\n",n);
		contoallarovescia(n-1);
	}
}

int main(int argc, char *argv[]) {
	int n;
	printf("inserire numero : ");
	scanf("%d",&n);
	contoallarovescia(n);
	
	return 0;
	
}
