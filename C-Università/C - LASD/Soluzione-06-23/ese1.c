#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>

struct el
{
	int info;
	struct el *prev;
	struct el *next;
};
typedef struct el listaDP;

listaDP *new_nodo(int info)
{
	listaDP *tmp;

	tmp=(listaDP *)malloc(sizeof(listaDP));

	if(tmp)
	{
		tmp->info=info;
		tmp->next=NULL;
		tmp->prev=NULL;
	}

	return tmp;
}

void insert(listaDP **head, int info)
{
	listaDP *nuovo_nodo=new_nodo(info);

	if(*head==NULL)
	{
		*head=nuovo_nodo;
	}
	else if((*head)->next==NULL)
	{
		(*head)->next=nuovo_nodo;;
		nuovo_nodo->prev=*head;
	}
	else
	{
		insert(&((*head)->next),info);
	}
}

void stampa_lista(listaDP *head)
{
	if(head==NULL)
	{
		printf("FINE LISTA\n");
	}
	else
	{
		printf("%d->", head->info);
		stampa_lista(head->next);
	}
}

void rimuovi_occorrenze(listaDP **head, int numero)
{
	if(*head==NULL)
	{
		return;
	}

	if((*head)->info == numero)
	{
		listaDP *temp=*head;
		*head=(*head)->next;

		if(*head!=NULL)
		{
			(*head)->prev=NULL;
		}

		free(temp);

		rimuovi_occorrenze(head,numero);
	}
	else
	{
		rimuovi_occorrenze(&((*head)->next),numero);
	}
}

int conta_occorrenze(listaDP *head)
{
	if(head==NULL)
	{
		return 0;
	}

	int conteggio;

	if(head->info==2)
	{
		conteggio=1;
	}
	else
	{
		conteggio=0;
	}

	return conteggio + conta_occorrenze(head->next);
}

void inserisci_testa_n_occorrenze(listaDP **head, int valore)
{
	listaDP *nuovonodo=(listaDP *)malloc(sizeof(listaDP));

	if(nuovonodo)
	{
		nuovonodo->info=valore;
		nuovonodo->prev=NULL;
		nuovonodo->next=*head;
	}

	if(*head!=NULL)
	{
		(*head)->prev=nuovonodo;
	}

	*head=nuovonodo;
}

int main()
{
	listaDP *l=NULL;
	listaDP *l2=NULL;

	int elementi;
	int i;
	int val;

	printf("Quanti elementi vuoi inserire? ");
	scanf("%d", &elementi);

	for(i=0; i<elementi; i++)
	{
		printf("\nInserire elemento numero %d : ", i+1);
		scanf("%d", &val);

		insert(&l,val);
	}

	stampa_lista(l);

	int occorrenze=conta_occorrenze(l);

	printf("Il numero di occorrenze del numero 2 nella prima lista è %d\n", occorrenze);

	rimuovi_occorrenze(&l,2);

	stampa_lista(l);

	printf("\n\n");

	printf("Quanti elementi vuoi inserire nella seconda lista? ");
	scanf("%d", &elementi);

	for(i=0;i<elementi;i++)
	{
		printf("\nInserire elemento numero %d : ", i+1);
		scanf("%d", &val);

		insert(&l2,val);
	}

	stampa_lista(l2);

	printf("Insersco nella lista numero 2, il numero di occorrenze del numero 2 della prima lista\n");
	inserisci_testa_n_occorrenze(&l2,occorrenze);

	stampa_lista(l2);

}

