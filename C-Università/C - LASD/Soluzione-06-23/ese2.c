
#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>

struct el
{
	int info_radice;
	struct el *sx;
	struct el *dx;
};
typedef struct el albero;

int vuoto(albero *radice)
{
	if(radice) return 0;
	else return 1;
}

albero *costruisci(albero *s, int r, albero *d)
{
	albero *tmp;

	tmp=(albero *)malloc(sizeof(albero));

	if(tmp)
	{
		tmp->info_radice=r;
		tmp->sx=s;
		tmp->dx=d;
	}
	else
	{
		printf("Memoria non allocata\n");
	}

	return tmp;
}

void visita_ordine(albero *radice)
{
	if(radice)
	{
		visita_ordine(radice->sx);
		printf("%d | ", radice->info_radice);
		visita_ordine(radice->dx);
	}
}

albero *inserisci(albero *radice, int el)
{
	albero *aux;

	if(vuoto(radice))
	{
		aux=(albero *)malloc(sizeof(albero));
		if(aux)
		{
			aux->info_radice=el;
			aux->sx=NULL;
			aux->dx=NULL;
			radice=aux;
		}
		else
		{
			printf("Memoria non allocata\n");
		}
	}
	else if( el < radice->info_radice)
	{
		radice->sx=inserisci(radice->sx,el);
	}
	else if( el > radice->info_radice)
	{
		radice->dx=inserisci(radice->dx,el);
	}

	return radice;
}

int ricerca_minimo(albero *radice)
{
	int min=0;

	if(!(vuoto(radice)))
	{
		if(radice->sx==NULL)
		{
			min=radice->info_radice;
		}
		else
		{
			min=ricerca_minimo(radice->sx);
		}
	}
	return min;
}

void elimina_secondo_minimo(albero **radice, int el)
{
	if(vuoto(*radice))
	{
		printf("Elemento non trovato");
		return;
	}

	if((*radice)->info_radice > el)
	{
		elimina_secondo_minimo(&((*radice)->sx),el);
	}
	else if((*radice)->info_radice < el)
	{
		elimina_secondo_minimo(&((*radice)->dx),el);
	}
	else
	{
		if((*radice)->sx && (*radice)->dx)
		{
			albero *aux=ricerca_minimo((*radice)->dx);
			(*radice)->info_radice=aux->info_radice;
			elimina_secondo_minimo(&((*radice)->dx), aux->info_radice);
		}
		else
		{
			albero *aux=*radice;
			if((*radice)->sx==NULL)
			{
				*radice=(*radice)->dx;
			}
			else if((*radice)->dx == NULL)
			{
				*radice=(*radice)->sx;
			}
			free(aux);
		}
	}
}

albero *ricerca_secondo_minimo(albero *radice)
{
	if(vuoto(radice))
	{
		return NULL;
	}

	albero *minimo=radice;
	albero *secondo_minimo=NULL;

	while(minimo->sx!=NULL)
	{
		if(minimo->sx->sx == NULL && minimo->sx->dx==NULL)
		{
			secondo_minimo=minimo;
		}
		minimo=minimo->sx;
	}

	if(secondo_minimo!=NULL)
	{
		albero *tmp=secondo_minimo->sx;
		secondo_minimo->sx=secondo_minimo->sx->dx;
		free(tmp);
	}

	return radice;
}

int main()
{
	int elemento;
	int i;
	int val;
	int min;

	albero *abr=NULL;

	printf("\nQuanti elementi vuoi inserire? ");
	scanf("%d", &elemento);

	for(i=0; i<elemento; i++)
	{
		printf("\nInserisci elemento numero %d : ", i+1);
		scanf("%d", &val);

		abr=inserisci(abr,val);
	}

	visita_ordine(abr);

	min=ricerca_minimo(abr);

	printf("Il minimo dell'albero è %d\n", min);

	abr=ricerca_secondo_minimo(abr);

	printf("Il secodno elemento minimo dell'albero è %d\n", abr->info_radice);

	visita_ordine(abr);
}

