#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "libreria.h"

void gestici_errore(int codice)
{
	if (codice==1) {
		printf("Memoria non allocata: Exit\n");
		exit(-1);
	}
}

static struct nodo *NewElem(int info)
{
	struct nodo *e;
	
	e = (struct nodo *) malloc(sizeof(struct nodo));
	if (e==NULL) gestici_errore(1);
	
	e->info = info;
	e->next = NULL;
	
	return e;
} 

struct nodo *insertTesta(struct nodo *head, int info)
{
	struct nodo *e;
	
	e = NewElem(info);
	
	e->next = head;
	
	return e;
}

struct nodo *insertCoda(struct nodo *head, int info)
{
	struct nodo *e, *tmp;
	
	e = NewElem(info);
	
	if (head==NULL) return e;

	tmp = head;
	while (tmp->next != NULL){
		tmp = tmp->next;
	}
	
	tmp->next = e;

	return head;
}

void stampaLista(struct nodo *head)
{
	while (head != NULL) {
		if(head != NULL && head->next == NULL){
			printf("%d", head->info);
	     	head = head->next;
		} else {
			printf("%d->", head->info);
		    head = head->next;
		}
	}
	printf("\n\n");
}

int lengthLista(struct nodo *head)
{
	int cont=0;
	
	while (head != NULL) {
		cont++;
		head = head->next;
	}
	
	return cont;
}

void freeLista(struct nodo *head)
{
	struct nodo *tmp;
	
	while(head != NULL) {
		tmp = head;
		head = head->next;
		free(tmp);
	}
	
}

struct nodo *eliminaElementi(struct nodo *head, int x){
	if(!head) return NULL;
	if(head->info<x) return eliminaElementi(head->next,x);
	head->next = eliminaElementi(head->next,x);
    return head;
}

static int StrtoI(char *cifra)
{
	if (strcmp(cifra,"uno")==0) return 1;
	if (strcmp(cifra,"due")==0) return 2;
	if (strcmp(cifra,"tre")==0) return 3;
	if (strcmp(cifra,"quattro")==0) return 4;
	if (strcmp(cifra,"cinque")==0) return 5;
	if (strcmp(cifra,"sei")==0) return 6;
	if (strcmp(cifra,"sette")==0) return 7;
	if (strcmp(cifra,"otto")==0) return 8;
	if (strcmp(cifra,"nove")==0) return 9;
	return 0;
}

struct nodo *leggiFile(FILE *fp, struct nodo *head)
{
	char cifra[256];
	int n = 0;
	while (fscanf(fp,"%s",cifra) == 1) {
		if (strcmp(cifra,"stop") != 0) {
			n = n*10 + StrtoI(cifra);
		} else {
			head = insertCoda(head, n);
			n = 0;
		}
	}
	return head;
}

static void ItoStr(char *dest, char c)
{
	if (c=='0') strcpy(dest,"zero");
	if (c=='1') strcpy(dest,"uno");
	if (c=='2') strcpy(dest,"due");
	if (c=='3') strcpy(dest,"tre");
	if (c=='4') strcpy(dest,"quattro");
	if (c=='5') strcpy(dest,"cinque");
	if (c=='6') strcpy(dest,"sei");
	if (c=='7') strcpy(dest,"sette");
	if (c=='8') strcpy(dest,"otto");
	if (c=='9') strcpy(dest,"nove");
}

void scriviFile(FILE *fp, struct nodo *head)
{
	int length;
	char **str;
	char val[256], cifra[256], buffer[256];
	int i, j, n;
	
	if (head != NULL) {
		length = lengthLista(head);
		str = malloc(length*sizeof(char *));
		for (i=0; i<length; i++) {
			str[i] = malloc(256*sizeof(char));
		}
		
		for (i=0; head!=NULL; i++, head=head->next) {
			str[i][0] = '\0';
			sprintf(val,"%d",head->info);
			n = strlen(val);
			for (j=0; j<n; j++) {
				ItoStr(cifra,val[j]);
				strcat(str[i],cifra);
				strcat(str[i]," ");
			}
		}
		
		for (i=0; i<length; i++) {
			for (j=i+1; j<length; j++) {
				if (strcmp(str[i], str[j]) > 0) {
					strcpy(buffer, str[i]);
					strcpy(str[i], str[j]);
					strcpy(str[j], buffer);
				}
			}
		}
		
		for (i=0; i<length; i++) {
			fprintf(fp,"%s", str[i]);
			printf("%s", str[i]);
			fprintf(fp,"stop\n");
			printf("stop\n");
			free(str[i]);
		}
		free(str);	
	}
}
